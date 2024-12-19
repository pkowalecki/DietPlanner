package pl.kowalecki.dietplanner.model;

import java.util.Map;

public class SimpleEntry implements Map.Entry<String, String> {

    private String key;
    private String value;

    public SimpleEntry(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String setValue(String value) {
        String oldValue = this.value;
        this.value = value;
        return oldValue;
    }
}