package io.yfarich.rest;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("/rest")
public class RestProxyService {

  private final String REMOTE_SERVER_URL = "http://localhost:3000";

  @POST
  @Path("/{param}")
  public Response getMsg(@PathParam("param") String serverURL, String request) {
    return sendRequestToServer(serverURL, request);
  }

  private Response sendRequestToServer(String serverURL, String postRequest) {

    try {
      Client client = Client.create();
      WebResource webResource = client
          .resource(REMOTE_SERVER_URL + "/" + serverURL);

      ClientResponse remoteServerResponse = webResource.accept("application/json")
          .get(ClientResponse.class);

      return Response
          .status(200)
          .entity(remoteServerResponse.getEntity(String.class))
          .build();

    } catch (ClientHandlerException e) {
      return Response
          .status(400)
          .entity(e.getMessage())
          .build();
    } catch (Exception e) {
      return Response
          .status(500)
          .entity(e.getMessage())
          .build();
    }
  }

}