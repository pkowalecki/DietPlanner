//package pl.kowalecki.dietplanner.controller.logged;
//
//import feign.Response;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.AllArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//import pl.kowalecki.dietplanner.controller.DietplannerApiClient;
//import pl.kowalecki.dietplanner.model.DTO.meal.IngredientNameDTO;
//import pl.kowalecki.dietplanner.utils.ClassMapper;
//import pl.kowalecki.dietplanner.utils.UrlTools;
//
//@Controller
//@AllArgsConstructor
//@RequestMapping("/app/auth")
//public class AddIngredientNamesController {
//
//    private final ClassMapper classMapper;
//    private final DietplannerApiClient apiClient;
//    private final String ADD_INGREDIENT_VIEW = "pages/logged/addIngredient";
//
//    @GetMapping(value = "/addIngredientName")
//    public String addIngredientPage(Model model, HttpServletRequest request, HttpServletResponse httpResponse) {
//        return ADD_INGREDIENT_VIEW;
//    }
//
//    @PostMapping(value = "/addIngredientName")
//    public String addIngredient(@ModelAttribute("addIngredientForm") IngredientNameDTO ingredientNameDTO, HttpServletRequest request, HttpServletResponse httpResponse){
//        System.out.println("dodaje nowego ingredienta: " + ingredientNameDTO);
//        String url = "http://" + UrlTools.apiUrl + "/auth/ingredientNames/ingredient";
//
//        ResponseEntity<String> apiResponse = apiClient.addIngredientName(ingredientNameDTO);
//        //TODO continue
//        return ADD_INGREDIENT_VIEW;
//    }
//
//}
