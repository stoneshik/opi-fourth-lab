package lab.third.jmx;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

public final class JmxRegistry {
    private static final DotStats DOT_STATS = new DotStats();
    private static final ClickInterval CLICK_INTERVAL = new ClickInterval();
    static {
        try {
            MBeanServer server = ManagementFactory.getPlatformMBeanServer();
            server.registerMBean(
                DOT_STATS,
                new ObjectName("lab.third:type=DotStats")
            );
            server.registerMBean(
                CLICK_INTERVAL,
                new ObjectName("lab.third:type=ClickInterval")
            );
        } catch (Exception e) {
            throw new IllegalStateException("JMX MBean registration failed", e);
        }
    }

    private JmxRegistry() {}
    public static DotStats dotStats() { return DOT_STATS; }
    public static ClickInterval clickInterval() { return CLICK_INTERVAL; }
}
