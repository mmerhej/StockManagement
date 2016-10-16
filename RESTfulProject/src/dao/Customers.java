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
import dto.CustomerObjects;


public class Customers {
	public ArrayList<CustomerObjects> GetCustomers(Connection connection)
			throws Exception {
		ArrayList<CustomerObjects> customerData = new ArrayList<CustomerObjects>();
		try {
			// String uname = request.getParameter("uname");
			PreparedStatement ps = connection
					.prepareStatement("SELECT fullName,role,phoneNumber FROM customers");
			// ps.setString(1,uname);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				CustomerObjects customerObjects = new CustomerObjects();
				customerObjects.setFullName(rs.getString("fullName"));
				customerObjects.setRole(rs.getString("role"));
				customerObjects.setPhoneNumber(rs.getString("phoneNumber"));
				customerData.add(customerObjects);

			}
			return customerData;
		} catch (Exception e) {
			throw e;
		}
	}

	public ArrayList<CustomerObjects> setCustomers(Connection connection,
			String customers) {

		Gson gson = new Gson();

		JsonArray jsonArray = new JsonParser().parse(customers)
				.getAsJsonArray();

		String sqldelete = "TRUNCATE customers";
		// Execute deletion

		String sql = "insert into customers (ID ,fullName, role, phoneNumber) values (?,?, ?, ?)";
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(sql);
			ps.executeUpdate(sqldelete);
			for (int i = 0; i < jsonArray.size(); i++) {
				JsonElement str = jsonArray.get(i);
				CustomerObjects obj = gson.fromJson(str, CustomerObjects.class);
				// use the add method from the list and returns it.
				ps.setInt(1, i + 1);
				ps.setString(2, obj.getFullName());
				ps.setString(3, obj.getRole());
				ps.setString(4, obj.getPhoneNumber());
				ps.addBatch();
			}
			ps.executeBatch();
			ps.close();
			connection.close();
			System.out.println("sucess customer");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("error");
			e.printStackTrace();
		}
		return null;
	}
}
