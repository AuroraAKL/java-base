package loki.network;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;

public class UDP {

    public static void receiver(){
        System.out.println("receiver ready");
        try {
            DatagramSocket socket = new DatagramSocket( 3000);
            byte b[] = new byte[1024];
            while(true) {
                DatagramPacket datagramPacket = new DatagramPacket(b, 1024);
                socket.receive(datagramPacket);
                System.out.print(new String(b, 0, datagramPacket.getLength()));
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sender(){
        System.out.println("sender ready");
        try {
            DatagramSocket socket = new DatagramSocket();
            Scanner scanner = new Scanner(System.in);
            while(scanner.hasNextLine()) {
                byte b[] = (scanner.nextLine() + System.lineSeparator()).getBytes();
                DatagramPacket datagramPacket = new DatagramPacket(b, b.length, InetAddress.getByName("127.0.0.1"), 3000);
                socket.send(datagramPacket);
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Thread(UDP::receiver).start();
        sender();
    }
}
