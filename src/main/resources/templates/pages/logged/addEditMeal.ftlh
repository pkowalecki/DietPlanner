<#include "../../webData/header.ftlh">
<link rel="stylesheet" href="../../../static/css/addMealPage.css"/>
<div class="form-container">
    <form id="mealForm">
        <input type="hidden" name="mealId" value="<#if meal?? && meal.getMealId()??>${meal.getMealId()}<#else>-1</#if>"/>
        <div class="mainFields">
            <@simpleInput name="mealName" placeholder="Nazwa posiłku" label="Nazwa posiłku" id="mealName" value="${meal.getName()}" />
            <@simpleTextArea name="recipe" placeholder="Wprowadź przepis..." label="Przepis" id="recipe" rows="4" value="${meal.getRecipe()}"/>
            <@simpleTextArea name="description" placeholder="Wprowadź opis tutaj..." label="Opis posiłku" id="description" rows="4" value="${meal.getDescription()}" />
            <@simpleTextArea name="notes" placeholder="Wprowadź notatki..." label="Notatki" id="notes" rows="4" value="${meal.getNotes()}"/>
            <@simpleSelect name="mealType" id="mealType" source=mealTypeList placeholder="dummy" label="Typ posiłku" divWrapperClass="form-group-container" selectedValues=selectedMealTypeIds![]/>
            <@simpleInput name="portions" placeholder="Na ile porcji dzielisz przepis?" label="Liczba porcji" id="portions" type="number" step="0.25" min="0" max="1000" value="${meal.getPortions()}"/>
        </div>

        <div id="ingredientList">
        </div>

        <div class="mb-3 goCenter">
            <a id="openIngredientModalBtn" href="#">Add Ingredient</a>
            <div class="error-message" id="ingredientError"></div>
        </div>


<#--        <@sendFormButton id="sendFormButton" formToSend="mealForm" method="post" url="/app/auth/addMeal" buttonText="Wyślij"   />-->
    </form>
    <button type="submit" id="submitButton" class="btn btn-primary mt-3">Submit</button>
</div>

<div id="ingredientModal" class="modal" role="dialog">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">Dodaj składnik do posiłku</h4>
                <button id="closeIngredientModal" type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="row" style="margin-top: 50px">
                <div class="col-md-12">
                    <@simpleSelect name="ingredientName" id="ingredientNameId" source=ingredientNameList label="Składnik" placeholder="Składnik" multiple=false />
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
                    min="0"
                    max="2000"
                    label="Wartość"
                    inputStyle="max-width: 150px"
                    labelStyle="width:35%"/>
                    <@simpleSelect
                    name="ingredientUnits"
                    source=ingredientUnitList
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
                    min="0"
                    max="2000"
                    label="Miara"
                    inputStyle="max-width: 150px"
                    labelStyle="width:35%"/>
                    <@simpleSelect
                    name="measurementTypes"
                    source=measurementTypeList
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


