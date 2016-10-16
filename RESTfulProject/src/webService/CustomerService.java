package webService;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import model.CustomerManager;

import com.google.gson.Gson;

import dto.CustomerObjects;

@Path("/Customers")
public class CustomerService {

	@GET
	@Path("/Get")
	@Produces("application/json")
	public String customer() {
		String customers = null;
		try { 
			ArrayList<CustomerObjects> customerData = null;
			CustomerManager customertManager = new CustomerManager();
			customerData = customertManager.GetCustomers();
			// StringBuffer sb = new StringBuffer();
			Gson gson = new Gson();
			System.out.println(gson.toJson(customerData));
			customers = gson.toJson(customerData);

		} catch (Exception e) {
			System.out.println("error");
		}
		return customers;
	}
	
	
	@POST
	@Path("/Post")
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.TEXT_PLAIN)
	public String processRequset(@FormParam("param") String pData) {
		System.out.println("data===="+pData);
		CustomerManager customertManager = new CustomerManager();
		try {
			customertManager.SetCustomers(pData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();  
		}
	    return pData;
	}
}