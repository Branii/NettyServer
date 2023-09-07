import java.io.IOException;
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

private static final SystemRandomNumber1x0 time1x0 = new SystemRandomNumber1x0();
private static final SystemRandomNumber1x5 time1x5 = new SystemRandomNumber1x5();
private static final SystemRandomNumber3x0 time3x0 = new SystemRandomNumber3x0();
private static final SystemRandomNumber5x0 time5x0 = new SystemRandomNumber5x0();
private static final SystemRandomNumber10x0 time10x0 = new SystemRandomNumber10x0();


public static void main(String[] args) throws Exception {
	

 ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);

	//Task1
 Runnable Task1 = new Runnable() {
			
  OkHttpClient client = new OkHttpClient();
  Request request;

	@Override
	public void run() {
		
		 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
         DateTimeFormatter ss = DateTimeFormatter.ofPattern("ss");
         LocalDateTime currTime = LocalDateTime.now();
         if(ss.format(currTime).toString().equals("00")) {
        	 
        	 
        	  String url = "http://69.49.228.42/1kball/folder/AutoInsert1x0.php?time=" + formatter.format(currTime);
	            request = new Request.Builder().url(url).build();
	            try (Response response = client.newCall(request).execute()){
	                //System.out.println(response.body().string());
	                response.body().close();
	                
	            } catch (Exception e) {
	                System.out.println(e.getMessage());
	            }
	            
	            try {
				  time1x0.systemInsertRandomNumbers(formatter.format(currTime));
				} catch (InstantiationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        	 
         }
		
	}
	
};executor.scheduleAtFixedRate(Task1, 0, 1, TimeUnit.SECONDS);

	//Task2
 Runnable Task2 = new Runnable() {
				
  OkHttpClient client = new OkHttpClient();
  Request request;

	@Override
	public void run() {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		LocalDateTime currTime = LocalDateTime.now();
		if(Arrays.asList(new TimeSet().time1x5).contains(formatter.format(currTime))) {
			
			 String url = "http://69.49.228.42/1kball/folder/AutoInsert1x5.php?time=" + formatter.format(currTime);
		       request = new Request.Builder().url(url).build();
		       try (Response response = client.newCall(request).execute()){
		           //System.out.println(response.body().string());
		           response.body().close();
		           
		       } catch (Exception e) {
		           System.out.println(e.getMessage());
		       }
		       
		       try {
					time1x5.systemInsertRandomNumbers(formatter.format(currTime));
				} catch (InstantiationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		    
		}
		
	}
	
};executor.scheduleAtFixedRate(Task2, 0, 1, TimeUnit.SECONDS);

    //Task3
 Runnable Task3 = new Runnable() {
			
OkHttpClient client = new OkHttpClient();
Request request;

@Override
public void run() {
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
	LocalDateTime currTime = LocalDateTime.now();
	if(Arrays.asList(new TimeSet().time3x0).contains(formatter.format(currTime))) {
	    
		 String url = "http://69.49.228.42/1kball/folder/AutoInsert3x0.php?time=" + formatter.format(currTime);
	       request = new Request.Builder().url(url).build();
	       try (Response response = client.newCall(request).execute()){
	           //System.out.println(response.body().string());
	           response.body().close();
	           
	       } catch (Exception e) {
	           System.out.println(e.getMessage());
	       }
	       
	       try {
				time3x0.systemInsertRandomNumbers(formatter.format(currTime));
			} catch (InstantiationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    
	}
	
}

};executor.scheduleAtFixedRate(Task3, 0, 1, TimeUnit.SECONDS);

    //Task4
 Runnable Task4 = new Runnable() {
			
OkHttpClient client = new OkHttpClient();
Request request;

@Override
public void run() {
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
	LocalDateTime currTime = LocalDateTime.now();
	if(Arrays.asList(new TimeSet().time5x0).contains(formatter.format(currTime))) {
	    
		 String url = "http://69.49.228.42/1kball/folder/AutoInsert5x0.php?time=" + formatter.format(currTime);
	       request = new Request.Builder().url(url).build();
	       try (Response response = client.newCall(request).execute()){
	           //System.out.println(response.body().string());
	           response.body().close();
	           
	       } catch (Exception e) {
	           System.out.println(e.getMessage());
	       }
	       
	       try {
				time5x0.systemInsertRandomNumbers(formatter.format(currTime));
			} catch (InstantiationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    
	}
	
}

};executor.scheduleAtFixedRate(Task4, 0, 1, TimeUnit.SECONDS);

    //Task5
 Runnable Task5 = new Runnable() {
			
OkHttpClient client = new OkHttpClient();
Request request;

@Override
public void run() {
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
	LocalDateTime currTime = LocalDateTime.now();
	if(Arrays.asList(new TimeSet().time10x0).contains(formatter.format(currTime))) {
	    
		 String url = "http://69.49.228.42/1kball/folder/AutoInsert10x0.php?time=" + formatter.format(currTime);
	       request = new Request.Builder().url(url).build();
	       try (Response response = client.newCall(request).execute()){
	           //System.out.println(response.body().string());
	           response.body().close();
	           
	       } catch (Exception e) {
	           System.out.println(e.getMessage());
	       }
	       
	       try {
				time10x0.systemInsertRandomNumbers(formatter.format(currTime));
			} catch (InstantiationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    
	}
	
}

};executor.scheduleAtFixedRate(Task5, 0, 1, TimeUnit.SECONDS);

   //Start application
 NettyServer nettyServer = new NettyServer();
 nettyServer.start(new InetSocketAddress(HOST_NAME, HOST_PORT));
	       
  }
  	
}
