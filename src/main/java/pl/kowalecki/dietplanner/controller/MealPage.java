package pl.kowalecki.dietplanner.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.kowalecki.dietplanner.model.Meal;
import pl.kowalecki.dietplanner.model.page.FoodBoardPageData;
import pl.kowalecki.dietplanner.repository.MealRepository;
import pl.kowalecki.dietplanner.repository.MealRepositoryImplementation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class MealPage {

    @Autowired
    MealRepository mealRepository;

    @Autowired
    MealRepositoryImplementation mealRepositoryImpl;

    public MealPage(MealRepository mealRepository) {
        this.mealRepository = mealRepository;
    }

    @GetMapping(value = "/generateMealBoard")
    public String mealPage(Model model){
        List<Meal> mealList = mealRepository.findAll();
        model.addAttribute("mealList", mealList);
        return "pages/foodBoardPage";
    }

    @PostMapping(value = "/generateMealBoard")
    public String resultPage(Model model, HttpServletResponse response, @ModelAttribute("form") FoodBoardPageData form){
        List<Long> idsList = form.getMealValues();
        model.addAttribute("result", mealRepositoryImpl.getMealIngredientsFinalList(idsList, form.getMultiplier()));
//        model.addAttribute("meals", mealRepositoryImpl.getmeal)
        return "pages/foodBoardResult";
    }
}
