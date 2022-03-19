package loki.nio.test.a;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 *
 * @author zhengq
 */
public class FileUtils {

    /**
     * 写文件
     * @param path 要写入文件的路径
     * @param bytes 要写入的内容
     */
    public static void to(String path, byte[] bytes) throws IOException {
        File file = new File(path);
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        // channel -> 标识一个资源管道, 保存
        FileChannel channel = randomAccessFile.getChannel();
        // buffer -> 可以复用
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        channel.write(byteBuffer);
    }

    /**
     * 读取文件
     * @param path 文件路径
     * @return 返回读取的字节数组
     * @throws IOException 读取异常
     */
    public static byte[] of(String path) throws IOException {
        File file = new File(path);
        RandomAccessFile accessFile = new RandomAccessFile(file, "rw");
        FileChannel fileChannel = accessFile.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) fileChannel.size());
        fileChannel.read(byteBuffer);
        return byteBuffer.array();
    }

    public static void main(String[] args) throws IOException {
        String path = "D:/temp/1.txt";
        path = "C:\\Users\\Rnti\\Desktop\\departments";
        byte[] of = FileUtils.of(path);
        byte[] decode = Base64.getEncoder().encode(of);
        System.out.println(new String(decode, StandardCharsets.UTF_8));


        String writePath = "D:/temp/2.txt";
        FileUtils.to(writePath, of);
    }

}
