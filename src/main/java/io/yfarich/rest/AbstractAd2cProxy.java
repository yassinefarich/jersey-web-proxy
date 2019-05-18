package io.yfarich.rest;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

public abstract class AbstractAd2cProxy {

  @GET
  @Path("/{param}/{param2}/{param3}")
  public Response getMsgAsHTML(@PathParam("param") String param,
      @PathParam("param2") String param2,
      @PathParam("param3") String param3,
      @Context ServletContext servletContext) {
    return getMsgAsHTML(param + "/" + param2, param3, servletContext);
  }

  @GET
  @Path("/{param}/{param2}")
  public Response getMsgAsHTML(@PathParam("param") String param,
      @PathParam("param2") String param2,
      @Context ServletContext servletContext) {
    return getMsgAsHTML(param + "/" + param2, servletContext);
  }

  @GET
  @Path("/{param}")
  public Response getMsgAsHTML(@PathParam("param") String param,
      @Context ServletContext servletContext) {
    return sendGetRequest(param, servletContext);
  }

  public abstract Response sendGetRequest(String param, ServletContext servletContext);

  @POST
  @Path("/{param}/{param2}/{param3}")
  public Response postMsgAsHTML(
      @PathParam("param") String param,
      @PathParam("param2") String param2,
      @PathParam("param3") String param3,
      String message,
      @Context ServletContext servletContext) {
    return postMsgAsHTML(param + "/" + param2, message, servletContext);
  }

  @POST
  @Path("/{param}/{param2}")
  public Response postMsgAsHTML(@PathParam("param") String param,
      @PathParam("param2") String param2,
      String message,
      @Context ServletContext servletContext) {
    return postMsgAsHTML(param + "/" + param2, message, servletContext);
  }

  @POST
  @Path("/{param}")
  public Response postMsgAsHTML(
      @PathParam("param") String param,
      String message,
      @Context ServletContext servletContext) {
    return sendPOSTRequest(param, message, servletContext);
  }

  public abstract Response sendPOSTRequest(String param, String message,
      ServletContext servletContext);

}
