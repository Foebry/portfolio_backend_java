package schemas.schemaTypes;

import java.util.HashMap;

import services.Request;

public class TypeSchema {

    protected boolean isRequired;

    public TypeSchema() {
        this.isRequired = false;
    }

    public void setRequired(boolean required) {
        this.isRequired = required;
    }

    protected boolean validateRequired(String value) {
        if (value == null && this.isRequired)
            return false;
        return true;
    }

    public boolean parse(Request request, HashMap<String, Object> queryOrParams, String key) {
        String value = (String) queryOrParams.get(key);
        return this.validateRequired(value);
    }
}
