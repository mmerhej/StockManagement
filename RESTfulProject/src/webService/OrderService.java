package webService;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import model.OrderManager;

import com.google.gson.Gson;

import dto.OrderObjects;

@Path("/Orders")
public class OrderService {
	@GET
	@Path("/Get")
	@Produces("application/json")
	public String order() {
		String orders = null;
		try {
			ArrayList<OrderObjects> orderData = null;
			OrderManager orderManager = new OrderManager();
			orderData = orderManager.GetOrders();
			// StringBuffer sb = new StringBuffer();
			Gson gson = new Gson();
			System.out.println(gson.toJson(orderData));
			orders = gson.toJson(orderData);

		} catch (Exception e) {
			System.out.println("error");
		}
		return orders;
	}
	
	@POST
	@Path("/Post")
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.APPLICATION_JSON)
	public String processRequset(@FormParam("param") String pData) {
		
		OrderManager orderManager = new OrderManager();
		try {
			orderManager.SetOrders(pData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return pData;
	}
}
