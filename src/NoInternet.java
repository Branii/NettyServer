
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

public class NoInternet implements ChannelHandler{
	
	PreparedStatement pstmt;

	public void RemoveClientIfNoResponds(String gameId,String drawDate,ConcurrentHashMap<String, Channel> sessions,String gameHash) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException{

		try (Connection connection = Conf.getConnection()){

			String drawTable = "draw_" + gameId;
		    String selectSQL = "SELECT * FROM "+ drawTable +" WHERE draw_date = ?";
		    PreparedStatement pstmt = connection.prepareStatement(selectSQL);
		    pstmt.setString(1,drawDate);
	        //pstmt.setString(2,drawTime);
		    ResultSet res = pstmt.executeQuery();

		    if(res.next()){

	        System.out.println("Already Inserted " + gameId);
	        pstmt.close();
	        res.close();
	        connection.close();
	        return;

		    }else{
		    	
	        sessions.remove(gameHash);
	        System.err.println("Execution has been terminated: ");
		    String info = "Client is offline, session terminated";
	        FileWriter writer = new FileWriter("log.txt",true);
            writer.write("Game Id: " + gameId + " Message: " + info + "\n");
            writer.close();

		    }
		  
		} catch (SQLException e) {
		    System.out.print(e.getMessage());
	    }
   }

	@Override
	public void exceptionCaught(ChannelHandlerContext arg0, Throwable arg1) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handlerAdded(ChannelHandlerContext arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	

}

