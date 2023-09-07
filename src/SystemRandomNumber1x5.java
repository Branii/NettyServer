import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SystemRandomNumber1x5 {
	
	PreparedStatement pstmt;
	private static final AutoInsert autoinsert = new AutoInsert();
	
	public void systemInsertRandomNumbers(String drawTime) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException {

		try (Connection connection = Conf.getConnection()){

		    String selectSQL = "SELECT * FROM games WHERE command = ? AND notify = ? AND sessionId = ?";
		    PreparedStatement pstmt = connection.prepareStatement(selectSQL);
		    pstmt.setString(1,"start");
		    pstmt.setString(2,"hide");
		    pstmt.setString(3,"offline");
		    ResultSet res = pstmt.executeQuery();

		    while(res.next()){

		    String gameTime = res.getString("second_per_issue");
		    String gameType = res.getString("rand_num_type");
		    String gameId = res.getString("game_id");
		    String gameTable = "draw_" + gameId;

    	    String timeTable = "";
    	    if(gameTime == "90"){
    	    timeTable = "time1x0";
    	    }else if (gameTime == "90") {
    	    timeTable = "time1x5";
    	    }else if (gameTime == "180") {
    	    timeTable = "time3x0";
    	    }else if (gameTime == "300") {
    	    timeTable = "time5x0"; 
    	    }else if (gameTime == "600") {
    	    timeTable = "time10x0";
    	    }
    	    
    	    if(timeTable == "60") {
    	    	
		    	switch(gameType) {
		    	
		    	case "5d": 
	    		String sql1 = "SELECT id,count,timeset FROM "+ gameTable +" WHERE timeset = ? ORDER BY id DESC LIMIT 1";
	    		PreparedStatement pstmt1 = connection.prepareStatement(sql1);
	    		pstmt1.setString(1,drawTime);
	    		ResultSet result1 = pstmt1.executeQuery();
	    		if(result1.next()) {
	    		String drawCount = result1.getString("count");
			    String drawTimee = result1.getString("timeset");
			    String drawDate = MyUtil.getTodaysDate() + drawCount;
			    String drawNumber =  MyUtil.getRandomNumbers(5);
			    String dateToday = MyUtil.getTodaysDateDash();
			    String info = "insjava";
			    autoinsert.addRecord(gameId,drawDate,drawTimee,drawNumber,drawCount,dateToday,info);
	    		}
	    		break;
	    		
		    	case "3d": 
	    		String sql2 = "SELECT id,count,timeset FROM "+ gameTable +" WHERE timeset = ? ORDER BY id DESC LIMIT 1";
	    		PreparedStatement pstmt2 = connection.prepareStatement(sql2);
	    		pstmt2.setString(1,drawTime);
	    		ResultSet result2 = pstmt2.executeQuery();
	    		if(result2.next()) {
	    		String drawCount = result2.getString("count");
			    String drawTimee = result2.getString("timeset");
			    String drawDate = MyUtil.getTodaysDate();
			    String drawNumber =  MyUtil.getRandomNumbers(3);
			    String dateToday = MyUtil.getTodaysDateDash();
			    String info = "insjava";
			    autoinsert.addRecord(gameId,drawDate,drawTimee,drawNumber,drawCount,dateToday,info);
	    		}
	    		break;
	    		
		    	case "fast_3": 
	    		String sql3 = "SELECT id,count,timeset FROM "+ gameTable +" WHERE timeset = ? ORDER BY id DESC LIMIT 1";
	    		PreparedStatement pstmt3 = connection.prepareStatement(sql3);
	    		pstmt3.setString(1,drawTime);
	    		ResultSet result3 = pstmt3.executeQuery();
	    		if(result3.next()) {
	    		String drawCount = result3.getString("count");
			    String drawTimee = result3.getString("timeset");
			    String drawDate = MyUtil.getTodaysDate();
			    String drawNumber =  MyUtil.getRandomNumbers(3);
			    String dateToday = MyUtil.getTodaysDateDash();
			    String info = "insjava";
			    autoinsert.addRecord(gameId,drawDate,drawTimee,drawNumber,drawCount,dateToday,info);
	    		}
	    		break;
	    		
		    	case "pc_28": 
	    		String sql4 = "SELECT id,count,timeset FROM "+ gameTable +" WHERE timeset = ? ORDER BY id DESC LIMIT 1";
	    		PreparedStatement pstmt4 = connection.prepareStatement(sql4);
	    		pstmt4.setString(1,drawTime);
	    		ResultSet result4 = pstmt4.executeQuery();
	    		if(result4.next()) {
	    		String drawCount = result4.getString("count");
			    String drawTimee = result4.getString("timeset");
			    String drawDate = MyUtil.getTodaysDate();
			    String drawNumber =  MyUtil.getRandomNumbers(3);
			    String dateToday = MyUtil.getTodaysDateDash();
			    String info = "insjava";
			    autoinsert.addRecord(gameId,drawDate,drawTimee,drawNumber,drawCount,dateToday,info);
	    		}
	    		break;
	    		
		    	case "pk_10": 
	    		String sql5 = "SELECT id,count,timeset FROM "+ gameTable +" WHERE timeset = ? ORDER BY id DESC LIMIT 1";
	    		PreparedStatement pstmt5 = connection.prepareStatement(sql5);
	    		pstmt5.setString(1,drawTime);
	    		ResultSet result5 = pstmt5.executeQuery();
	    		if(result5.next()) {
	    		String drawCount = result5.getString("count");
			    String drawTimee = result5.getString("timeset");
			    String drawDate = MyUtil.getTodaysDate();
			    String drawNumber =  MyUtil.generateUniqueRandomNumbers(1, 10, 10);
			    String dateToday = MyUtil.getTodaysDateDash();
			    String info = "insjava";
			    autoinsert.addRecord(gameId,drawDate,drawTimee,drawNumber,drawCount,dateToday,info);
	    		}
	    		break;
	    		
		    	case "11x5": 
	    		String sql6 = "SELECT id,count,timeset FROM "+ gameTable +" WHERE timeset = ? ORDER BY id DESC LIMIT 1";
	    		PreparedStatement pstmt6 = connection.prepareStatement(sql6);
	    		pstmt6.setString(1,drawTime);
	    		ResultSet result6 = pstmt6.executeQuery();
	    		if(result6.next()) {
	    		String drawCount = result6.getString("count");
			    String drawTimee = result6.getString("timeset");
			    String drawDate = MyUtil.getTodaysDate();
		        String[] array = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11"};
			    String drawNumber =  MyUtil.shuffleArrayToString(array);
			    String dateToday = MyUtil.getTodaysDateDash();
			    String info = "insjava";
			    autoinsert.addRecord(gameId,drawDate,drawTimee,drawNumber,drawCount,dateToday,info);
	    		}
	    		break;
	    		
		    	case "49x7": 
	    		String sql7 = "SELECT id,count,timeset FROM "+ gameTable +" WHERE timeset = ? ORDER BY id DESC LIMIT 1";
	    		PreparedStatement pstmt7 = connection.prepareStatement(sql7);
	    		pstmt7.setString(1,drawTime);
	    		ResultSet result7 = pstmt7.executeQuery();
	    		if(result7.next()) {
	    		String drawCount = result7.getString("count");
			    String drawTimee = result7.getString("timeset");
			    String drawDate = MyUtil.getTodaysDate();
		        String[] array = {"01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49"};
			    String drawNumber =  MyUtil.shuffleArrayToString(array);
			    String dateToday = MyUtil.getTodaysDateDash();
			    String info = "insjava";
			    autoinsert.addRecord(gameId,drawDate,drawTimee,drawNumber,drawCount,dateToday,info);
	    		}
	    		break;
	    		
		    	case "keno": 
	    		String sql8 = "SELECT id,count,timeset FROM "+ gameTable +" WHERE timeset = ? ORDER BY id DESC LIMIT 1";
	    		PreparedStatement pstmt8 = connection.prepareStatement(sql8);
	    		pstmt8.setString(1,drawTime);
	    		ResultSet result8 = pstmt8.executeQuery();
	    		if(result8.next()) {
	    		String drawCount = result8.getString("count");
			    String drawTimee = result8.getString("timeset");
			    String drawDate = MyUtil.getTodaysDate();
			    String drawNumber =  MyUtil.getRandomNumbers(20);
			    String dateToday = MyUtil.getTodaysDateDash();
			    String info = "insjava";
			    autoinsert.addRecord(gameId,drawDate,drawTimee,drawNumber,drawCount,dateToday,info);
	    		}
	    		break;
		    
		    	
		      }
    	    	
    	    }

		  }
		    
		} catch (SQLException e) {
		    System.out.print(e.getMessage());
	    }

		
	}

}
