
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import io.netty.channel.Channel;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyJobs {

	//private static final AutoInsert autoinsert = new AutoInsert();
	private static final NoInternet nointernet = new NoInternet();
	
	OkHttpClient client = new OkHttpClient();
	Request request;

	
	public void runnerGame(String message,ConcurrentHashMap<String, Channel> sessionsMap, String gameHash) throws IOException {
		
		String url = "http://69.49.228.42/1kball/folder/getdraws.php?gameHash=" + gameHash + "&time=" + message;
        request = new Request.Builder().url(url).build();
        try (Response response = client.newCall(request).execute()){
        	String data = response.body().string();
        	String[] splited = data.split("#");
            response.body().close();
            
            if(data.length() > 40) {
            	
             System.out.println(data);
             
             Channel channel = sessionsMap.get(splited[4]);
             if(channel == null) {
            	 System.err.println("Game has disconnected");
            	 return;
             }else {
             channel.writeAndFlush("@"+ splited[0] +"&" + splited[2] +"&"+ splited[1] +"&"+ splited[3] +"&"+ splited[5]);
             
             ScheduledExecutorService executor1 = Executors.newSingleThreadScheduledExecutor();
             executor1.schedule(()->{
            	 
            	 // new implementation // assuming the client is not able to respond within 30 sec// remove the client from the server

//            	DateTimeFormatter dtft = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
//    			LocalDateTime now = LocalDateTime.now();
    			
                try {
					//autoinsert.addRecord(splited[5],splited[1],splited[3],splited[0],splited[2],dtft.format(now),"Delayed 35 min before insert"  + " Time: " + splited[2] + "\n");
                	nointernet.RemoveClientIfNoResponds(splited[5], splited[1], sessionsMap, gameHash);
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	 
             }, 32, TimeUnit.SECONDS);
             
             executor1.shutdown();
             }
             
            }else {
            	//System.out.println("Data: " +  data);
            }
            
        } catch (Exception e) {
            //System.out.println(e.getMessage());
            System.err.println("Function execution failed: " + e.getMessage());
            FileWriter writer = new FileWriter("log.txt",true);
            writer.write("Game Hash: " + gameHash + " Message: " + e.getMessage() + "Time: " + message + "\n");
            writer.close();
            runnerGame(message,sessionsMap,gameHash);
        }
		
			
	}
	
	//this method is just for testing
//	public void Game1_job(String formattedTime,ConcurrentHashMap<String, Channel> sessionsMap) {
//
//		String url = "http://69.49.228.42/1kball/folder/getdraws.php?gameHash=8b3fb44d4bc2d5c5d8cd25891ba6e32c1&time=" + formattedTime;
//        request = new Request.Builder().url(url).build();
//        try (Response response = client.newCall(request).execute()){
//        	String data = response.body().string();
//        	String[] splited = data.split("#");
//            response.body().close();
//            
//            System.out.println("Data: " +  data);
//            
//            if(data.length() > 40) {
//            	
//             System.out.println("Data: " +  data);
//             Channel channel = sessionsMap.get(splited[4]);
//             if(channel == null) {
//            	 return;
//             }
//             channel.writeAndFlush("@"+splited[0]+"&" + splited[2] +"&"+splited[1]+"&"+ splited[3]  + "&10001");
//             
//             ScheduledExecutorService executor1 = Executors.newSingleThreadScheduledExecutor();
//             executor1.schedule(()->{
//            	 
//            	DateTimeFormatter dtft = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
//    			LocalDateTime now = LocalDateTime.now();
//    			
//                try {
//					autoinsert.addRecord("10001",splited[1],splited[3],splited[0],splited[2],dtft.format(now));
//				} catch (InstantiationException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IllegalAccessException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (ClassNotFoundException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//            	 
//             }, 30, TimeUnit.SECONDS);
//             executor1.shutdown();
//             
//            	
//            }
//            
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//           
//        }
//		
//	}
//	

}
