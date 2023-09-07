import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Sharable
public class NettyServerHandler extends ChannelInboundHandlerAdapter implements AutoCloseable, ChannelInboundHandler  {

	private static final ConcurrentHashMap<String, Channel> sessions = new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<String, String> sessionKeys = new ConcurrentHashMap<>();
    private static final int MAX_RETRIES = 3;
    private static final int RETRY_DELAY = 1000; // milliseconds
    private static String HOST_NAME = "69.49.228.42";
    private static int HOST_PORT = 2020;
	
	private ScheduledExecutorService executorService;
	private ScheduledFuture<?> scheduledFuture;
	private ConcurrentHashMap<String, ScheduledFuture<?>> futureMap;
	
	public NettyServerHandler() {
		executorService = Executors.newSingleThreadScheduledExecutor();
		futureMap = new ConcurrentHashMap<>();
	}
	
	ExecutorService executorx = new ThreadPoolExecutor(
		    10, // Number of threads in the pool
		    18, // Maximum number of threads in the pool
		    0L, // Time to keep idle threads alive
		    TimeUnit.MILLISECONDS, // Time unit for idle thread keep-alive time
		    new LinkedBlockingQueue<Runnable>() // Task queue
		);
	ScheduledExecutorService executor = Executors.newScheduledThreadPool(18);

	private NotifyGameOffline offlineObject = new NotifyGameOffline();
	private NotifyGameOnline onlineObject = new NotifyGameOnline();
	private AutoInsert autoinsert = new AutoInsert();
	//Timer timer1 = new Timer();
	
	MyJobs gameJobs = new MyJobs();

