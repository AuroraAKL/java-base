package loki.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @date 2021/3/28 1:55
 * @Description
 */
public class FileReader {

    public static byte[] reader(String path) throws IOException {
        File file = new File(path);
        //创建文件的输入流
        FileInputStream fileInputStream = new FileInputStream(file);

        //通过fileInputStream 获取对应的fileChannel
        FileChannel fileChannel = fileInputStream.getChannel();

        //创建缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());

        //将通道的数据输入到缓冲区
        fileChannel.read(byteBuffer);

        return byteBuffer.array();
    }

    public static void main(String[] args) throws IOException {
        System.out.println(new String(reader("D:/1.txt")));
    }
}
