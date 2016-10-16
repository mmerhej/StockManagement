package model;

import java.sql.Connection;
import java.util.ArrayList;

import dao.Customers;
import dao.Database;
import dto.CustomerObjects;

public class CustomerManager {

	public ArrayList<CustomerObjects> GetCustomers() throws Exception {
		ArrayList<CustomerObjects> customerList = null;
		try {
			Database database = new Database();
			Connection connection = database.Get_Connection();
			Customers customer = new Customers();
			customerList = customer.GetCustomers(connection);

		} catch (Exception e) {
			throw e;
		}
		return customerList;
	}
	public ArrayList<CustomerObjects> SetCustomers(String customers) throws Exception {
		ArrayList<CustomerObjects> customerList = null;
		try {
			Database database = new Database();
			Connection connection = database.Get_Connection();
			Customers customer = new Customers();
			customerList = customer.setCustomers(connection, customers);
		} catch (Exception e) {
			throw e;
		}
		return customerList;
	}

}
