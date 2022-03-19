package loki.netty.test2.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * 服务端自定义handler
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 读取实际数据(这里我们可以读取客户端发送的消息)
     *
     * @param ctx 上下文对象,含有管道pipeline,通道channel ,地址
     * @param msg 客户端发送的内容
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("客户端发送: " + buf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端地址为:" + ctx.channel().remoteAddress());

    }

    /**
     * 读取完成后
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("你好,客户端", CharsetUtil.UTF_8));
    }


    /**
     * 处理异常,一般是关闭通道
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}