<#include "../../webData/header.ftlh">
<link rel="stylesheet" href="../../../static/css/foodBoardPage.css"/>
<div class="meal-form-container">
    <form id="mealBoardForm" name="form" action="/app/auth/generateMealBoard" method="POST">
        <div class="meal-board-container">
            <div class="meal-board-table">
                <#assign idx = 1 />
                <#list days as day>
                    <div class="meal-box" id="day-${idx}" data-day="${day}">
                        <div class="meal-day">${day}</div>
                        <div class="meal-content">
                            <div class="meal-type"><b>Śniadanie:</b> <span class="meal-text" data-type="br">Brak</span>
                            </div>
                            <hr/>
                            <div class="meal-type"><b>Przekąska:</b> <span class="meal-text" data-type="sn">Brak</span>
                            </div>
                            <hr/>
                            <div class="meal-type"><b>Obiad:</b> <span class="meal-text" data-type="lu">Brak</span>
                            </div>
                            <hr/>
                            <div class="meal-type"><b>Kolacja:</b> <span class="meal-text" data-type="su">Brak</span>
                            </div>
                        </div>
                        <#list ["br", "sn", "lu", "su"] as type>
                            <input type="hidden" name="meals[${day}][${type}]" class="meal-id" data-type="${type}"
                                   value="-1">
                        </#list>
                    </div>
                    <#assign idx = idx + 1 />
                </#list>
            </div>
        </div>
        <label>Przelicznik:
            <input id="multiplier" name="multiplier" type="number" step="0.1" value="1">
        </label>
        <label>
            <input type="checkbox" name="isSnackMultiplied"> Mnożyć przekąskę?
        </label>
        <input type="submit" value="Wygeneruj listę zakupów">
    </form>
</div>

<div id="ingredientModal" class="modal" role="dialog">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">Wybierz posiłki</h4>
                <button id="closeIngredientModal" type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="d-flex align-items-center gap-3 flex-wrap">
                <div class="d-flex flex-nowrap flex-grow-1 align-items-center flex-column">
                    <div class="meal-type-name">Śniadanie</div>
                    <@simpleSelectPlain name="meals" id="mealsId_br" source=breakfastList placeholder="-" multiple=false showMinus=true />
                    <div class="meal-type-name">Przekąska</div>
                    <@simpleSelectPlain name="meals" id="mealsId_sn" source=snackList placeholder="-" multiple=false showMinus=true/>
                    <div class="meal-type-name">Obiad</div>
                    <@simpleSelectPlain name="meals" id="mealsId_lu" source=lunchList placeholder="-" multiple=false showMinus=true/>
                    <div class="meal-type-name">Kolacja</div>
                    <@simpleSelectPlain name="meals" id="mealsId_su" source=supperList placeholder="-" multiple=false showMinus=true/>
                </div>
                <button id="addIngredientBtn" type="button" class="btn btn-primary mt-3">Dodaj do kalendarza</button>
            </div>
        </div>
    </div>
</div>

<script>
    <#noparse>
    $(document).ready(function () {
        let selectedDay = "";

        $(".meal-box").click(function () {
            selectedDay = $(this).data("day");
            $("#ingredientModal").show();
        });

        $("#closeIngredientModal").click(() => {
            $("#ingredientModal").hide();
        });

        $("#addIngredientBtn").click(function () {
            $(".meal-id").each(function () {
                let type = $(this).data("type");
                let mealName = $(`#mealsId_${type} option:selected`).text() || "Brak";
                let mealValue = $(`#mealsId_${type} option:selected`).val() || "-1";
                $(`.meal-box[data-day='${selectedDay}'] .meal-text[data-type='${type}']`).text(mealName);
                $(`.meal-box[data-day='${selectedDay}'] .meal-id[data-type='${type}']`).val(mealValue);
            });

            $("#ingredientModal").hide();
        });
    });
    </#noparse>
</script>
<#include "../../webData/footer.ftlh">
