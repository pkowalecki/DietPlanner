<#include "../../webData/header.ftlh">
<link rel="stylesheet" href="../../../static/css/mealDetailsPage.css"/>
<div class="form-container">
    <div class="page-section">
        <div class="titleSection">
            <div class="meal-title-container">
                <div class="">
                    <p class="meal-title">${meal.name}</p>
                </div>
                <div class="meal-buttons-container">
                    <@simpleButton id="bEditMeal_${meal.getMealId()}" url="${url}${meal.getMealId()}" buttonText="Edytuj posiłek"/>
                    <@simpleButton id="bDeleteMeal_${meal.getMealId()}" url="" buttonText="Usuń posiłek"/>
                </div>
            </div>
            <p class="meal-description">${meal.description}</p>
        </div>
        <hr>
        <div class="contentSection">
            <div class="leftPart">
                <p class="sectionName">Na ile porcji dzielisz przepis?</p>
                <div class="quantity">
                    <button class="btnS minus" type="button" onclick="decrement()">
                        <i class="fa-solid fa-minus"></i>
                    </button>
                    <input type="number" value="${meal.getPortions()}" id="qtyInput">
                    <button class="btnS plus" type="button" onclick="increment()">
                        <i class="fa-solid fa-plus"></i>
                    </button>
                </div>
                <div class="ingredients">
                    <p class="sectionName">Składniki</p>
                    <#list meal.getIngredients() as ingredient>
                        <li id="ingredient-${ingredient_index}">
                            <span class="amount">${ingredient.getIngredientAmount()}</span>${ingredient.getIngredientUnit().getShortName()}
                            - ${ingredient.getName().getIngredientName()}
                        </li>
                    </#list>
                </div>
            </div>
            <div class="rightPart">
                <div class="recipe">
                    <p class="sectionName">Przygotowanie</p>
                    <#assign lines = meal.getRecipe()?split("\n") />
                    <#list lines as line>
                        ${line} <#if line?has_next><br></#if>
                    </#list>
                </div>
                <div class="notes">
                    <p class="sectionName">Notatki</p>
                    <p class="notes">${meal.getNotes()}</p>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    const qty = document.querySelector("#qtyInput");
    const ingredients = document.querySelectorAll(".ingredients li");
    const initialPortions = parseFloat(qty.value);

    function formatValue(value) {
        value = parseFloat(value).toFixed(1);
        return value.endsWith('.0') ? parseInt(value, 10) : value;
    }

    function updateIngredients(newPortions) {
        ingredients.forEach((ingredient) => {
            const amountSpan = ingredient.querySelector(".amount");
            const initialAmount = parseFloat(amountSpan.dataset.initialAmount);
            const updatedAmount = (initialAmount * newPortions / initialPortions).toFixed(2);
            amountSpan.textContent = formatValue(updatedAmount);
        });
    }

    function decrement() {
        let currentValue = parseFloat(qty.value) || 0.1;
        const newValue = Math.max(0.1, (currentValue - 0.1).toFixed(1));
        qty.value = formatValue(newValue);
        updateIngredients(newValue);
    }

    function increment() {
        let currentValue = parseFloat(qty.value) || 0.1;
        const newValue = (currentValue + 0.1).toFixed(1);
        qty.value = formatValue(newValue);
        updateIngredients(newValue);
    }

    qty.addEventListener("input", () => {
        let currentValue = parseFloat(qty.value) || 0.1;
        if (currentValue < 0.1) {
            qty.value = "0.1";
        } else {
            qty.value = formatValue(currentValue);
            updateIngredients(currentValue);
        }
    });

    ingredients.forEach((ingredient) => {
        const amountSpan = ingredient.querySelector(".amount");
        const initialAmount = parseFloat(amountSpan.textContent);
        amountSpan.dataset.initialAmount = initialAmount;
    });
</script>
<#include "../../webData/footer.ftlh">