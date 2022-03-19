package loki.javacv;

import org.bytedeco.ffmpeg.avcodec.AVPacket;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.*;
import org.bytedeco.opencv.opencv_core.Mat;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.*;

import static org.bytedeco.ffmpeg.global.avutil.*;

/**
 * 测试 获取 rstp协议的流
 */
public class JavaCVFFmpegRSTP {

    /**
     * 从 rstp 协议中拉取流
     *
     * @param rstpURI
     * @return
     * @throws IOException
     */
    public static FFmpegFrameGrabber pullStreamFromFLV(String rstpURI) throws IOException {
        FFmpegFrameGrabber grabber = FFmpegFrameGrabber.createDefault(rstpURI);
        grabber.setVideoCodec(avcodec.AV_CODEC_ID_H264);
        grabber.start();
        return grabber;
    }

    /**
     * 从 rstp 协议中拉取流
     *
     * @param rstpURI
     * @return
     * @throws IOException
     */
    public static FFmpegFrameGrabber pullStreamFromRTSP(String rstpURI) throws IOException {
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(rstpURI) ;
        grabber.setOption("rtsp_transport", "tcp"); // 使用tcp的方式，不然会丢包很严重
        grabber.setOption("rtsp_flags", "prefer_tcp"); // 使用tcp的方式，不然会丢包很严重
//        grabber.setImageWidth(400);
//        grabber.setImageHeight(400);
        grabber.start();
        return grabber;
    }

    public static FFmpegFrameRecorder buildRTMPRecorder(String url) throws IOException {
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(url, 0);
        recorder.setInterleaved(true);
//        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
        recorder.setFormat("flv");
        recorder.setVideoOption("-c", "libx264");
        return recorder;
    }

    /**
     * 将流 拉取出 并 推到指定文件下
     *
     * @param grabber
     * @param file
     * @return
     * @throws IOException
     */
    public static void pushStreamToFile(FFmpegFrameGrabber grabber, File file) throws IOException {
        // 流媒体输出地址，分辨率（长，高），是否录制音频（0:不录制/1:录制）
        int imageHeight = grabber.getImageHeight();
        int imageWidth = grabber.getImageWidth();
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(file, imageWidth, imageHeight, 0);
        pushStream(grabber, recorder);
    }

    public static FFmpegFrameGrabber pushStream(FFmpegFrameGrabber grabber, FFmpegFrameRecorder recorder) throws IOException {
        recorder.start();
        Frame frame = grabber.grab();
        while(frame != null) {
            recorder.record(frame);
//            show(frame);
            frame = grabber.grab();
        }
        return grabber;
    }

    static CanvasFrame canvasFrame;// new CanvasFrame("预览");
    public static void show(Frame frame) {
        canvasFrame.showImage(frame);
    }

    public static void testFLV() throws IOException {
        FFmpegFrameGrabber fFmpegFrameGrabber = pullStreamFromFLV("file:C:\\Users\\Rnti\\Desktop\\source.flv");
        pushStreamToFile(fFmpegFrameGrabber, new File("C:\\Users\\Rnti\\Desktop\\tmp.m3u8"));
    }

    public static void testRSTP() throws IOException {
        FFmpegFrameGrabber fFmpegFrameGrabber = pullStreamFromRTSP("rtsp://admin:shinemo123@39.170.35.150:1554/h264/ch0/1");
        pushStreamToFile(fFmpegFrameGrabber, new File("C:\\Users\\Rnti\\Desktop\\tmp333.m3u8"));
    }

    public static void testRSTPToRTMP(String rtsp, String out) throws IOException {
        if (rtsp == null) {
            rtsp = "rtsp://admin:shinemo123@39.170.35.150:1554/h264/ch0/1";
        }
        if (out == null) {
            out = "rtmp://10.0.10.104/live/000001";
        }
        FFmpegFrameGrabber fFmpegFrameGrabber = pullStreamFromRTSP(rtsp);
        pushStream(fFmpegFrameGrabber, buildRTMPRecorder(out));
    }


    public static void main(String[] args) throws IOException {
        FFmpegLogCallback.set();
        // 开启日志打印
//        avutil.av_log_set_level(AV_LOG_DEBUG);
//        testRSTP();
//        testFLV();
        testRSTPToRTMP(args[0], args[1]);
    }

}
