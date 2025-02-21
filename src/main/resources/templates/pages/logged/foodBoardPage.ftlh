<#include "../../webData/header.ftlh">
<link rel="stylesheet" href="../../../static/css/foodBoardPage.css"/>
<div class="meal-form-container">
    <form id="mealBoardForm">
        <div class="meal-board-container">
            <div class="meal-board-table">
                <#assign idx = 1 />
                <#list days as day>
                    <div class="meal-box" id="day-${idx}" data-day="${day}">
                        <div class="meal-day">${day}</div>
                        <div class="meal-content">
                            <div class="meal-type"><b>Śniadanie:</b> <span class="meal-text" data-type="1">Brak</span>
                            </div>
                            <hr/>
                            <div class="meal-type"><b>Przekąska:</b> <span class="meal-text" data-type="2">Brak</span>
                            </div>
                            <hr/>
                            <div class="meal-type"><b>Obiad:</b> <span class="meal-text" data-type="3">Brak</span>
                            </div>
                            <hr/>
                            <div class="meal-type"><b>Kolacja:</b> <span class="meal-text" data-type="4">Brak</span>
                            </div>
                        </div>
                        <#list 1..4 as type>
                            <input type="hidden" name="meals[${idx}][${type}]" class="meal-id" data-type="${type}"
                                   value="-1">
                        </#list>
                    </div>
                    <#assign idx = idx + 1 />
                </#list>
            </div>
        </div>
        <div class="options">
            <label>Przelicznik:
                <input id="multiplier" name="multiplier" type="number" step="0.1" value="1">
            </label>
        </div>
        <div class="form-field">
            <@sendFormButton
            id="sendMealBoardForm"
            formToSend="mealBoardForm"
            url="/app/auth/generateMealBoard"/>
        </div>
    </form>
</div>

<div id="mealsModal" class="modal" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">Wybierz posiłki - <span id="mealDay"></span></h4>
                <button id="closeMealsModal" type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">
                <div class="meal-type-name">
                    <div class="meal-type-header">Śniadanie:</div>
                    <@simpleSelectPlain name="meals" id="mealsId_1" source=breakfastList placeholder="-" multiple=false showMinus=true selectWrapperStyle="width: 300px;"/>
                </div>
                <div class="meal-type-name">
                    <div class="meal-type-header">Przekąska:</div>
                    <@simpleSelectPlain name="meals" id="mealsId_2" source=snackList placeholder="-" multiple=false showMinus=true selectWrapperStyle="width: 300px;"/>
                </div>
                <div class="meal-type-name">
                    <div class="meal-type-header">Obiad:</div>
                    <@simpleSelectPlain name="meals" id="mealsId_3" source=lunchList placeholder="-" multiple=false showMinus=true selectWrapperStyle="width: 300px;"/>
                </div>
                <div class="meal-type-name">
                    <div class="meal-type-header">Kolacja:</div>
                    <@simpleSelectPlain name="meals" id="mealsId_4" source=supperList placeholder="-" multiple=false showMinus=true selectWrapperStyle="width: 300px;"/>
                </div>
                <button id="addMealButton" type="button" class="btn btn-primary mt-3">Dodaj do kalendarza</button>
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

            $(".meal-id").each(function () {
                let type = $(this).data("type");
                let mealValue = $(`.meal-box[data-day='${selectedDay}'] .meal-id[data-type='${type}']`).val() || "-1";
                $(`#mealsId_${type}`).val(mealValue).trigger("change");
            });
            $("#mealDay").text(selectedDay);
            $("#mealsModal").show();
        });

        $("#closeMealsModal").click(() => {
            $("#mealsModal").hide();
        });

        $("#addMealButton").click(function () {
            $(".meal-id").each(function () {
                let type = $(this).data("type");
                let mealName = $(`#mealsId_${type} option:selected`).text() || "Brak";
                let mealValue = $(`#mealsId_${type} option:selected`).val() || "-1";

                $(`.meal-box[data-day='${selectedDay}'] .meal-text[data-type='${type}']`).text(mealName);
                $(`.meal-box[data-day='${selectedDay}'] .meal-id[data-type='${type}']`).val(mealValue);
            });

            $("#mealsModal").hide();
        });
    });
    </#noparse>
</script>
<#include "../../webData/footer.ftlh">
