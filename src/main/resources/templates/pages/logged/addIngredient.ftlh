<#include "../../webData/header.ftlh">
<link rel="stylesheet" href="../../../static/css/addIngredientPage.css"/>
<div id="content-wrap" class="container wrapper">
    <div class="ingredient-form-container">
        <h2>Dodaj nowy składnik</h2>
        <form id="ingredientForm" method="POST" action="/app/auth/addIngredientName" name="addIngredientForm">
            <label for="ingredientName">Nazwa składnika:</label>
            <input type="text" id="ingredientName" name="ingredientName" placeholder="Wpisz nazwę składnika"
                   autocomplete="off"
                   required/>
            <ul id="suggestionsList" class="suggestions-list"></ul>
            <input type="text" id="ingredientBrand" name="ingredientBrand" placeholder="Wpisz markę produktu"
                   autocomplete="off"/>
            <input type="number" id="protein" name="protein" placeholder="Białka" autocomplete="off"/>
            <input type="number" id="carbohydrates" name="carbohydrates" placeholder="Węglowodany" autocomplete="off"/>
            <input type="number" id="fat" name="fat" placeholder="Tłuszcze" autocomplete="off"/>
            <input type="number" id="kcal" name="kcal" placeholder="Kcal" autocomplete="off"/>

            <button type="submit">Dodaj składnik</button>
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
                                listItem.textContent = ingredient.ingredientName +", " + ingredient.ingredientBrand ;
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
</script>
<#include "../../webData/footer.ftlh">