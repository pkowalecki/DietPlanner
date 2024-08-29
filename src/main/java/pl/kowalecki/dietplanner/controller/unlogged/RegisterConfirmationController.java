package pl.kowalecki.dietplanner.controller.unlogged;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import pl.kowalecki.dietplanner.IWebPageService;
import pl.kowalecki.dietplanner.model.DTO.ResponseDTO;
import pl.kowalecki.dietplanner.utils.UrlTools;

@RequestMapping("/app")
@Controller
@AllArgsConstructor
public class RegisterConfirmationController {

    private final IWebPageService webPageService;
    private final RestTemplate restTemplate;


    @GetMapping("/confirm")
    public String confirmUser(Model model, @RequestParam("token") String confirmationToken, HttpServletRequest request, HttpServletResponse servletResponse) {
        String url = "http://" + UrlTools.apiUrl + "/confirm?token=" + confirmationToken;
        ResponseEntity<ResponseDTO> response = webPageService.sendGetRequest(url, ResponseDTO.class, request, servletResponse);
        ResponseDTO responseDTO = response.getBody();
        if (responseDTO != null) {
            switch (responseDTO.getStatus()) {
                case ERROR -> model.addAttribute("message", responseDTO.getData());
                case OK -> model.addAttribute("activated", true);
                default -> model.addAttribute("message", "other error, contact administrator");
            }
        } else {
            model.addAttribute("message", "Unexpected error, please try again later.");
        }
        return "pages/unlogged/confirmation";
    }
}
