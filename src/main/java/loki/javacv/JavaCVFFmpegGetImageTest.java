package loki.javacv;

import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

/**
 * get video first frame image
 */
public class JavaCVFFmpegGetImageTest {

    public void getVideoInfo(File video, String picPath) {
        try {
            FileInputStream fileInputStream = new FileInputStream(video);
            FFmpegFrameGrabber fFmpegFrameGrabber = new FFmpegFrameGrabber(fileInputStream);
            fFmpegFrameGrabber.start();

            Frame frame = fFmpegFrameGrabber.grabImage();
            Java2DFrameConverter java2DFrameConverter = new Java2DFrameConverter();
            BufferedImage bufferedImage = java2DFrameConverter.getBufferedImage(frame);
            ImageIO.write(bufferedImage, "jpg", new File(picPath));
            fFmpegFrameGrabber.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static File getVideoPic(File video, String picPath) {
        FFmpegFrameGrabber ff = new FFmpegFrameGrabber(video);

        try {
            ff.start();
            int lenght = ff.getLengthInFrames();
            int i = 0;
            Frame f = null;
            while (i < lenght) {
                // 过滤前5帧，避免出现全黑的图片，依自己情况而定
                f = ff.grabFrame();
                if ((i > 5) && (f.image != null)) {
                    break;
                }
                i++;
            }

            // 截取的帧图片
            Java2DFrameConverter converter = new Java2DFrameConverter();
            BufferedImage srcImage = converter.getBufferedImage(f);
            int srcImageWidth = srcImage.getWidth();
            int srcImageHeight = srcImage.getHeight();

            // 对截图进行等比例缩放(缩略图)
            int width = 1080;
            int height = (int) (((double) width / srcImageWidth) * srcImageHeight);
            BufferedImage thumbnailImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
            thumbnailImage.getGraphics().drawImage(srcImage.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);

            File picFile = new File(picPath);
            ImageIO.write(thumbnailImage, "jpg", picFile);

            ff.stop();
            return picFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws IOException {
        System.out.println(new Date());
        URL url = new URL("file:C:\\Users\\Rnti\\Desktop\\source.flv");
        URLConnection connection = url.openConnection();
        InputStream inputStream = connection.getInputStream();
        File video = new File("video.mp4");
        OutputStream outputStream = new FileOutputStream(video);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        while ((bytesRead = inputStream.read(buffer, 0, 8192)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.close();
        inputStream.close();
        String picPath = "video.jpg";
        String picPath2 = "video2.jpg";
        getVideoPic(video, picPath);
        getVideoPic(video, picPath2);
        video.delete();
        System.out.println(new Date());
    }
}
