package lab.third.jmx;

public class ClickInterval implements ClickIntervalMBean {
    private long lastClickTime = -1;
    private long totalInterval = 0;
    private long intervalCount = 0;
    private long clickCount = 0;

    /**
     * Вызывать при каждом клике пользователя
     */
    public synchronized void registerClick() {
        long now = System.currentTimeMillis();
        clickCount++;
        if (lastClickTime != -1) {
            long interval = now - lastClickTime;
            totalInterval += interval;
            intervalCount++;
        }
        lastClickTime = now;
    }

    @Override
    public synchronized double getAverageIntervalMillis() {
        if (intervalCount == 0) {
            return 0.0;
        }
        return (double) totalInterval / intervalCount;
    }

    @Override
    public synchronized long getClickCount() { return clickCount; }

    @Override
    public synchronized void reset() {
        lastClickTime = -1;
        totalInterval = 0;
        intervalCount = 0;
        clickCount = 0;
    }
}
