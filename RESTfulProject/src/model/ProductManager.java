package model;

import java.sql.Connection;
import java.util.ArrayList;

import dao.Database;
import dao.Products;
import dto.ProductObjects;

public class ProductManager {
	public ArrayList<ProductObjects> GetProducts() throws Exception {
		ArrayList<ProductObjects> productList = null;
		try {
			Database database = new Database();
			Connection connection = database.Get_Connection();
			Products products = new Products();
			productList = products.GetProducts(connection);

		} catch (Exception e) {
			throw e;
		}
		return productList;
	}

	public ArrayList<ProductObjects> SetProducts(String product) throws Exception {
		ArrayList<ProductObjects> productList = null;
		try {
			Database database = new Database();
			Connection connection = database.Get_Connection();
			Products products = new Products();
			productList = products.setProducts(connection, product);
		} catch (Exception e) {
			throw e;
		}
		return productList;
	}
}
