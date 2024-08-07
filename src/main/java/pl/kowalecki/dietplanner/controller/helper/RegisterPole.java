package pl.kowalecki.dietplanner.controller.helper;

public enum RegisterPole {

    NICKNAME("nickname"),
    NAME("name"),
    SURNAME("surname"),
    EMAIL("emailReg"),
    PASSWORD("passwordReg"),
    PASSWORD2("passwordReg2"),
    ROLE("role")
    ;

    private String fieldName;
    RegisterPole(String fieldName) {
        this.fieldName=fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
