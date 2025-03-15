<#include "../../webData/header.ftlh">
<link rel="stylesheet" href="../../../static/css/foodBoardPage.css"/>
<link rel="stylesheet" href="../../../static/css/foodBoardResult.css"/>
<div class="meal-form-container">
    <div class="meal-board-container">
        <div class="meal-board-table">
            <#assign idx = 1 />
            <#assign mealIdx = 0 />
            <#list days as day>
                <div class="meal-box" id="day-${idx}" data-day="${day}">
                    <div class="meal-day">${day}</div>
                    <div class="meal-content">
                        <#list 1..4 as type>
                            <div class="meal-type">
                                <b>
                                    <#if type == 1>Śniadanie:</#if>
                                    <#if type == 2>Przekąska:</#if>
                                    <#if type == 3>Obiad:</#if>
                                    <#if type == 4>Kolacja:</#if>
                                </b>
                                <#if mealIdx < mealHistory.mealNames?size>
                                    <span class="meal-text" data-type="${type}">
                                    ${mealHistory.mealNames[mealIdx]}
                                </span>
                                    <input type="hidden" name="meals[${idx}][${type}]" class="meal-id"
                                           data-type="${type}" value="${mealIdx}">
                                    <#assign mealIdx = mealIdx + 1 />
                                <#else>
                                    <span class="meal-text" data-type="${type}">Brak</span>
                                    <input type="hidden" name="meals[${idx}][${type}]" class="meal-id"
                                           data-type="${type}" value="-1">
                                </#if>
                            </div>
                            <hr/>
                        </#list>
                    </div>
                </div>
                <#assign idx = idx + 1 />
            </#list>
        </div>
    </div>
    <form action="/auth/document/downloadMealDocument" method="post">
        <input type="hidden" name="pageId" value="${mealHistory.getDocumentId()}"/>
        <button type="submit">Pobierz dokument</button>
    </form>
    <p class="shopping-list-title">Lista zakupów</p>
    <#assign id = 1 />
    <#list mealHistory.getIngredientsToBuy() as toBuy>
        <div class="ingredients" data-original-parent="ingredients-list" data-original-index="${id - 1}">
            <input type="checkbox" id="check_${id}" class="checkbox" onclick="toggleIngredient(${id})"/>
            <div class="ingredient" id="ingredient_${id}">
                ${toBuy.getName()} ${toBuy.getIngredientAmount()}${toBuy.getIngredientUnit()}
                <#list toBuy.getMeasurementList() as measurement>
                    ${measurement.getMeasurementAmount()} ${measurement.getMeasurementUnit()}
                </#list>
            </div>
        </div>
        <#assign id = id + 1/>
    </#list>
    <br/>
    <div id="ingredients-list"></div>
    <br/>
    <div class="ingredients-owned"></div>
</div>
<script>
    document.addEventListener('DOMContentLoaded', function() {
        var ingredientsList = document.getElementById('ingredients-list');
        var ingredients = document.querySelectorAll('.ingredients');
        ingredients.forEach(function(element) {
            ingredientsList.appendChild(element);
        });
    });

    function toggleIngredient(id) {
        var checkbox = document.getElementById('check_' + id);
        var ingredientDiv = document.getElementById('ingredient_' + id).parentElement;
        var ingredientsOwned = document.querySelector('.ingredients-owned');
        var ingredientsList = document.getElementById('ingredients-list');

        if (checkbox.checked) {
            ingredientDiv.classList.add('strike');
            ingredientsOwned.appendChild(ingredientDiv);
        } else {
            ingredientDiv.classList.remove('strike');
            var originalParentId = ingredientDiv.getAttribute('data-original-parent');
            var originalIndex = ingredientDiv.getAttribute('data-original-index');
            var originalParent = document.getElementById(originalParentId);

            if (originalParent) {
                var referenceNode = originalParent.children[originalIndex];
                if (referenceNode) {
                    originalParent.insertBefore(ingredientDiv, referenceNode);
                } else {
                    originalParent.appendChild(ingredientDiv);
                }
            }
        }
    }
</script>
<#include "../../webData/footer.ftlh">