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

import dto.OrderObjects;

public class Orders {
	public ArrayList<OrderObjects> GetOrders(Connection connection) throws Exception
	{
		ArrayList<OrderObjects> customerData = new ArrayList<OrderObjects>();
		try
		{
			//String uname = request.getParameter("uname");
			PreparedStatement ps = connection.prepareStatement("SELECT type,date,product,customer,quantity FROM orders");
			//ps.setString(1,uname);
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				OrderObjects orderObjects = new OrderObjects();
				orderObjects.setCustomer(rs.getString("customer"));
				orderObjects.setDate(rs.getString("date"));
				orderObjects.setProduct(rs.getString("product"));
				orderObjects.setQuantity(rs.getString("quantity"));
				orderObjects.setType(rs.getString("type"));
				customerData.add(orderObjects);
				
			}
			return customerData;
		}
		catch(Exception e)
		{
			throw e;
		}
	}


	public ArrayList<OrderObjects> setOrders(Connection connection,String order) {
		
		Gson gson = new Gson();		

		JsonArray jsonArray = new JsonParser().parse(order).getAsJsonArray();

		String sqldelete = "TRUNCATE orders";
	    // Execute deletion

		String sql = "insert into orders (ID ,type, date, product,customer,quantity) values (?,?, ?, ?,?,?)";
		PreparedStatement ps;
		try {
		ps = connection.prepareStatement(sql);
		ps.executeUpdate(sqldelete);
		 for (int i = 0; i < jsonArray.size(); i++) {
		        JsonElement str = jsonArray.get(i);
		        OrderObjects obj = gson.fromJson(str, OrderObjects.class);
		        //use the add method from the list and returns it.
		        ps.setInt(1, i+1);
		        ps.setString(2, obj.getType());
				ps.setString(3, obj.getDate());
				ps.setString(4, obj.getProduct());
				ps.setString(5, obj.getCustomer());
				ps.setString(6, obj.getQuantity());				
				ps.addBatch();}
		ps.executeBatch();
		ps.close();
		connection.close();	
		System.out.println("sucess orders");
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("error");
			e.printStackTrace();
		}
		return null;
	}
}
