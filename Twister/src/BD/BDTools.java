package BD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mysql.jdbc.PreparedStatement;
import com.mongodb.client.*;

import java.util.*;

public class BDTools {
	public static boolean checkUserExist(String login) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection conn=Database.getMySQLConnection();
		String query="SELECT * FROM  user WHERE user_login="+login+";";
		Statement st=conn.createStatement();
		ResultSet rs=st.executeQuery(query);
		boolean userExists=false;
		while(rs.next()) {
			userExists=true;
		}
		return userExists;
	}

	public static boolean checkCleExists(String cle) throws SQLException {
		// TODO Auto-generated method stub
		
		Connection conn=Database.getMySQLConnection();
		String query="SELECT * FROM  user WHERE cle="+cle+";";
		Statement st=conn.createStatement();
		ResultSet rs=st.executeQuery(query);
		boolean cleExists=false;
		while(rs.next()) {
			cleExists=true;
		}
		return cleExists;
	}
	public static boolean checkUserPassword(String login,String password) throws SQLException {
		Connection conn=Database.getMySQLConnection();
		String query="SELECT * FROM  user WHERE user_login="+login+" AND password="+password+";";
		Statement st=conn.createStatement();
		ResultSet rs=st.executeQuery(query);
		boolean passwordExists=false;
		while(rs.next()) {
			passwordExists=true;
		}
		return passwordExists;
	}
	
	public static int identification(String login, String mail, String password) throws SQLException {
		Connection c = Database.getMySQLConnection();
		Statement st = c.createStatement();
		int user_id=1;
		String query="SELECT user_id FROM user"
				+ " WHERE user.user_password=PASSWORD('password')"
				+ " AND user.user_mail="+mail+"OR user.user.login="+login+";";
		ResultSet rs = st.executeQuery(query);
		while(rs.next())
			user_id=rs.getInt("user_id");
		st.close();
		c.close();
		return user_id;
	}


	public static String InsertSession(int user_id,boolean root) throws SQLException {
		Connection conn=Database.getMySQLConnection();
		String query="INSERT INTO session(user_id,session_key,session_root)"
				+ " VALUES ("+user_id+",?,"+root+");";
		java.sql.PreparedStatement pst=conn.prepareStatement(query);
		boolean success=false;
		//String key=generateRandomKey();
		String key="cleeee";
		while(!success) {
			try {
				pst.setString(1, key);
				pst.executeUpdate();
				success=true;
			}catch(SQLException e) {
				if(e.getMessage() != null)
					key="0";
				else
					e.printStackTrace();
			}
		}
		conn.close();
		
		
		//pst.executeQuery(query);

		return key;
	}
	public static void newUser(String login, String password) throws SQLException {
		Connection c=Database.getMySQLConnection();
		Statement st = c.createStatement();
		
		

		//Création de la query
		String query="INSERT INTO user(user_login,user_password)"
				+ " VALUES ("+login+",?,"+password+");";
		
		st.executeUpdate(query);
		st.close();
		c.close();
}
	public static String Deconnexion(String cle) throws SQLException {
		Connection conn=Database.getMySQLConnection();
		String query="DELETE FROM cle WHERE id="+cle+";";
		Statement st=conn.createStatement();
		st.executeQuery(query);

		return "clé retiré";
	}
	public static boolean checkFriendExist(int id) throws SQLException {
		Connection conn=Database.getMySQLConnection();
		String query="SELECT * FROM  user WHERE id="+id+";";
		Statement st=conn.createStatement();
		ResultSet rs=st.executeQuery(query);
		boolean friendExists=false;
		while(rs.next()) {
			friendExists=true;
		}
		return friendExists;
	}
	public static String addFriend(int id) throws SQLException {
		Connection conn=Database.getMySQLConnection();
		String query="INSERT INTO friend (user_id) VALUES ("+id+");";
		Statement st=conn.createStatement();
		st.executeQuery(query);

		return "friend ajouté";
		
	}	
	public static String removeFriend(int id) throws SQLException {
		Connection conn=Database.getMySQLConnection();
		String query="DELETE FROM friend WHERE id="+id+";";
		Statement st=conn.createStatement();
		st.executeQuery(query);

		return "friend retiré";
	}
	

	//Message
	public static String InsertMessage(String message) {
		
		MongoClient mongo= new MongoClient("mongodb://localhost:27017");
		MongoDatabase db = mongo.getDatabase("3i017");
		MongoCollection<Document> message_collection = db.getCollection("message");
		Document query = new Document();
		query.append("user_id", 3);
		query.append("date", 3);
		query.append("content", 3);
		message_collection.insertOne(query);
		mongo.close();
		return message;
	}
	public static String deleteMessage(String cle) {
		return "effacé";
	}
}
