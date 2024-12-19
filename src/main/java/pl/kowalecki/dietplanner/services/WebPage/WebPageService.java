package pl.kowalecki.dietplanner.services.WebPage;

import freemarker.template.SimpleSequence;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import org.springframework.ui.Model;
import pl.kowalecki.dietplanner.model.DTO.MealType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@Slf4j
@AllArgsConstructor
public class WebPageService implements IWebPageService {

    private final Map<String, ArrayList<String>> errors = new HashMap<String, ArrayList<String>>();

    private Map<String, String> setMsg(MessageType type, String message) {
        Map<String, String> messageData = new HashMap<>();
        messageData.put("type", type.name());
        messageData.put("message", message);
        messageData.put("icon", type.getPole());
        return messageData;
    }

    @Override
    public Map<String, String> addMessageToPage(MessageType type, String message) {
        return setMsg(type, message);
    }

    @Override
    public void addError(final String pole, final String error) {
        System.out.println("WRZUCIŁEM ERORR DO PAGE: " + pole +" " + error);
        ArrayList<String> lista = errors.get(pole);
        if (lista == null) lista = new ArrayList<String>();
        lista.add(error);
        errors.put(pole, lista);
    }

    @Override
    public Map<String, ArrayList<String>> getErrors() {
        return errors;
    }

    @Override
    public SimpleSequence getErrorsAsList() {
        SimpleSequence ss = new SimpleSequence();
        for (String key : errors.keySet()) {
            Map<String, Object> mapka = new HashMap<String, Object>();
            mapka.put("name", key);
            mapka.put("messages", errors.get(key));
            ss.add(mapka);
        }
        return ss;
    }


}