<script>
    $(document).ready(function () {

        function parseForNumber(value) {
            if (typeof value !== 'string') return value;
            const cleanValue = value.replace(/\s/g, '').replace(',', '.');
            const numberValue = parseFloat(cleanValue);

            return isNaN(numberValue) ? cleanValue : (numberValue % 1 === 0 ? parseInt(numberValue, 10) : numberValue);
        }

        const presetIngredients = [
            <#list meal.ingredients as ingredient>
            {
                ingredientNameId: "${ingredient.name.getId()}",
                ingredientName: "${ingredient.name.getIngredientName()}",
                ingredientAmount: parseForNumber("${ingredient.getIngredientAmount()}"),
                ingredientUnit: "${ingredient.ingredientUnit.getId()}",
                ingredientUnitText: "${ingredient.ingredientUnit.getShortName()}",
                measurementValue: parseForNumber("${ingredient.getMeasurementValue()}"),
                measurementType: "${ingredient.measurementType.getId()}",
                measurementTypeText: "${ingredient.measurementType.getNamePL()}"
            }<#if ingredient?has_next>,</#if>
            </#list>
        ];


        const ingredientsList  = [];

        if (presetIngredients && presetIngredients.length > 0) {
            loadPresetIngredients(presetIngredients);
        }

        $("#openIngredientModalBtn").click(e => {
            e.preventDefault();
            $("#ingredientModal").show();
        });

        $("#closeIngredientModal").click(() => {
            $("#ingredientModal").hide();
            resetModal();
        });

        $("#addIngredientBtn").click(() => {
            const ingredient = getIngredientFromModal();
            if (validateIngredient(ingredient)) {
                ingredientsList.push(ingredient);
                updateIngredientList();
                $("#ingredientModal").hide();
                resetModal();
            } else {
                alert("Wybierz wszystkie wartości.");
            }
        });

        function updateIngredient(index) {
            const ingredient = getIngredientFromModal();

            if (validateIngredient(ingredient)) {
                ingredientsList[index] = ingredient;
                updateIngredientList();
                $("#ingredientModal").hide();
                resetModal();
            } else {
                alert("Please fill in all fields before updating an ingredient.");
            }
        }

        $("#submitButton").click(e => {
            e.preventDefault();
            const mealData = {
                mealId: $("input[name='mealId']").val(),
                mealName: $("#mealName").val(),
                description: $("#description").val(),
                recipe: $("#recipe").val(),
                notes: $("#notes").val(),
                mealTypes: $("#mealType").val(),
                ingredients: ingredientsList,
                portions: $("#portions").val()
            };

            $.ajax({
                url: '/app/auth/addOrUpdateMeal',
                method: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(mealData),
                success: () => showSwalMessage("Sukces", "Posiłek został dodany!", "success"),
                error: () => showSwalMessage("Błąd", "Nie udało się dodać posiłku.", "error")
            });
        });
        function getIngredientFromModal() {
            return {
                ingredientNameId: $("#ingredientNameId").val(),
                ingredientName: $("#ingredientNameId option:selected").text(),
                ingredientAmount: parseFloat(parseFloat($("#ingredientAmount").val()).toFixed(2)),
                ingredientUnit: $("#ingredientUnitsSelect").val(),
                ingredientUnitText: $("#ingredientUnitsSelect option:selected").text(),
                measurementValue: parseFloat(parseFloat($("#measurementValue").val()).toFixed(2)),
                measurementType: $("#measurementTypesSelect").val(),
                measurementTypeText: $("#measurementTypesSelect option:selected").text()
            };
        }

        function validateIngredient(ingredient) {
            return ingredient.ingredientNameId && ingredient.ingredientAmount && ingredient.ingredientUnit && ingredient.measurementValue && ingredient.measurementType;
        }

        function updateIngredientList() {
            const ingredientListDiv = $("#ingredientList");
            ingredientListDiv.empty();

            ingredientsList.forEach((ingredient, index) => {
                const ingredientDiv = $("<div>").addClass("ingredient-item").attr("id", "ingredient-" + index);
                const ingredientDesc = $("<p>").html(
                    "<strong>" + ingredient.ingredientName + "</strong>: " +
                    ingredient.ingredientAmount +
                    ingredient.ingredientUnitText + ", " +
                    ingredient.measurementValue + " " +
                    ingredient.measurementTypeText
                );

                const editButton = $("<i>")
                    .addClass("fa fa-edit edit-ingredient")
                    .css("padding", "0px 5px")
                    .attr("data-index", index)
                    .click(function () {
                        editIngredient(index);
                    });

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
                        ingredientsList.splice(index, 1);
                        updateIngredientList();
                    });

                ingredientDesc.append(editButton, removeIcon);
                ingredientDiv.append(ingredientDesc);
                ingredientListDiv.append(ingredientDiv);
            });
        }

        function editIngredient(index) {
            const ingredientEl = ingredientsList[index];

            $("#ingredientNameId").val(ingredientEl.ingredientNameId).selectpicker('refresh').change();
            $("#ingredientAmount").val(ingredientEl.ingredientAmount);
            $("#ingredientUnitsSelect").val(ingredientEl.ingredientUnit).selectpicker('refresh');
            $("#measurementValue").val(ingredientEl.measurementValue);
            $("#measurementTypesSelect").val(ingredientEl.measurementType).selectpicker('refresh');

            $("#ingredientModal").show();

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

        function resetModal() {
            $("#ingredientAmount").val('');
            $("#measurementValue").val('');
            $("#ingredientModal").hide();
        }


    function loadPresetIngredients(presetIngredients) {
        if (presetIngredients && presetIngredients.length > 0) {
            presetIngredients.forEach((ingredient) => {
                ingredientsList.push(ingredient);
            });
            updateIngredientList();
        }
    }
    });
</script>
<#include "../../webData/footer.ftlh">
