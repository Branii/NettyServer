
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AutoInsert{
	
	PreparedStatement pstmt;

	public void addRecord(String gameId,String drawDate,String drawTime,String drawNumber,String drawCount,String dateToday, String info) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException{

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
		    	
		    StringBuilder StringArranger = new StringBuilder(drawNumber);
		    StringBuilder inverseDrawNumber = StringArranger.reverse();
		    
		    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
		    LocalDateTime now = LocalDateTime.now();
		    String getTime = dtf.format(now).toString();
		    
	        String insertSQL  = "INSERT INTO "+ drawTable + "(draw_date,draw_time,draw_number,draw_count,date_created,client,get_time) VALUES (?, ?, ?, ?, ?, ?, ?)";
	        pstmt = connection.prepareStatement(insertSQL);
	        pstmt.setString(1,drawDate);
	        pstmt.setString(2,drawTime);
	        pstmt.setString(3,inverseDrawNumber.toString());
	        pstmt.setString(4,drawCount);
	        pstmt.setString(5,dateToday);
	        pstmt.setString(6,"box");
	        pstmt.setString(7,getTime);
	        pstmt.executeUpdate();
	        System.out.println(" Inserted := " + gameId);
	        pstmt.close();
	        res.close();
	        connection.close();
	        FileWriter writer = new FileWriter("log.txt",true);
            writer.write("Game Id: " + gameId + " Message: " + info + "\n");
            writer.close();

		    }
		  
		} catch (SQLException e) {
		    System.out.print(e.getMessage());
	  }
   }
}

