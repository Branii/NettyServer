
import java.net.InetSocketAddress;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;


public class NettyServer {

    NettyServerHandler nettyServerHandler = new NettyServerHandler();
    ChannelHandlerContext ctx;

    public void start(InetSocketAddress socketAddress){

        EventLoopGroup eventGroup = new NioEventLoopGroup(1);
        EventLoopGroup workGroup = new NioEventLoopGroup();
        
        ServerBootstrap bootstrap = new ServerBootstrap()
        .group(eventGroup, workGroup)
        .channel(NioServerSocketChannel.class)
        .childHandler(new ServerChannelInitializer())
        .localAddress(socketAddress)
        .option(ChannelOption.SO_BACKLOG, 1024)
        .childOption(ChannelOption.SO_REUSEADDR, true)
        .childOption(ChannelOption.SO_KEEPALIVE, true)
        .childOption(ChannelOption.TCP_NODELAY, true)
        .childOption(ChannelOption.SO_SNDBUF, 32000)
        .childOption(ChannelOption.SO_RCVBUF, 32000)
        .childOption(ChannelOption.TCP_NODELAY, true)
        //other options below
//        .option(ChannelOption.SO_BACKLOG, 128)
//        .childOption(ChannelOption.SO_KEEPALIVE, true)
//        .childOption(ChannelOption.WRITE_BUFFER_WATER_MARK, new WriteBufferWaterMark(32 * 1024, 64 * 1024))
//        .childOption(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator(64, 1024, 65536))
//        .childOption(ChannelOption.AUTO_READ, false)
//        .childOption(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
//        .childOption(ChannelOption.TCP_NODELAY, true)
//        .childOption(ChannelOption.WRITE_SPIN_COUNT, 16)
//        .childOption(ChannelOption.ALLOW_HALF_CLOSURE, false)
//        .childOption(ChannelOption.MESSAGE_SIZE_ESTIMATOR, DefaultMessageSizeEstimator.DEFAULT)
//        .childOption(ChannelOption.SO_REUSEADDR, true)
//        .childOption(ChannelOption.SO_SNDBUF, 1048576)
//        .childOption(ChannelOption.SO_RCVBUF, 1048576)
//        .childOption(ChannelOption.SO_LINGER, -1)
//        .childOption(ChannelOption.SO_KEEPALIVE, true)
//        .childOption(ChannelOption.WRITE_BUFFER_WATER_MARK, new WriteBufferWaterMark(32 * 1024, 64 * 1024))
//        .childOption(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator(64, 1024, 65536))
//        .childOption(ChannelOption.AUTO_READ, false)
//        .childOption(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
//        .childOption(ChannelOption.TCP_NODELAY, true)
//        .childOption(ChannelOption.WRITE_SPIN_COUNT, 16)
//        .childOption(ChannelOption.ALLOW_HALF_CLOSURE, false)
//        .childOption(ChannelOption.MESSAGE_SIZE_ESTIMATOR, DefaultMessageSizeEstimator.DEFAULT)
       // .handler(new NettyServerHandler())
        .childHandler(new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
               
                // pipeline code
                ChannelPipeline channelPipeline = ch.pipeline();
                channelPipeline.addLast(new StringDecoder());
                channelPipeline.addLast(new StringEncoder());
                channelPipeline.addLast(nettyServerHandler);
                
            }
            
        })

        .localAddress(socketAddress)
        //set queue size
        .option(ChannelOption.SO_BACKLOG, 1024)
        // inactivity detection
        .childOption(ChannelOption.SO_KEEPALIVE, true);

        try {
            ChannelFuture future = bootstrap.bind(socketAddress).sync().await();
            System.out.println("\nPlease start the game:");
            //System.out.print("Server is listening> "+ socketAddress.getPort());
            future.channel().closeFuture().sync();
            
        } catch (InterruptedException e) {

            System.out.println(e.getMessage());
            
        } finally {
            //close the main thread group
            eventGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }

    }

}


