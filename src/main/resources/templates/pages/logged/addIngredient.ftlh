<#include "../../webData/header.ftlh">
<link rel="stylesheet" href="../../../static/css/addIngredientPage.css"/>
<div class="form-container">
    <h2>Dodaj lub edytuj składnik</h2>
    <p>Wartości odżywcze powinny być dodawane w odniesieniu do 100g/100ml na produkt</p>
    <div class="mainFields">
        <form id="ingredient-form">
            <div class="parent">
                <div class="div1">
                    <@simpleInputPlain name="liveSearch" placeholder="Wyszukaj składnik"/>
                </div>
                <div class="div2">
                    <@simpleInput name="ingredientName" placeholder="Wpisz nazwę składnika" label="Składnik" id="ingredientName" />
                </div>
                <div class="div3">
                    <@simpleInput name="ingredientBrand" placeholder="Marka produktu" label="Marka" id="ingredientBrand" />
                </div>
                <div class="div4">
                    <@simpleInput name="protein" placeholder="Białka" label="Białka" id="protein" type="number" step="0.25" min="0" max="1000"/>
                </div>
                <div class="div5">
                    <@simpleInput name="carbohydrates" placeholder="Węglowodany" label="Węglowodany" id="carbohydrates" type="number" step="0.25" min="0" max="1000"/>
                </div>
                <div class="div6">
                    <@simpleInput name="fat" placeholder="Tłuszcze" label="Tłuszcze" id="fat" type="number" step="0.25" min="0" max="1000"/>
                </div>
                <div class="div7">
                    <@simpleInput name="kcal" placeholder="Kcal" label="Kcal" id="kcal" type="number" step="0.25" min="0" max="1000"/>
                </div>
                <div class="div8">
                    <@sendFormButton
                    id="sendIngredientForm"
                    formToSend="ingredient-form"
                    url="/app/auth/addIngredientName"
                    reloadPageAfterSuccess=true />
                </div>
            </div>
        </form>
    </div>
</div>
<#include "../../webData/footer.ftlh">