

import java.io.FileWriter;
import java.io.IOException;
import java.net.BindException;
import java.net.ConnectException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.RejectedExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.CharsetUtil;


@Sharable
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {
	
	private static final Logger LOGGER = Logger.getLogger(ServerChannelInitializer.class.getName());
	private static final int READ_IDLE_TIME_SECONDS = 30; // Define the idle timeout in seconds

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        // 
        ch.pipeline().addLast("decoder", (ChannelHandler) new StringDecoder(CharsetUtil.UTF_8));
        ch.pipeline().addLast("encoder", (ChannelHandler) new StringEncoder(CharsetUtil.UTF_8));
        
        ch.pipeline().addLast(new NettyServerHandler());
        ch.pipeline().addLast(new IdleStateHandler(READ_IDLE_TIME_SECONDS, 0, 0));
        ch.pipeline().addLast("readTimeoutHandler", new ReadTimeoutHandler(30));
        ch.pipeline().addLast("myHandler", new MyHandler());
        
        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
          @Override
            public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws IOException{
              
        	  if (cause instanceof IOException && "Connection reset by peer".equals(cause.getMessage())) {
                  // Handle connection reset by peer exception
                  // e.g. log error message and close the channel
                  LOGGER.log(Level.SEVERE,"Connection reset by peer: {}", cause.getMessage());
                  FileWriter writer = new FileWriter("serverlog.txt");
      			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
      		    LocalDateTime now = LocalDateTime.now();
      			writer.write("DATE: " + dtf.format(now) + " MESSAGE " + cause.getMessage());
      			writer.close();
                  ctx.close();
                } else if (cause instanceof BindException) {
                  // Handle bind exception
                  // e.g. log error message and shutdown the server
                  LOGGER.log(Level.SEVERE,"Bind exception: {}", cause.getMessage());
                  FileWriter writer = new FileWriter("serverlog.txt");
      			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
      		    LocalDateTime now = LocalDateTime.now();
      			writer.write("DATE: " + dtf.format(now) + " MESSAGE " + cause.getMessage());
      			writer.close();
                } else if (cause instanceof ConnectException) {
                  // Handle connect exception
                  // e.g. log error message and retry connection
                  LOGGER.log(Level.SEVERE,"Connect exception: {}", cause.getMessage());
                  FileWriter writer = new FileWriter("serverlog.txt");
      			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
      		    LocalDateTime now = LocalDateTime.now();
      			writer.write("DATE: " + dtf.format(now) + " MESSAGE " + cause.getMessage());
      			writer.close();
                  // Retry connection logic here...
                } else if(cause instanceof java.io.IOException && "Connection timed out".equals(cause.getMessage())){

        			FileWriter writer = new FileWriter("serverlog.txt");
        			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
        		    LocalDateTime now = LocalDateTime.now();
        			writer.write("DATE: " + dtf.format(now) + " MESSAGE " + cause.getMessage());
        			writer.close();
        		}else {
                  // Handle other exceptions
                  // e.g. log error message and close the channel
                  LOGGER.log(Level.SEVERE,"Unexpected exception: {}", cause.getMessage());
                  ctx.close();
                }
            	
            }
        });
        
    }
    
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof RejectedExecutionException) {
            // Handle the rejected task
            // For example, return an error response to the client
//            ctx.channel().writeAndFlush("HTTP/1.1 503 Service Unavailable\r\n" +
//                    "Content-Length: 0\r\n" +
//                    "Connection: close\r\n\r\n");
        	System.out.print("RejectedExecutionException");
        } else {
            // Handle other exceptions
            super.exceptionCaught(ctx, cause);
        }
    }
    
    
}




