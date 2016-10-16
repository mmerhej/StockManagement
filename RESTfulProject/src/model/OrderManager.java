package model;

import java.util.ArrayList;
import java.sql.Connection;

import dao.Database;
import dao.Orders;
import dto.OrderObjects;

public class OrderManager {
	public ArrayList<OrderObjects> GetOrders() throws Exception {
		ArrayList<OrderObjects> orderList = null;
		try {
			Database database = new Database();
			Connection connection = database.Get_Connection();
			Orders orders = new Orders();
			orderList = orders.GetOrders(connection);

		} catch (Exception e) {
			throw e;
		}
		return orderList;
	}

	public ArrayList<OrderObjects> SetOrders(String order) throws Exception {
		ArrayList<OrderObjects> orderList = null;
		try {
			Database database = new Database();
			Connection connection = database.Get_Connection();
			Orders orders = new Orders();
			orderList = orders.setOrders(connection, order);
		} catch (Exception e) {
			throw e;
		}
		return orderList;
	}

}
