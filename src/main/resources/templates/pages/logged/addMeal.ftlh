<#include "../../webData/header.ftlh">
<link rel="stylesheet" href="../../../static/css/addMealPage.css"/>
<div id="content-wrap" class="container wrapper">

    <div class="ingredient-form">
        <div class="mainFields">
            <div class="field mealName">
                <label for="mealName" class="form-label">Name</label>
                <input type="text" class="form-control" id="mealName" name="mealName" placeholder="Meal name"
                       required>
                <div class="error-message" id="mealNameError"></div>
            </div>

            <div class="field description">
                <label for="description" class="form-label">Description</label>
                <textarea class="form-control" id="description" name="description"
                          placeholder="Enter your description here..." rows="4"></textarea>
                <div class="error-message" id="descriptionError"></div>
            </div>

            <div class="field recipe">
                <label for="recipe" class="form-label">Recipe</label>
                <textarea class="form-control" id="recipe" name="recipe" placeholder="Enter the recipe..."
                          rows="4"></textarea>
                <div class="error-message" id="recipeError"></div>
            </div>

            <div class="field notes">
                <label for="notes" class="form-label">Notes</label>
                <textarea class="form-control" id="notes" name="notes" placeholder="Your notes here..."
                          rows="4"></textarea>
                <div class="error-message" id="notesError"></div>
            </div>

            <div class="field mealTypes">
                <label for="mealTypesSelect" class="form-label">Meal Types</label>
                <select class="selectpicker form-control" data-live-search="true" name="mealTypes"
                        id="mealTypesSelect" multiple>
                    <#list mealList.mealTypeList as mealType>
                        <#if mealType??>
                            <option value="${mealType.getMealTypePl()}">${mealType.getMealTypePl()}</option>
                        </#if>
                    </#list>
                </select>
                <div class="error-message" id="mealTypesSelectError"></div>
            </div>
        </div>
        <div id="ingredientList">
        </div>
        <div class="mb-3 goCenter">
            <a id="openIngredientModalBtn" href="#">Add Ingredient</a>
            <div class="error-message" id="ingredientError"></div>
        </div>
        <div class="field portions">
            <label for="portions" class="form-label">Na ile porcji dzielisz przepis?</label>
            <input type="number" class="input shorten" id="portions" name="portions"
                   step="0.25" value="1" min="0" max="1000" required>
            <div class="error-message" id="portionsError"></div>
        </div>
        <button type="submit" id="submitButton" class="btn btn-primary mt-3">Submit</button>
    </div>
</div>

<div id="ingredientModal" class="modal">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">Add Ingredient</h4>
                <button id="closeIngredientModal" type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="ingredientPackage">
                <div class="ingredient" id="ingredient">
                    <label for="ingredientSelect" class="label">Ingredient</label>
                    <select class="selectpicker" data-width="100%" data-live-search="true" name="ingredientNameId"
                            id="ingredientSelect">
                        <#list mealList.ingredientNameList as ingredient>
                            <#if ingredient??>
                                <option value="${ingredient.getId()}">${ingredient.getIngredientName()}</option>
                            </#if>
                        </#list>
                    </select>
                </div>
                <div class="inline-group ingredientGroup">
                    <div class="amount">
                        <label for="ingredientAmount" class="label">Amount</label>
                        <div class="inputs-container">
                            <input type="number" class="input shorten" id="ingredientAmount" name="ingredientAmount"
                                   step="0.25" value="1" min="0" max="2000" required>
                            <select class="selectpicker" data-width="55%" data-live-search="true" name="ingredientUnits"
                                    id="ingredientUnitsSelect">
                                <#list mealList.ingredientUnitList as ingredientUnit>
                                    <#if ingredientUnit??>
                                        <option value="${ingredientUnit.getShortName()}">${ingredientUnit.getShortName()}</option>
                                    </#if>
                                </#list>
                            </select>
                        </div>
                    </div>
                    <div class="measurement">
                        <label for="measurementValue" class="label">Measurement</label>
                        <div class="inputs-container">
                            <input type="number" class="input shorten" id="measurementValue" name="measurementValue"
                                   step="0.25" value="1" min="0" max="2000" required>
                            <select class="selectpicker" data-width="70%" data-live-search="true"
                                    name="measurementTypes" id="measurementTypesSelect">
                                <#list mealList.measurementTypeList as measurementType>
                                    <#if measurementType??>
                                        <option value="${measurementType.getId()}">${measurementType.getNamePL()}</option>
                                    </#if>
                                </#list>
                            </select>
                        </div>
                    </div>
                </div>
            </div>
            <button id="addIngredientBtn" type="button" class="btn btn-primary mt-3">Add ingredient</button>
        </div>
    </div>
</div>

