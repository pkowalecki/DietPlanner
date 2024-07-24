<#include "../webData/header.ftlh">
    <style>
        .container{
            padding: 5px 5px 5px 5px;
        }
        .ingredients{
            display: flex;
            align-items: center;
            margin-bottom: 5px;
        }
        .checkbox {
            margin-right: 10px;
        }
        .strike{
            text-decoration: line-through;
        }
    </style>
<div class="container">
    <p>Lista zakupów</p>
    <#assign id = 1 />
    <#list result as food>
        <div class="ingredients" data-original-parent="ingredients-list" data-original-index="${id - 1}">
            <input type="checkbox" id="check_${id}" class="checkbox" onclick="toggleIngredient(${id})"/>
            <div class="ingredient" id="ingredient_${id}">
                ${food.getName()} ${food.getIngredientAmount()}${food.getIngredientUnit()}
                ${food.getMeasurementAmount()} ${food.getMeasurementUnit()}
            </div>
        </div>
        <#assign id = id + 1/>
    </#list>
    <br/>
    <div id="ingredients-list">
    </div>
    <br/>
    <div class="ingredients-owned">
    </div>
    <form action="/downloadMealDocument" method="post">
        <input type="hidden" name="ids" value="${idsList?join(',')}"/>
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

<#include "../webData/footer.ftlh">