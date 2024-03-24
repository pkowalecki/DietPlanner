package pl.kowalecki.dietplanner.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.kowalecki.dietplanner.model.Meal;
import pl.kowalecki.dietplanner.model.page.FoodBoardPageData;
import pl.kowalecki.dietplanner.repository.MealRepository;
import pl.kowalecki.dietplanner.repository.MealRepositoryImplementation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MealPageController {

    @Autowired
    MealRepository mealRepository;

    @Autowired
    MealRepositoryImplementation mealRepositoryImpl;

    public MealPageController(MealRepository mealRepository) {
        this.mealRepository = mealRepository;
    }

    @GetMapping(value = "/generateMealBoard")
    public String mealPage(Model model){
        List<Meal> mealList = mealRepository.findAll();
        model.addAttribute("mealList", mealList);
        return "pages/foodBoardPage";
    }

    @PostMapping(value = "/generateMealBoard")
    public String resultPage(Model model,HttpSession httpSession, HttpServletRequest req, HttpServletResponse resp, @ModelAttribute("form") FoodBoardPageData form){
        List<Long> idsList = form.getMealValues();
        List<Meal> meal = mealRepository.findMealsByMealIdIn(idsList);

        model.addAttribute("result", mealRepositoryImpl.getMealIngredientsFinalList(idsList, form.getMultiplier()));
        model.addAttribute("meals", meal);
        model.addAttribute("idsList", idsList);
        return "pages/foodBoardResult";
    }
}
