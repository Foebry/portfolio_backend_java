package controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import entities.Experience;
import entities.Paginated;
import entities.Serializable;

public class HomeController {

	final String page_key = "page";
	final String pageSize_key = "pageSize";
	final String companyName_key = "companyName";
	final String id_key = "id";

	Stack<Experience> experiences;
	private int pageSize;
	private int page;
	private String companyName;
	private String experienceId;
	private Stack<String> queryKeys;

	public HomeController(Stack<Experience> experiences) {
		super();
		this.experiences = experiences;
		this.pageSize = 20;
		this.page = 1;
		this.companyName = "";
		this.experienceId = "";

		this.queryKeys = new Stack<String>();
		this.queryKeys.push(page_key);
		this.queryKeys.push(pageSize_key);
		this.queryKeys.push(companyName_key);
		this.queryKeys.push(id_key);
	}

	private void setQuery(HashMap<String, ?> query) {
		this.companyName = query.containsKey(companyName_key) ? (String) query.get(companyName_key) : "";
		this.experienceId = query.containsKey(id_key) ? (String) query.get(id_key) : "";
		this.page = query.containsKey(page_key) ? (int) query.get(page_key) : 1;
		this.pageSize = query.containsKey(pageSize_key) ? (int) query.get(pageSize_key) : 20;
	}

	public Paginated<Serializable> getExperiences(HashMap<String, ?> query) {
		this.setQuery(query);
		final int total = this.experiences.size();

		Stack<Serializable> data = new Stack<Serializable>();

		for (int i = 0; i < this.experiences.size(); i++) {

			final String experience_company_name = this.experiences.get(i).getCompany().getName().toLowerCase();
			final String experience_id = this.experiences.get(i).getId();

			final boolean matchingCompanyName = query.containsKey(companyName_key)
					? experience_company_name.equals(this.companyName.toLowerCase())
					: true;

			final boolean matchingExperienceId = query.containsKey(id_key) ? experience_id.equals(this.experienceId)
					: true;
			final boolean shouldBeIncluded = matchingCompanyName && matchingExperienceId;

			if (shouldBeIncluded)
				data.push(this.experiences.get(i));
		}

		final int skip = pageSize * (page - 1);
		final int take = Math.min(skip + pageSize, data.size());

		final Paginated<Serializable> result = new Paginated<Serializable>(page, pageSize, total,
				new ArrayList<Serializable>());

		if (skip > data.size())

			return result;

		for (int i = skip; i < take; i++) {
			Experience experience = this.experiences.get(i);
			result.getItems().add(experience);
		}

		return result;
	}

	public Experience getExperienceById(String id) {
		for (Experience experience : this.experiences) {
			if (experience.getId().equals(id))
				return experience;
		}
		return null;
	}

	public void removeExperience(Experience experience) {
		final Stack<Experience> new_experiences = new Stack<Experience>();

		for (Experience exp : this.experiences) {
			if (exp.getId().equals(experience.getId()))
				continue;
			new_experiences.add(exp);
		}

		this.experiences = new_experiences;
	}

}
