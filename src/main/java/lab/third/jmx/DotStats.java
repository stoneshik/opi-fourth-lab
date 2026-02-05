package lab.third.jmx;

import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;

public class DotStats extends NotificationBroadcasterSupport implements DotStatsMBean {
    private static final String MISS_NOTIFICATION_TYPE = "lab.third.miss";
    private long totalCount;
    private long hitCount;
    private long missCount;
    private long sequenceNumber;

    /**
     * Регистрирует попадание или промах
     */
    public synchronized void registerDot(boolean hit) {
        totalCount++;
        if (hit) {
            hitCount++;
        } else {
            missCount++;
            sendMissNotification();
        }
    }

    private void sendMissNotification() {
        Notification notification = new Notification(
            MISS_NOTIFICATION_TYPE,
            this,
            ++sequenceNumber,
            System.currentTimeMillis(),
            "Miss № " + missCount
        );
        sendNotification(notification);
    }

    @Override
    public synchronized long getTotalCount() { return totalCount; }
    @Override
    public synchronized long getHitCount() { return hitCount; }
    @Override
    public synchronized long getMissCount() { return missCount; }
}
