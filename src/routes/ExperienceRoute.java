package routes;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.HashMap;

import controllers.HomeController;

import entities.Experience;
import entities.Paginated;
import entities.Serializable;
import schemas.experiences.List.ListSchema;
import services.Request;
import annotations.Route;

public class ExperienceRoute extends BaseRoute {

	private HomeController controller;
	private String routeName = "experiences";

	public ExperienceRoute(Request request) {
		super(request);
		this.controller = request.factory.getHomeController();
	}

	public String getRouteName() {
		return this.routeName;
	}

	@Route(endpoint = "/", method = "GET", name = "listExperiences", schema = ListSchema.class)
	public OutputStream get(Request request) throws IOException, IllegalArgumentException, IllegalAccessException {

		HashMap<String, ?> query = request.getQuery();
		Paginated<Serializable> experiences = this.controller.getExperiences(query);

		String result = Paginated.serialize(experiences);

		return this.getResponse().ok(result);

	}

	@Route(endpoint = "/{id}", method = "GET", name = "getExperienceById")
	public OutputStream getById(Request request) throws IOException {
		String id = request.getResourceId();
		Serializable experience = this.controller.getExperienceById(id);
		if (experience == null)
			return this.getResponse().notFound();

		String result = experience.serialize((Class<?>) null);

		return this.getResponse().ok(result);
	}

	@Route(endpoint = "/{id}/company", method = "GET", name = "getCompanyOfExperienceById")
	public OutputStream getCompanyOfExperienceById(Request request) throws IOException {
		String id = request.getResourceId();
		Experience experience = this.controller.getExperienceById(id);
		if (experience == null)
			return this.getResponse().notFound();

		Serializable company = experience.getCompany();

		String result = company.serialize((Class<?>) null);

		return this.getResponse().ok(result);
	}

	@Route(endpoint = "/", method = "POST", name = "createExperience")
	public OutputStream post(Request request) throws IOException {
		final String id = (String) request.getQuery().get("id");
		final Experience experience = this.controller.getExperienceById(id);

		if (experience == null)
			throw new NullPointerException("Experience not found");

		this.controller.removeExperience(experience);

		return this.getResponse().noContent();

	}

	@Route(endpoint = "/{id}", method = "PUT", name = "updateExperience")
	public OutputStream put(Request request) throws IOException {
		final String id = (String) request.getQuery().get("id");
		final Experience experience = this.controller.getExperienceById(id);

		if (experience == null)
			throw new NullPointerException("Experience not found");

		HashMap<String, ?> requestBody = request.getRequestBody();

		String startDate = (String) requestBody.get("startDate");
		Calendar newStartDate = Calendar.getInstance();
		newStartDate.set(Integer.parseInt(startDate.split("-")[0]), Integer.parseInt(startDate.split("-")[1]),
				Integer.parseInt(startDate.split("-")[2]));

		String endDate = (String) requestBody.get("endDate");
		Calendar newEndDate = Calendar.getInstance();
		newEndDate.set(Integer.parseInt(endDate.split("-")[0]), Integer.parseInt(endDate.split("-")[1]),
				Integer.parseInt(endDate.split("-")[2]));

		experience.setStartDate(newStartDate);
		experience.setEndDate(newEndDate);

		return this.getResponse().ok("OK");

	}

	@Route(endpoint = "/{id}", method = "PATCH", name = "patchExperience")
	public OutputStream patch() throws IOException {
		return this.getResponse().ok("OK");
	}

	@Route(endpoint = "/{id}", method = "DELETE", name = "deleteExperience")
	public OutputStream delete(Request request) throws IOException {
		String id = request.getResourceId();
		Experience experience = this.controller.getExperienceById(id);

		if (experience == null)
			return this.getResponse().notFound();

		this.controller.removeExperience(experience);

		return this.getResponse().noContent();

	}

}
