package controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Stack;

import entities.Company;
import entities.Experience;
import entities.Paginated;
import entities.Serializable;
import helperClasses.DateHelperClass;
import services.Database;

public class ExperienceController extends AbstractController<Experience> {

	Stack<Experience> experiences;
	Database db;

	public Experience create() {
		String id = "";
		return new Experience(id, null, null, null, null, null);
	}

	public Experience getById(String id) {
		return new Experience(id, null, null, null, id, id);
	}

	public void delete(String id) {

	}

	public Experience update(String id) {
		return new Experience(id, null, null, null, id, id);
	}

	public Paginated<Experience> getManyAndCount(HashMap<String, Object> query) throws SQLException {
		Integer page = (Integer) query.get("page");
		Integer pageSize = (Integer) query.get("pageSize");
		String id = (String) query.get("id");
		String companyName = (String) query.get("companyName");

		Integer offset = (page - 1) * pageSize;
		ArrayList<Experience> experiences = new ArrayList<Experience>();

		String sql = "Select * from experiences limit " + pageSize + " offset " + offset;

		ResultSet result = this.db.execute(sql);
		DateHelperClass dhc = new DateHelperClass();

		while (result.next()) {
			String experienceId = String.valueOf(result.getInt(1));
			Calendar startDate = dhc.convertStringToCalendar(result.getString(2), "date");
			Calendar endDate = dhc.convertStringToCalendar(result.getString(3), "date");

			HashMap<String, Object> data = new HashMap<>();
			data.put("id", experienceId);
			data.put("startDate", startDate);
			data.put("endDate", endDate);

			Experience experience = new Experience(data);
			experiences.add(experience);
		}

		return new Paginated<Experience>(page, pageSize, experiences.size(), experiences);
	}

	public ExperienceController(Stack<Experience> experiences, Database db) {
		super();
		this.experiences = experiences;
		this.db = db;
	}

	public Paginated<Serializable> getExperiences(HashMap<String, ?> query) {
		// this.setQuery(query);
		final int total = this.experiences.size();

		Stack<Serializable> data = new Stack<Serializable>();

		for (int i = 0; i < this.experiences.size(); i++) {

			final String experience_company_name = this.experiences.get(i).getCompany().getName().toLowerCase();
			final String experience_id = this.experiences.get(i).getId();
			final String experienceId = (String) query.get("id");
			final String companyName = (String) query.get("companyName");

			final boolean matchingCompanyName = companyName == null
					|| experience_company_name.contains(companyName.toLowerCase());
			final boolean matchingExperienceId = experienceId == null || experience_id.equals(experienceId);

			final boolean shouldBeIncluded = matchingCompanyName && matchingExperienceId;

			if (shouldBeIncluded)
				data.push(this.experiences.get(i));
		}

		final int page = (Integer) query.get("page");
		final int pageSize = (Integer) query.get("pageSize");

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
