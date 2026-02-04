package lab.third.jmx;

public interface ClickIntervalMBean {
    /** Средний интервал между кликами (мс) */
    double getAverageIntervalMillis();
    /** Количество зарегистрированных кликов */
    long getClickCount();
    /** Сброс статистики */
    void reset();
}
