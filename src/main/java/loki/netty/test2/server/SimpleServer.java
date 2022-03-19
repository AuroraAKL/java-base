package loki.netty.test2.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author jack
 */
public class SimpleServer {

    public static void main(String[] args) {
        //创建bossGroup , 只负责连接请求
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        //创建workerGroup , 负责客户端业务处理
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        //创建服务端启动对象,配置参数.
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        try {
            serverBootstrap.group(bossGroup, workerGroup)//设置线程组
                    .channel(NioServerSocketChannel.class)//使用NioSocketChannel作为服务端的通道实现
                    .option(ChannelOption.SO_BACKLOG, 128)//设置线程队列得到连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true)//设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() {//创建一个通道测试对象
                        //给pipeline设置处理器
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new NettyServerHandler()); //自定义handler
                        }
                    });//workerGroup的EventLoop对应的管道设置处理器

            System.out.println("服务端准备就绪...");

            //绑定一个端口并且同步,生成了一个channelFuture对象
            ChannelFuture cf = serverBootstrap.bind(6667).sync();
            //对关闭通道进行监听
            cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

}