package loki.socket.粘包和分包;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) throws IOException {
        final ServerSocket server = new ServerSocket(8080);
        System.out.println(server.getReceiveBufferSize());
        System.out.println("监听端口:" + server);
        final Socket client = server.accept();
        System.out.println(client.getKeepAlive() + " | tcpDelay" + client.getTcpNoDelay());
        client.setTcpNoDelay(true);
        System.out.println("收到请求:" + client);

        final Thread thread = new Thread(new ReadClient(client));
        thread.setDaemon(false);
        thread.start();
    }

    public static class ReadClient implements Runnable {
        private final Socket socket;
        public ReadClient(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            final byte[] bytes = new byte[50];
            int len = 0;
            do {
                try {
                    len = socket.getInputStream().read(bytes);
                    System.out.println(new String(bytes, 0, len));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } while (len > 0);
        }
    }

}
