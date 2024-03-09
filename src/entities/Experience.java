package entities;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

import annotations.Column;
import annotations.Entity;
import annotations.Relation;
import annotations.Serialize;

@Entity(name = "Experience")
public class Experience extends Serializable {

	@Column(type = "id")
	@Serialize()
	protected String id;

	@Column(type = "Date")
	@Serialize()
	protected Calendar startDate;

	@Column(type = "Date", nullable = true)
	@Serialize()
	protected Calendar endDate;

	@Relation(type = "ManyToOne")
	@Serialize()
	protected Company company;

	@Column(type = "varchar")
	@Serialize()
	protected String position;

	@Column(type = "varchar", nullable = true)
	@Serialize()
	protected String stuff;

	public Experience(Calendar startDate, Calendar endDate, Company company, String position, String stuff) {
		super();

		Random random = new Random();
		final Float floatingPoint = random.nextFloat();

		this.id = Float.toString(floatingPoint).getBytes().toString();
		this.startDate = startDate;
		this.endDate = endDate;
		this.company = company;
		this.position = position;
		this.stuff = stuff;
	}

	public Experience(HashMap<String, ?> data) {
		super();
		String id = (String) data.get("id");
		Calendar startDate = (Calendar) data.get("startDate");
		Calendar endDate = (Calendar) data.get("endDate");
		String position = (String) data.get("position");
		String stuff = (String) data.get("stuff");

		if (id != null)
			this.id = id;
		if (startDate != null)
			this.startDate = startDate;
		if (endDate != null)
			this.endDate = endDate;
		if (position != null)
			this.position = position;
		if (stuff != null)
			this.stuff = stuff;

	}

	public Experience(String id, Calendar startDate, Calendar endDate, String position, String stuff) {
		super();
		this.id = id;
		this.startDate = startDate;
		this.endDate = endDate;
		this.position = position;
		this.stuff = stuff;
	}

	public Experience(String id, Calendar startDate, Calendar endDate, Company company, String position, String stuff) {
		super();
		this.id = id;
		this.startDate = startDate;
		this.endDate = endDate;
		this.company = company;
		this.position = position;
		this.stuff = stuff;
	}

	public Company getCompany() {
		return this.company;
	}

	public String getId() {
		return this.id;
	}

	public void setStartDate(Calendar date) {
		this.startDate = date;
	}

	public void setEndDate(Calendar date) {
		this.endDate = date;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public void setPostion(String position) {
		this.position = position;
	}

	public void setStuff(String stuff) {
		this.stuff = stuff;
	}

}
