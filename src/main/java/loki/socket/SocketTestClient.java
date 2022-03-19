package loki.socket;

import java.io.*;
import java.lang.reflect.Field;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketImpl;
import java.nio.charset.StandardCharsets;

public class SocketTestClient {
    public static void main(String[] args) throws IOException, NoSuchFieldException, IllegalAccessException {

        Socket socket = new Socket("localhost", 9999);

        // 取出socket 对应的实现类
        Field implField = socket.getClass().getDeclaredField("impl");
        implField.setAccessible(true);
        SocketImpl socketImpl = (SocketImpl) implField.get(socket);

//        // 取出 socket 对应的文件描述符
//        Field fdField = socketImpl.getClass().getSuperclass().getSuperclass().getSuperclass().getDeclaredField("fd");
//        fdField.setAccessible(true);
//        FileDescriptor fileDescriptor = (FileDescriptor) fdField.get(socketImpl);
//
//
//        FileInputStream fileInputStream =  new FileInputStream(fileDescriptor);
//
//        System.out.println(fileInputStream);
//        System.out.println(fileInputStream.getFD());
//
//        while (true) {
//            byte [] bytes = new byte[1024];
//            int index = -1;
//            do {
//                index = fileInputStream.read(bytes);
//                System.out.println(new String(bytes, 0, index, StandardCharsets.UTF_8));
//            }while (index > 0);
//            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
//        }
//        InputStream inputStream = socket.getInputStream();
//        System.out.println(inputStream);
//
//        socket.getOutputStream().write(123);
//        socket.getOutputStream().flush();
    }
}
