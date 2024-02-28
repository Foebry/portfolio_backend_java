package schemas.schemaTypes;

import java.util.HashMap;

import services.Request;

public class StringSchema extends TypeSchema {
    private Integer maxLength;
    private Integer minLength;
    private String format;
    private String defaultValue;

    public StringSchema() {
        super();
        this.format = null;
        this.defaultValue = null;
    }

    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void setDefault(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean parse(Request request, HashMap<String, Object> queryOrParams, String key) {
        // if key not in query or params (which ever we are currently checking) set it
        // to the default value if there is one.
        final String value = (String) queryOrParams.get(key);

        final boolean validatedRequired = this.validateRequired(value);
        if (!validatedRequired) {
            request.getResponse().getSchemaError().put(key, "Is required");
            return false;
        }

        if (value == null) {
            if (this.defaultValue != null)
                queryOrParams.put(key, (Object) this.defaultValue);
            else
                return true;
        }

        final boolean validatedLength = this.validateLength(key, value, request);
        if (!validatedLength)
            return false;

        // final boolean validatedFormat = this.validateFormat(key, value, request);
        // if (!validatedFormat)
        // return false;

        queryOrParams.put(key, value);
        return true;
    }

    boolean validateLength(String key, String value, Request request) {
        final boolean isTooShort = value.length() < this.minLength;
        final boolean isTooLong = value.length() > this.maxLength;

        if (isTooShort)
            request.getResponse().getSchemaError().put(key,
                    "Should be at least " + this.minLength + " characters long.");

        if (isTooLong)
            request.getResponse().getSchemaError().put(key,
                    "Should be maximum " + this.maxLength + " characters long.");

        if (isTooLong || isTooShort)
            return false;

        return true;
    }
}
