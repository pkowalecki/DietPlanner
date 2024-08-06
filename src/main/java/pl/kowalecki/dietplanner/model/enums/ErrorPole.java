package pl.kowalecki.dietplanner.model.enums;

public enum ErrorPole {
    TOKEN("");

    private String fieldName;

    ErrorPole(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
