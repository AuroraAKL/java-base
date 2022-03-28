package loki.socket.粘包和分包;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Client {

    public static void main(String[] args) throws IOException, InterruptedException {

        final Socket socket = new Socket("127.0.0.1", 8080);
        System.out.println("建立连接:" + socket.isConnected() + " tcyDelay:" + socket.getTcpNoDelay());
        socket.setTcpNoDelay(true);
        final OutputStream stream = socket.getOutputStream();
        for (int i = 0; i < 100; i++) {
            final String s = new String("这是一个包!");
            stream.write(s.getBytes(StandardCharsets.UTF_8));
            stream.flush();
        }

        Thread.sleep(3000);
    }
}
