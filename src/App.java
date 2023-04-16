import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class App{

    static ScheduledExecutorService executor = Executors.newScheduledThreadPool(18);
    private static String HOST_NAME = "69.49.228.42";
    private static int HOST_PORT = 2020;
    public static void main(String[] args) throws Exception {
    	
    	
    		//1
    		new Thread(new Runnable() {

				@Override
				public void run() {
					
					Runnable T1 = new Runnable() {
						
					  OkHttpClient client = new OkHttpClient();
					  Request request;

						@Override
						public void run() {
							
							 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
					         DateTimeFormatter ss = DateTimeFormatter.ofPattern("ss");
					         LocalDateTime currTime = LocalDateTime.now();
					         if(ss.format(currTime).toString().equals("00")) {
					        	 
					        	 String url1 = "http://69.49.228.42/timerTask/counter.php?gameid=10001&time=" + formatter.format(currTime);
						            request = new Request.Builder().url(url1).build();
						            try (Response response = client.newCall(request).execute()){
						                //System.out.println(response.body().string());
						                response.body().close();
						                
						            } catch (Exception e) {
						                System.out.println(e.getMessage());
						            }
					        	 
					         }
							
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

							@Override
							public void run() {
								
								 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
						         LocalDateTime currTime = LocalDateTime.now();
						         if(Arrays.asList(new TimeSet().time5x0).contains(formatter.format(currTime))) {
						        	 
						        	    String url1 = "http://69.49.228.42/timerTask/counter.php?gameid=10002&time=" + formatter.format(currTime);
							            request = new Request.Builder().url(url1).build();
							            try (Response response = client.newCall(request).execute()){
							                //System.out.println(response.body().string());
							                response.body().close();
							                
							            } catch (Exception e) {
							                System.out.println(e.getMessage());
							            }
						        	 
						         }
								
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

							@Override
							public void run() {
								
								 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
								 DateTimeFormatter ss = DateTimeFormatter.ofPattern("ss");
						         LocalDateTime currTime = LocalDateTime.now();
						         if(ss.format(currTime).toString().equals("00")) {
						        	 
						        	    String url1 = "http://69.49.228.42/timerTask/counter.php?gameid=10003&time=" + formatter.format(currTime);
							            request = new Request.Builder().url(url1).build();
							            try (Response response = client.newCall(request).execute()){
							                //System.out.println(response.body().string());
							                response.body().close();
							                
							            } catch (Exception e) {
							                System.out.println(e.getMessage());
							            }
						        	 
						         }
								
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

							@Override
							public void run() {
								
								DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
								LocalDateTime currTime = LocalDateTime.now();
								if(Arrays.asList(new TimeSet().time3x0).contains(formatter.format(currTime))) {
								    
								       String url1 = "http://69.49.228.42/timerTask/counter.php?gameid=10004&time=" + formatter.format(currTime);
								       request = new Request.Builder().url(url1).build();
								       try (Response response = client.newCall(request).execute()){
								           //System.out.println(response.body().string());
								           response.body().close();
								           
								       } catch (Exception e) {
								           System.out.println(e.getMessage());
								       }
								    
								}
								
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

							@Override
							public void run() {
								
								DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
								LocalDateTime currTime = LocalDateTime.now();
								if(Arrays.asList(new TimeSet().time1x5).contains(formatter.format(currTime))) {
								    
								       String url1 = "http://69.49.228.42/timerTask/counter.php?gameid=10005&time=" + formatter.format(currTime);
								       request = new Request.Builder().url(url1).build();
								       try (Response response = client.newCall(request).execute()){
								           //System.out.println(response.body().string());
								           response.body().close();
								           
								       } catch (Exception e) {
								           System.out.println(e.getMessage());
								       }
								    
								}
								
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

							@Override
							public void run() {
								
								DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
								LocalDateTime currTime = LocalDateTime.now();
								if(Arrays.asList(new TimeSet().time3x0).contains(formatter.format(currTime))) {
								    
								       String url1 = "http://69.49.228.42/timerTask/counter.php?gameid=10006&time=" + formatter.format(currTime);
								       request = new Request.Builder().url(url1).build();
								       try (Response response = client.newCall(request).execute()){
								           //System.out.println(response.body().string());
								           response.body().close();
								           
								       } catch (Exception e) {
								           System.out.println(e.getMessage());
								       }
								    
								}
								
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

							@Override
							public void run() {
								
								DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
								LocalDateTime currTime = LocalDateTime.now();
								if(Arrays.asList(new TimeSet().time1x5).contains(formatter.format(currTime))) {
								    
								       String url1 = "http://69.49.228.42/timerTask/counter.php?gameid=10007&time=" + formatter.format(currTime);
								       request = new Request.Builder().url(url1).build();
								       try (Response response = client.newCall(request).execute()){
								           //System.out.println(response.body().string());
								           response.body().close();
								           
								       } catch (Exception e) {
								           System.out.println(e.getMessage());
								       }
								    
								}
								
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

							@Override
							public void run() {
								
								DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
								LocalDateTime currTime = LocalDateTime.now();
								if(Arrays.asList(new TimeSet().time1x5).contains(formatter.format(currTime))) {
								    
								       String url1 = "http://69.49.228.42/timerTask/counter.php?gameid=10008&time=" + formatter.format(currTime);
								       request = new Request.Builder().url(url1).build();
								       try (Response response = client.newCall(request).execute()){
								           //System.out.println(response.body().string());
								           response.body().close();
								           
								       } catch (Exception e) {
								           System.out.println(e.getMessage());
								       }
								    
								}
								
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

							@Override
							public void run() {
								
								DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
								LocalDateTime currTime = LocalDateTime.now();
								if(Arrays.asList(new TimeSet().time3x0).contains(formatter.format(currTime))) {
								    
								       String url1 = "http://69.49.228.42/timerTask/counter.php?gameid=10009&time=" + formatter.format(currTime);
								       request = new Request.Builder().url(url1).build();
								       try (Response response = client.newCall(request).execute()){
								           //System.out.println(response.body().string());
								           response.body().close();
								           
								       } catch (Exception e) {
								           System.out.println(e.getMessage());
								       }
								    
								}
								
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

							@Override
							public void run() {
								
								DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
								LocalDateTime currTime = LocalDateTime.now();
								if(Arrays.asList(new TimeSet().time1x5).contains(formatter.format(currTime))) {
								    
								       String url1 = "http://69.49.228.42/timerTask/counter.php?gameid=10010&time=" + formatter.format(currTime);
								       request = new Request.Builder().url(url1).build();
								       try (Response response = client.newCall(request).execute()){
								           //System.out.println(response.body().string());
								           response.body().close();
								           
								       } catch (Exception e) {
								           System.out.println(e.getMessage());
								       }
								    
								}
								
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

							@Override
							public void run() {
								
								DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
								LocalDateTime currTime = LocalDateTime.now();
								if(Arrays.asList(new TimeSet().time1x5).contains(formatter.format(currTime))) {
								    
								       String url1 = "http://69.49.228.42/timerTask/repair.php?gameid=10011&time=" + formatter.format(currTime);
								       request = new Request.Builder().url(url1).build();
								       try (Response response = client.newCall(request).execute()){
								           //System.out.println(response.body().string());
								           response.body().close();
								       } catch (Exception e) {
								           System.out.println(e.getMessage());
								       }
								    
								}
								
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
								LocalDateTime currTime = LocalDateTime.now();
								if(Arrays.asList(new TimeSet().time5x0).contains(formatter.format(currTime))) {
								    
								       String url1 = "http://69.49.228.42/timerTask/counter.php?gameid=10012&time=" + formatter.format(currTime);
								       request = new Request.Builder().url(url1).build();
								       try (Response response = client.newCall(request).execute()){
								           //System.out.println(response.body().string());
								           response.body().close();
								           
								       } catch (Exception e) {
								           System.out.println(e.getMessage());
								       }
								    
								}
								
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

							@Override
							public void run() {
								
								DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
								LocalDateTime currTime = LocalDateTime.now();
								if(Arrays.asList(new TimeSet().time1x5).contains(formatter.format(currTime))) {
								    
								       String url1 = "http://69.49.228.42/timerTask/counter.php?gameid=10013&time=" + formatter.format(currTime);
								       request = new Request.Builder().url(url1).build();
								       try (Response response = client.newCall(request).execute()){
								           //System.out.println(response.body().string());
								           response.body().close();
								           
								       } catch (Exception e) {
								           System.out.println(e.getMessage());
								       }
								    
								}
								
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

							@Override
							public void run() {
								
								DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
								LocalDateTime currTime = LocalDateTime.now();
								if(Arrays.asList(new TimeSet().time3x0).contains(formatter.format(currTime))) {
								    
								       String url1 = "http://69.49.228.42/timerTask/counter.php?gameid=10014&time=" + formatter.format(currTime);
								       request = new Request.Builder().url(url1).build();
								       try (Response response = client.newCall(request).execute()){
								           //System.out.println(response.body().string());
								           response.body().close();
								           
								       } catch (Exception e) {
								           System.out.println(e.getMessage());
								       }
								    
								}
								
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

							@Override
							public void run() {
								
								DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
								LocalDateTime currTime = LocalDateTime.now();
								if(Arrays.asList(new TimeSet().time1x5).contains(formatter.format(currTime))) {
								    
								       String url1 = "http://69.49.228.42/timerTask/counter.php?gameid=10015&time=" + formatter.format(currTime);
								       request = new Request.Builder().url(url1).build();
								       try (Response response = client.newCall(request).execute()){
								           //System.out.println(response.body().string());
								           response.body().close();
								           
								       } catch (Exception e) {
								           System.out.println(e.getMessage());
								       }
								    
								}
								
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
						  
							@Override
							public void run() {
								
								DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
								LocalDateTime currTime = LocalDateTime.now();
								
								if(Arrays.asList(new TimeSet().time3x0).contains(formatter.format(currTime))) {
								    
								       String url1 = "http://69.49.228.42/timerTask/counter.php?gameid=10016&time=" + formatter.format(currTime);
								       request = new Request.Builder().url(url1).build();
								       try (Response response = client.newCall(request).execute()){
								           //System.out.println(response.body().string());
								           response.body().close();
								           
								       } catch (Exception e) {
								           System.out.println(e.getMessage());
								       }
								    
								}
								
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

							@Override
							public void run() {
								
								DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
								LocalDateTime currTime = LocalDateTime.now();
								if(Arrays.asList(new TimeSet().time5x0).contains(formatter.format(currTime))) {
								    
								       String url1 = "http://69.49.228.42/timerTask/counter.php?gameid=10017&time=" + formatter.format(currTime);
								       request = new Request.Builder().url(url1).build();
								       try (Response response = client.newCall(request).execute()){
								           //System.out.println(response.body().string());
								           response.body().close();
								           
								       } catch (Exception e) {
								           System.out.println(e.getMessage());
								       }
								    
								}
								
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

							@Override
							public void run() {
								
								 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
						         DateTimeFormatter ss = DateTimeFormatter.ofPattern("ss");
						         LocalDateTime currTime = LocalDateTime.now();
						         if(ss.format(currTime).toString().equals("00")) {
						        	 
						        	 String url1 = "http://69.49.228.42/timerTask/counter.php?gameid=10018&time=" + formatter.format(currTime);
							            request = new Request.Builder().url(url1).build();
							            try (Response response = client.newCall(request).execute()){
							                //System.out.println(response.body().string());
							                response.body().close();
							                
							            } catch (Exception e) {
							                System.out.println(e.getMessage());
							            }
						        	 
						         }
								
							}
							
						};executor.scheduleAtFixedRate(T18, 0, 1, TimeUnit.SECONDS);
					
				}
    			
    		}).start();
    	
    		
    	   /*
    	   new Thread(new Runnable() {

			@Override
			public void run() {
				
			  OkHttpClient client = new OkHttpClient();
			  Request request;
			  boolean isRunning = false;
			  String isDrawNumber = null;
			  
				
				while(true) {
					
					
		            
		            //2
		            String url2 = "http://69.49.228.42/timerTask/counter.php?gameid=10002";
		            request = new Request.Builder().url(url2).build();
		            try (Response response = client.newCall(request).execute()){
		                //System.out.println(response.body().string());
		                response.body().close();
		            } catch (Exception e) {
		                System.out.println(e.getMessage());
		            }
		            
		            //3
		            String url3 = "http://69.49.228.42/timerTask/counter.php?gameid=10003";
		            request = new Request.Builder().url(url3).build();
		            try (Response response = client.newCall(request).execute()){
		                //System.out.println(response.body().string());
		                response.body().close();
		            } catch (Exception e) {
		                System.out.println(e.getMessage());
		            }
		            
		            //4
		            String url4 = "http://69.49.228.42/timerTask/counter.php?gameid=10004";
		            request = new Request.Builder().url(url4).build();
		            try (Response response = client.newCall(request).execute()){
		                //System.out.println(response.body().string());
		                response.body().close();
		            } catch (Exception e) {
		                System.out.println(e.getMessage());
		            }
		            
		            //5
		            String url5 = "http://69.49.228.42/timerTask/counter.php?gameid=10005";
		            request = new Request.Builder().url(url5).build();
		            try (Response response = client.newCall(request).execute()){
		                //System.out.println(response.body().string());
		                response.body().close();
		            } catch (Exception e) {
		                System.out.println(e.getMessage());
		            }
		            
		            //6
		            String url6 = "http://69.49.228.42/timerTask/counter.php?gameid=10006";
		            request = new Request.Builder().url(url6).build();
		            try (Response response = client.newCall(request).execute()){
		                //System.out.println(response.body().string());
		                response.body().close();
		            } catch (Exception e) {
		                System.out.println(e.getMessage());
		            }
		            
		            //7
		            String url7 = "http://69.49.228.42/timerTask/counter.php?gameid=10007";
		            request = new Request.Builder().url(url7).build();
		            try (Response response = client.newCall(request).execute()){
		                //System.out.println(response.body().string());
		                response.body().close();
		            } catch (Exception e) {
		                System.out.println(e.getMessage());
		            }
		            
		            
		            //8
		            String url8 = "http://69.49.228.42/timerTask/counter.php?gameid=10008";
		            request = new Request.Builder().url(url8).build();
		            try (Response response = client.newCall(request).execute()){
		                //System.out.println(response.body().string());
		                response.body().close();
		            } catch (Exception e) {
		                System.out.println(e.getMessage());
		            }
		            
		            
		            //9
		            String url9 = "http://69.49.228.42/timerTask/counter.php?gameid=10009";
		            request = new Request.Builder().url(url9).build();
		            try (Response response = client.newCall(request).execute()){
		                //System.out.println(response.body().string());
		                response.body().close();
		            } catch (Exception e) {
		                System.out.println(e.getMessage());
		            }
		            
		            //10
		            String url10 = "http://69.49.228.42/timerTask/counter.php?gameid=10010";
		            request = new Request.Builder().url(url10).build();
		            try (Response response = client.newCall(request).execute()){
		                //System.out.println(response.body().string());
		                response.body().close();
		            } catch (Exception e) {
		                System.out.println(e.getMessage());
		            }
		            
		            //11
		            String url11 = "http://69.49.228.42/timerTask/counter.php?gameid=10011";
		            request = new Request.Builder().url(url11).build();
		            try (Response response = client.newCall(request).execute()){
		                //System.out.println(response.body().string());
		                response.body().close();
		            } catch (Exception e) {
		                System.out.println(e.getMessage());
		            }
		            
		            //12
		            String url12 = "http://69.49.228.42/timerTask/counter.php?gameid=10012";
		            request = new Request.Builder().url(url12).build();
		            try (Response response = client.newCall(request).execute()){
		                //System.out.println(response.body().string());
		                response.body().close();
		            } catch (Exception e) {
		                System.out.println(e.getMessage());
		            }
		            
		            //13
		            String url13 = "http://69.49.228.42/timerTask/counter.php?gameid=10013";
		            request = new Request.Builder().url(url13).build();
		            try (Response response = client.newCall(request).execute()){
		                //System.out.println(response.body().string());
		                response.body().close();
		            } catch (Exception e) {
		                System.out.println(e.getMessage());
		            }
		            
		            //14
		            String url14 = "http://69.49.228.42/timerTask/counter.php?gameid=10014";
		            request = new Request.Builder().url(url14).build();
		            try (Response response = client.newCall(request).execute()){
		                //System.out.println(response.body().string());
		                response.body().close();
		            } catch (Exception e) {
		                System.out.println(e.getMessage());
		            }
		            
		            //15
		            String url15 = "http://69.49.228.42/timerTask/counter.php?gameid=10015";
		            request = new Request.Builder().url(url15).build();
		            try (Response response = client.newCall(request).execute()){
		                //System.out.println(response.body().string());
		                response.body().close();
		            } catch (Exception e) {
		                System.out.println(e.getMessage());
		            }
		            
		            //16
		            String url16 = "http://69.49.228.42/timerTask/counter.php?gameid=10016";
		            request = new Request.Builder().url(url16).build();
		            try (Response response = client.newCall(request).execute()){
		                //System.out.println(response.body().string());
		                response.body().close();
		            } catch (Exception e) {
		                System.out.println(e.getMessage());
		            }
		            
		            //17
		            String url17 = "http://69.49.228.42/timerTask/counter.php?gameid=10017";
		            request = new Request.Builder().url(url17).build();
		            try (Response response = client.newCall(request).execute()){
		                //System.out.println(response.body().string());
		                response.body().close();
		            } catch (Exception e) {
		                System.out.println(e.getMessage());
		            }
		            
		            //18
		            String url18 = "http://69.49.228.42/timerTask/counter.php?gameid=10018";
		            request = new Request.Builder().url(url18).build();
		            try (Response response = client.newCall(request).execute()){
		                //System.out.println(response.body().string());
		                response.body().close();
		            } catch (Exception e) {
		                System.out.println(e.getMessage());
		            }
					
					  
					
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
    		   
    	   }).start();
	     */
	     
	
	       //Start the server
	       NettyServer nettyServer = new NettyServer();
	       nettyServer.start(new InetSocketAddress(HOST_NAME, HOST_PORT));
			       
		}
        	
}
