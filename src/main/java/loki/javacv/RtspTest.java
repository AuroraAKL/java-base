package loki.javacv;

import org.bytedeco.javacv.BufferRing;

import java.io.*;
import java.net.Socket;
import java.nio.Buffer;
import java.nio.charset.StandardCharsets;

public class RtspTest {

    public static String getString() {
        String op = "DESCRIBE";
        String streamUrl = "rtsp://172.23.133.11:1183/09921849233200480101?DstCode=01&ServiceType=1&ClientType=0&StreamID=1&SrcTP=2&DstTP=2&SrcPP=1&DstPP=1&MediaTransMode=0&BroadcastType=0&SV=1&Token=qUHpt5cWwZc5gtidSOGysEsscaC6C5bJiB8dzFey/V4=&DomainCode=f6bea8bc31de4d9aa387301fd0dcdf95&UserId=5&";
        String rtspVersion = "RTSP/1.0";

        String cSeq = "Cseq: 1";

        return op + " " + streamUrl + " " + rtspVersion
                + "\r\n" + cSeq
                + "\r\n" + "\r\n";
    }

    public static String getOptionsString() {
        String op = "OPTIONS";
        String streamUrl = "rtsp://172.23.133.11:1183/09921849233200480101?DstCode=01&ServiceType=1&ClientType=0&StreamID=1&SrcTP=2&DstTP=2&SrcPP=1&DstPP=1&MediaTransMode=0&BroadcastType=0&SV=1&Token=qUHpt5cWwZc5gtidSOGysEsscaC6C5bJiB8dzFey/V4=&DomainCode=f6bea8bc31de4d9aa387301fd0dcdf95&UserId=5&";
        String rtspVersion = "RTSP/1.0";

        String cSeq = "Cseq: 1";
        return op + " " + streamUrl + " " + rtspVersion
                + "\r\n" + cSeq
                + "\r\n" + "\r\n";
    }

    public static void main(String[] args) throws IOException {

        Socket socket = new Socket("172.23.133.11", 1183);
        OutputStream stream = socket.getOutputStream();
        new Thread(()->{
            while (true) {
                try {
                    InputStream inputStream = socket.getInputStream();
                    BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
                    String s = null;
                    StringBuilder bf = new StringBuilder();
                    while((s = in.readLine() ) != null ){
                        bf.append(s).append("\r\n");
                    }
                    System.out.println(bf.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        stream.write(getOptionsString().getBytes(StandardCharsets.UTF_8));
        stream.flush();
        System.out.println("write end");



    }
}
