package pl.kowalecki.dietplanner.services.WebPage;

import freemarker.template.SimpleSequence;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface IWebPageService {
    Map<String, String> addMessageToPage(MessageType type, String message);
    void addError(String pole, String error);
    Map<String, ArrayList<String>> getErrors();
    SimpleSequence getErrorsAsList();
}
