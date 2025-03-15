package pl.kowalecki.dietplanner.service.meal.mealManagement;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import pl.kowalecki.dietplanner.client.dpa.meal.DietPlannerApiClient;
import pl.kowalecki.dietplanner.controller.helper.AddMealHelper;
import pl.kowalecki.dietplanner.model.DTO.MealStarterPack;
import pl.kowalecki.dietplanner.model.DTO.MealType;
import pl.kowalecki.dietplanner.model.DTO.meal.AddMealRequestDTO;
import pl.kowalecki.dietplanner.model.DTO.meal.MealMainInfoDTO;
import pl.kowalecki.dietplanner.model.Meal;
import pl.kowalecki.dietplanner.model.WebPageResponse;
import pl.kowalecki.dietplanner.service.WebPage.IWebPageResponseBuilder;
import pl.kowalecki.dietplanner.utils.MapUtils;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class MealManagementService implements IMealManagementService {

    private final DietPlannerApiClient apiMealService;
    private final AddMealHelper addMealHelper;
    private final IWebPageResponseBuilder responseBuilder;

    @Override
    public Mono<String> getAddMealPage(Model model) {
        return apiMealService.getMealStarterPack()
                .map(mealStarterPack -> {
                    model.addAttribute("meal", new Meal());
                    model.addAttribute("ingredientNameList", MapUtils.mapIngredientName(mealStarterPack.getIngredientNameList()));
                    model.addAttribute("ingredientUnitList", MapUtils.mapIngredientUnit(mealStarterPack.getIngredientUnitList()));
                    model.addAttribute("mealTypeList", MapUtils.mapMealType(mealStarterPack.getMealTypeList()));
                    model.addAttribute("measurementTypeList", MapUtils.mapMeasurementType(mealStarterPack.getMeasurementTypeList()));
                    return "pages/logged/addEditMeal";
                });
    }

    @Override
    public Mono<WebPageResponse> addOrUpdateMeal(AddMealRequestDTO addMealRequestDTO) {
        Map<String, String> errors = addMealHelper.checkData(addMealRequestDTO);
        if (!errors.isEmpty()) {
            return Mono.just(responseBuilder.buildErrorWithFields(errors));
        }
        return apiMealService.addOrUpdateMeal(addMealRequestDTO)
                .flatMap(response -> {
                    if (response.getStatusCode().is2xxSuccessful()) {
                        return Mono.just(responseBuilder.buildRedirect("/addOrUpdateMeal", "Posiłek został dodany."));
                    } else {
                        return Mono.just(responseBuilder.buildErrorMessage("Nie udało się dodać posiłku."));
                    }
                });
    }

    @Override
    public Mono<String> getMealDetailsPage(Long id, Model model) {
        return apiMealService.getMealDetails(id)
                .map(response -> {
                    model.addAttribute("meal", response);
                    return "pages/logged/mealDetailsPage";
                });
    }

    @Override
    public Mono<String> getEditMealPage(Long id, Model model) {
        return Mono.zip(
                apiMealService.getMealStarterPack(),
                apiMealService.getMealDetails(id)
        ).map(tuple -> {

            MealStarterPack starterPack = tuple.getT1();
            Meal mealDetails = tuple.getT2();

            model.addAttribute("ingredientNameList", MapUtils.mapIngredientName(starterPack.getIngredientNameList()));
            model.addAttribute("ingredientUnitList", MapUtils.mapIngredientUnit(starterPack.getIngredientUnitList()));
            model.addAttribute("mealTypeList", MapUtils.mapMealType(starterPack.getMealTypeList()));
            model.addAttribute("measurementTypeList", MapUtils.mapMeasurementType(starterPack.getMeasurementTypeList()));
            model.addAttribute("meal", mealDetails);
            model.addAttribute("selectedMealTypeIds", mealDetails.getMealTypes().stream().map(MealType::getId).collect(Collectors.toList()));
            return "pages/logged/addEditMeal";
        });
    }

    @Override
    public Mono<List<MealMainInfoDTO>> searchMealsByName(String query) {
        if (query.length() < 3){
            return Mono.just(Collections.emptyList());
        }
        //every 300ms request
        return apiMealService.searchMealsByName(query);
    }
}
