<#include "../../webData/header.ftlh">
<link rel="stylesheet" href="../../../static/css/mealHistoryPage.css"/>
<link rel="stylesheet" href="../../../static/css/foodBoardPage.css"/>
<div class="meal-board-container">
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

                    <#assign mealIndex = (row - 1) * 7>
                    <#list 1..7 as column>
                        <div>
                            <#if (mealHistory.getMealNames()?has_content && mealHistory.getMealNames()?size > mealIndex + (column - 1))>
                                ${mealHistory.getMealNames()[mealIndex + (column - 1)]}
                            <#else >
                                Brak danych
                            </#if>

                        </div>
                    </#list>
            </#list>
    </div>
    <div>
        <span>
            Przelicznik:
            <#if mealHistory.getMultiplier()?has_content>
                ${mealHistory.getMultiplier()}
            <#else>
                Brak danych
            </#if>

            <button type="button">Wygeneruj tabelkę</button>
        </span>
    </div>
    <div>
        <h2>Lista zakupów</h2>
        <#if mealHistory.getIngredientsToBuy()?has_content>
            <#list mealHistory.getIngredientsToBuy() as ingredientToBuy>
                <p>${ingredientToBuy.getName()} ${ingredientToBuy.getIngredientAmount()}${ingredientToBuy.getIngredientUnit()}</p>
            </#list>
        </#if>
    </div>
</div>
<#include "../../webData/footer.ftlh">