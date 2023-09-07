import java.io.IOException;
import java.net.BindException;
import java.net.ConnectException;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.ReadTimeoutException;

public class MyHandler extends ChannelInboundHandlerAdapter  {
	
	private static final Logger LOGGER = Logger.getLogger(ServerChannelInitializer.class.getName());
	
	 @Override
	  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
	          throws Exception {
	      if (cause instanceof ReadTimeoutException) {
	          // do something
	    	  System.out.print("TimeOut Handler removed");
	    	  ctx.close();
	      } else {
	          super.exceptionCaught(ctx, cause);
	      }
	      
	      if (cause instanceof IOException && "Connection reset by peer".equals(cause.getMessage())) {
              // Handle connection reset by peer exception
              // e.g. log error message and close the channel
              LOGGER.log(Level.SEVERE,"Connection reset by peer: {}", cause.getMessage());
              ctx.close();
            } else if (cause instanceof BindException) {
              // Handle bind exception
              // e.g. log error message and shutdown the server
              LOGGER.log(Level.SEVERE,"Bind exception: {}", cause.getMessage());
            } else if (cause instanceof ConnectException) {
              // Handle connect exception
              // e.g. log error message and retry connection
              LOGGER.log(Level.SEVERE,"Connect exception: {}", cause.getMessage());
              // Retry connection logic here...
            } else {
              // Handle other exceptions
              // e.g. log error message and close the channel
              LOGGER.log(Level.SEVERE,"Unexpected exception: {}", cause.getMessage());
              ctx.close();
            }
	      
	  }

}
