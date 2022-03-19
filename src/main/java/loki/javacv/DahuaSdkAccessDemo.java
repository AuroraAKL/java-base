package loki.javacv;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import javax.swing.JFrame;

import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;

/**
 * 大华sdk接收实时视频回调演示demo
 *
 * @author eguid
 */
public class DahuaSdkAccessDemo {

    JavaCVProcessThread t = null;

    //通过海康/大华sdk回调函数每次回调传输过来的视频字节数组数据写入到管道流
    public void onMediaStream(byte[] data, int offset, int length, boolean isAudio) throws IOException {
        if (t == null) {
            //启动javacv解析处理器线程
            t = new JavaCVProcessThread();
            t.start();
        }
        if (t != null) {
            //写出视频码流到javacv多线程解析处理器
            t.push(data, length);
        }

    }
}

/**
 * javacv多线程解析处理器，用于读取海康/大华/宇视设备sdk回调视频码流并解析
 *
 * @author eguid
 */
class JavaCVProcessThread extends Thread {
    FFmpegFrameGrabber grabber = null;
    CanvasFrame canvas = null;

    PipedInputStream pin;
    PipedOutputStream pout;

    /**
     * 创建用于把字节数组转换为inputstream流的管道流
     *
     * @throws IOException
     */
    public JavaCVProcessThread() throws IOException {
        pout = new PipedOutputStream();
        pin = new PipedInputStream(pout);
    }

    /**
     * 异步接收海康/大华/宇视设备sdk回调实时视频裸流数据
     *
     * @param data
     * @param size
     */
    public void push(byte[] data, int size) {
        try {
            pout.write(data, 0, size);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public void run() {

        grabber = new FFmpegFrameGrabber(pin, 0);


        long stime = System.currentTimeMillis();
        //从这里就要等待海康/大华/宇视等设备sdk回调函数传输数据，所以需要开始阻塞等待，这里只等三秒，大家可以根据自己需要修改
        long waitStreamDelay = 3000;
        for (; ; ) {
            System.out.println("等待检查流");
            //检测管道流中是否存在数据，如果3s后依然没有写入1024的数据，则认为管道流中无数据，避免grabber.start();发生阻塞
            if (System.currentTimeMillis() - stime > waitStreamDelay) {
                return;
            }
            try {
                if (pin.available() == 1024) {
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e1) {
            }
        }
        try {
            grabber.start();
            canvas = new CanvasFrame("预览图像");// 新建一个窗口
            canvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            Frame frame = null;
            for (int i = 0; (frame = grabber.grab()) != null; ) {
                System.out.println("读取到帧" + i++);

                if (frame.image != null) {
                    System.err.println("读取到视频帧");
                    canvas.showImage(frame);
                } else if (frame.samples != null) {
                    System.err.println("读取到音频帧");
                }

            }
            if (grabber != null) {
                grabber.close();
            }

        } catch (org.bytedeco.javacv.FrameGrabber.Exception e) {
            e.printStackTrace();
        } finally {
            if (grabber != null) {
                try {
                    grabber.close();
                } catch (org.bytedeco.javacv.FrameGrabber.Exception e) {
                }
            }
        }

        if (canvas != null) {
            canvas.dispose();
        }
    }
}
