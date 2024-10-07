package pl.kowalecki.dietplanner.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kowalecki.dietplanner.model.DTO.FoodBoardPageRequest;
import pl.kowalecki.dietplanner.model.DTO.MealStarterPackDTO;
import pl.kowalecki.dietplanner.model.DTO.User.LoginRequestDTO;
import pl.kowalecki.dietplanner.model.DTO.meal.AddMealRequestDTO;
import pl.kowalecki.dietplanner.model.DTO.meal.IngredientNameDTO;
import pl.kowalecki.dietplanner.model.DTO.meal.MealBoardDTO;
import pl.kowalecki.dietplanner.model.Meal;

import java.util.List;

//@FeignClient(name = "diet-planner-api", path = "${diet.planner.api.prefix}", url = "localhost:8082")
@FeignClient(name = "diet-planner-api")
public interface DietplannerApiClient {

    @PostMapping("/ingredientNames/ingredient")
    ResponseEntity<String> addIngredientName(IngredientNameDTO ingredientNameDTO);

    @GetMapping("/ingredientNames/search")
    ResponseEntity<List<IngredientNameDTO>> getIngredientNames(@RequestParam("query") String query);

    @GetMapping("/getMealStarterPack")
    ResponseEntity<MealStarterPackDTO> getMealStarterPack();

    @PostMapping("/meal/addMeal")
    ResponseEntity<String> addMeal(AddMealRequestDTO addMealRequestDTO);

    @GetMapping("/meal/allMeal")
    ResponseEntity<List<Meal>> getAllMeals();

    @PostMapping("/meal/generateFoodBoard")
    ResponseEntity<MealBoardDTO> generateFoodBoard(FoodBoardPageRequest apiReq);

    @PostMapping("/meal/getMealNamesById")
    ResponseEntity<List<String>> getMealNamesById(List<Long> ids);

    @GetMapping("/meal/getMealHistory")
    ResponseEntity<HttpStatus> getMealHistory();

    @RequestMapping(method = RequestMethod.POST, value= "/api/v1/login")
    ResponseEntity<HttpStatus> postLoginRequest(@RequestBody LoginRequestDTO loginRequestDto);

    @GetMapping("/")
    ResponseEntity<HttpStatus> userConfirmation(@RequestParam("token") String confirmationToken);
}