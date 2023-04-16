import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AutoGenerate {
	
	public void AutoGenerate1(String gameId,String drawDate, String drawTime) {
		
		try (Connection connection = Conf.getConnection()) {
			 
			 String drawTable = "draw_" + gameId;
			 String sql = "SELECT drawdatee,drawtimee FROM '"+ drawTable +"' WHERE drawdatee = ? AND drawtimee = ?";
			 PreparedStatement pstmt = connection.prepareStatement(sql);
			 pstmt.setString(1, drawDate);
			 pstmt.setString(2, drawTime);
			 pstmt.executeUpdate();
			 System.out.print("GAME IS ONLINE");
			 pstmt.close();
			 connection.close();
			 
		 } catch (SQLException e) {
			 
			 System.out.println(e.getMessage());
			 
		}
		
	}

}


