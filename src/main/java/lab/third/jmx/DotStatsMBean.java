package lab.third.jmx;

public interface DotStatsMBean {
    /** Общее число установленных пользователем точек */
    long getTotalCount();
    /** Число попавших точек */
    long getHitCount();
    /** Число промахов */
    long getMissCount();
}
