package schemas.experiences.List;

import java.util.HashMap;

import schemas.Schema;
import services.Request;

public class ListSchema extends Schema {
    QuerySchema query;

    public ListSchema() {
        super();
        this.query = new QuerySchema();
    }

    public boolean parse(Request request, HashMap<String, Object> objectToParse) {
        return this.query.parse(request, objectToParse);
    }
}
