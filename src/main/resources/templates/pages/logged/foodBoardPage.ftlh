<#include "../../webData/header.ftlh">
<link rel="stylesheet" href="../../../static/css/foodBoardPage.css"/>
<div class="meal-board-container">
    <form id="mealBoardForm" name="form" action="/app/auth/generateMealBoard" method="POST">
        <div class="meal-board-table">

            <div class="meal-type-header">Typ</div>
            <div class="day-name">Poniedziałek</div>
            <div class="day-short">Pon</div>
            <div class="day-name">Wtorek</div>
            <div class="day-short">Wt</div>
            <div class="day-name">Środa</div>
            <div class="day-short">Śr</div>
            <div class="day-name">Czwartek</div>
            <div class="day-short">Czw</div>
            <div class="day-name">Piątek</div>
            <div class="day-short">Pią</div>
            <div class="day-name">Sobota</div>
            <div class="day-short">Sob</div>
            <div class="day-name">Niedziela</div>
            <div class="day-short">Nie</div>


            <#list 1..4 as row>
                <div class="meal-type">
                    <#if row == 1>Śniadanie</#if>
                    <#if row == 2>Przekąska</#if>
                    <#if row == 3>Obiad</#if>
                    <#if row == 4>Kolacja</#if>
                </div>
                <#list 1..7 as column>
                        <select class="selectpicker" data-live-search="true" name="mealIds">
                            <option id="0" name="0" value="0">-</option>
                            <#list mealList as meal>
                                <#if row == 1 && meal.mealTypes?contains("BREAKFAST")>
                                    <option value="${meal.mealId}">${meal.name}</option>
                                </#if>
                                <#if row == 2 && meal.mealTypes?contains("SNACK")>
                                    <option value="${meal.mealId}">${meal.name}</option>
                                </#if>
                                <#if row == 3 && meal.mealTypes?contains("LUNCH")>
                                    <option value="${meal.mealId}">${meal.name}</option>
                                </#if>
                                <#if row == 4 && meal.mealTypes?contains("SUPPER")>
                                    <option value="${meal.mealId}">${meal.name}</option>
                                </#if>
                            </#list>
                        </select>
                </#list>
            </#list>
        </div>
        <div class="options">
            <label>Przelicznik: <input id="multiplier" name="multiplier" type="number" step="0.1" value="1"></label>
            <label><input type="checkbox" name="isSnackMultiplied"> Mnożyć przekąskę?</label>
        </div>
        <input type="submit" value="Wygeneruj listę zakupów">
    </form>
</div>
<#include "../../webData/footer.ftlh">