<#include "../../webData/header.ftlh">
<link rel="stylesheet" href="../../../static/css/foodBoardResult.css"/>
<div class="form-container">
    <p class="shopping-list-title">Lista zakupów</p>
    <#assign id = 1 />
    <#list ingredients as toBuy>
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
    <form action="/auth/document/downloadMealDocument" method="post">
        <input type="hidden" name="pageId" value="${pageId}"/>
        <button type="submit">Pobierz dokument</button>
    </form>
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