package model;

import java.sql.Connection;
import java.util.ArrayList;

import dao.Database;
import dao.Login;
import dto.FeedObjects;

public class LoginManager {
	
	
	public ArrayList<FeedObjects> GetFeeds()throws Exception {
		ArrayList<FeedObjects> feeds = null;
		try {
			    Database database= new Database();
			    Connection connection = database.Get_Connection();
				Login login= new Login();
				feeds=login.GetFeeds(connection);
		
		} catch (Exception e) {
			throw e;
		}
		return feeds;
	}

	public ArrayList<FeedObjects> SetLogin(String logins) throws Exception {
		ArrayList<FeedObjects> feeds = null;
		try {
			Database database = new Database();
			Connection connection = database.Get_Connection();
			Login login= new Login();
			feeds = login.setLogin(connection, logins);
		} catch (Exception e) {
			throw e;
		}
		return feeds;
	}
}
