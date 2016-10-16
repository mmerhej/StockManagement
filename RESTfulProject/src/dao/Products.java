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

import dto.ProductObjects;

public class Products {

	public ArrayList<ProductObjects> GetProducts(Connection connection)
			throws Exception {
		ArrayList<ProductObjects> productData = new ArrayList<ProductObjects>();
		try {
			// String uname = request.getParameter("uname");
			PreparedStatement ps = connection
					.prepareStatement("SELECT product,inStock,barcode FROM products");
			// ps.setString(1,uname);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				ProductObjects productObjects = new ProductObjects();
				productObjects.setProduct(rs.getString("product"));
				productObjects.setInStock(rs.getString("inStock"));
				productObjects.setBarcode(rs.getString("barcode"));
				productData.add(productObjects);

			}
			return productData;
		} catch (Exception e) {
			throw e;
		}
	}


public ArrayList<ProductObjects> setProducts(Connection connection,String order) {
		
		Gson gson = new Gson();		

		JsonArray jsonArray = new JsonParser().parse(order).getAsJsonArray();

		String sqldelete = "TRUNCATE products";
	    // Execute deletion

		String sql = "insert into products (ID ,inStock, product, barcode) values (?,?, ?, ?)";
		PreparedStatement ps;
		try {
		ps = connection.prepareStatement(sql);
		ps.executeUpdate(sqldelete);
		 for (int i = 0; i < jsonArray.size(); i++) {
		        JsonElement str = jsonArray.get(i);
		        ProductObjects obj = gson.fromJson(str, ProductObjects.class);
		        //use the add method from the list and returns it.
		        ps.setInt(1, i+1);
		        ps.setString(2, obj.getInStock());
				ps.setString(3, obj.getProduct());
				ps.setString(4, obj.getBarcode());
				ps.addBatch();}
		ps.executeBatch();
		ps.close();
		connection.close();	
		System.out.println("sucess products");
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("error");
			e.printStackTrace();
		}
		return null;
	}
}
