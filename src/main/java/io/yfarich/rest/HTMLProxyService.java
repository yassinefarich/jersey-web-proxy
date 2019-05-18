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
public class HTMLProxyService {

  private final String REMOTE_SERVER_URL = "http://localhost";
  private final String CURRENT_BASE_HREF = "/jersey/proxy/html/";

  @GET
  @Path("/{param}/{param2}/{param3}")
  public Response getMsgAsHTML(@PathParam("param") String param,
      @PathParam("param2") String param2,
      @PathParam("param3") String param3,
      @Context ServletContext servletContext) {
    return getMsgAsHTML(param+"/"+param2,param3,servletContext);
  }

  @GET
  @Path("/{param}/{param2}")
  public Response getMsgAsHTML(@PathParam("param") String param,
      @PathParam("param2") String param2,
      @Context ServletContext servletContext) {
    return getMsgAsHTML(param+"/"+param2,servletContext);
  }

  @GET
  @Path("/{param}")
  public Response getMsgAsHTML(@PathParam("param") String param,
      @Context ServletContext servletContext) {
    return sendRequestToServer(param,servletContext);
  }

  private Response sendRequestToServer(String serverURL,ServletContext servletContext) {

    try {
      WebResource webResource = Client
          .create()
          .resource(REMOTE_SERVER_URL + "/" + serverURL);

      String remoteServerResponse = webResource
          .accept(MediaType.WILDCARD)
          .get(ClientResponse.class)
          .getEntity(String.class);

      return Response
          .status(Status.OK)
          .entity(replaceBaseHref(remoteServerResponse,CURRENT_BASE_HREF))
          .type(MediaType.TEXT_HTML_TYPE)
          .build();

    } catch (Exception e) {
      String errorMessage = e.getMessage();
      if (e instanceof ClientHandlerException) {
        errorMessage = String
            .format("Erreur de comunication avec le serveur distant %s .", REMOTE_SERVER_URL);
      }
      return Response
          .status(Status.INTERNAL_SERVER_ERROR)
          .entity(errorMessage)
          .build();
    }
  }

  private String replaceBaseHref(String response,String baseHREF) {
    return response.replaceAll("<base href=\".*\">",
        "<base href=\"" + baseHREF + "\">");
  }

}