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
                        <#list mealTypes as mealType>
                            <#if mealType??>
                                <option value="${mealType}">${mealType.getMealTypePl()}</option>
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
                <div class="ingredient">
                    <label for="ingredientSelect" class="label">Ingredient</label>
                    <select class="selectpicker" data-width="100%" data-live-search="true" name="ingredientNames" id="ingredientSelect">
                        <#list ingredientsNames as ingredient>
                            <#if ingredient??>
                                <option value="${ingredient.getId()}">${ingredient.getName()}</option>
                            </#if>
                        </#list>
                    </select>
                </div>
                <div class="inline-group ingredientGroup">
                    <div class="amount">
                        <label for="ingredientAmount" class="label">Amount</label>
                        <div class="inputs-container">
                            <input type="text" class="input shorten" id="ingredientAmount" name="ingredientAmount"
                                   required>
                            <select class="selectpicker" data-width="55%" data-live-search="true"name="ingredientUnits" id="ingredientUnitsSelect">
                                <#list ingredientUnits as ingredientUnit>
                                    <#if ingredientUnit??>
                                        <option value="${ingredientUnit}">${ingredientUnit.getShortName()}</option>
                                    </#if>
                                </#list>
                            </select>
                        </div>
                    </div>
                    <div class="measurement">
                        <label for="measurementValue" class="label">Measurement</label>
                        <div class="inputs-container">
                            <input type="text" class="input shorten" id="measurementValue" name="measurementValue"
                                   required>
                            <select class="selectpicker" data-width="70%" data-live-search="true" name="measurementTypes" id="measurementTypesSelect">
                                <#list measurementTypes as measurementType>
                                    <#if measurementType??>
                                        <option value="${measurementType}">${measurementType.getMeasurementName()}</option>
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

        $("#submitButton").click(function (e) {
            e.preventDefault();
            submitMeal();
        });

        function openModal() {
            $("#ingredientModal").show();
        }

        function closeModal() {
            $("#ingredientModal").hide();
        }

        function addIngredient() {
            const ingredient = {
                jsIngredientId: $("#ingredientSelect").val(),
                jsIngredientName: $("#ingredientSelect option:selected").text(),
                jsIngredientAmount: $("#ingredientAmount").val(),
                jsIngredientUnit: $("#ingredientUnitsSelect").val(),
                jsIngredientUnitText: $("#ingredientUnitsSelect option:selected").text(),
                jsMeasurementValue: $("#measurementValue").val(),
                jsMeasurementType: $("#measurementTypesSelect").val(),
                jsMeasurementTypeText: $("#measurementTypesSelect option:selected").text()
            };

            if (ingredient.jsIngredientId && ingredient.jsIngredientAmount && ingredient.jsIngredientUnit &&
                ingredient.jsMeasurementValue && ingredient.jsMeasurementType) {

                jsIngredientsList.push(ingredient);
                updateIngredientList();

            } else {
                alert("Please fill in all fields before adding an ingredient.");
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
                    "<strong>" + ingredientEl.jsIngredientName + "</strong>: " +
                    ingredientEl.jsIngredientAmount +
                    ingredientEl.jsIngredientUnitText + ", " +
                    ingredientEl.jsMeasurementValue + " " +
                    ingredientEl.jsMeasurementTypeText
                );

                const removeIcon = $("<span>")
                    .html("&times;")
                    .addClass("remove-ingredient")
                    .attr("data-index", index)
                    .css({
                        color: "red",
                        cursor: "pointer",
                        fontWeight: "bold",
                        marginLeft: "10px"
                    })
                    .click(function () {
                        removeIngredient(index);
                    });

                ingredientDiv.append(ingredientDesc).append(removeIcon);
                ingredientList.append(ingredientDiv);
            });
        }

        function submitMeal() {
            const mealData = {
                mealName: $("#mealName").val(),
                description: $("#description").val(),
                recipe: $("#recipe").val(),
                notes: $("#notes").val(),
                mealTypes: $("#mealTypesSelect").val(),
                ingredients: jsIngredientsList.map(function (ingredient) {
                    return {
                        ingredientAmount: parseFloat(ingredient.jsIngredientAmount),
                        ingredientUnit: ingredient.jsIngredientUnit,
                        measurementValue: parseFloat(ingredient.jsMeasurementValue),
                        measurementType: ingredient.jsMeasurementType,
                        ingredientNameId: parseInt(ingredient.jsIngredientId)
                    };
                })
            };

            $.ajax({
                url: '/app/auth/addMeal',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(mealData),
                success: function (response) {
                    alert("Meal added successfully!");
                    // window.location.href = "/jakisRedir";
                },
                error: function (xhr, status, error) {
                    console.error("Error adding meal: ", error);
                    alert("There was an error adding your meal. Please try again.");
                }
            });
        }

    });
</script>
<#include "../../webData/footer.ftlh">
