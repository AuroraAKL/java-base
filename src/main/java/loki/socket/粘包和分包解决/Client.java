package loki.socket.粘包和分包解决;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Client {

    public static int bytesToInt(byte[] b) {
        int i = 0;
        i = i | b[0];
        i = i << 8 | b[1];
        i = i << 8 | b[2];
        i = i << 8 | b[3];
        return i;
    }

    public static byte[] toBytes(int i) {
        byte[] targets = new byte[4];
        targets[3] = (byte) (i & 0xFF);
        targets[2] = (byte) (i >> 8 & 0xFF);
        targets[1] = (byte) (i >> 16 & 0xFF);
        targets[0] = (byte) (i >> 24 & 0xFF);
        return targets;
    }

    public static byte[] concat(byte []a, byte []b) {
        int len = a.length + b.length;
        byte[] c = new byte[len];
        int k = 0;
        for (byte value : a) {
            c[k++] = value;
        }
        for (byte value : b) {
            c[k++] = value;
        }
        return c;
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        final Socket socket = new Socket("127.0.0.1", 8080);
        System.out.println("建立连接:" + socket.isConnected() + " tcyDelay:" + socket.getTcpNoDelay());
        socket.setTcpNoDelay(true);
        final OutputStream stream = socket.getOutputStream();
        for (int i = 0; i < 100; i++) {
            final String s = new String("这是一个完整的TCP包!");
            byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
            final byte[] lenBytes = toBytes(bytes.length);
            System.out.println(Arrays.toString(lenBytes));
            System.out.println(Arrays.toString(bytes));
            bytes = concat(lenBytes, bytes);
            System.out.println(Arrays.toString(bytes));
            stream.write(bytes);
            stream.flush();
        }

        Thread.sleep(3000);
    }
}
