package pl.kowalecki.dietplanner.controller.logged;


import lombok.Getter;

public record UserData(
        String firstName,
        String lastName
) {
    @Getter
    enum UserDataPola {
        FIRSTNAME("firstName"),
        LASTNAME("lastName");
        private final String field;

        UserDataPola(String field) {
            this.field = field;
        }
    }

}

