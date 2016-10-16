package webService;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import model.ProductManager;

import com.google.gson.Gson;

import dto.ProductObjects;

@Path("/Products")
public class ProductService {
	@GET
	@Path("/Get")
	@Produces("application/json")
	public String product() {
		String products = null;
		try {
			ArrayList<ProductObjects> productData = null;
			ProductManager productManager = new ProductManager();
			productData = productManager.GetProducts();
			// StringBuffer sb = new StringBuffer();
			Gson gson = new Gson();
			System.out.println(gson.toJson(productData));
			products = gson.toJson(productData);

		} catch (Exception e) {
			System.out.println("error");
		}
		return products;
	}
	
	@POST
	@Path("/Post")
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.APPLICATION_JSON)
	public String processRequset( @FormParam("param") String pData) {
		
		ProductManager productManager = new ProductManager();
		try {
			productManager.SetProducts(pData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return pData;
	}
}
