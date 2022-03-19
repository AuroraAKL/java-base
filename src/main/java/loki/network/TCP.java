package loki.network;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TCP {
    public static void receiver(){
        ExecutorService executor = Executors.newCachedThreadPool();
        try {
            ServerSocket serverSocket = new ServerSocket(3000);
            Socket socket = serverSocket.accept();
            Runnable task = ()-> {
                byte b[] = new byte[1024];
                int i = 0;
                    while(true) {
                        try {
                            if ((i = socket.getInputStream().read(b)) == -1) break;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.err.print(new String(b, 0 , i));
                    }
            };
            executor.submit(task);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sender(){
        Scanner scanner = new Scanner(System.in);
        while(true){
            try {
                Socket socket = new Socket("127.0.0.1", 3000);
                while(scanner.hasNextLine()){
                    socket.getOutputStream().write(scanner.nextLine().getBytes());
                    socket.getOutputStream().write(System.lineSeparator().getBytes());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) {

        new Thread(TCP::receiver).start();
        sender();


    }
}
