<#include "../../webData/header.ftlh">
<link rel="stylesheet" href="../../../static/css/addIngredientPage.css"/>
<div id="content-wrap" class="container wrapper">
    <div class="ingredient-form-container">
        <h2>Dodaj nowy składnik</h2>
        <p>Wartości odżywcze powinny być dodawane w odniesieniu do 100g/100ml na produkt</p>
        <form id="ingredientForm" method="POST" action="/app/auth/addIngredientName" name="addIngredientForm">
            <div class="flex-container">
                <div class="flex-child">
                    <label for="ingredientName">Nazwa składnika</label>
                    <input type="text" class="form-control" id="ingredientName" name="ingredientName"
                           placeholder="Wpisz nazwę składnika"
                           autocomplete="off"
                           required/>
                    <ul id="suggestionsList" class="suggestions-list"></ul>
                </div>
                <div class="flex-child">
                    <label for="ingredientBrand">Marka produktu</label>
                    <input type="text" class="form-control" id="ingredientBrand" name="ingredientBrand"
                           placeholder="Wpisz markę produktu"
                           autocomplete="off"/>
                </div>
            </div>
            <div class="flex-container">
                <div class="flex-child">
                    <label for="protein">Białka</label>
                    <input type="number" class="form-control" id="protein"
                           name="protein" placeholder="Białka"
                           autocomplete="off"/>
                </div>
                <div class="flex-child">
                    <label for="carbohydrates">Węglowodany</label>
                    <input type="number" class="form-control"
                           id="carbohydrates" name="carbohydrates"
                           placeholder="Węglowodany"
                           autocomplete="off"/>
                </div>
                <div class="flex-child">
                    <label for="fat">Tłuszcze</label>
                    <input type="number" class="form-control" id="fat" name="fat"
                           placeholder="Tłuszcze"
                           autocomplete="off"/>
                </div>
                <div class="flex-child">
                    <label for="kcal">Kcal</label>
                    <input type="number" class="form-control" id="kcal" name="kcal"
                           placeholder="Kcal"
                           autocomplete="off"/>
                </div>
            </div>

            <button type="submit" class="btn btn-primary mt-3">Dodaj składnik</button>
        </form>
        <div id="ingredientMsg" class="message"></div>
    </div>
</div>
<script>
    let debounceTimer;
    document.getElementById('ingredientName').addEventListener('input', function () {
        const query = this.value.trim();
        if (query.length < 3) {
            document.getElementById('suggestionsList').innerHTML = '';
            return;
        }
        clearTimeout(debounceTimer);
        debounceTimer = setTimeout(() => {
            fetch('/app/auth/ingredientNames/search?query=' + encodeURIComponent(query))
                .then(response => response.json())
                .then(data => {
                    const suggestionsList = document.getElementById('suggestionsList');
                    suggestionsList.innerHTML = '';
                    if (data.length > 0) {
                        data.forEach(ingredient => {
                            const listItem = document.createElement('li');

                            if (ingredient.ingredientBrand && ingredient.ingredientBrand.trim() !== "") {
                                listItem.textContent = ingredient.ingredientName + ", " + ingredient.ingredientBrand;
                            } else {
                                listItem.textContent = ingredient.ingredientName;
                            }
                            listItem.classList.add('suggestion-item');
                            listItem.addEventListener('click', () => {
                                document.getElementById('ingredientName').value = ingredient.ingredientName;
                                suggestionsList.innerHTML = '';
                            });
                            suggestionsList.appendChild(listItem);
                        });
                    } else {
                        suggestionsList.innerHTML = '<li>Brak wyników</li>';
                    }
                }).catch(error => {
                document.getElementById('ingredientMsg').textContent = 'Wystąpił błąd przy wyszukiwaniu składników.';
                document.getElementById('ingredientMsg').style.color = 'red';
            });
        }, 300); //ms
    });

    document.addEventListener('click', function (event) {
        const inputElement = document.getElementById('ingredientName');
        const suggestionsList = document.getElementById('suggestionsList');

        if (!inputElement.contains(event.target) && !suggestionsList.contains(event.target)) {
            suggestionsList.innerHTML = '';
        }
    });
</script>
<#include "../../webData/footer.ftlh">