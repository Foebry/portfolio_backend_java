package routes;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import entities.Response;
import services.Request;
import annotations.Resource;
import annotations.Route;

public class Router {

	final String catch_all = "/*";

	@Resource(path = "/studies")
	OutputStream studies(Request request, String basePath) throws IOException {
		return this.resolveAction(request, StudyRoute.class, basePath);
	}

	@Resource(path = "/experiences")
	OutputStream experiences(Request request, String basePath) throws IOException {
		return this.resolveAction(request, ExperienceRoute.class, basePath);
	}

	@Resource(path = catch_all)
	OutputStream catchAll(Request request, String basePath) throws IOException {
		Response response = new Response(request.getClient());
		return response.resourceNotFound();
	}

	public OutputStream forwardRequest(Request request) throws IOException {

		final String resource = request.getResource();
		final Method[] methods = this.getClass().getDeclaredMethods();

		for (Method method : methods) {
			if (!method.isAnnotationPresent(Resource.class))
				continue;
			final Resource annotation = method.getAnnotation(Resource.class);
			final String basePath = annotation.path();
			final Boolean is_correct_resource = basePath.equals(resource) || basePath.equals(this.catch_all);

			if (is_correct_resource) {
				try {
					return (OutputStream) method.invoke(this, request, basePath);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
					final Response response = new Response(request.getClient());
					return response.internalServerError();
				}
			}
		}

		final Response response = new Response(request.getClient());
		return response.resourceNotFound();
	}

	private OutputStream resolveAction(Request request, Class<?> resource, String basePath)
			throws IOException {
		try {
			final Constructor<?> constructor = resource.getDeclaredConstructor(Request.class);
			final BaseRoute object = (BaseRoute) constructor.newInstance(request);
			final Method[] methods = object.getClass().getDeclaredMethods();
			final ArrayList<Method> matching_methods = new ArrayList<Method>();

			for (Method method : methods) {
				boolean method_has_requested_endpoint = Router.methodMatchesRequestedEndpoint(method, request,
						basePath);
				if (method_has_requested_endpoint)
					matching_methods.add(method);
			}

			if (matching_methods.size() == 0) {
				Response response = new Response(request.getClient());
				return response.routeNotFound();
			}

			for (Method method : matching_methods) {
				boolean method_has_requested_action = Router.methodHasRequestedAction(method, request);
				if (method_has_requested_action) {
					return (OutputStream) method.invoke(object, request);

				}
			}
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			Response response = new Response(request.getClient());
			response.internalServerError();
		}

		Response response = new Response(request.getClient());
		return response.notAllowed();
	}

	private static Boolean methodMatchesRequestedEndpoint(Method method, Request request, String basePath) {
		final Route route_annotation = method.getAnnotation(Route.class);
		if (route_annotation == null)
			return false;

		final String route_endpoint = basePath + route_annotation.endpoint();
		final Boolean is_dynamic_route = route_endpoint.contains("{id}");
		final Boolean request_has_resource_id = request.getResourceId() != null;

		if (is_dynamic_route && request_has_resource_id) {
			final String current_route = route_endpoint.replace("{id}", request.getResourceId());
			final Boolean match_without_trailing_slash = current_route.equals(request.getRequestedEndpoint());
			final Boolean match_with_trailing_slash = current_route.equals(request.getRequestedEndpoint() + "/");

			return match_with_trailing_slash || match_without_trailing_slash;
		}

		return route_endpoint.equals(request.getRequestedEndpoint())
				|| route_endpoint.equals(request.getRequestedEndpoint() + "/");
	}

	private static Boolean methodHasRequestedAction(Method method, Request request) {
		final Route annotation = method.getAnnotation(Route.class);
		final String annotation_method = annotation.method();

		return annotation_method.equals(request.getAction());
	}
}
