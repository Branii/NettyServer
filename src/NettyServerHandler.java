import java.io.FileWriter;
import java.io.IOException;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Sharable
public class NettyServerHandler extends ChannelInboundHandlerAdapter implements AutoCloseable {

	private static final ConcurrentHashMap<String, ChannelHandlerContext> sessions = new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<String, String> sessionKeys = new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<String, ChannelGroup> sessionId = new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<String, String> getClientId = new ConcurrentHashMap<>();

	//private static final ConcurrentHashMap<String, String> SAVED_GAME_DRAW = new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<String, String> SERVER_GAME_DRAW = new ConcurrentHashMap<>();
//	private static final ConcurrentHashMap<String, String> SERVER_RESPONSE = new ConcurrentHashMap<>();

	private static final ConcurrentHashMap<String, String> DRAWDATE = new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<String, String> DRAWTIME = new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<String, String> DRAWCOUNT = new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<String, String> DRAWNUMBER = new ConcurrentHashMap<>();

	ScheduledExecutorService executor = Executors.newScheduledThreadPool(18);
	ScheduledExecutorService executor1 = Executors.newScheduledThreadPool(18);
	private NotifyGameOffline offlineObject = new NotifyGameOffline();
	private NotifyGameOnline onlineObject = new NotifyGameOnline();
	private AutoInsert autoinsert = new AutoInsert();
	
	private String gameid;