	public void channelRegisterd(ChannelHandlerContext ctx) {
		ctx.channel().attr(null).set(ctx);
	}

	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		
		//System.out.println("Channel is  active : Channel" + ctx);
	
	}

	private void handleConnectionTimeout() throws IOException {
        int retryCount = 0;
        boolean success = false;

        while (retryCount < MAX_RETRIES && !success) {
            try {
                TimeUnit.MILLISECONDS.sleep(RETRY_DELAY);
                System.out.println("Retrying connection...");
                NettyServer nettyServer = new NettyServer();
                nettyServer.start(new InetSocketAddress(HOST_NAME, HOST_PORT));
                
                success = true;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
            retryCount++;
        }

        if (!success) {
            System.err.println("Failed to establish connection after multiple retries");
            FileWriter writer = new FileWriter("log.txt",true);
            writer.write("Message: failed to establish connection after multiple retries" + "\n");
            writer.close();
            // Handle the failure case appropriately, such as logging an error or terminating the application
        }
    }
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// Handle SocketException
		if (cause instanceof SocketException && "Connection reset".equals(cause.getMessage())) {
			// Implement your own logic here
			System.out.print("Connetion reset by user");
			FileWriter writer = new FileWriter("serverlog.txt");
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
		    LocalDateTime now = LocalDateTime.now();
			writer.write("DATE: " + dtf.format(now) + " MESSAGE " + cause.getMessage());
			writer.close();
			ctx.close();
		} else if(cause instanceof IOException && "Connection reset by peer".equals(cause.getMessage())){
			System.err.print("Messaege" + cause.getMessage());
			FileWriter writer = new FileWriter("serverlog.txt");
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
		    LocalDateTime now = LocalDateTime.now();
			writer.write("DATE: " + dtf.format(now) + " MESSAGE " + cause.getMessage());
			writer.close();
		} else if(cause instanceof java.io.IOException && "Connection timed out".equals(cause.getMessage())){
			//handleConnectionTimeout(); // restart the application
			removeChannelFromSessionsAndHandlers(ctx);
			System.err.print("Messaege" + cause.getMessage());
			FileWriter writer = new FileWriter("serverlog.txt");
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
		    LocalDateTime now = LocalDateTime.now();
			writer.write("DATE: " + dtf.format(now) + " MESSAGE " + cause.getMessage());
			writer.close();
		}else if(cause instanceof java.io.IOException && "No route to host".equals(cause.getMessage())){
			System.err.print("Messaege" + cause.getMessage());
			handleConnectionTimeout(); // restart the application
			FileWriter writer = new FileWriter("serverlog.txt");
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
		    LocalDateTime now = LocalDateTime.now();
			writer.write("DATE: " + dtf.format(now) + " MESSAGE " + cause.getMessage());
			writer.close();
		}else {
			// Handle other exceptions
			super.exceptionCaught(ctx, cause);
		}
	}
	
	@SuppressWarnings("unlikely-arg-type")
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		
		OkHttpClient client = new OkHttpClient();
		String clientIdd = ctx.channel().id().asLongText().toString();
		String gamehash = sessionKeys.get(clientIdd);
		
		//close the session when game is offline
		offlineObject.NotifyOffline(gamehash);
		Request delsessClientrequest = new Request.Builder().url("http://69.49.228.42/1kball/folder/delSession.php?gamehash=" + gamehash ).build();
		try (Response delsessClientresponse = client.newCall(delsessClientrequest).execute()) {
			//System.out.print(delsessClientresponse.body().string());
		delsessClientresponse.body().close();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.print("SESS " + e.getMessage() );
		}
			
		
		
        if (futureMap.containsKey(clientIdd)) {
            futureMap.get(clientIdd).cancel(false);
            futureMap.remove(clientIdd);
        }
			
		 String gameid =  ctx.channel().id().asLongText().toString();
         if(!sessionKeys.containsKey(gameid))return;
         String thegamehash = sessionKeys.get(gameid);
         if(!sessions.containsKey(thegamehash))return;
         sessionKeys.remove(gameid);
         sessions.remove(thegamehash);
         sessions.values().remove(thegamehash);

         ctx.pipeline().remove(this);
         super.channelInactive(ctx);
		
			
	}
	
	public void removeChannelFromSessionsAndHandlers(ChannelHandlerContext ctx) {
		
		String clientIdd = ctx.channel().id().asLongText().toString();
		
        if (futureMap.containsKey(clientIdd)) {
            futureMap.get(clientIdd).cancel(false);
            futureMap.remove(clientIdd);
        }
			
		 String gameid =  ctx.channel().id().asLongText().toString();
         if(!sessionKeys.containsKey(gameid))return;
         String thegamehash = sessionKeys.get(gameid);
         if(!sessions.containsKey(thegamehash))return;
         sessionKeys.remove(gameid);
         sessions.remove(thegamehash);
         ctx.pipeline().remove(this);
		
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        			  
        	            String message = msg.toString();
        	            System.out.print(message);
        	            if(!message.contains("&")){
        	                
        	            	if(message.length() > 20) {
        	            	
        	                	if(sessions.containsKey(message)) {
        	                		return; //do not repeat the key
        	                	}else {
        	                		
        	                		String clientIdd = ctx.channel().id().asLongText().toString();
        	                		onlineObject.NotifyOnline(message);
            	                    sessions.putIfAbsent(message,ctx.channel());
            	                    sessionKeys.put(clientIdd,message);
            	        			
            	        			if (futureMap.containsKey(clientIdd)) {
            	        	            futureMap.get(clientIdd).cancel(false);
            	        	        }
            	        			
            	        			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            	        	        
            	        	        // Create a new execution service for the client
            	        	        scheduledFuture = executorService.scheduleAtFixedRate(() -> {
            	        	        	
        	        	     		LocalDateTime currTime = LocalDateTime.now();
        	        	     		
        	        	     		if(Arrays.asList(new TimeSet().time1x0).contains(formatter.format(currTime))) {
    	        	     			 String message1x0 = formatter.format(currTime);
    	        	     			 //System.out.println("Sending message: " + formatter.format(currTime));
    	        	     			 try {
										gameJobs.runnerGame(message1x0, sessions,sessionKeys.get(clientIdd));
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
    	        	     			 return;
        	        	     		}
        	        	     		
        	        	     		 //###-------------------------------------------------###//
        	        	             
        	        	     		if(Arrays.asList(new TimeSet().time1x5).contains(formatter.format(currTime))) {
       	        	     			 String message1x5 = formatter.format(currTime);
       	        	     			 //System.out.println("Sending message: " + formatter.format(currTime));
       	        	     			 try {
										gameJobs.runnerGame(message1x5, sessions,sessionKeys.get(clientIdd));
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
       	        	     			 return;
       	        	     		    }

            	        	        }, 0, 1, TimeUnit.SECONDS);
            	        	        futureMap.put(clientIdd, scheduledFuture);
        	                		
        	                	}
        	                	
        	            	}// this is not a hash but 888 response
        	                
        	                
        	            }else{
        	               
        	                if("888".equals(message)){
        	                    return;
        	                }
        	                String[] openData = message.split("&");
        	                if(openData.length!=5){
        	                    return;
        	                }
        	                
        	                String drawNum = openData[0].substring(1);
        	                String count = openData[1];
        	                String date = openData[2];
        	                String time = openData[3];
        	                String gameID = openData[4];
        	                
        	               
    	                	DateTimeFormatter dtft = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
        					LocalDateTime now = LocalDateTime.now();
        					
    						try {
								autoinsert.addRecord(gameID,date,time,drawNum,count,dtft.format(now),"Instant server insertion" + "Time: " + time + "\n");
							} catch (InstantiationException e) {
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							} catch (ClassNotFoundException e) {
								e.printStackTrace();
							}
        	                	
        	                   
        	            }
        	            
        	        //}).start();
        		   
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	public void sendMsgCommand5(ChannelHandlerContext ctx, String message) {
		try {
			ctx.write(message);
			ctx.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendMsgCommandd3(ChannelHandlerContext ctx, String message) {
		try {
			// ChannelHandlerContext = sessions.get("verifyCode");
			ctx.write(message);
			ctx.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub

	}

	public void checkInternetConnection(ChannelHandlerContext ctx) {
        if (ctx != null && ctx.channel().isActive()) {
            ChannelFuture future = ctx.writeAndFlush("ping");
            future.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws IOException {
                    if (!future.isSuccess()) {
                        // Handle lost Internet connection
                    	System.out.println("Client lost internet connection");
                    	FileWriter writer = new FileWriter("serverlog.txt");
            			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
            		    LocalDateTime now = LocalDateTime.now();
            			writer.write("DATE: " + dtf.format(now) + " MESSAGE: The client lost internet connection");
            			writer.close();
                    }
                }
            });
        }
    }

  @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (e.state() == IdleState.READER_IDLE) {
                // Client is idle, consider it offline and close the channel
            	System.out.println("Client lost internet connection");
                ctx.close();
            }
        }
        super.userEventTriggered(ctx, evt);
    }
	
}

