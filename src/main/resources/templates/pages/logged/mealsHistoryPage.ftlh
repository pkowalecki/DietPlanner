<#include "../../webData/header.ftlh">
<link rel="stylesheet" href="../../../static/css/mealsHistoryPage.css"/>
<div class="form-container">
    <h2>Twoja historia posiłków</h2>
    <div class="history-list">
        <#if mealHistory?has_content>
            <#list mealHistory as history>
                <div class="history-bar">
                    <form action="/auth/meal-history/mealHistory" method="POST">
                        <input type="hidden" name="id" value="${history.getPublic_id()}"/>
                        <button class="history-button">
                            <span class="icon"><i class="bi bi-search"></i></span>
                            <span class="details">Data generowania: ${history.created}</span>
                            <span class="details">Przelicznik: ${history.multiplier}</span>
                        </button>
                    </form>
                </div>
            </#list>
            <#else>
            <p>Brak historii posiłków.</p>
        </#if>
    </div>
</div>
<#include "../../webData/footer.ftlh">