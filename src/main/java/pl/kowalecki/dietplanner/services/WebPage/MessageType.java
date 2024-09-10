package pl.kowalecki.dietplanner.services.WebPage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum MessageType {

    SUCCESS("success"),
    ERROR("error"),
    WARNING("warning"),
    INFO("info"),
    QUESTION("question"),
    ;

    private String pole;
}
