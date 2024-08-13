package pl.kowalecki.dietplanner.controller.logged;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.kowalecki.dietplanner.model.Meal;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RequestMapping("/app/auth")
@Controller
@AllArgsConstructor
public class MealController {

    @GetMapping( value = "/addMeal")
    public String getListMeal(){
        return "pages/logged/addMeal";
    }
}
