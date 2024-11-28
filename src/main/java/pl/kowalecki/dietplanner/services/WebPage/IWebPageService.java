package pl.kowalecki.dietplanner.services.WebPage;


import org.springframework.ui.Model;

public interface IWebPageService {
    void setSessionAttribute(String key, Object value);
    Object getSessionAttribute(String key);
    void setMsg(MessageType type, String message);
    void addCommonWebData(Model model);
}
