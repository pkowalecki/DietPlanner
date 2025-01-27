<#include "../../webData/header.ftlh">
<link rel="stylesheet" href="../../../static/css/addMealPage.css"/>
<div class="form-container">
    <form id="mealForm">
        <input type="hidden" name="mealId"
               value="<#if meal?? && meal.getMealId()??>
                   ${meal.getMealId()}
                   <#else>
                   -1
               </#if> "/>
        <div class="mainFields">
            <@simpleInput name="mealName" placeholder="Nazwa posiłku" label="Nazwa posiłku" id="mealName" value="${meal.getName()}" />
            <@simpleTextArea name="recipe" placeholder="Wprowadź przepis..." label="Przepis" id="recipe" rows="4" value="${meal.getRecipe()}"/>
            <@simpleTextArea name="description" placeholder="Wprowadź opis tutaj..." label="Opis posiłku" id="description" rows="4" value="${meal.getDescription()}" />
            <@simpleTextArea name="notes" placeholder="Wprowadź notatki..." label="Notatki" id="notes" rows="4" value="${meal.getNotes()}"/>
            <@simpleSelect name="mealType" id="mealType" source=mealTypeList placeholder="dummy" label="Typ posiłku" divWrapperClass="form-group-container"/>
            <@simpleInput name="portions" placeholder="Na ile porcji dzielisz przepis?" label="Liczba porcji" id="portions" type="number" step="0.25" min="0" max="1000" value="${meal.getPortions()}"/>
        </div>

        <div id="ingredientList">
        </div>
        <input type="hidden" name="ingredients" id="ingredientsInput" />

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
    <#noparse>
    $(document).ready(function () {
        const ingredientsList  = [];

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
            } else {
                alert("Wybierz wszystkie wartości.");
            }
        });

        $("#submitButton").click(e => {
            e.preventDefault();
            const mealData = {
                mealName: $("#mealName").val(),
                description: $("#description").val(),
                recipe: $("#recipe").val(),
                notes: $("#notes").val(),
                mealType: $("#mealType").val(),
                ingredients: ingredientsList,
                portions: $("#portions").val()
            };

            $.ajax({
                url: '/app/auth/addOrUpdateMeal',
                method: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(mealData),
                success: () => swal("Sukces", "Posiłek został dodany!", "success"),
                error: () => swal("Błąd", "Nie udało się dodać posiłku.", "error")
            });
        });
        function getIngredientFromModal() {
            return {
                ingredientNameId: $("#ingredientNameId").val(),
                ingredientName: $("#ingredientNameId option:selected").text(),
                ingredientAmount: parseFloat($("#ingredientAmount").val()),
                ingredientUnit: $("#ingredientUnitsSelect").val(),
                measurementValue: parseFloat($("#measurementValue").val()),
                measurementType: $("#measurementTypesSelect").val()
            };
        }

        function validateIngredient(ingredient) {
            return ingredient.ingredientNameId && ingredient.ingredientAmount && ingredient.ingredientUnit && ingredient.measurementValue && ingredient.measurementType;
        }

        function updateIngredientList() {
            const ingredientListDiv = $("#ingredientList");
            ingredientListDiv.empty();

            ingredientsList.forEach((ingredient, index) => {
                const ingredientDiv = $("<div>").addClass("ingredient-item").text(`${ingredient.ingredientName}: ${ingredient.ingredientAmount} ${ingredient.ingredientUnit}`);
                const removeBtn = $("<button>").text("Usuń").click(() => {
                    ingredientsList.splice(index, 1);
                    updateIngredientList();
                });

                ingredientDiv.append(removeBtn);
                ingredientListDiv.append(ingredientDiv);
            });
        }

        function resetModal() {
            $("#ingredientAmount").val('');
            $("#measurementValue").val('');
            $("#ingredientModal").hide();
        }
    });

    </#noparse>
</script>
<#include "../../webData/footer.ftlh">
