<#include "../../webData/header.ftlh">
<link rel="stylesheet" href="../../../static/css/addIngredientPage.css"/>
<div id="content-wrap" class="container wrapper">
    <div class="ingredient-form-container">
        <h2>Dodaj nowy składnik</h2>
        <p>Wartości odżywcze powinny być dodawane w odniesieniu do 100g/100ml na produkt</p>
            <div class="flex-container">
                <div class="flex-child">
                    <div class="field ingredientName">
                        <label for="ingredientName">Nazwa składnika</label>
                        <input type="text" class="form-control" id="ingredientName" name="ingredientName"
                               placeholder="Wpisz nazwę składnika"
                               autocomplete="off"
                               required/>
                        <ul id="suggestionsList" class="suggestions-list"></ul>
                        <div class="error-message" id="ingredientNameError"></div>
                    </div>
                </div>
                <div class="flex-child">
                    <div class="field ingredientBrand">
                        <label for="ingredientBrand">Marka produktu</label>
                        <input type="text" class="form-control" id="ingredientBrand" name="ingredientBrand"
                               placeholder="Wpisz markę produktu"
                               autocomplete="off"/>
                        <div class="error-message" id="ingredientBrandError"></div>
                    </div>
                </div>
            </div>
            <div class="flex-container">
                <div class="flex-child">
                    <div class="field protein">
                        <label for="protein">Białka</label>
                        <input type="number" class="form-control" id="protein"
                               name="protein" placeholder="Białka"
                               autocomplete="off"/>
                        <div class="error-message" id="proteinError"></div>
                    </div>
                </div>
                <div class="flex-child">
                    <div class="field carbohydrates">
                        <label for="carbohydrates">Węglowodany</label>
                        <input type="number" class="form-control"
                               id="carbohydrates" name="carbohydrates"
                               placeholder="Węglowodany"
                               autocomplete="off"/>
                        <div class="error-message" id="carbohydratesError"></div>
                    </div>
                </div>
                <div class="flex-child">
                    <div class="field fat">
                        <label for="fat">Tłuszcze</label>
                        <input type="number" class="form-control" id="fat" name="fat"
                               placeholder="Tłuszcze"
                               autocomplete="off"/>
                        <div class="error-message" id="fatError"></div>
                    </div>
                </div>
                <div class="flex-child">
                    <div class="field kcal">
                        <label for="kcal">Kcal</label>
                        <input type="number" class="form-control" id="kcal" name="kcal"
                               placeholder="Kcal"
                               autocomplete="off"/>
                        <div class="error-message" id="kcalError"></div>
                    </div>
                </div>
            </div>
            <button type="submit" class="btn btn-primary mt-3">Dodaj składnik</button>
    </div>
</div>
<script>

    $(document).ready(function () {
        $("#submitButton").click(function (e) {
            e.preventDefault();
            resetErrors();
            sendIngredient();
        });

        function sendIngredient() {
            var ingredients = {
                ingredientName: $("#ingredientName").val(),
                ingredientBrand: $("#ingredientBrand").val(),
                protein: $("#protein").val(),
                carbohydrates: $("#carbohydrates").val(),
                fat: $("#fat").val(),
                kcal: $("#kcal").val()
            };
            var url = '/app/auth/addIngredientName';
            var method = 'POST';
            var contentType = 'application/json';

            ajaxCall(url, method, contentType, JSON.stringify(ingredients));

        }
    });
</script>
<#include "../../webData/footer.ftlh">