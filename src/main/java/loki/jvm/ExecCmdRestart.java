package loki.jvm;

import sun.management.VMManagement;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * java 自重启
 */
public class ExecCmdRestart {
    public static int getProcessId() throws Exception {

        /*Get the current process id using a reflection hack */
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        Field jvm = runtime.getClass().getDeclaredField("jvm");

        jvm.setAccessible(true);
        VMManagement mgmt = (sun.management.VMManagement) jvm.get(runtime);

        Method pid_method = mgmt.getClass().getDeclaredMethod("getProcessId");

        pid_method.setAccessible(true);

        int pid = (Integer) pid_method.invoke(mgmt);
        return pid;
    }

    public static void main(String[] args) throws Exception {
        int pid = getProcessId();
        Runtime.getRuntime().exec("kill -15 " + pid);
        while (true) {
            System.out.println(1);
        }
    }

}
