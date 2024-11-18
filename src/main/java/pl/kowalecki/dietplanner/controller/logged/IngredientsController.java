//package pl.kowalecki.dietplanner.controller.logged;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.AllArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import pl.kowalecki.dietplanner.controller.DietplannerApiClient;
//import pl.kowalecki.dietplanner.model.DTO.ResponseBodyDTO;
//import pl.kowalecki.dietplanner.model.DTO.meal.IngredientNameDTO;
//import pl.kowalecki.dietplanner.utils.ClassMapper;
//import pl.kowalecki.dietplanner.utils.UrlTools;
//
//import java.util.List;
//
//@RestController
//@AllArgsConstructor
//@RequestMapping("/app/auth")
//public class IngredientsController {
//
//    ClassMapper classMapper;
//
//
//    @GetMapping("/ingredientNames/search")
//    public List<IngredientNameDTO> searchIngredients(@RequestParam("query") String query, HttpServletRequest request, HttpServletResponse response) {
//        String url = "http://" + UrlTools.apiUrl + "/auth/ingredientNames/search?query=" + query;
//        ResponseEntity<List<IngredientNameDTO>> apiResponse = apiClient.getIngredientNames(query);
//        return apiResponse.getBody();
//
//    }
//}
