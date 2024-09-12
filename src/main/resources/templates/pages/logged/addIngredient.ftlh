<#include "../../webData/header.ftlh">
<link rel="stylesheet" href="../../../static/css/addIngredientPage.css"/>
<div id="content-wrap" class="container wrapper">
    <div class="ingredient-form-container">
        <h2>Dodaj nowy składnik</h2>
        <form id="ingredientForm" method="POST" action="/app/auth/addIngredientName">
            <label for="ingredientName">Nazwa składnika:</label>
            <input type="text" id="ingredientName" name="newIngredientName" placeholder="Wpisz nazwę składnika" autocomplete="off"
                   required/>
            <ul id="suggestionsList" class="suggestions-list"></ul>

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
                            listItem.textContent = ingredient.name;
                            listItem.classList.add('suggestion-item');
                            listItem.addEventListener('click', () => {
                                document.getElementById('ingredientName').value = ingredient.name;
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