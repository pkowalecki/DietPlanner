package pl.kowalecki.dietplanner.controller.unlogged;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.kowalecki.dietplanner.IWebPageService;
import pl.kowalecki.dietplanner.model.DTO.ResponseDTO;
import pl.kowalecki.dietplanner.utils.UrlTools;

@RequestMapping("/app")
@Controller
@AllArgsConstructor
public class RegisterConfirmationController {

    private final IWebPageService webPageService;


    @GetMapping("/confirm")
    public String confirmUser(Model model, @RequestParam("token") String confirmationToken) {
        String url = "http://" + UrlTools.apiUrl + "/confirm?token=" + confirmationToken;
        ResponseEntity<ResponseDTO> response = webPageService.sendGetRequest(url, ResponseDTO.class);
//        ResponseEntity<ResponseDTO> response = restTemplate.getForEntity(
//                "http://"+ UrlTools.apiUrl +"/confirm?token=" + confirmationToken,
//                ResponseDTO.class
//        );
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
