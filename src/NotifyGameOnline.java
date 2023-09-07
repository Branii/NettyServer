import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException; 

class NotifyGameOnline{
	
	public void NotifyOnline(String gamehash) {
		// TODO Auto-generated method stub
		try (Connection connection = Conf.getConnection()) {
			 
			 String sql = "UPDATE games SET sessionId = ? WHERE game_hash = ?";
			 PreparedStatement pstmt = connection.prepareStatement(sql);
			 pstmt.setString(1, "online");
			 pstmt.setString(2, gamehash);
			 pstmt.executeUpdate();
 			 //System.out.print("GAME IS ONLINE");
			 pstmt.close();
			 connection.close();
			 
		 } catch (SQLException e) {
			 
			 System.out.println(e.getMessage());
			 
		}
		
	}

}


