package pl.kowalecki.dietplanner.services.WebPage;



import java.util.Map;

public interface IWebPageService {
    Map<String, String> addMessageToPage(MessageType type, String message);
}
