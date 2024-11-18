//package pl.kowalecki.dietplanner.controller;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import pl.kowalecki.dietplanner.model.DTO.FoodBoardPageRequest;
//import pl.kowalecki.dietplanner.model.DTO.MealStarterPackDTO;
//import pl.kowalecki.dietplanner.model.DTO.User.LoginRequestDTO;
//import pl.kowalecki.dietplanner.model.DTO.meal.AddMealRequestDTO;
//import pl.kowalecki.dietplanner.model.DTO.meal.IngredientNameDTO;
//import pl.kowalecki.dietplanner.model.DTO.meal.MealBoardDTO;
//import pl.kowalecki.dietplanner.model.Meal;
//
//import java.util.List;
//
//public interface DietplannerApiClient {
//
//    @PostMapping("/api/v1/dpa/ingredientNames/ingredient")
//    ResponseEntity<String> addIngredientName(IngredientNameDTO ingredientNameDTO);
//
//    @GetMapping("/api/v1/dpa/ingredientNames/search")
//    ResponseEntity<List<IngredientNameDTO>> getIngredientNames(@RequestParam("query") String query);
//
//    @GetMapping("/api/v1/dpa/getMealStarterPack")
//    ResponseEntity<MealStarterPackDTO> getMealStarterPack();
//
//    @PostMapping("/api/v1/dpa/meal/addMeal")
//    ResponseEntity<String> addMeal(AddMealRequestDTO addMealRequestDTO);
//
//    @GetMapping("/api/v1/dpa/meal/allMeal")
//    ResponseEntity<List<Meal>> getAllMeals();
//
//    @PostMapping("/api/v1/dpa/meal/generateFoodBoard")
//    ResponseEntity<MealBoardDTO> generateFoodBoard(FoodBoardPageRequest apiReq);
//
//    @PostMapping("/api/v1/dpa/meal/getMealNamesById")
//    ResponseEntity<List<String>> getMealNamesById(List<Long> ids);
//
//    @GetMapping("/api/v1/dpa/meal/getMealHistory")
//    ResponseEntity<HttpStatus> getMealHistory();
//
//    @RequestMapping(method = RequestMethod.POST, value= "/api/v1/dpa/login")
//    ResponseEntity<HttpStatus> postLoginRequest(@RequestBody LoginRequestDTO loginRequestDto);
//
//    @GetMapping("/api/v1/dpa/")
//    ResponseEntity<HttpStatus> userConfirmation(@RequestParam("token") String confirmationToken);
//}