package webService;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import model.LoginManager;

import com.google.gson.Gson;

import dto.FeedObjects;

@Path("/Login")
public class LoginService {
	
	@GET
	@Path("/Get")
	@Produces("application/json")
	public String feed()
	{
		String feeds  = null;
		try 
		{
			ArrayList<FeedObjects> feedData = null;
			LoginManager loginManager= new LoginManager();
			feedData = loginManager.GetFeeds();
			//StringBuffer sb = new StringBuffer();
			Gson gson = new Gson();
			System.out.println(gson.toJson(feedData));
			feeds = gson.toJson(feedData);

		} catch (Exception e)
		{
			System.out.println("error");
		}
		return feeds;
	}
	
	@POST
	@Path("/Post")
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.APPLICATION_JSON)
	public String processRequset(@FormParam("param") String pData) {
		
		LoginManager loginManager= new LoginManager();
		try {
			loginManager.SetLogin(pData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return pData;
	}

}
