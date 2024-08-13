package pl.kowalecki.dietplanner.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.kowalecki.dietplanner.model.Meal;
import pl.kowalecki.dietplanner.model.page.FoodBoardPageData;

import pl.kowalecki.dietplanner.services.MealServiceImpl;

import java.util.List;

@RequestMapping("/app/auth")
@Controller
@AllArgsConstructor
public class MealPageController {

    private final MealServiceImpl mealService;

    @GetMapping(value = "/generateMealBoard")
    public String mealPage(Model model){
        List<Meal> mealList = mealService.getAllMeals();
        model.addAttribute("mealList", mealList);
        return "pages/logged/foodBoardPage";
    }

    @PostMapping(value = "/generateMealBoard")
    public String resultPage(Model model, HttpServletResponse response, @ModelAttribute("form") FoodBoardPageData form){
        List<Long> idsList = form.getMealValues();
        model.addAttribute("result", mealService.getMealIngredientsFinalList(idsList, form.getMultiplier()));
//        model.addAttribute("meals", mealRepositoryImpl.getmeal)
        return "pages/logged/foodBoardResult";
    }
}
