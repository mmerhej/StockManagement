package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import dto.FeedObjects;


public class Login {
	
	
	public ArrayList<FeedObjects> GetFeeds(Connection connection) throws Exception
	{
		ArrayList<FeedObjects> feedData = new ArrayList<FeedObjects>();
		try
		{
			//String uname = request.getParameter("uname");
			PreparedStatement ps = connection.prepareStatement("SELECT username,password FROM user");
			//ps.setString(1,uname);
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				FeedObjects feedObject = new FeedObjects();
				feedObject.setUsername(rs.getString("username"));
				feedObject.setPassword(rs.getString("password"));
				feedData.add(feedObject);
			}
			return feedData;
		}
		catch(Exception e)
		{
			throw e;
		}
	}


	public ArrayList<FeedObjects> setLogin(Connection connection,String logins) {
		
		Gson gson = new Gson();		

		JsonArray jsonArray = new JsonParser().parse(logins).getAsJsonArray();

		String sqldelete = "TRUNCATE user";
	    // Execute deletion

		String sql = "insert into user (username, password) values (?,?)";
		PreparedStatement ps;
		try {
		ps = connection.prepareStatement(sql);
		ps.executeUpdate(sqldelete);
		 for (int i = 0; i < jsonArray.size(); i++) {
		        JsonElement str = jsonArray.get(i);
		        FeedObjects obj = gson.fromJson(str, FeedObjects.class);
		        //use the add method from the list and returns it.
		        //ps.setInt(1, i+1);
		        ps.setString(1, obj.getUsername());
				ps.setString(2, obj.getPassword());
				
				ps.addBatch();}
		ps.executeBatch();
		ps.close();
		connection.close();	
		System.out.println("success login");
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("error");
			e.printStackTrace();
		}
		return null;
	}
}
