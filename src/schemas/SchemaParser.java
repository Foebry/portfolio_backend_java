package schemas;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import entities.Response;
import schemas.schemaTypes.StringSchema;
import schemas.schemaTypes.TypeSchema;
import services.Request;

interface ParseSchemaResponse {
    Map<String, Object> parsedSchema = null;
    Map<String, String> invalidSchema = null;
}

public class SchemaParser {

    private Schema schema;
    private Request request;

    public boolean parseSchema(Class<? extends Schema> schemaClass, Request request) {
        try {

            Constructor<?> constructor = schemaClass.getConstructor();
            Schema schema = (Schema) constructor.newInstance();

            // final boolean parsedQuery = schema.parse(request, request.getQuery());
            final boolean parsedQuery = schema.parse(request, request.getQuery());

            // final boolean parsedParams = schema.parse(request.getParams());

            return parsedQuery;

        }

        catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException exception) {
            return false;
        }
    }

    // private boolean parseQuery(Request request) throws IllegalArgumentException,
    // IllegalAccessException {
    // Field[] fields = this.schema.getClass().getDeclaredFields();

    // for (Field field : fields) {
    // final TypeSchema value = (TypeSchema) field.get(this.schema);
    // final boolean parsed = value.parse(this.request, field);
    // if (!parsed)
    // return false;
    // }
    // return true;
    // };

    // private boolean parseStringSchema(String value, String key, Object schema)
    // throws IOException {
    // final boolean valid = ((StringSchema) schema).validate(key, value,
    // this.request);
    // if (!valid)
    // return false;

    // this.request.getQuery().put(key, value);
    // return true;
    // }

}
