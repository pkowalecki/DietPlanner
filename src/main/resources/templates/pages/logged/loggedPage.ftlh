<#include "../../webData/header.ftlh">
<link rel="stylesheet" href="../../../static/css/main-page.css"/>
<div class="form-container">
    <#assign activeMealType = activeMealType! "all">
    <#assign baseUrl = actionType + "?mealType=" + activeMealType + "&page=">
    <div class="filter-container">
        <div class="filter-buttons">
            <@simpleButton id="bAllMeals" url="${actionType}?mealType=all" buttonText="Wszystkie" class="${(activeMealType == 'all')?then('active', '')}"/>
            <@simpleButton id="bBreakfast" url="${actionType}?mealType=breakfast" buttonText="Śniadanie" class="${(activeMealType == 'breakfast')?then('active', '')}"/>
            <@simpleButton id="bSnack" url="${actionType}?mealType=snack" buttonText="Przekąska" class="${(activeMealType == 'snack')?then('active', '')}"/>
            <@simpleButton id="bLunch" url="${actionType}?mealType=lunch" buttonText="Obiad" class="${(activeMealType == 'lunch')?then('active', '')}"/>
            <@simpleButton id="bSupper" url="${actionType}?mealType=supper" buttonText="Kolacja" class="${(activeMealType == 'supper')?then('active', '')}"/>
        </div>
        <@simpleInputPlain name="searchInput" id="searchInput" placeholder="Szukaj posiłków..." class="search-bar"/>
    </div>


    <div class="item-grid" id="meal-grid">
        <#list meals as meal>
            <a href="details/${meal.getMealId()}" class="item-link">
                <div class="item-container">
                    <div class="icon">
                        <div class="no-image">Brak zdjęcia</div>
                    </div>
                    <div class="item-details">
                        <p class="item-name">${meal.getName()}</p>
                        <p class="item-description">${meal.getDescription()}</p>
                    </div>
                </div>
            </a>
        </#list>
    </div>
    <div>
        <div>
            Liczba elementów: ${totalElements}
            Strona: ${currentPage} z ${totalPages}
        </div>

        <div class="pagination">
            <#if currentPage gt 1>
                <a href="${baseUrl + (currentPage - 1)}" class="btn">&laquo;</a>
            <#else>
                <span class="btn disabled">&laquo;</span>
            </#if>

            <#list totalPagesListed as page>
                <a href="${baseUrl + page}" class="btn <#if page == currentPage>active</#if>">${page}</a>
            </#list>

            <#if currentPage < totalPages>
                <a href="${baseUrl + (currentPage + 1)}" class="btn">&raquo;</a>
            <#else>
                <span class="btn disabled">&raquo;</span>
            </#if>
        </div>
    </div>
</div>
<script>
    let currentSearchPage = 0;
    const searchInput = document.getElementById('searchInput');
    const mealGrid = document.getElementById('meal-grid');
    const paginationContainer = document.querySelector('.pagination');

    function updateSearchResults(query, page = 0) {
        if (query.length < 3) return;

        const url = '${liveSearchUrl}?query=' + encodeURIComponent(query) + '&page='+page+'&size=10';

        fetch(url)
            .then(response => response.json())
            .then(data => {
                mealGrid.innerHTML = '';
                paginationContainer.innerHTML = '';

                if (data.content.length > 0) {
                    data.content.forEach(meal => {
                        <#noparse>
                        const itemLink = document.createElement('a');
                        itemLink.href = `details/${meal.mealId}`;
                        itemLink.classList.add('item-link');
                        itemLink.innerHTML = `
                        <div class="item-container">
                            <div class="icon">
                                <div class="no-image">${meal.image ? '' : 'Brak zdjęcia'}</div>
                            </div>
                            <div class="item-details">
                                <p class="item-name">${meal.name}</p>
                                <p class="item-description">${meal.description || 'Brak opisu'}</p>
                            </div>
                        </div>`;
                        mealGrid.appendChild(itemLink);
                        </#noparse>
                    });

                    const totalPages = data.totalPages;
                    for (let i = 0; i < totalPages; i++) {
                        const pageLink = document.createElement('a');
                        pageLink.href = '#';
                        pageLink.classList.add('btn');
                        if (i === page) {
                            pageLink.classList.add('active');
                        }
                        pageLink.innerText = i + 1;
                        pageLink.addEventListener('click', (e) => {
                            e.preventDefault();
                            updateSearchResults(query, i);
                        });
                        paginationContainer.appendChild(pageLink);
                    }
                } else {
                    mealGrid.innerHTML = '<div class="no-results">Brak wyników dla podanej frazy.</div>';
                }
            })
            .catch(() => {
                mealGrid.innerHTML = '<div class="error">Wystąpił błąd podczas wyszukiwania posiłków.</div>';
            });
    }

    let searchTimeout;
    searchInput.addEventListener('input', function () {
        clearTimeout(searchTimeout);
        const query = this.value.trim();
        if (query.length < 3) return;

        searchTimeout = setTimeout(() => {
            currentSearchPage = 0;
            updateSearchResults(query);
        }, 300);
    });
</script>


<#include "../../webData/footer.ftlh">