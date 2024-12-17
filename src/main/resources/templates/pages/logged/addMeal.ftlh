<#include "../../webData/header.ftlh">
<link rel="stylesheet" href="../../../static/css/addMealPage.css"/>
<div class="form-container">
    <form id="mealForm">
        <div class="mainFields">
            <@simpleInput name="mealName" placeholder="Nazwa posiłku" label="Nazwa posiłku" id="mealName" />
            <@simpleTextArea name="recipe" placeholder="Wprowadź przepis..." label="Przepis" id="recipe" rows="4" />
            <@simpleTextArea name="description" placeholder="Wprowadź opis tutaj..." label="Opis posiłku" id="description" rows="4" />
            <@simpleTextArea name="notes" placeholder="Wprowadź notatki..." label="Notatki" id="notes" rows="4" />
            <@simpleSelect name="mealType" id="mealTypeId" source=mealTypes placeholder="dummy" label="Typ posiłku" divWrapperClass="form-group-container"/>
            <@simpleInput name="portions" placeholder="Na ile porcji dzielisz przepis?" label="Liczba porcji" id="portions" type="number" step="0.25" />
        </div>

        <div id="ingredientList">
        </div>

        <div class="mb-3 goCenter">
            <a id="openIngredientModalBtn" href="#">Add Ingredient</a>
            <div class="error-message" id="ingredientError"></div>
        </div>


        <@sendFormButton id="sendFormButton" formToSend="mealForm" method="post" url="/app/auth/addMeal" buttonText="Wyślij" />
    </form>
</div>

<div id="ingredientModal" class="modal" role="dialog" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">Dodaj składnik do posiłku</h4>
                <button id="closeIngredientModal" type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="row" style="margin-top: 50px">
                <div class="col-md-12">
                    <@simpleSelect name="ingredientName" id="ingredientNameId" source=ingredientNames label="Składnik" placeholder="Składnik" multiple=false />
                </div>
            </div>
            <div class="d-flex align-items-center gap-3 flex-wrap">
                <div class="d-flex flex-nowrap flex-grow-1 align-items-center">
                    <@simpleInput
                    name="ingredientAmount"
                    placeholder="Wprowadź ilość..."
                    id="ingredientAmount"
                    type="number"
                    step="0.25"
                    minLength="0"
                    maxLength="2000"
                    label="Wartość"
                    inputStyle="max-width: 150px"
                    labelStyle="width:35%"/>
                    <@simpleSelect
                    name="ingredientUnits"
                    source=ingredientUnit
                    placeholder="g"
                    id="ingredientUnitsSelect"
                    multiple=false
                    divWrapperStyle="max-width: 70px"
                    />
                </div>
                <div class="d-flex flex-nowrap flex-grow-1 align-items-center">
                    <@simpleInput
                    name="measurementValue"
                    placeholder="Wprowadź miarę..."
                    id="measurementValue"
                    type="number"
                    step="0.25"
                    minLength="0"
                    maxLength="2000"
                    label="Miara"
                    inputStyle="max-width: 150px"
                    labelStyle="width:35%"/>
                    <@simpleSelect
                    name="measurementTypes"
                    source=measurementType
                    placeholder="porcja"
                    id="measurementTypesSelect"
                    multiple=false
                    divWrapperStyle="max-width: 130px"
                    />
                </div>
            </div>
            <button id="addIngredientBtn" type="button" class="btn btn-primary mt-3">Add ingredient</button>
        </div>
    </div>
</div>


<#--<div id="content-wrap" class="container wrapper">-->
<#--    <div class="ingredient-form">-->
<#--        <div class="mainFields">-->
<#--            <div class="field mealTypes">-->
<#--                <label for="mealTypesSelect" class="form-label">Meal Types</label>-->
<#--                <select class="selectpicker form-control" data-live-search="true" name="mealTypes"-->
<#--                        id="mealTypesSelect" multiple>-->
<#--                    <#list mealList.mealTypeList as mealType>-->
<#--                        <#if mealType??>-->
<#--                            <option value="${mealType.getMealTypePl()}">${mealType.getMealTypePl()}</option>-->
<#--                        </#if>-->
<#--                    </#list>-->
<#--                </select>-->
<#--                <div class="error-message" id="mealTypesSelectError"></div>-->
<#--            </div>-->
<#--        </div>-->
<#--        <div id="ingredientList">-->
<#--        </div>-->
<#--        <div class="mb-3 goCenter">-->
<#--            <a id="openIngredientModalBtn" href="#">Add Ingredient</a>-->
<#--            <div class="error-message" id="ingredientError"></div>-->
<#--        </div>-->
<#--        <div class="field portions">-->
<#--            <label for="portions" class="form-label">Na ile porcji dzielisz przepis?</label>-->
<#--            <input type="number" class="input shorten" id="portions" name="portions"-->
<#--                   step="0.25" value="1" min="0" max="1000" required>-->
<#--            <div class="error-message" id="portionsError"></div>-->
<#--        </div>-->
<#--        <button type="submit" id="submitButton" class="btn btn-primary mt-3">Submit</button>-->
<#--    </div>-->
<#--</div>-->


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
            console.log("przed resetem");
            resetErrors();
            console.log("po resecie");
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
