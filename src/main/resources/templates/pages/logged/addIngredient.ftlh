<#include "../../webData/header.ftlh">
<link rel="stylesheet" href="../../../static/css/addIngredientPage.css"/>
<div class="form-container">
    <h2>Dodaj lub edytuj składnik</h2>
    <p>Wartości odżywcze powinny być dodawane w odniesieniu do 100g/100ml na produkt.</p>
    <p>Jeżeli chcesz edytować wartości, w pierwszej kolejności wyszukaj dany składnik.</p>
    <div class="mainFields">
        <div class="form-field div1">
            <@simpleInputPlain id="liveSearch" name="liveSearch" placeholder="Wyszukaj składnik"/>
            <div id="suggestionsList" class="suggestions-list"></div>
        </div>
        <form id="ingredient-form">
            <input type="hidden" name="ingredientId" id="ingredientId" value="-1"/>
                <div class="form-field">
                    <@simpleInput name="ingredientName" placeholder="Wpisz nazwę składnika" label="Składnik" id="ingredientName" min="3" max="254" required=true/>
                    <@simpleInput name="ingredientBrand" placeholder="Marka produktu" label="Marka" id="ingredientBrand" />
                </div>
                <div class="form-field">
                    <@simpleInput name="protein" placeholder="Białka" label="Białka" id="protein" type="number" step="0.25" min="0" max="1000"/>
                    <@simpleInput name="carbohydrates" placeholder="Węglowodany" label="Węglowodany" id="carbohydrates" type="number" step="0.25" min="0" max="1000"/>
                    <@simpleInput name="fat" placeholder="Tłuszcze" label="Tłuszcze" id="fat" type="number" step="0.25" min="0" max="1000"/>
                    <@simpleInput name="kcal" placeholder="Kcal" label="Kcal" id="kcal" type="number" step="0.25" min="0" max="1000"/>
                </div>
        </form>
    </div>
        <@sendFormButton id="sendIngredientForm" formToSend="ingredient-form" url="/app/auth/addIngredientName"/>
</div>


<script>
    let debounceTimer;
    document.getElementById('liveSearch').addEventListener('input', function () {
        const query = this.value.trim();
        const suggestionsList = document.getElementById('suggestionsList');

        if (query.length < 3) {
            suggestionsList.innerHTML = '';
            return;
        }

        clearTimeout(debounceTimer);
        debounceTimer = setTimeout(() => {
            const url = '/auth/ingredientNames/search?query=' + encodeURIComponent(query);
            fetch(url)
                .then(response => response.json())
                .then(data => {
                    suggestionsList.innerHTML = '';
                    if (data.length > 0) {
                        data.forEach(ingredient => {
                            const listItem = document.createElement('li');
                            listItem.textContent = ingredient.ingredientBrand && ingredient.ingredientBrand.trim()
                                ? ingredient.ingredientName + ", " + ingredient.ingredientBrand
                                : ingredient.ingredientName;
                            listItem.classList.add('suggestion-item');
                            listItem.addEventListener('click', () => {
                                document.getElementById('ingredientId').value = ingredient.ingredientId || '-1';
                                document.getElementById('ingredientName').value = ingredient.ingredientName || '';
                                document.getElementById('ingredientBrand').value = ingredient.ingredientBrand || '';
                                document.getElementById('protein').value = ingredient.protein || '';
                                document.getElementById('carbohydrates').value = ingredient.carbohydrates || '';
                                document.getElementById('fat').value = ingredient.fat || '';
                                document.getElementById('kcal').value = ingredient.kcal || '';

                                suggestionsList.innerHTML = '';
                            });
                            suggestionsList.appendChild(listItem);
                        });
                    } else {
                        suggestionsList.innerHTML = '<li>Brak wyników</li>';
                    }
                })
                .catch(() => {
                    document.getElementById('ingredientMsg').textContent = 'Wystąpił błąd przy wyszukiwaniu składników.';
                    document.getElementById('ingredientMsg').style.color = 'red';
                });
        }, 300); // 300 ms opóźnienia
    });

    document.addEventListener('click', function (event) {
        const inputElement = document.getElementById('liveSearch');
        const suggestionsList = document.getElementById('suggestionsList');

        if (!inputElement.contains(event.target) && !suggestionsList.contains(event.target)) {
            suggestionsList.innerHTML = '';
        }
    });
</script>
<#include "../../webData/footer.ftlh">