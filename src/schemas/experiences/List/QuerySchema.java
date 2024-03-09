package schemas.experiences.List;

import schemas.Schema;
import schemas.schemaTypes.IntegerSchema;
import schemas.schemaTypes.StringSchema;

public class QuerySchema extends Schema {

    public StringSchema companyName;
    public StringSchema id;
    public IntegerSchema page;
    public IntegerSchema pageSize;

    public QuerySchema() {
        super();
        this.companyName = this.setCompanyName();
        this.id = this.setExperienceId();
        this.page = this.setPage();
        this.pageSize = this.setPageSize();
    }

    StringSchema setCompanyName() {
        final StringSchema stringSchema = new StringSchema();
        stringSchema.setMinLength(1);
        stringSchema.setRequired(true);

        return stringSchema;
    }

    StringSchema setExperienceId() {
        final StringSchema stringSchema = new StringSchema();
        stringSchema.setMinLength(1);

        return stringSchema;
    }

    IntegerSchema setPage() {
        final IntegerSchema integerSchema = new IntegerSchema();
        integerSchema.setDefault(1);
        integerSchema.setMinimum(1);

        return integerSchema;
    }

    IntegerSchema setPageSize() {
        final IntegerSchema integerSchema = new IntegerSchema();
        integerSchema.setDefault(15);
        integerSchema.setMinimum(1);

        return integerSchema;
    }
}
