<#include "../../webData/header.ftlh">
<link rel="stylesheet" href="../../../static/css/mealsHistoryPage.css"/>
<style>
    table {
        border-collapse: collapse;
        width: 100%;
    }

    th, td {
        border: 1px solid #dddddd;
        text-align: left;
        padding: 8px;
    }

    th {
        background-color: #f2f2f2;
    }

    .foodType {
        background-color: #f2f2f2;
    }
</style>
<div id="content-wrap" class="container wrapper">
    <div class="tabelka">
        <table>
            <thead>
            <tr>
                <th>Typ</th>
                <th>Poniedziałek</th>
                <th>Wtorek</th>
                <th>Środa</th>
                <th>Czwartek</th>
                <th>Piątek</th>
                <th>Sobota</th>
                <th>Niedziela</th>
            </tr>
            </thead>
            <tbody>
            <#list 1..4 as row>
                <tr>
                    <#if row ==1>
                        <td class="foodType">Śniadanie</td></#if>
                    <#if row ==2>
                        <td class="foodType">Przekąska</td></#if>
                    <#if row ==3>
                        <td class="foodType">Obiad</td></#if>
                    <#if row ==4>
                        <td class="foodType">Kolacja</td></#if>

                    <#assign mealIndex = (row - 1) * 7>
                    <#list 1..7 as column>
                        <td>
                            <#if (mealHistory.getMealNames()?size > mealIndex + (column - 1))>
                                ${mealHistory.getMealNames()[mealIndex + (column - 1)]}
                            <#else >
                                Brak danych
                            </#if>

                        </td>
                    </#list>
                </tr>
            </#list>
            </tbody>
        </table>
        <span>
            Przelicznik:
            <#if mealHistory.getMultiplier()??>
                ${mealHistory.getMultiplier()}
            <#else>
                Brak danych
            </#if>

            <button type="button">Wygeneruj tabelkę</button>
        </span>
    </div>
    <div>
        <p>
            <#if mealHistory.getMeals()?has_content>
                <#list mealHistory.getMeals() as meal>
                    ${meal.getName()} <br/>
                    ${meal.getDescription()} <br/>
                    ${meal.getNotes()} <br/>
                    ${meal.getRecipe()} <br/>
                    <#if meal.getIngredients()?has_content>
                        <#list meal.getIngredients() as ingredient>
                            ${ingredient.getName().getIngredientName()}
                            ${ingredient.getIngredientAmount()}
                            ${ingredient.getIngredientUnit().getShortName()}
                            ${ingredient.getMeasurementValue()}
                            ${ingredient.getMeasurementType().getMeasurementName()}
                        </#list>
                    </#if>
                </#list>
            </#if>
        </p>
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