<script>
    $(document).ready(function () {

        const jsIngredientsList = [];
        $("#openIngredientModalBtn").click(function (e) {
            e.preventDefault();
            openModal();
        });

        $("#closeIngredientModal").click(function (e) {
            e.preventDefault();
            closeModal();
        });

        $("#addIngredientBtn").click(function (e) {
            e.preventDefault();
            addIngredient();
        });

        function openModal() {
            $("#ingredientModal").show();
        }

        function closeModal() {
            $("#ingredientModal").hide();
            resetModal();
        }

        $("#submitButton").click(function (e) {
            e.preventDefault();
            resetErrors();
            submitMeal();
        });

        function resetModal() {
            $("#ingredientAmount").val('');
            $("#measurementValue").val('');
            $("#addIngredientBtn").show();
            $("#updateIngredientBtn").hide();
        }

        function addIngredient() {
            const ingredient = getIngredientFromModal();

            if (validateIngredient(ingredient)) {
                jsIngredientsList.push(ingredient);
                updateIngredientList();
                closeModal();
            } else {
                alert("Please fill in all fields before adding an ingredient.");
            }
        }

        function getIngredientFromModal() {
            const ingredient = {
                ingredientNameId: $("#ingredientSelect").val(),
                ingredientName: $("#ingredientSelect option:selected").text(),
                ingredientAmount: parseFloat(parseFloat($("#ingredientAmount").val()).toFixed(2)),
                ingredientUnit: $("#ingredientUnitsSelect").val(),
                ingredientUnitText: $("#ingredientUnitsSelect option:selected").text(),
                measurementValue: parseFloat(parseFloat($("#measurementValue").val()).toFixed(2)),
                measurementType: $("#measurementTypesSelect").val(),
                measurementTypeText: $("#measurementTypesSelect option:selected").text()
            };
            return ingredient;
        }

        function validateIngredient(ingredient) {
            return ingredient.ingredientNameId && ingredient.ingredientAmount && ingredient.ingredientUnit &&
                ingredient.measurementValue && ingredient.measurementType;
        }

        function updateIngredient(index) {
            const ingredient = getIngredientFromModal();

            if (validateIngredient(ingredient)) {
                jsIngredientsList[index] = ingredient;
                updateIngredientList();
                closeModal();
            } else {
                alert("Please fill in all fields before updating an ingredient.");
            }
        }

        function editIngredient(index) {
            const ingredientEl = jsIngredientsList[index];

            $("#ingredientSelect").val(ingredientEl.ingredientNameId).selectpicker('refresh');
            $("#ingredientAmount").val(ingredientEl.ingredientAmount);
            $("#ingredientUnitsSelect").val(ingredientEl.ingredientUnit).selectpicker('refresh');
            $("#measurementValue").val(ingredientEl.measurementValue);
            $("#measurementTypesSelect").val(ingredientEl.measurementType).selectpicker('refresh');

            openModal();

            $("#addIngredientBtn").hide();
            const updateButton = $("#updateIngredientBtn");
            if (updateButton.length === 0) {
                $("<button>")
                    .attr("id", "updateIngredientBtn")
                    .addClass("btn btn-primary mt-3")
                    .text("Update Ingredient")
                    .insertAfter("#addIngredientBtn")
                    .click(function () {
                        updateIngredient(index);
                    });
            } else {
                updateButton.off().click(function () {
                    updateIngredient(index);
                }).show();
            }
        }

        function removeIngredient(index) {
            jsIngredientsList.splice(index, 1);
            updateIngredientList();
        }

        function updateIngredientList() {
            const ingredientList = $("#ingredientList");
            ingredientList.empty();

            jsIngredientsList.forEach(function (ingredientEl, index) {
                const ingredientDiv = $("<div>").addClass("ingredient-item").attr("id", "ingredient-" + index);
                const ingredientDesc = $("<p>").html(
                    "<strong>" + ingredientEl.ingredientName + "</strong>: " +
                    ingredientEl.ingredientAmount +
                    ingredientEl.ingredientUnitText + ", " +
                    ingredientEl.measurementValue + " " +
                    ingredientEl.measurementTypeText
                );

                const editButton = $("<i>")
                    .addClass("fa fa-edit edit-ingredient")
                    .css("padding", "0px 5px")
                    .attr("data-index", index)
                    .click(function () {
                        editIngredient(index);
                    });

                const removeButton = $("<i>")
                    .addClass("fa fa-times remove-ingredient")
                    .css("color", "red")
                    .attr("data-index", index)
                    .click(function () {
                        removeIngredient(index);
                    });

                ingredientDesc.append(editButton, removeButton);

                ingredientDiv.append(ingredientDesc);
                ingredientList.append(ingredientDiv);

            });
        }

        function submitMeal() {
            var mealData = {
                mealName: $("#mealName").val(),
                description: $("#description").val(),
                recipe: $("#recipe").val(),
                notes: $("#notes").val(),
                mealTypes: $("#mealTypesSelect").val(),
                ingredients: jsIngredientsList,
                portions: $("#portions").val()
            };
            var url = '/app/auth/addMeal';
            var method = 'POST';
            var contentType = 'application/json';

            ajaxCall(url, method, contentType, JSON.stringify(mealData));

        }
    });
</script>
<#include "../../webData/footer.ftlh">
