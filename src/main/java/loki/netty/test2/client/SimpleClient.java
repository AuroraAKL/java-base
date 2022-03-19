package loki.netty.test2.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author jack
 */
public class SimpleClient {

    public static void main(String[] args) {
        //客户端需要一个事件循环组
        NioEventLoopGroup clientLoopGroup = new NioEventLoopGroup();
        //创建客户端启动对象
        Bootstrap bootstrap = new Bootstrap();
        try {
            bootstrap.group(clientLoopGroup)//设置线程组
                    .channel(NioSocketChannel.class)//设置客户端通道实现类
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new NettyClientHandler());//加入自定义处理器
                        }
                    });

            System.out.println("客户端已准备就绪");
            //连接服务器
            ChannelFuture cf = bootstrap.connect("127.0.0.1", 6667).sync();
            cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            clientLoopGroup.shutdownGracefully();
        }
    }

}