package pl.kowalecki.dietplanner.controller.unlogged;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import pl.kowalecki.dietplanner.model.DTO.ResponseDTO;
import pl.kowalecki.dietplanner.utils.UrlTools;

@RequestMapping("/app")
@Controller
@AllArgsConstructor
public class RegisterConfirmationController {

    private final RestTemplate restTemplate;

    @GetMapping("/confirm")
    public String confirmUser(Model model, @RequestParam("token") String confirmationToken) {
        ResponseEntity<ResponseDTO> response = restTemplate.getForEntity(
                "http://"+ UrlTools.apiUrl +"/confirm?token=" + confirmationToken,
                ResponseDTO.class
        );
        ResponseDTO responseDTO = response.getBody();
        assert responseDTO != null;
        if (responseDTO.getStatus().equals(ResponseDTO.ResponseStatus.ERROR)){
            model.addAttribute("message", responseDTO.getData());
        }else if(responseDTO.getStatus().equals(ResponseDTO.ResponseStatus.OK)){
            model.addAttribute("activated", true);
        }else{
            model.addAttribute("message", "other error, contact administrator");
        }
        return "pages/unlogged/confirmation";
    }
}
