package loki.nio.test.a;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author zhengq
 */
public class SocketUtils {
    public static void of(int port) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(9999));

        // 设置为没有阻塞
        serverSocketChannel.configureBlocking(false);
        while(true){
            SocketChannel socketChannel = serverSocketChannel.accept();
            if(socketChannel != null){
                ByteBuffer byteBuffer = ByteBuffer.allocate(255);
                int read = socketChannel.read(byteBuffer);

            }
        }
    }
}
