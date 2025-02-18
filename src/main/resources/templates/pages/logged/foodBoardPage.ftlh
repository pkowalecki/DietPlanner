<#include "../../webData/header.ftlh">
<link rel="stylesheet" href="../../../static/css/foodBoardPage.css"/>
<div class="meal-form-container">
    <form id="mealBoardForm" name="form" action="/app/auth/generateMealBoard" method="POST">
        <div class="meal-board-container">
            <div class="meal-board-table">
                <#list days as day>
                    <div class="meal-box" id="day" data-day="${day}">
                        <div class="meal-day">${day}</div>
                        <div class="meal-content">Kliknij, aby dodać posiłki</div>
                        <#list 1..4 as num>
                            <input type="hidden" class="meal-id_${num}" value="">
                        </#list>
                    </div>
                </#list>
            </div>
        </div>
        <label>Przelicznik:
            <input id="multiplier" name="multiplier" type="number" step="0.1" value="1">
        </label>
        <label>
            <input type="checkbox" name="isSnackMultiplied">
            Mnożyć przekąskę?
        </label>
        <div class="mb-3 goCenter">
            <a id="openIngredientModalBtn" href="#">Open modal</a>
            <div class="error-message" id="ingredientError"></div>
        </div>
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
                    <div class="meal-select">
                        <@simpleSelectPlain
                        name="meals"
                        id="mealsId_br"
                        source=breakfastList
                        placeholder="-"
                        multiple=false
                        showMinus=true
                        selectWrapperStyle="width:200px;"
                        />
                    </div>

                    <div class="meal-type-name">Przekąska</div>
                    <div class="meal-select">
                        <@simpleSelectPlain
                        name="meals"
                        id="mealsId_sn"
                        source=snackList
                        placeholder="-"
                        multiple=false
                        showMinus=true
                        selectWrapperStyle="width:200px;"
                        />
                    </div>
                    <div class="meal-type-name">Obiad</div>
                    <div class="meal-select">
                        <@simpleSelectPlain
                        name="meals"
                        id="mealsId_lu"
                        source=lunchList
                        placeholder="-"
                        multiple=false
                        showMinus=true
                        selectWrapperStyle="width:200px;"
                        />
                    </div>

                    <div class="meal-type-name">Kolacja</div>
                    <div class="meal-select">
                        <@simpleSelectPlain
                        name="meals"
                        id="mealsId_su"
                        source=supperList
                        placeholder="-"
                        multiple=false
                        showMinus=true
                        selectWrapperStyle="width:200px;"
                        />
                    </div>
                </div>
            </div>
            <button id="addIngredientBtn" type="button" class="btn btn-primary mt-3">Dodaj do kalendarza</button>
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
            const breakfast = $("#mealsId_br option:selected").text();
            const snack = $("#mealsId_sn option:selected").text();
            const lunch = $("#mealsId_lu option:selected").text();
            const dinner = $("#mealsId_su option:selected").text();

            let mealSummary = [breakfast, snack, lunch, dinner].filter(m => m !== "-").join("\n");
            mealSummary = mealSummary || "Kliknij, aby dodać posiłki";

            const box = $(`.meal-box[data-day='${selectedDay}'] .meal-content`);
            box.text(mealSummary);

            $(`.meal-box[data-day='${selectedDay}'] .meal-id_1`).val($("#mealsId_br option:selected").val());
            $(`.meal-box[data-day='${selectedDay}'] .meal-id_2`).val($("#mealsId_sn option:selected").val());
            $(`.meal-box[data-day='${selectedDay}'] .meal-id_3`).val($("#mealsId_lu option:selected").val());
            $(`.meal-box[data-day='${selectedDay}'] .meal-id_4`).val($("#mealsId_su option:selected").val());

            $("#ingredientModal").hide();
        });
    });
    </#noparse>
</script>
<#include "../../webData/footer.ftlh">