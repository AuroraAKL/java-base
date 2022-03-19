package loki.nio;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @date 2021/3/28 1:57
 * @Description
 */
public class FileWriter {

    /**
     *
     * @param path
     * @param data
     * @throws IOException
     */
    public static void writer(String path, byte[] data) throws IOException {
        //创建一个输出流->  channel
        FileOutputStream fileOutputStream = new FileOutputStream(path);

        //通过 FileOutPutStream 获取对应的 FileChannel
        FileChannel fileChannel = fileOutputStream.getChannel();

        //创建一个缓冲区 bytebuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        //将str放入byteBuffer
        byteBuffer.put(data);
        //对byteBuffer 进行filp
        byteBuffer.flip();

        //将byteBuffer 数据写入到 fileChannel
        fileChannel.write(byteBuffer);
        fileOutputStream.close();
    }

    public static void main(String[] args) throws IOException {
        writer("D://1.txt", "hello".getBytes());
    }
}
