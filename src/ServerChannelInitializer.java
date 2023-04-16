

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

@Sharable
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        // 
        ch.pipeline().addLast("decoder", (ChannelHandler) new StringDecoder(CharsetUtil.UTF_8));
        ch.pipeline().addLast("encoder", (ChannelHandler) new StringEncoder(CharsetUtil.UTF_8));
        ch.pipeline().addLast(new NettyServerHandler());
        
    }
    
}

