package loki.nio.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class SocketServerTest {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(9999));

        while(true){
            SocketChannel socketChannel = serverSocketChannel.accept();

            if (socketChannel == null) {
                continue;
            }
            socketChannel.configureBlocking(false);


            Selector selector = Selector.open();
            socketChannel.register(selector, SelectionKey.OP_READ);
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            SelectionKey selectionKey = null;
            for (SelectionKey key : selectionKeys) {
                selectionKey = key;
                if (selectionKey.isReadable()) {
                    System.out.println("readable");
                }
                System.out.println(selectionKey.readyOps());
            }
            System.out.println("end");

        }


    }
}
