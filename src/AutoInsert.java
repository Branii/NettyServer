
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AutoInsert{
	
	PreparedStatement pstmt;

	public void addRecord(String gameId,String drawDate,String drawTime,String drawNumber,String drawCount,String dateToday) throws InstantiationException, IllegalAccessException, ClassNotFoundException{

		try (Connection connection = Conf.getConnection()){

			String drawTable = "draw_" + gameId;
		    String selectSQL = "SELECT * FROM "+ drawTable +" WHERE date_created = ? AND draw_time = ?";
		    PreparedStatement pstmt = connection.prepareStatement(selectSQL);
		    pstmt.setString(1,dateToday);
	        pstmt.setString(2,drawTime);
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
	        int affected = pstmt.executeUpdate();
	        System.out.println(affected + " row affected JAVA_" + gameId);
	        pstmt.close();
	        res.close();
	        connection.close();

		    }
		  

		} catch (SQLException e) {
			e.printStackTrace();
		    System.out.print(e.getMessage());
		}
	}

}

