package io.yfarich.rest;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/html")
public class ProxyService extends AbstractAd2cProxy {

  private final String REMOTE_SERVER_URL = "http://localhost:3000";
  private final String CURRENT_BASE_HREF = "/jersey/proxy/html/";

  @Override
  public Response sendGetRequest(String param, ServletContext servletContext) {
    try {
      WebResource webResource = Client
          .create()
          .resource(REMOTE_SERVER_URL + "/" + param);

      ClientResponse remoteServerResponse = webResource
          .accept(MediaType.WILDCARD)
          .get(ClientResponse.class);
      return makeResponse(remoteServerResponse);

    } catch (Exception e) {
      return handleException(e);
    }
  }

  @Override
  public Response sendPOSTRequest(String param, String message, ServletContext servletContext) {
    try {
      WebResource webResource = Client
          .create()
          .resource(REMOTE_SERVER_URL + "/" + param);

      ClientResponse remoteServerResponse = webResource
          .accept(MediaType.WILDCARD)
          .post(ClientResponse.class, message);
      return makeResponse(remoteServerResponse);

    } catch (Exception e) {
      return handleException(e);
    }

  }

  private Response makeResponse(ClientResponse remoteServerResponse){
    return Response
        .status(remoteServerResponse.getStatus())
        .entity(replaceBaseHref(remoteServerResponse.getEntity(String.class), CURRENT_BASE_HREF))
        .type(remoteServerResponse.getType())
        .build();
  }


  private Response handleException(Exception e) {
    String errorMessage = e.getMessage();
    if (e instanceof ClientHandlerException) {
      errorMessage = String
          .format("Erreur de communication avec le serveur distant %s .", REMOTE_SERVER_URL);
    }
    return Response
        .status(Status.INTERNAL_SERVER_ERROR)
        .entity(errorMessage)
        .build();
  }

  private String replaceBaseHref(String response, String baseHREF) {
    return response.replaceAll("<base href=\".*\">",
        "<base href=\"" + baseHREF + "\">");
  }
}