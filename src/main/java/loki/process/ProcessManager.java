package loki.process;

import java.io.IOException;

/**
 * 进程管理
 */
public class ProcessManager {


    public static void main(String[] args) throws IOException, InterruptedException {

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("notepad");
        Process process = processBuilder.start();
        Thread.sleep(3000);
        process.destroy();
    }
}
