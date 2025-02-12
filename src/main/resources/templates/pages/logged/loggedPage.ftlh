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
            <@simpleButton id="bPrivate" url="${actionType}?mealType=private" buttonText="Własne" class="${(activeMealType == 'private')?then('active', '')}"/>
        </div>
        <div class="suggestions">
            <@simpleInputPlain name="searchInput" id="searchInput" placeholder="Szukaj posiłków..."/>
            <div id="suggestionsList" class="suggestions-list"></div>
        </div>
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

    $(document).ready(function(){
        const searchInput = $('#searchInput');
        let debounceTimeout;

        searchInput.on('input', function () {
            clearTimeout(debounceTimeout);
            const query = $(this).val().trim();

            if (query.length >= 3) {
                debounceTimeout = setTimeout(function () {
                    fetchSuggestions(query);
                }, 300);
            } else {
                clearSuggestions();
            }
        });

        $(document).on('click', function (e) {
            if (!$(e.target).closest('#searchInput, #suggestionsList').length) {
                clearSuggestions();
            }
        });

        function fetchSuggestions(query) {
            $.ajax({
                url: '${liveSearchUrl}?query='+ encodeURIComponent(query),
                method: 'GET',
                success: function (data) {
                    renderSuggestions(data);
                },
                error: function () {
                    clearSuggestions();
                }
            });
        }

        function renderSuggestions(data) {
            const suggestionsList = $('#suggestionsList');
            suggestionsList.empty();
            if (data.length > 0) {
                data.forEach(function (meal) {

                    suggestionsList.append(
                        `<li class="suggestion-item"><a href="${details}<#noparse>${meal.mealId}" style="color: #333;">${meal.name}</#noparse></a></li>`
                    );
                    suggestionsList.addClass('show');
                });
            } else {
                suggestionsList.append('<li>Brak wyników</li>').addClass('show');
            }
        }

        function clearSuggestions() {
            $('#suggestionsList').empty().removeClass('show');
        }
    });

</script>

<#include "../../webData/footer.ftlh">
