package loki.socket.粘包和分包解决;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

/**
 * 使用数据报长度解决粘包
 */
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
            int len = 0;
            do {
                try {
                    final byte[] lenByte = new byte[4];
                    socket.getInputStream().read(lenByte);
                    System.out.println(Arrays.toString(lenByte));
                    int pckLen = Client.bytesToInt(lenByte);
                    System.out.println(pckLen);
                    final byte[] bytes = new byte[pckLen];

                    // 这里是每次只读一个包大小的数据, 这里效率其实很低的. 应该给一个较大的缓存读取出来再说, 然后再进行解析.
                    len = socket.getInputStream().read(bytes);
                    System.out.println(new String(bytes, 0, len));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } while (len > 0);
        }
    }
}
