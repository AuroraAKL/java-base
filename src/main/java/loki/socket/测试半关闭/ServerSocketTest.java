package loki.socket.测试半关闭;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ServerSocketTest {
    /**
     * 4次挥手变成 2次
     * ---  由于sleep， 服务端并没有close连接，导致连接 半关闭
     * @throws IOException
     */
    static void testTCP() throws IOException {
        ServerSocket socket = new ServerSocket(9991);
        Socket accept;
        while (true){
            accept = socket.accept();
            System.out.println("new socket");
            Socket finalAccept = accept;
            new Thread(() -> {
                OutputStream outputStream = null;
                try {
                    outputStream = finalAccept.getOutputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    outputStream.write(new String("1234567890-123").getBytes(StandardCharsets.UTF_8));
                    outputStream.flush();
                    System.out.println("write");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int i = 0;
                while (i++ < 10){
                    try {
                        outputStream.write(new String("1").getBytes(StandardCharsets.UTF_8));
                        outputStream.flush();
                        System.out.println("write again");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    /**
     *
     * @throws IOException
     */
    static void testTCPOne() throws IOException {
        ServerSocket serverSocket = new ServerSocket(9999);
        System.out.println("服务器等待连接...");
        Socket clientSocket = serverSocket.accept();
        System.out.println("服务端正在接收信息...");
        InputStream inStream = clientSocket.getInputStream();
        OutputStream outStream = clientSocket.getOutputStream();
        Scanner in = new Scanner(inStream);
        PrintWriter out = new PrintWriter(outStream, true /*autoFlush*/);
        out.println("Hello! Enter BYE to exit");
        System.out.println("服务端正在读取信息...");
        boolean done = false;
        while (!done && in.hasNextLine()) {
            String line = in.nextLine();
            // 回声
            String echo = "Echo:" + line;
            out.println(echo);
            if (line.trim().equals("BYE")) done = true;
        }
        System.out.println("服务器关闭连接...");
        inStream.close();
        serverSocket.close();
    }
    public static void main(String[] args) throws IOException {
        new Thread(()->{
            try {
                testTCP();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        testTCPOne();
    }
}
