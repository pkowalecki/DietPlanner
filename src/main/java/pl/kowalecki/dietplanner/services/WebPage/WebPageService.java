package pl.kowalecki.dietplanner.services.WebPage;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
@AllArgsConstructor
public class WebPageService implements IWebPageService {

    @Autowired
    private HttpSession session;

    @Override
    public void setSessionAttribute(String key, Object value) {
        session.setAttribute(key, value);
    }

    @Override
    public Object getSessionAttribute(String key) {
        return session.getAttribute(key);
    }

    public void setMsg(MessageType type, String message) {
        Map<String, String> messageData = new HashMap<>();
        messageData.put("type", type.name());
        messageData.put("message", message);
        messageData.put("icon", type.getPole());
        session.setAttribute("webMsg", messageData);
    }

    private void getMsg(Model model) {
        Map<String, String> messageData = (Map<String, String>) session.getAttribute("webMsg");
        if (messageData != null) {
            model.addAttribute("webMsg", messageData);
            removeMsg();
        }
    }

    private void removeMsg(){
        session.removeAttribute("webMsg");
    }

    @Override
    public void addCommonWebData(Model model) {
        getMsg(model);
    }

}