	public void channelRegisterd(ChannelHandlerContext ctx) {
		ctx.channel().attr(null).set(ctx);
	}

	
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		
		///1
		new Thread(new Runnable() {

			@Override
			public void run() {
				
				Runnable T1 = new Runnable() {
					
					OkHttpClient client = new OkHttpClient();
					Request request;

					@SuppressWarnings("unused")
					@Override
					public void run() {
						
						 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
						 DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
				         DateTimeFormatter ss = DateTimeFormatter.ofPattern("ss");
				         LocalDateTime currTime = LocalDateTime.now();
				         
				         if(ss.format(currTime).toString().equals("00")) {
						
						 String url1 = "http://69.49.228.42/1kball/folder/getdraws.php?gameId=10001&time=" + formatter.format(currTime);
			             request = new Request.Builder().url(url1).build();
			             
			             try (Response response1 = client.newCall(request).execute()){
			            	String data = response1.body().string();
			            	String[] splited = data.split("#");
			            	System.out.println("DATA: " +  data);
			                response1.body().close();
			                
			                if(data.length() > 42 ) {     
			                	
			                	 String gamehashkey = sessionKeys.get(splited[4]);
	                             if(gamehashkey == null){
	                             System.out.println("Game hash is null");
	                             }else{
	                             	
	                         	DRAWDATE.put(splited[4], splited[1]);
								DRAWTIME.put(splited[4], splited[3]);
								DRAWCOUNT.put(splited[4], splited[2]);
								DRAWNUMBER.put(splited[4], splited[0]);
									
								 ChannelGroup sesstionctx = sessionId.get(splited[4]);
	                             if(sesstionctx == null){
	                             //client exit the game
	                             	System.out.println("Null channel");
	                             
	                             }else{
	                             
	                             sesstionctx.write("@"+splited[0]+"&11971&"+splited[1]+"&10009");
	                             sesstionctx.flush();
									
	                             executor1.schedule(()->{
	                             	
	                             if(sesstionctx == null) {
	                            	 return;
	                             }else {
	                            	 
	                            	DateTimeFormatter dtft = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
	 								LocalDateTime now = LocalDateTime.now();

										//autoinsert.addRecord("10001",DRAWDATE.get(splited[4]),DRAWTIME.get(splited[4]),DRAWNUMBER.get(splited[4]),DRAWCOUNT.get(splited[4]),dtft.format(now));
										String url11 = "http://69.49.228.42/1kball/folder/insert.php?gameid=10001&drawDate=" + DRAWDATE.get(splited[4]) + "&drawTime=" + DRAWTIME.get(splited[4]) + "&drawCount=" + DRAWCOUNT.get(splited[4])+"&drawNumber="+ DRAWNUMBER.get(splited[4]);
							            request = new Request.Builder().url(url11).build();
							            try (Response response11 = client.newCall(request).execute()){
							                System.out.println(response11.body().string());
							                response11.body().close();
							                
							            } catch (Exception e) {
							                System.out.println(e.getMessage());
							            }
									
	 								
	                            	 
	                             }

	                             },28,TimeUnit.SECONDS);
	                             
	                             }
	                           }
			                	
			                }else {
			                	System.out.print("");
			                }
			                
			            } catch (Exception e) {
			                System.out.println(e.getMessage());//-------------------------------------------retry
			                DateTimeFormatter dtff = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
							LocalDateTime now = LocalDateTime.now();
							try {
								FileWriter writer = new FileWriter("log.txt",true);
								writer.write("DATE: " + dtff.format(now)+ " Message: " + e.getMessage()+ "\n");
								writer.close();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
			            }
				      }// if the time is equal to the system time then send request to random box to get number
						
					}
					
				};executor.scheduleAtFixedRate(T1, 0, 1, TimeUnit.SECONDS);
				
			}
			
		}).start();
		
		//2
		new Thread(new Runnable() {

			@Override
			public void run() {
				
					Runnable T2 = new Runnable() {
					
					OkHttpClient client = new OkHttpClient();
					Request request;

					@SuppressWarnings("unused")
					@Override
					public void run() {
						
						 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
				         LocalDateTime currTime = LocalDateTime.now();
				         if(Arrays.asList(new TimeSet().time5x0).contains(formatter.format(currTime))) {
						
						String url2 = "http://69.49.228.42/1kball/folder/getdraws.php?gameId=10002&time=" + formatter.format(currTime);
			            request = new Request.Builder().url(url2).build();
			            try (Response response2 = client.newCall(request).execute()){
			            	String data = response2.body().string();
			            	String[] splited = data.split("#");
			            	//System.out.println("DATA: " +  data);
			                response2.body().close();
			                
			                if(data.length() > 42 ) {
			                	
			                	String gamehashkey = sessionKeys.get(splited[4]);
	                             if(gamehashkey == null){
	                             //client exit the game
	                             }else{
	                             	
	                         	DRAWDATE.put(splited[4], splited[1]);
								DRAWTIME.put(splited[4], splited[3]);
								DRAWCOUNT.put(splited[4], splited[2]);
								DRAWNUMBER.put(splited[4], splited[0]);
									
								 ChannelGroup sesstionctx = sessionId.get(splited[4]);
	                             if(sesstionctx == null){
	                             //client exit the game
	                             	System.out.println("null channel");
	                             
	                             }else{
	                             
	                             sesstionctx.write("@"+splited[0]+"&11971&"+splited[1]+"&10009");
	                             sesstionctx.flush();
									
	                             executor1.schedule(()->{
	                            		 
	                            	 
	                            	 if(sesstionctx == null) {
		                            	 return;
		                             }else {
		                            	 
		                            	DateTimeFormatter dtft = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
		 								LocalDateTime now = LocalDateTime.now();
		 								
											//autoinsert.addRecord("10002",DRAWDATE.get(splited[4]),DRAWTIME.get(splited[4]),DRAWNUMBER.get(splited[4]),DRAWCOUNT.get(splited[4]),dtft.format(now));
											String url22 = "http://69.49.228.42/1kball/folder/insert.php?gameid=10002&drawDate=" + DRAWDATE.get(splited[4]) + "&drawTime=" + DRAWTIME.get(splited[4]) + "&drawCount=" + DRAWCOUNT.get(splited[4])+"&drawNumber="+ DRAWNUMBER.get(splited[4]);
								            request = new Request.Builder().url(url22).build();
								            try (Response response22 = client.newCall(request).execute()){
								                System.out.println(response22.body().string());
								                response22.body().close();
								                
								            } catch (Exception e) {
								                System.out.println(e.getMessage());
								            }
		                            	 
		                             }	 
	                            	 
	                            	 
	                             },28,TimeUnit.SECONDS);
	                             
	                             }
	                           }
			                	
			                }else {
			                	System.out.print("");
			                }
			                
			            } catch (Exception e) {
			                System.out.println(e.getMessage());
			                DateTimeFormatter dtff = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
							LocalDateTime now = LocalDateTime.now();
							try {
								FileWriter writer = new FileWriter("log.txt",true);
								writer.write("10002 DATE: " + dtff.format(now)+ " Message: " + e.getMessage()+ "\n");
								writer.close();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
			            }
						
				         }//end of timeset
						
					}
					
				};executor.scheduleAtFixedRate(T2, 0, 1, TimeUnit.SECONDS);
				
				
			}
			
		}).start();
		
		//3
		new Thread(new Runnable() {

			@Override
			public void run() {
				
					Runnable T3 = new Runnable() {
					
					OkHttpClient client = new OkHttpClient();
					Request request;

					@SuppressWarnings("unused")
					@Override
					public void run() {
						
						
						 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
						 DateTimeFormatter ss = DateTimeFormatter.ofPattern("ss");
				         LocalDateTime currTime = LocalDateTime.now();

				         if(ss.format(currTime).toString().equals("00")) {
				        	 
							String url3 = "http://69.49.228.42/1kball/folder/getdraws.php?gameId=10003&time=" + formatter.format(currTime);
				            request = new Request.Builder().url(url3).build();
				            
			                try (Response response3 = client.newCall(request).execute()){
			            	String data = response3.body().string();
			            	String[] splited = data.split("#");
			            	//System.out.println("DATA: " +  data);
			                response3.body().close();
			                
			                if(data.length() > 42 ) {
			                	
			                	String gamehashkey = sessionKeys.get(splited[4]);
	                             if(gamehashkey == null){
	                             //client exit the game
	                             }else{
	                             	
	                         	DRAWDATE.put(splited[4], splited[1]);
								DRAWTIME.put(splited[4], splited[3]);
								DRAWCOUNT.put(splited[4], splited[2]);
								DRAWNUMBER.put(splited[4], splited[0]);
									
								 ChannelGroup sesstionctx = sessionId.get(splited[4]);
	                             if(sesstionctx == null){
	                             //client exit the game
	                             	System.out.println("null channel");
	                             
	                             }else{
	                             
	                             sesstionctx.write("@"+splited[0]+"&11971&"+splited[1]+"&10009");
	                             sesstionctx.flush();
	                             
									
	                             executor1.schedule(()->{
	                            		 
	                            	
	                            	 if(sesstionctx == null) {
		                            	 return;
		                             }else {
		                            	 
		                            	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
		 								LocalDateTime now = LocalDateTime.now();
		 								
											//autoinsert.addRecord("10003",DRAWDATE.get(splited[4]),DRAWTIME.get(splited[4]),DRAWNUMBER.get(splited[4]),DRAWCOUNT.get(splited[4]),dtf.format(now));
											String url33 = "http://69.49.228.42/1kball/folder/insert.php?gameid=10003&drawDate=" + DRAWDATE.get(splited[4]) + "&drawTime=" + DRAWTIME.get(splited[4]) + "&drawCount=" + DRAWCOUNT.get(splited[4])+"&drawNumber="+ DRAWNUMBER.get(splited[4]);
								            request = new Request.Builder().url(url33).build();
								            try (Response response33 = client.newCall(request).execute()){
								                System.out.println(response33.body().string());
								                response33.body().close();
								                
								            } catch (Exception e) {
								                System.out.println(e.getMessage());
								            }
		 								
		                            	 
		                             }		 
	                            	 
	                            	 
	                             },28,TimeUnit.SECONDS);
	                             
	                             }
	                           }
			                	
			                }else {
			                	System.out.print("");
			                }
			                
			            } catch (Exception e) {
			                System.out.println(e.getMessage());
			                DateTimeFormatter dtft = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
							LocalDateTime now = LocalDateTime.now();
							try {
								FileWriter writer = new FileWriter("log.txt",true);
								writer.write("10003 DATE: " + dtft.format(now)+ " Message: " + e.getMessage()+ "\n");
								writer.close();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
			            }
						
				         }//end 
						
					}
					
				};executor.scheduleAtFixedRate(T3, 0, 1, TimeUnit.SECONDS);
				
				
			}
			
		}).start();

		//4
		new Thread(new Runnable() {

			@Override
			public void run() {
				
					Runnable T4 = new Runnable() {
					
					OkHttpClient client = new OkHttpClient();
					Request request;

					@SuppressWarnings("unused")
					@Override
					public void run() {
						
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
						DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
						LocalDateTime currTime = LocalDateTime.now();
						if(Arrays.asList(new TimeSet().time3x0).contains(formatter.format(currTime))) {
						
						String url4 = "http://69.49.228.42/1kball/folder/getdraws.php?gameId=10004&time=" + formatter.format(currTime);
			            request = new Request.Builder().url(url4).build();
			            try (Response response4 = client.newCall(request).execute()){
			            	String data = response4.body().string();
			            	String[] splited = data.split("#");
			            	//System.out.println("DATA: " +  data);
			                response4.body().close();
			                
			                if(data.length() > 42 ) {
			                	
			                	String gamehashkey = sessionKeys.get(splited[4]);
	                             if(gamehashkey == null){
	                             //client exit the game
	                             }else{
	                             	
	                         	DRAWDATE.put(splited[4], splited[1]);
								DRAWTIME.put(splited[4], splited[3]);
								DRAWCOUNT.put(splited[4], splited[2]);
								DRAWNUMBER.put(splited[4], splited[0]);
									
								 ChannelGroup sesstionctx = sessionId.get(splited[4]);
	                             if(sesstionctx == null){
	                             //client exit the game
	                             	System.out.println("null channel");
	                             
	                             }else{
	                             
	                             sesstionctx.write("@"+splited[0]+"&11971&"+splited[1]+"&10009");
	                             sesstionctx.flush();
									
	                             executor1.schedule(()->{
	                            		 
	                            	 if(sesstionctx == null) {
		                            	 return;
		                             }else {
		                            	 
		                            	DateTimeFormatter dtft = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
		 								LocalDateTime now = LocalDateTime.now();
		 								
											//autoinsert.addRecord("10004",DRAWDATE.get(splited[4]),DRAWTIME.get(splited[4]),DRAWNUMBER.get(splited[4]),DRAWCOUNT.get(splited[4]),dtft.format(now));
											String url44 = "http://69.49.228.42/1kball/folder/insert.php?gameid=10004&drawDate=" + DRAWDATE.get(splited[4]) + "&drawTime=" + DRAWTIME.get(splited[4]) + "&drawCount=" + DRAWCOUNT.get(splited[4])+"&drawNumber="+ DRAWNUMBER.get(splited[4]);
								            request = new Request.Builder().url(url44).build();
								            try (Response response44 = client.newCall(request).execute()){
								                System.out.println(response44.body().string());
								                response44.body().close();
								                
								            } catch (Exception e) {
								                System.out.println(e.getMessage());
								            }
		                             }
	                            	 
	                             },28,TimeUnit.SECONDS);
	                             
	                             }
	                           }
			                	
			                }else {
			                	System.out.print("");
			                }
			                
			            } catch (Exception e) {
			                System.out.println(e.getMessage());
			                DateTimeFormatter dtfd = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
							LocalDateTime now = LocalDateTime.now();
							try {
								FileWriter writer = new FileWriter("log.txt",true);
								writer.write("10004 DATE: " + dtfd.format(now)+ " Message: " + e.getMessage()+ "\n");
								writer.close();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
			            }
						
						}//end of timerset
						
					}
					
				};executor.scheduleAtFixedRate(T4, 0, 1, TimeUnit.SECONDS);
				
				
			}
			
		}).start();
		
		//5
		new Thread(new Runnable() {

			@Override
			public void run() {
				
					Runnable T5 = new Runnable() {
					
					OkHttpClient client = new OkHttpClient();
					Request request;

					@SuppressWarnings("unused")
					@Override
					public void run() {
						
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
						DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
						LocalDateTime currTime = LocalDateTime.now();
						if(Arrays.asList(new TimeSet().time1x5).contains(formatter.format(currTime))) {
						
						String url5 = "http://69.49.228.42/1kball/folder/getdraws.php?gameId=10005&time=" + formatter.format(currTime);
			            request = new Request.Builder().url(url5).build();
			            try (Response response5 = client.newCall(request).execute()){
			            	String data = response5.body().string();
			            	String[] splited = data.split("#");
			            	//System.out.println("DATA: " +  data);
			                response5.body().close();
			                
			                if(data.length() > 42 ) {
			                	
			                	String gamehashkey = sessionKeys.get(splited[4]);
	                             if(gamehashkey == null){
	                             //client exit the game
	                             }else{
	                             	
	                         	DRAWDATE.put(splited[4], splited[1]);
								DRAWTIME.put(splited[4], splited[3]);
								DRAWCOUNT.put(splited[4], splited[2]);
								DRAWNUMBER.put(splited[4], splited[0]);
									
								 ChannelGroup sesstionctx = sessionId.get(splited[4]);
	                             if(sesstionctx == null){
	                             //client exit the game
	                             	System.out.println("null channel");
	                             
	                             }else{
	                             
	                             sesstionctx.write("@"+splited[0]+"&11971&"+splited[1]+"&10009");
	                             sesstionctx.flush();
									
	                             executor1.schedule(()->{
	                            		 
	                            	 if(sesstionctx == null) {
		                            	 return;
		                             }else {
		                            	 
		                            	DateTimeFormatter dtft = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
		 								LocalDateTime now = LocalDateTime.now();
		 							
											//autoinsert.addRecord("10005",DRAWDATE.get(splited[4]),DRAWTIME.get(splited[4]),DRAWNUMBER.get(splited[4]),DRAWCOUNT.get(splited[4]),dtft.format(now));
											String url55 = "http://69.49.228.42/1kball/folder/insert.php?gameid=10005&drawDate=" + DRAWDATE.get(splited[4]) + "&drawTime=" + DRAWTIME.get(splited[4]) + "&drawCount=" + DRAWCOUNT.get(splited[4])+"&drawNumber="+ DRAWNUMBER.get(splited[4]);
								            request = new Request.Builder().url(url55).build();
								            try (Response response55 = client.newCall(request).execute()){
								                System.out.println(response55.body().string());
								                response55.body().close();
								                
								            } catch (Exception e) {
								                System.out.println(e.getMessage());
								            }
		                            	 
		                             }	
	                            	 
	                             },28,TimeUnit.SECONDS);
	                             
	                             }
	                           }
			                	
			                }else {
			                	System.out.print("");
			                }
			                
			            } catch (Exception e) {
			                System.out.println(e.getMessage());
			                DateTimeFormatter dtfz = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
							LocalDateTime now = LocalDateTime.now();
							try {
								FileWriter writer = new FileWriter("log.txt",true);
								writer.write("10005 DATE: " + dtfz.format(now)+ " Message: " + e.getMessage()+ "\n");
								writer.close();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
			            }
						}//end of timerset
						
					}
					
				};executor.scheduleAtFixedRate(T5, 0, 1, TimeUnit.SECONDS);
				
				
			}
			
		}).start();

		//6
		new Thread(new Runnable() {

			@Override
			public void run() {
				
					Runnable T6 = new Runnable() {
					
					OkHttpClient client = new OkHttpClient();
					Request request;

					@SuppressWarnings("unused")
					@Override
					public void run() {
						
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
						DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
						LocalDateTime currTime = LocalDateTime.now();
						if(Arrays.asList(new TimeSet().time3x0).contains(formatter.format(currTime))) {
						
						String url6 = "http://69.49.228.42/1kball/folder/getdraws.php?gameId=10006&time=" + formatter.format(currTime);
			            request = new Request.Builder().url(url6).build();
			            try (Response response6 = client.newCall(request).execute()){
			            	String data = response6.body().string();
			            	String[] splited = data.split("#");
			            	//System.out.println("DATA: " +  data);
			                response6.body().close();
			                
			                if(data.length() > 42 ) {
			                	
			                	String gamehashkey = sessionKeys.get(splited[4]);
	                             if(gamehashkey == null){
	                             //client exit the game
	                             }else{
	                             	
	                         	DRAWDATE.put(splited[4], splited[1]);
								DRAWTIME.put(splited[4], splited[3]);
								DRAWCOUNT.put(splited[4], splited[2]);
								DRAWNUMBER.put(splited[4], splited[0]);
									
								 ChannelGroup sesstionctx = sessionId.get(splited[4]);
	                             if(sesstionctx == null){
	                             //client exit the game
	                             	System.out.println("null channel");
	                             
	                             }else{
	                             
	                             sesstionctx.write("@"+splited[0]+"&11971&"+splited[1]+"&10009");
	                             sesstionctx.flush();
									
	                             executor1.schedule(()->{
	                            		 
	                            	
	                            	 if(sesstionctx == null) {
		                            	 return;
		                             }else {
		                            	 
		                            	DateTimeFormatter dtft = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
		 								LocalDateTime now = LocalDateTime.now();
		 								
											//autoinsert.addRecord("10006",DRAWDATE.get(splited[4]),DRAWTIME.get(splited[4]),DRAWNUMBER.get(splited[4]),DRAWCOUNT.get(splited[4]),dtft.format(now));
											String url66 = "http://69.49.228.42/1kball/folder/insert.php?gameid=10006&drawDate=" + DRAWDATE.get(splited[4]) + "&drawTime=" + DRAWTIME.get(splited[4]) + "&drawCount=" + DRAWCOUNT.get(splited[4])+"&drawNumber="+ DRAWNUMBER.get(splited[4]);
								            request = new Request.Builder().url(url66).build();
								            try (Response response66 = client.newCall(request).execute()){
								                System.out.println(response66.body().string());
								                response66.body().close();
								                
								            } catch (Exception e) {
								                System.out.println(e.getMessage());
								            }
		                            	 
		                             }		 
	                            	 
	                            	 
	                             },28,TimeUnit.SECONDS);
	                             
	                             }
	                           }
			                	
			                }else {
			                	System.out.print("");
			                }
			                
			            } catch (Exception e) {
			                System.out.println(e.getMessage());
			                DateTimeFormatter dtfx = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
							LocalDateTime now = LocalDateTime.now();
							try {
								FileWriter writer = new FileWriter("log.txt",true);
								writer.write("10006 DATE: " + dtf.format(now)+ " Message: " + e.getMessage()+ "\n");
								writer.close();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
			            }
						}//end of timerset
						
					}
					
				};executor.scheduleAtFixedRate(T6, 0, 1, TimeUnit.SECONDS);
				
				
			}
			
		}).start();

		//7
		new Thread(new Runnable() {

			@Override
			public void run() {
				
					Runnable T7 = new Runnable() {
					
					OkHttpClient client = new OkHttpClient();
					Request request;

					@SuppressWarnings("unused")
					@Override
					public void run() {
						
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
						DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
						LocalDateTime currTime = LocalDateTime.now();
						if(Arrays.asList(new TimeSet().time1x5).contains(formatter.format(currTime))) {
						
						String url7 = "http://69.49.228.42/1kball/folder/getdraws.php?gameId=10007&time=" + formatter.format(currTime);
			            request = new Request.Builder().url(url7).build();
			            try (Response response7 = client.newCall(request).execute()){
			            	String data = response7.body().string();
			            	String[] splited = data.split("#");
			            	//System.out.println("DATA: " +  data);
			                response7.body().close();
			                
			                if(data.length() > 42 ) {
			                	
			                	String gamehashkey = sessionKeys.get(splited[4]);
	                             if(gamehashkey == null){
	                             //client exit the game
	                             }else{
	                             	
	                         	DRAWDATE.put(splited[4], splited[1]);
								DRAWTIME.put(splited[4], splited[3]);
								DRAWCOUNT.put(splited[4], splited[2]);
								DRAWNUMBER.put(splited[4], splited[0]);
									
								 ChannelGroup sesstionctx = sessionId.get(splited[4]);
	                             if(sesstionctx == null){
	                             //client exit the game
	                             	System.out.println("null channel");
	                             
	                             }else{
	                             
	                             sesstionctx.write("@"+splited[0]+"&11971&"+splited[1]+"&10009");
	                             sesstionctx.flush();
									
	                             executor1.schedule(()->{
	                            		 
	                            	
	                            	 if(sesstionctx == null) {
		                            	 return;
		                             }else {
		                            	 
		                            	DateTimeFormatter dtft = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
		 								LocalDateTime now = LocalDateTime.now();
		 								
											//autoinsert.addRecord("10007",DRAWDATE.get(splited[4]),DRAWTIME.get(splited[4]),DRAWNUMBER.get(splited[4]),DRAWCOUNT.get(splited[4]),dtft.format(now));
											String url77 = "http://69.49.228.42/1kball/folder/insert.php?gameid=10007&drawDate=" + DRAWDATE.get(splited[4]) + "&drawTime=" + DRAWTIME.get(splited[4]) + "&drawCount=" + DRAWCOUNT.get(splited[4])+"&drawNumber="+ DRAWNUMBER.get(splited[4]);
								            request = new Request.Builder().url(url77).build();
								            try (Response response77 = client.newCall(request).execute()){
								                System.out.println(response77.body().string());
								                response77.body().close();
								                
								            } catch (Exception e) {
								                System.out.println(e.getMessage());
								            }
		                            	 
		                             }		 
	                            	 
	                            	 
	                             },28,TimeUnit.SECONDS);
	                             
	                             }
	                           }
			                	
			                }else {
			                	System.out.print("");
			                }
			                
			            } catch (Exception e) {
			                System.out.println(e.getMessage());
			                DateTimeFormatter dttf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
							LocalDateTime now = LocalDateTime.now();
							try {
								FileWriter writer = new FileWriter("log.txt",true);
								writer.write("10007 DATE: " + dttf.format(now)+ " Message: " + e.getMessage()+ "\n");
								writer.close();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
			             }
						}//end of time
						
					}
					
				};executor.scheduleAtFixedRate(T7, 0, 1, TimeUnit.SECONDS);
				
				
			}
			
		}).start();

		//8
		new Thread(new Runnable() {

			@Override
			public void run() {
				
					Runnable T8 = new Runnable() {
					
					OkHttpClient client = new OkHttpClient();
					Request request;

					@SuppressWarnings("unused")
					@Override
					public void run() {
						
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
						DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
						LocalDateTime currTime = LocalDateTime.now();
						if(Arrays.asList(new TimeSet().time1x5).contains(formatter.format(currTime))) {
						
						String url8 = "http://69.49.228.42/1kball/folder/getdraws.php?gameId=10008&time=" + formatter.format(currTime);
			            request = new Request.Builder().url(url8).build();
			            try (Response response8 = client.newCall(request).execute()){
			            	String data = response8.body().string();
			            	String[] splited = data.split("#");
			            	//System.out.println("DATA: " +  data);
			                response8.body().close();
			                
			                if(data.length() > 42 ) {
			                	
			                	String gamehashkey = sessionKeys.get(splited[4]);
	                             if(gamehashkey == null){
	                             //client exit the game
	                             }else{
	                             	
	                         	DRAWDATE.put(splited[4], splited[1]);
								DRAWTIME.put(splited[4], splited[3]);
								DRAWCOUNT.put(splited[4], splited[2]);
								DRAWNUMBER.put(splited[4], splited[0]);
									
								 ChannelGroup sesstionctx = sessionId.get(splited[4]);
	                             if(sesstionctx == null){
	                             //client exit the game
	                             	System.out.println("null channel");
	                             
	                             }else{
	                             
	                             sesstionctx.write("@"+splited[0]+"&11971&"+splited[1]+"&10009");
	                             sesstionctx.flush();
									
	                             executor1.schedule(()->{
	                            		 
	                            	 if(sesstionctx == null) {
		                            	 return;
		                             }else {
		                            	 
		                            	DateTimeFormatter dtft = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
		 								LocalDateTime now = LocalDateTime.now();
		 								
											//autoinsert.addRecord("10008",DRAWDATE.get(splited[4]),DRAWTIME.get(splited[4]),DRAWNUMBER.get(splited[4]),DRAWCOUNT.get(splited[4]),dtft.format(now));
											String url88 = "http://69.49.228.42/1kball/folder/insert.php?gameid=10008&drawDate=" + DRAWDATE.get(splited[4]) + "&drawTime=" + DRAWTIME.get(splited[4]) + "&drawCount=" + DRAWCOUNT.get(splited[4])+"&drawNumber="+ DRAWNUMBER.get(splited[4]);
								            request = new Request.Builder().url(url88).build();
								            try (Response response88 = client.newCall(request).execute()){
								                System.out.println(response88.body().string());
								                response88.body().close();
								                
								            } catch (Exception e) {
								                System.out.println(e.getMessage());
								            }
		                            	 
		                             }	
	                            	 
	                             },28,TimeUnit.SECONDS);
	                             
	                             }
	                           }
			                	
			                }else {
			                	System.out.print("");
			                }
			                
			            } catch (Exception e) {
			                System.out.println(e.getMessage());
			                DateTimeFormatter dttf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
							LocalDateTime now = LocalDateTime.now();
							try {
								FileWriter writer = new FileWriter("log.txt",true);
								writer.write("10008 DATE: " + dttf.format(now)+ " Message: " + e.getMessage()+ "\n");
								writer.close();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
			            }
			            
						}//end of timerset
						
					}
					
				};executor.scheduleAtFixedRate(T8, 0, 1, TimeUnit.SECONDS);
				
				
			}
			
		}).start();

		//9
		new Thread(new Runnable() {

			@Override
			public void run() {
				
					Runnable T9 = new Runnable() {
					
					OkHttpClient client = new OkHttpClient();
					Request request;

					@SuppressWarnings("unused")
					@Override
					public void run() {
						
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
						DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
						LocalDateTime currTime = LocalDateTime.now();
						if(Arrays.asList(new TimeSet().time3x0).contains(formatter.format(currTime))) {
						
						String url9 = "http://69.49.228.42/1kball/folder/getdraws.php?gameId=10009&time=" + formatter.format(currTime);
			            request = new Request.Builder().url(url9).build();
			            try (Response response9 = client.newCall(request).execute()){
			            	String data = response9.body().string();
			            	String[] splited = data.split("#");
			            	//System.out.println("DATA: " +  data);
			                response9.body().close();
			                
			                if(data.length() > 42 ) {
			                	
			                	String gamehashkey = sessionKeys.get(splited[4]);
	                             if(gamehashkey == null){
	                             //client exit the game
	                             }else{
	                             	
	                         	DRAWDATE.put(splited[4], splited[1]);
								DRAWTIME.put(splited[4], splited[3]);
								DRAWCOUNT.put(splited[4], splited[2]);
								DRAWNUMBER.put(splited[4], splited[0]);
									
								 ChannelGroup sesstionctx = sessionId.get(splited[4]);
	                             if(sesstionctx == null){
	                             //client exit the game
	                             	System.out.println("null channel");
	                             
	                             }else{
	                             
	                             sesstionctx.write("@"+splited[0]+"&11971&"+splited[1]+"&10009");
	                             sesstionctx.flush();
									
	                             executor1.schedule(()->{
	                            	
	                            	 if(sesstionctx == null) {
		                            	 return;
		                             }else {
		                            	 
		                            	DateTimeFormatter dtft = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
		 								LocalDateTime now = LocalDateTime.now();
		 								
											//autoinsert.addRecord("10009",DRAWDATE.get(splited[4]),DRAWTIME.get(splited[4]),DRAWNUMBER.get(splited[4]),DRAWCOUNT.get(splited[4]),dtft.format(now));
											String url99 = "http://69.49.228.42/1kball/folder/insert.php?gameid=10009&drawDate=" + DRAWDATE.get(splited[4]) + "&drawTime=" + DRAWTIME.get(splited[4]) + "&drawCount=" + DRAWCOUNT.get(splited[4])+"&drawNumber="+ DRAWNUMBER.get(splited[4]);
								            request = new Request.Builder().url(url99).build();
								            try (Response response99 = client.newCall(request).execute()){
								                System.out.println(response99.body().string());
								                response99.body().close();
								                
								            } catch (Exception e) {
								                System.out.println(e.getMessage());
								            }
		                            	 
		                             }	 
	                            	 
	                             },28,TimeUnit.SECONDS);
	                             
	                             }
	                           }
			                	
			                }else {
			                	System.out.print("");
			                }
			                
			            } catch (Exception e) {
			                System.out.println(e.getMessage());
			                DateTimeFormatter dtff = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
							LocalDateTime now = LocalDateTime.now();
							try {
								FileWriter writer = new FileWriter("log.txt",true);
								writer.write("10009 DATE: " + dtff.format(now)+ " Message: " + e.getMessage()+ "\n");
								writer.close();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
			            }
						}//end of timerset
						
					}
					
				};executor.scheduleAtFixedRate(T9, 0, 1, TimeUnit.SECONDS);
				
				
			}
			
		}).start();

		//10
		new Thread(new Runnable() {

			@Override
			public void run() {
				
					Runnable T10 = new Runnable() {
					
					OkHttpClient client = new OkHttpClient();
					Request request;

					@SuppressWarnings("unused")
					@Override
					public void run() {
						
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
						DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
						LocalDateTime currTime = LocalDateTime.now();
						if(Arrays.asList(new TimeSet().time1x5).contains(formatter.format(currTime))) {
						
						String url10 = "http://69.49.228.42/1kball/folder/getdraws.php?gameId=10010&time=" + formatter.format(currTime);
			            request = new Request.Builder().url(url10).build();
			            try (Response response10 = client.newCall(request).execute()){
			            	String data = response10.body().string();
			            	String[] splited = data.split("#");
			            	//System.out.println("DATA: " +  data);
			                response10.body().close();
			                
			                if(data.length() > 42 ) {
			                	
			                	String gamehashkey = sessionKeys.get(splited[4]);
	                             if(gamehashkey == null){
	                             //client exit the game
	                             }else{
	                             	
	                         	DRAWDATE.put(splited[4], splited[1]);
								DRAWTIME.put(splited[4], splited[3]);
								DRAWCOUNT.put(splited[4], splited[2]);
								DRAWNUMBER.put(splited[4], splited[0]);
									
								 ChannelGroup sesstionctx = sessionId.get(splited[4]);
	                             if(sesstionctx == null){
	                             //client exit the game
	                             	System.out.println("null channel");
	                             
	                             }else{
	                             
	                             sesstionctx.write("@"+splited[0]+"&11971&"+splited[1]+"&10009");
	                             sesstionctx.flush();
									
	                             executor1.schedule(()->{
	                            		 
	                            	 if(sesstionctx == null) {
		                            	 return;
		                             }else {
		                            	 
		                            	DateTimeFormatter dtft = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
		 								LocalDateTime now = LocalDateTime.now();
		 								
											//autoinsert.addRecord("10010",DRAWDATE.get(splited[4]),DRAWTIME.get(splited[4]),DRAWNUMBER.get(splited[4]),DRAWCOUNT.get(splited[4]),dtft.format(now));
											String url100 = "http://69.49.228.42/1kball/folder/insert.php?gameid=10010&drawDate=" + DRAWDATE.get(splited[4]) + "&drawTime=" + DRAWTIME.get(splited[4]) + "&drawCount=" + DRAWCOUNT.get(splited[4])+"&drawNumber="+ DRAWNUMBER.get(splited[4]);
								            request = new Request.Builder().url(url100).build();
								            try (Response response100 = client.newCall(request).execute()){
								                System.out.println(response100.body().string());
								                response100.body().close();
								                
								            } catch (Exception e) {
								                System.out.println(e.getMessage());
								            }
		                            	 
		                             }	
	                            	 
	                             },28,TimeUnit.SECONDS);
	                             
	                             }
	                           }
			                	
			                }else {
			                	System.out.print("");
			                }
			                
			            } catch (Exception e) {
			                System.out.println(e.getMessage());
			                DateTimeFormatter dtff = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
							LocalDateTime now = LocalDateTime.now();
							try {
								FileWriter writer = new FileWriter("log.txt",true);
								writer.write("10010 DATE: " + dtff.format(now)+ " Message: " + e.getMessage()+ "\n");
								writer.close();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
			            }
						}//end of timersert
						
					}
					
				};executor.scheduleAtFixedRate(T10, 0, 1, TimeUnit.SECONDS);
				
				
			}
			
		}).start();
		
		//11
		new Thread(new Runnable() {

			@Override
			public void run() {
				
					Runnable T11 = new Runnable() {
					
					OkHttpClient client = new OkHttpClient();
					Request request;

					@SuppressWarnings("unused")
					@Override
					public void run() {
						
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
						DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
						LocalDateTime currTime = LocalDateTime.now();
						if(Arrays.asList(new TimeSet().time1x5).contains(formatter.format(currTime))) {
						
						String url11 = "http://69.49.228.42/1kball/folder/getdraws.php?gameId=10011&time=" + formatter.format(currTime);
			            request = new Request.Builder().url(url11).build();
			            try (Response response11 = client.newCall(request).execute()){
			            	String data = response11.body().string();
			            	String[] splited = data.split("#");
			            	//System.out.println("DATA: " +  data);
			                response11.body().close();
			                
			                if(data.length() > 42 ) {
			                	
			                	String gamehashkey = sessionKeys.get(splited[4]);
	                             if(gamehashkey == null){
	                             //client exit the game
	                             }else{
	                             	
	                         	DRAWDATE.put(splited[4], splited[1]);
								DRAWTIME.put(splited[4], splited[3]);
								DRAWCOUNT.put(splited[4], splited[2]);
								DRAWNUMBER.put(splited[4], splited[0]);
									
								 ChannelGroup sesstionctx = sessionId.get(splited[4]);
	                             if(sesstionctx == null){
	                             //client exit the game
	                             	System.out.println("null channel");
	                             
	                             }else{
	                             
	                             sesstionctx.write("@"+splited[0]+"&11971&"+splited[1]+"&10009");
	                             sesstionctx.flush();
									
	                             executor1.schedule(()->{
	                            		 
	                            	 if(sesstionctx == null) {
		                            	 return;
		                             }else {
		                            	 
		                            	DateTimeFormatter dtft = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
		 								LocalDateTime now = LocalDateTime.now();
		 								
											//autoinsert.addRecord("10011",DRAWDATE.get(splited[4]),DRAWTIME.get(splited[4]),DRAWNUMBER.get(splited[4]),DRAWCOUNT.get(splited[4]),dtft.format(now));
											String url111 = "http://69.49.228.42/1kball/folder/insert.php?gameid=10011&drawDate=" + DRAWDATE.get(splited[4]) + "&drawTime=" + DRAWTIME.get(splited[4]) + "&drawCount=" + DRAWCOUNT.get(splited[4])+"&drawNumber="+ DRAWNUMBER.get(splited[4]);
								            request = new Request.Builder().url(url111).build();
								            try (Response response111 = client.newCall(request).execute()){
								                System.out.println(response111.body().string());
								                response111.body().close();
								                
								            } catch (Exception e) {
								                System.out.println(e.getMessage());
								            }
		                            	 
		                             }	
	                            	 
	                             },28,TimeUnit.SECONDS);
	                             
	                             }
	                           }
			                	
			                }else {
			                	System.out.print("");
			                }
			                
			            } catch (Exception e) {
			                System.out.println(e.getMessage());
			                DateTimeFormatter dtff = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
							LocalDateTime now = LocalDateTime.now();
							try {
								FileWriter writer = new FileWriter("log.txt",true);
								writer.write("10011 DATE: " + dtff.format(now)+ " Message: " + e.getMessage()+ "\n");
								writer.close();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
			            }
					}//end of timerset
						
					}
					
				};executor.scheduleAtFixedRate(T11, 0, 1, TimeUnit.SECONDS);
				
				
			}
			
		}).start();

		//12
		new Thread(new Runnable() {

			@Override
			public void run() {
				
					Runnable T12 = new Runnable() {
					
					OkHttpClient client = new OkHttpClient();
					Request request;

					@Override
					public void run() {
						
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
						DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
						LocalDateTime currTime = LocalDateTime.now();
						if(Arrays.asList(new TimeSet().time5x0).contains(formatter.format(currTime))) {
						
						String url12 = "http://69.49.228.42/1kball/folder/getdraws.php?gameId=10012&time=" + formatter.format(currTime);
			            request = new Request.Builder().url(url12).build();
			            try (Response response12 = client.newCall(request).execute()){
			            	String data = response12.body().string();
			            	String[] splited = data.split("#");
			            	//System.out.println("DATA: " +  data);
			                response12.body().close();
			                
			                if(data.length() > 42 ) {
			                	
			                	String gamehashkey = sessionKeys.get(splited[4]);
	                             if(gamehashkey == null){
	                             //client exit the game
	                             }else{
	                             	
	                         	DRAWDATE.put(splited[4], splited[1]);
								DRAWTIME.put(splited[4], splited[3]);
								DRAWCOUNT.put(splited[4], splited[2]);
								DRAWNUMBER.put(splited[4], splited[0]);
									
								 ChannelGroup sesstionctx = sessionId.get(splited[4]);
	                             if(sesstionctx == null){
	                             //client exit the game
	                             	System.out.println("null channel");
	                             
	                             }else{
	                             
	                             sesstionctx.write("@"+splited[0]+"&11971&"+splited[1]+"&10009");
	                             sesstionctx.flush();
									
	                             executor1.schedule(()->{
	                            		 
	                            	 if(sesstionctx == null) {
		                            	 return;
		                             }else {
		                            	 
		                            	DateTimeFormatter dtftt = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
		 								LocalDateTime now = LocalDateTime.now();
		 								
											//autoinsert.addRecord("10012",DRAWDATE.get(splited[4]),DRAWTIME.get(splited[4]),DRAWNUMBER.get(splited[4]),DRAWCOUNT.get(splited[4]),dtft.format(now));
											String url122 = "http://69.49.228.42/1kball/folder/insert.php?gameid=10012&drawDate=" + DRAWDATE.get(splited[4]) + "&drawTime=" + DRAWTIME.get(splited[4]) + "&drawCount=" + DRAWCOUNT.get(splited[4])+"&drawNumber="+ DRAWNUMBER.get(splited[4]);
								            request = new Request.Builder().url(url122).build();
								            try (Response response122 = client.newCall(request).execute()){
								                System.out.println(response122.body().string());
								                response122.body().close();
								                
								            } catch (Exception e) {
								                System.out.println(e.getMessage());
								            }
		                            	 
		                             }
	                            	 
	                             },28,TimeUnit.SECONDS);
	                             
	                             }
	                           }
			                	
			                }else {
			                	System.out.print("");
			                }
			                
			            } catch (Exception e) {
			                System.out.println(e.getMessage());
			                DateTimeFormatter dtff = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
							LocalDateTime now = LocalDateTime.now();
							try {
								FileWriter writer = new FileWriter("log.txt",true);
								writer.write("10012 DATE: " + dtff.format(now)+ " Message: " + e.getMessage()+ "\n");
								writer.close();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
			            }
					}//end of timerset
						
					}
					
				};executor.scheduleAtFixedRate(T12, 0, 1, TimeUnit.SECONDS);
				
				
			}
			
		}).start();

		//13
		new Thread(new Runnable() {

			@Override
			public void run() {
				
					Runnable T13 = new Runnable() {
					
					OkHttpClient client = new OkHttpClient();
					Request request;

					@SuppressWarnings("unused")
					@Override
					public void run() {
						
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
						DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
						LocalDateTime currTime = LocalDateTime.now();
						if(Arrays.asList(new TimeSet().time1x5).contains(formatter.format(currTime))) {
						
						String url13 = "http://69.49.228.42/1kball/folder/getdraws.php?gameId=10013&time=" + formatter.format(currTime);;
			            request = new Request.Builder().url(url13).build();
			            try (Response response13 = client.newCall(request).execute()){
			            	String data = response13.body().string();
			            	String[] splited = data.split("#");
			            	//System.out.println("DATA: " +  data);
			                response13.body().close();
			                
			                if(data.length() > 42 ) {
			                	
			                	String gamehashkey = sessionKeys.get(splited[4]);
	                             if(gamehashkey == null){
	                             //client exit the game
	                             }else{
	                             	
	                         	DRAWDATE.put(splited[4], splited[1]);
								DRAWTIME.put(splited[4], splited[3]);
								DRAWCOUNT.put(splited[4], splited[2]);
								DRAWNUMBER.put(splited[4], splited[0]);
									
								 ChannelGroup sesstionctx = sessionId.get(splited[4]);
	                             if(sesstionctx == null){
	                             //client exit the game
	                             	System.out.println("null channel");
	                             
	                             }else{
	                             
	                             sesstionctx.write("@"+splited[0]+"&11971&"+splited[1]+"&10009");
	                             sesstionctx.flush();
									
	                             executor1.schedule(()->{
	                            		 
	                            	 if(sesstionctx == null) {
		                            	 return;
		                             }else {
		                            	 
		                            	DateTimeFormatter dtft = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
		 								LocalDateTime now = LocalDateTime.now();
		 								
											//autoinsert.addRecord("10013",DRAWDATE.get(splited[4]),DRAWTIME.get(splited[4]),DRAWNUMBER.get(splited[4]),DRAWCOUNT.get(splited[4]),dtft.format(now));
											String url133 = "http://69.49.228.42/1kball/folder/insert.php?gameid=10013&drawDate=" + DRAWDATE.get(splited[4]) + "&drawTime=" + DRAWTIME.get(splited[4]) + "&drawCount=" + DRAWCOUNT.get(splited[4])+"&drawNumber="+ DRAWNUMBER.get(splited[4]);
								            request = new Request.Builder().url(url133).build();
								            try (Response response133 = client.newCall(request).execute()){
								                System.out.println(response133.body().string());
								                response133.body().close();
								                
								            } catch (Exception e) {
								                System.out.println(e.getMessage());
								            }
		                            	 
		                             }
	                            	 
	                             },28,TimeUnit.SECONDS);
	                             
	                             }
	                           }
			                	
			                }else {
			                	System.out.print("");
			                }
			                
			            } catch (Exception e) {
			                System.out.println(e.getMessage());
			                DateTimeFormatter dtff = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
							LocalDateTime now = LocalDateTime.now();
							try {
								FileWriter writer = new FileWriter("log.txt",true);
								writer.write("10013 DATE: " + dtff.format(now)+ " Message: " + e.getMessage()+ "\n");
								writer.close();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
			            }
					}//end of timerset
						
					}
					
				};executor.scheduleAtFixedRate(T13, 0, 1, TimeUnit.SECONDS);
				
				
			}
			
		}).start();

		//14
		new Thread(new Runnable() {

			@Override
			public void run() {
				
					Runnable T14 = new Runnable() {
					
					OkHttpClient client = new OkHttpClient();
					Request request;

					@SuppressWarnings("unused")
					@Override
					public void run() {
						
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
						DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
						LocalDateTime currTime = LocalDateTime.now();
						if(Arrays.asList(new TimeSet().time3x0).contains(formatter.format(currTime))) {
						
						String url14 = "http://69.49.228.42/1kball/folder/getdraws.php?gameId=10014&time=" + formatter.format(currTime);
			            request = new Request.Builder().url(url14).build();
			            try (Response response14 = client.newCall(request).execute()){
			            	String data = response14.body().string();
			            	String[] splited = data.split("#");
			            	//System.out.println("DATA: " +  data);
			                response14.body().close();
			                
			                if(data.length() > 42 ) {
			                	
			                	String gamehashkey = sessionKeys.get(splited[4]);
	                             if(gamehashkey == null){
	                             //client exit the game
	                             }else{
	                             	
	                         	DRAWDATE.put(splited[4], splited[1]);
								DRAWTIME.put(splited[4], splited[3]);
								DRAWCOUNT.put(splited[4], splited[2]);
								DRAWNUMBER.put(splited[4], splited[0]);
									
								 ChannelGroup sesstionctx = sessionId.get(splited[4]);
	                             if(sesstionctx == null){
	                             //client exit the game
	                             	System.out.println("null channel");
	                             
	                             }else{
	                             
	                             sesstionctx.write("@"+splited[0]+"&11971&"+splited[1]+"&10009");
	                             sesstionctx.flush();
									
	                             executor1.schedule(()->{
	                            		 
	                            	 if(sesstionctx == null) {
		                            	 return;
		                             }else {
		                            	 
		                            	DateTimeFormatter dtft = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
		 								LocalDateTime now = LocalDateTime.now();
		 								
											//autoinsert.addRecord("10014",DRAWDATE.get(splited[4]),DRAWTIME.get(splited[4]),DRAWNUMBER.get(splited[4]),DRAWCOUNT.get(splited[4]),dtft.format(now));
											String url144 = "http://69.49.228.42/1kball/folder/insert.php?gameid=10014&drawDate=" + DRAWDATE.get(splited[4]) + "&drawTime=" + DRAWTIME.get(splited[4]) + "&drawCount=" + DRAWCOUNT.get(splited[4])+"&drawNumber="+ DRAWNUMBER.get(splited[4]);
								            request = new Request.Builder().url(url144).build();
								            try (Response response144 = client.newCall(request).execute()){
								                System.out.println(response144.body().string());
								                response144.body().close();
								                
								            } catch (Exception e) {
								                System.out.println(e.getMessage());
								            }
		                            	 
		                             }
	                            		 
	                            	 
	                             },28,TimeUnit.SECONDS);
	                             
	                             }
	                           }
			                	
			                }else {
			                	System.out.print("");
			                }
			                
			            } catch (Exception e) {
			                System.out.println(e.getMessage());
			                DateTimeFormatter dtff = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
							LocalDateTime now = LocalDateTime.now();
							try {
								FileWriter writer = new FileWriter("log.txt",true);
								writer.write("10014 DATE: " + dtff.format(now)+ " Message: " + e.getMessage()+ "\n");
								writer.close();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
			            }
					}//end of timerset
						
					}
					
				};executor.scheduleAtFixedRate(T14, 0, 1, TimeUnit.SECONDS);
				
				
			}
			
		}).start();
		
		//15
		new Thread(new Runnable() {

			@Override
			public void run() {
				
					Runnable T15 = new Runnable() {
					
					OkHttpClient client = new OkHttpClient();
					Request request;

					@SuppressWarnings("unused")
					@Override
					public void run() {
						
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
						DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
						LocalDateTime currTime = LocalDateTime.now();
						if(Arrays.asList(new TimeSet().time1x5).contains(formatter.format(currTime))) {
						
						String url15 = "http://69.49.228.42/1kball/folder/getdraws.php?gameId=10015&time=" + formatter.format(currTime);
			            request = new Request.Builder().url(url15).build();
			            try (Response response15 = client.newCall(request).execute()){
			            	String data = response15.body().string();
			            	String[] splited = data.split("#");
			            	//System.out.println("DATA: " +  data);
			                response15.body().close();
			                
			                if(data.length() > 42 ) {
			                	
			                	String gamehashkey = sessionKeys.get(splited[4]);
	                             if(gamehashkey == null){
	                             //client exit the game
	                             }else{
	                             	
	                         	DRAWDATE.put(splited[4], splited[1]);
								DRAWTIME.put(splited[4], splited[3]);
								DRAWCOUNT.put(splited[4], splited[2]);
								DRAWNUMBER.put(splited[4], splited[0]);
									
								 ChannelGroup sesstionctx = sessionId.get(splited[4]);
	                             if(sesstionctx == null){
	                             //client exit the game
	                             	System.out.println("null channel");
	                             
	                             }else{
	                             
	                             sesstionctx.write("@"+splited[0]+"&11971&"+splited[1]+"&10009");
	                             sesstionctx.flush();
									
	                             executor1.schedule(()->{
	                            		 
	                            	 if(sesstionctx == null) {
		                            	 return;
		                             }else {
		                            	 
		                            	DateTimeFormatter dtft = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
		 								LocalDateTime now = LocalDateTime.now();
		 								
											//autoinsert.addRecord("10015",DRAWDATE.get(splited[4]),DRAWTIME.get(splited[4]),DRAWNUMBER.get(splited[4]),DRAWCOUNT.get(splited[4]),dtft.format(now));
											String url155 = "http://69.49.228.42/1kball/folder/insert.php?gameid=10015&drawDate=" + DRAWDATE.get(splited[4]) + "&drawTime=" + DRAWTIME.get(splited[4]) + "&drawCount=" + DRAWCOUNT.get(splited[4])+"&drawNumber="+ DRAWNUMBER.get(splited[4]);
								            request = new Request.Builder().url(url155).build();
								            try (Response response155 = client.newCall(request).execute()){
								                System.out.println(response155.body().string());
								                response155.body().close();
								                
								            } catch (Exception e) {
								                System.out.println(e.getMessage());
								            }
		                            	 
		                             }
	                            	 
	                             },28,TimeUnit.SECONDS);
	                             
	                             }
	                           }
			                	
			                }else {
			                	System.out.print("");
			                }
			                
			            } catch (Exception e) {
			                System.out.println(e.getMessage());
			                DateTimeFormatter dtff = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
							LocalDateTime now = LocalDateTime.now();
							try {
								FileWriter writer = new FileWriter("log.txt",true);
								writer.write("10015 DATE: " + dtff.format(now)+ " Message: " + e.getMessage()+ "\n");
								writer.close();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
			            }
					  }//end of timer
						
				   }
					
				};executor.scheduleAtFixedRate(T15, 0, 1, TimeUnit.SECONDS);
				
				
			}
			
		}).start();
		
		//16
		new Thread(new Runnable() {

			@Override
			public void run() {
				
					Runnable T16 = new Runnable() {
					
					OkHttpClient client = new OkHttpClient();
					Request request;

					@SuppressWarnings("unused")
					@Override
					public void run() {
						
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
						DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
						LocalDateTime currTime = LocalDateTime.now();
						if(Arrays.asList(new TimeSet().time1x5).contains(formatter.format(currTime))) {
						
						String url16 = "http://69.49.228.42/1kball/folder/getdraws.php?gameId=10016&time=" + formatter.format(currTime);
			            request = new Request.Builder().url(url16).build();
			            try (Response response16 = client.newCall(request).execute()){
			            	String data = response16.body().string();
			            	String[] splited = data.split("#");
			            	//System.out.println("DATA: " +  data);
			                response16.body().close();
			                
			                if(data.length() > 42 ) {
			                	
			                	String gamehashkey = sessionKeys.get(splited[4]);
	                             if(gamehashkey == null){
	                             //client exit the game
	                             }else{
	                             	
	                         	DRAWDATE.put(splited[4], splited[1]);
								DRAWTIME.put(splited[4], splited[3]);
								DRAWCOUNT.put(splited[4], splited[2]);
								DRAWNUMBER.put(splited[4], splited[0]);
									
								 ChannelGroup sesstionctx = sessionId.get(splited[4]);
	                             if(sesstionctx == null){
	                             //client exit the game
	                             	System.out.println("null channel");
	                             
	                             }else{
	                             
	                             sesstionctx.write("@"+splited[0]+"&11971&"+splited[1]+"&10009");
	                             sesstionctx.flush();
									
	                             executor1.schedule(()->{
	                            		 
	                            	 if(sesstionctx == null) {
		                            	 return;
		                             }else {
		                            	 
		                            	DateTimeFormatter dtft = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
		 								LocalDateTime now = LocalDateTime.now();
		 								
											//autoinsert.addRecord("10016",DRAWDATE.get(splited[4]),DRAWTIME.get(splited[4]),DRAWNUMBER.get(splited[4]),DRAWCOUNT.get(splited[4]),dtft.format(now));
											String url166 = "http://69.49.228.42/1kball/folder/insert.php?gameid=10016&drawDate=" + DRAWDATE.get(splited[4]) + "&drawTime=" + DRAWTIME.get(splited[4]) + "&drawCount=" + DRAWCOUNT.get(splited[4])+"&drawNumber="+ DRAWNUMBER.get(splited[4]);
								            request = new Request.Builder().url(url166).build();
								            try (Response response166 = client.newCall(request).execute()){
								                System.out.println(response166.body().string());
								                response166.body().close();
								                
								            } catch (Exception e) {
								                System.out.println(e.getMessage());
								            }
		                            	 
		                             }
	                            	 
	                             },28,TimeUnit.SECONDS);
	                             
	                             }
	                           }
			                	
			                }else {
			                	System.out.print("");
			                }
			                
			            } catch (Exception e) {
			                System.out.println(e.getMessage());
			                DateTimeFormatter dtff = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
							LocalDateTime now = LocalDateTime.now();
							try {
								FileWriter writer = new FileWriter("log.txt",true);
								writer.write("10016 DATE: " + dtff.format(now)+ " Message: " + e.getMessage()+ "\n");
								writer.close();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
			            }
					}//end of timerset
						
					}
					
				};executor.scheduleAtFixedRate(T16, 0, 1, TimeUnit.SECONDS);
				
				
			}
			
		}).start();

		//17
		new Thread(new Runnable() {

			@Override
			public void run() {
				
					Runnable T17 = new Runnable() {
					
					OkHttpClient client = new OkHttpClient();
					Request request;

					@SuppressWarnings("unused")
					@Override
					public void run() {
						
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
						DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
						LocalDateTime currTime = LocalDateTime.now();
						if(Arrays.asList(new TimeSet().time5x0).contains(formatter.format(currTime))) {
						
						String url17 = "http://69.49.228.42/1kball/folder/getdraws.php?gameId=10017&time=" + formatter.format(currTime);
			            request = new Request.Builder().url(url17).build();
			            try (Response response17 = client.newCall(request).execute()){
			            	String data = response17.body().string();
			            	String[] splited = data.split("#");
			            	//System.out.println("DATA: " +  data);
			                response17.body().close();
			                
			                if(data.length() > 42 ) {
			                	
			                	String gamehashkey = sessionKeys.get(splited[4]);
	                             if(gamehashkey == null){
	                             //client exit the game
	                             }else{
	                             	
	                         	DRAWDATE.put(splited[4], splited[1]);
								DRAWTIME.put(splited[4], splited[3]);
								DRAWCOUNT.put(splited[4], splited[2]);
								DRAWNUMBER.put(splited[4], splited[0]);
									
								 ChannelGroup sesstionctx = sessionId.get(splited[4]);
	                             if(sesstionctx == null){
	                             //client exit the game
	                             	System.out.println("null channel");
	                             
	                             }else{
	                             
	                             sesstionctx.write("@"+splited[0]+"&11971&"+splited[1]+"&10009");
	                             sesstionctx.flush();
									
	                             executor1.schedule(()->{
	                            		 
	                            	
	                            	 if(sesstionctx == null) {
		                            	 return;
		                             }else {
		                            	 
		                            	DateTimeFormatter dtft = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
		 								LocalDateTime now = LocalDateTime.now();
		 								
											//autoinsert.addRecord("10017",DRAWDATE.get(splited[4]),DRAWTIME.get(splited[4]),DRAWNUMBER.get(splited[4]),DRAWCOUNT.get(splited[4]),dtft.format(now));
											String url177 = "http://69.49.228.42/1kball/folder/insert.php?gameid=10017&drawDate=" + DRAWDATE.get(splited[4]) + "&drawTime=" + DRAWTIME.get(splited[4]) + "&drawCount=" + DRAWCOUNT.get(splited[4])+"&drawNumber="+ DRAWNUMBER.get(splited[4]);
								            request = new Request.Builder().url(url177).build();
								            try (Response response177 = client.newCall(request).execute()){
								                System.out.println(response177.body().string());
								                response177.body().close();
								                
								            } catch (Exception e) {
								                System.out.println(e.getMessage());
								            }
		                            	 
		                             }
	                            	 
	                            	 
	                             },28,TimeUnit.SECONDS);
	                             
	                             }
	                           }
			                	
			                }else {
			                	System.out.print("");
			                }
			                
			            } catch (Exception e) {
			                System.out.println(e.getMessage());
			                DateTimeFormatter dtff = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
							LocalDateTime now = LocalDateTime.now();
							try {
								FileWriter writer = new FileWriter("log.txt",true);
								writer.write("10017 DATE: " + dtff.format(now)+ " Message: " + e.getMessage()+ "\n");
								writer.close();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
			            }
					}//end of  timerset
						
					}
					
				};executor.scheduleAtFixedRate(T17, 0, 1, TimeUnit.SECONDS);
				
				
			}
			
		}).start();
		
		//18
		new Thread(new Runnable() {

			@Override
			public void run() {
				
					Runnable T18 = new Runnable() {
					
					OkHttpClient client = new OkHttpClient();
					Request request;

					@SuppressWarnings("unused")
					@Override
					public void run() {
						
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
						 DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
				         DateTimeFormatter ss = DateTimeFormatter.ofPattern("ss");
				         LocalDateTime currTime = LocalDateTime.now();
				         
				         if(ss.format(currTime).toString().equals("00")) {
						
						String url18 = "http://69.49.228.42/1kball/folder/getdraws.php?gameId=10018&time=" + formatter.format(currTime);
			            request = new Request.Builder().url(url18).build();
			            try (Response response18 = client.newCall(request).execute()){
			            	String data = response18.body().string();
			            	String[] splited = data.split("#");
			            	//System.out.println("GAME DRAWs:---->  " +  data);
			                response18.body().close();
			                
			                if(data.length() > 42 ) {
			                	
			                	String gamehashkey = sessionKeys.get(splited[4]);
	                             if(gamehashkey == null){
	                             //client exit the game
	                             }else{
	                             	
	                         	DRAWDATE.put(splited[4], splited[1]);
								DRAWTIME.put(splited[4], splited[3]);
								DRAWCOUNT.put(splited[4], splited[2]);
								DRAWNUMBER.put(splited[4], splited[0]);
									
								 ChannelGroup sesstionctx = sessionId.get(splited[4]);
	                             if(sesstionctx == null){
	                             //client exit the game
	                             	System.out.println("null channel");
	                             
	                             }else{
	                             
	                             sesstionctx.write("@"+splited[0]+"&11971&"+splited[1]+"&10009");
	                             sesstionctx.flush();
									
	                             executor1.schedule(()->{
	                            	 
	                            	 if(sesstionctx == null) {
		                            	 return;
		                             }else {
		                            	 
		                            	DateTimeFormatter dtft = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
		 								LocalDateTime now = LocalDateTime.now();
		 								
											//autoinsert.addRecord("10018",DRAWDATE.get(splited[4]),DRAWTIME.get(splited[4]),DRAWNUMBER.get(splited[4]),DRAWCOUNT.get(splited[4]),dtft.format(now));
											String url188 = "http://69.49.228.42/1kball/folder/insert.php?gameid=10018&drawDate=" + DRAWDATE.get(splited[4]) + "&drawTime=" + DRAWTIME.get(splited[4]) + "&drawCount=" + DRAWCOUNT.get(splited[4])+"&drawNumber="+ DRAWNUMBER.get(splited[4]);
								            request = new Request.Builder().url(url188).build();
								            try (Response response188 = client.newCall(request).execute()){
								                System.out.println(response188.body().string());
								                response188.body().close();
								                
								            } catch (Exception e) {
								                System.out.println(e.getMessage());
								            }
		                            	 
		                             }
	                            		 
//	                            	 Request innertaskrequest2 = new Request.Builder().url("http://69.49.228.42/1kball/folder/insert.php?gameid=10018&drawDate="
//															+ DRAWDATE.get(splited[4])
//															+ "&drawCount="
//															+ DRAWCOUNT.get(splited[4])
//															+ "&drawNumber="
//															+ DRAWNUMBER.get(splited[4])
//																	.toString()
//															+ "&drawTime="
//															+ DRAWTIME.get(splited[4]))
//													.build();
//
//											try (Response innertaskresponse2 = client.newCall(innertaskrequest2).execute()) {
//
//												DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
//												LocalDateTime now = LocalDateTime.now();
//												autoinsert.addRecord("10018",DRAWDATE.get(splited[4]),DRAWTIME.get(splited[4]),DRAWNUMBER.get(splited[4]),DRAWCOUNT.get(splited[4]),dtf.format(now));
//												innertaskresponse2.body().close();
//
//											} catch (Exception e) {
//												// TODO: handle exception
//												DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
//												LocalDateTime now = LocalDateTime.now();
//												try {
//													FileWriter writer = new FileWriter("log.txt",true);
//													writer.write("10018 DATE: " + dtf.format(now)+ " Message: " + e.getMessage()+ "\n");
//													writer.close();
//												} catch (IOException e1) {
//													// TODO Auto-generated catch block
//													e1.printStackTrace();
//												}
//											}
	                            		 
	                            	 
	                            	 
	                             },28,TimeUnit.SECONDS);
	                             
	                             }
	                           }
			                	
			                }else {
			                	System.out.print("");
			                }
			                
			            } catch (Exception e) {
			                System.out.println(e.getMessage());
			                DateTimeFormatter dtff = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
							LocalDateTime now = LocalDateTime.now();
							try {
								FileWriter writer = new FileWriter("log.txt",true);
								writer.write("10018 DATE: " + dtff.format(now)+ " Message: " + e.getMessage()+ "\n");
								writer.close();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
			            }
					}//end of timerset
						
					}
					
				};executor.scheduleAtFixedRate(T18, 0, 1, TimeUnit.SECONDS);
				
				
			}
			
		}).start();
		
		
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
	

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		OkHttpClient client = new OkHttpClient();
		String clientId = ctx.channel().id().asShortText();
		String gamehash = getClientId.get(clientId);
		
			//close the session when game is offline
			offlineObject.NotifyOffline(gamehash);
			Request delsessClientrequest = new Request.Builder().url("http://69.49.228.42/1kball/folder/delSession.php?gamehash=" + gamehash ).build();
			try (Response delsessClientresponse = client.newCall(delsessClientrequest).execute()) {

				System.out.print(delsessClientresponse.body().string());
				delsessClientresponse.body().close();

			} catch (Exception e) {
				// TODO: handle exception
			}
			
			getClientId.remove(clientId);
			sessionId.remove(clientId);
			sessions.remove(clientId);
			System.out.print("\nChannel is inactive: " + clientId + " is disconnected ");
			if (!sessionKeys.containsKey(clientId))
			return;
			// sessions.remove(theKey);
			
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		
		
		//System.out.println("GAME HASH =>  " + msg.toString());
		String message = msg.toString();
		if(message.length() > 20) {
			SERVER_GAME_DRAW.putIfAbsent(message, message);
		}
		
		OkHttpClient client = new OkHttpClient();
		try {

			Request hashClientrequest = new Request.Builder().url("http://69.49.228.42/1kball/folder/gethash.php?gamehash=" + msg.toString()).build();

			try (Response hashClientresponse = client.newCall(hashClientrequest).execute()) {

				gameid = hashClientresponse.body().string();
				hashClientresponse.body().close();

			} catch (Exception e) {
				// TODO: handle exception
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		if (!gameid.contains("notfound")) {

			System.out.println("Game " + msg.toString() + " is connected");
			sessions.putIfAbsent(message, ctx);
			String clientId = ctx.channel().id().asShortText();
			// System.out.println("Read:" + clientId);

			ChannelGroup channelGroup = sessionId.computeIfAbsent(message,id -> new DefaultChannelGroup(GlobalEventExecutor.INSTANCE));
			channelGroup.add(ctx.channel());

			sessionKeys.put(message, message);
			getClientId.put(clientId, message);
			getClientId.put(gameid, gameid);
			onlineObject.NotifyOnline(message);
			
			//------------------------------------------------------------------------------//
			
			try {

				Request addsessClientrequest = new Request.Builder().url("http://69.49.228.42/1kball/folder/addSession.php?gamehash=" + msg.toString()).build();

				try (Response addsessClientresponse = client.newCall(addsessClientrequest).execute()) {

					System.out.print(addsessClientresponse.body().string());
					addsessClientresponse.body().close();

				} catch (Exception e) {
					// TODO: handle exception
				}

			} catch (Exception e) {
				// TODO: handle exception
			}
					
			
		} else {
			//System.out.println("message: game hash not found");
		}



	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
		System.out.print("\nChannel Read Completed:" + ctx);
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
                        // Handle lost internet connection
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
}
