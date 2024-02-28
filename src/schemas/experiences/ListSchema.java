package schemas.experiences;

import schemas.Schema;
import schemas.schemaTypes.StringSchema;

public class ListSchema extends Schema {

	public StringSchema companyName;
	public StringSchema experienceId;
	// IntegerSchema page;
	// IntegerSchema pageSize;

	public ListSchema() {
		super();
		this.companyName = this.setCompanyName();
		this.experienceId = this.setExperienceId();
	};

	StringSchema setCompanyName() {
		final StringSchema stringSchema = new StringSchema();
		stringSchema.setRequired(false);
		stringSchema.setMinLength(1);
		stringSchema.setMaxLength(100);

		return stringSchema;
	}

	StringSchema setExperienceId() {
		final StringSchema stringSchema = new StringSchema();
		stringSchema.setRequired(true);
		stringSchema.setMinLength(2);
		stringSchema.setMaxLength(100);

		return stringSchema;
	}
}
