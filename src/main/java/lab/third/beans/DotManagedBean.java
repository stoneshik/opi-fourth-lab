package lab.third.beans;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Model;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lab.third.entities.DotEntity;
import lab.third.jmx.JmxRegistry;
import lab.third.util.TransactionExecutor;

@Model
@ApplicationScoped
public class DotManagedBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(
        "opiPU"
    );
    private DotEntity currentEntity = new DotEntity();
    private List<DotEntity> dotEntities = new CopyOnWriteArrayList<>();

    public DotManagedBean() {
        initTransaction(
            manager -> this.dotEntities.addAll(
                manager.createQuery(
                    "SELECT e FROM DotEntity e",
                    DotEntity.class
                ).getResultList()
            )
        );
    }

    public DotEntity getCurrentEntity() {
        return currentEntity;
    }

    public List<DotEntity> getDotEntities() {
        return dotEntities;
    }

    public void setCurrentEntity(DotEntity newDotEntity) {
        this.currentEntity = newDotEntity;
    }

    public void setDotEntities(List<DotEntity> dotEntities) {
        this.dotEntities = dotEntities;
    }

    public boolean isEmpty() {
        return this.dotEntities.isEmpty();
    }

    public synchronized void addNewDot() {
        long startTime = System.nanoTime();
        currentEntity.updateInfo();
        currentEntity.setTimeLead(System.nanoTime() - startTime);
        // JMX: факт клика
        JmxRegistry.clickInterval().registerClick();
        // JMX: статистика попаданий
        JmxRegistry.dotStats().registerDot(currentEntity.getIsHit());
        initTransaction(manager -> manager.persist(currentEntity));
        dotEntities.add(0, currentEntity);
        currentEntity = new DotEntity();
    }

    public synchronized void removeAllDots() {
        initTransaction(
            manager -> manager.createQuery("DELETE FROM DotEntity").executeUpdate()
        );
        this.dotEntities.clear();
    }

    public void initTransaction(TransactionExecutor transactionExecutor) {
        EntityManager manager = this.entityManagerFactory.createEntityManager();
        try {
            manager.getTransaction().begin();
            transactionExecutor.executeTransaction(manager);
            manager.getTransaction().commit();
        } catch (Exception ex) {
            if (manager.getTransaction().isActive()) {
                manager.getTransaction().rollback();
            }
            System.out.println("An exception occurred during transaction.");
            ex.printStackTrace();
        } finally {
            manager.close();
        }
    }
}
