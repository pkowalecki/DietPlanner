package pl.kowalecki.dietplanner.controller.logged;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.kowalecki.dietplanner.model.DTO.ResponseBodyDTO;
import pl.kowalecki.dietplanner.model.DTO.meal.IngredientNameDTO;
import pl.kowalecki.dietplanner.services.WebPage.IWebPageService;
import pl.kowalecki.dietplanner.utils.ClassMapper;
import pl.kowalecki.dietplanner.utils.UrlTools;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/app/auth")
public class IngredientsController {

    IWebPageService webPageService;
    ClassMapper classMapper;

    @GetMapping("/ingredientNames/search")
    public ResponseEntity<List<IngredientNameDTO>> searchIngredients(@RequestParam("query") String query, HttpServletRequest request, HttpServletResponse response) {
        String url = "http://" + UrlTools.apiUrl + "/auth/ingredientNames/search?query=" + query;
        ResponseEntity<ResponseBodyDTO> apiResponse = webPageService.sendGetRequest(url, ResponseBodyDTO.class, request, response);
        if (apiResponse.getBody() != null) {
            List<?> ingredientNamesList = (List<?>) apiResponse.getBody().getData().get("ingredientNames");
            List<IngredientNameDTO> ingredientNames = classMapper.convertToDTOList(ingredientNamesList, IngredientNameDTO.class);
            return ResponseEntity.ok(ingredientNames);
        }else{
            return ResponseEntity.notFound().build();
        }
    }
}
