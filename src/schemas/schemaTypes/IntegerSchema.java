package schemas.schemaTypes;

import java.util.HashMap;

import services.Request;

public class IntegerSchema extends TypeSchema {
    private Integer maximum;
    private Integer minimum;
    private Integer defaultValue;

    public IntegerSchema() {
        super();
        this.defaultValue = null;
    }

    public void setMinimum(Integer minimum) {
        this.minimum = minimum;
    }

    public void setMaximum(Integer maximum) {
        this.maximum = maximum;
    }

    public void setDefault(Integer defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean parse(Request request, HashMap<String, Object> queryOrParams, String key) {
        // if key not in query or params (which ever we are currently checking) set it
        // to the default value if there is one.
        final String value = (String) queryOrParams.get(key);
        final boolean shouldSetDefaultValue = value == null && this.isRequired == false && this.defaultValue != null;

        final boolean validatedRequired = this.validateRequired(value);
        if (!validatedRequired) {
            request.getResponse().getSchemaError().put(key, "Is required");
            return false;
        }

        if (shouldSetDefaultValue) {
            queryOrParams.put(key, (Object) this.defaultValue);
            return true;
        }

        final boolean validatedFormat = this.validateFormat(key, value, request);
        if (!validatedFormat)
            return false;

        final boolean validatedRange = this.validateRange(key, value, request);
        if (!validatedRange)
            return false;

        queryOrParams.put(key, Integer.parseInt(value));
        return true;
    }

    private boolean validateFormat(String key, String value, Request request) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            request.getResponse().getSchemaError().put(key, "Can only be integer values");
            return false;
        }
    }

    boolean validateRange(String key, String value, Request request) {
        final boolean isTooLow = this.minimum != null && Integer.parseInt(value) < this.minimum;
        final boolean isTooHigh = this.maximum != null && Integer.parseInt(value) > this.maximum;

        if (isTooLow)
            request.getResponse().getSchemaError().put(key,
                    "Should be minimum " + this.minimum);

        if (isTooHigh)
            request.getResponse().getSchemaError().put(key,
                    "Should be maximum " + this.maximum);

        if (isTooHigh || isTooLow)
            return false;

        return true;
    }
}
