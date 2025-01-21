<#include "../../webData/header.ftlh">
<link rel="stylesheet" href="../../../static/css/mealDetailsPage.css"/>
<div class="form-container">
    <div class="meal-details">
        <div class="meal-header">
            <h1>${meal.name}</h1>
            <p class="meal-servings">${meal.getPortions()} porcje</p>
        </div>
        <div class="meal-body">
            <div class="meal-left">
                <h2>SKŁADNIKI</h2>
                <ul class="ingredients">
                    <#list meal.getIngredients() as ingredient>
                        <li>${ingredient.getName().getIngredientName()} - ${ingredient.getIngredientAmount()} ${ingredient.getIngredientUnit().getShortName()}</li>
                    </#list>
                </ul>
                <h2>NOTATKI</h2>
                <p>${meal.getNotes()}</p>
            </div>
            <div class="meal-right">
                <h2>PRZYGOTOWANIE</h2>
                <ol class="directions">
                <#assign lines = meal.getRecipe()?split("\n") />
                <#list lines as line>
                    ${line} <#if line?has_next><br></#if>
                </#list>
                </ol>
            </div>
        </div>
    </div>
</div>
<#include "../../webData/footer.ftlh">