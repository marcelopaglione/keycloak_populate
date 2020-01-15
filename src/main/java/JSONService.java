import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/json/users")
public class JSONService {

    @GET
    @Path("/get")
    @Produces("application/json")
    public User getUserInJSON(String id) {
        User user = new User();
        user.setFirstName("fistName" + id);
        user.setLastName("lastName%s" + id);
        user.setEmail("firstName%s@lastName%s" + id);
        return user;
    }


    @POST
    @Path("/post")
    @Consumes("application/json")
    public Response createProductInJSON(User user) {
        return Response.status(201).entity(user).build();
    }
}
