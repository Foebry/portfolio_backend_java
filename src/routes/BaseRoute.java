package routes;

import entities.Response;

import services.Request;

public class BaseRoute {

	Response response;

	public BaseRoute(Request request) {
		this.response = new Response(request.getClient());
	}

	public Response getResponse() {
		return this.response;
	}

}
