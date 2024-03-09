package schemas;

import java.lang.reflect.Field;
import java.util.HashMap;

import schemas.schemaTypes.TypeSchema;
import services.Request;

public class Schema {

    public Schema() {

    }

    public boolean parse(Request request, HashMap<String, Object> hashmap) {
        Field[] fields = this.getClass().getDeclaredFields();

        for (Field field : fields) {
            try {
                TypeSchema value = (TypeSchema) field.get(this);
                String key = field.getName();
                Boolean parsedField = value.parse(request, hashmap, key);
                if (!parsedField)
                    return false;
            } catch (IllegalArgumentException | IllegalAccessException e) {
                return false;
            }
        }
        return true;
    }
}
