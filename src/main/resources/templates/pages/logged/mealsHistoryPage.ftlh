<#include "../../webData/header.ftlh">
<link rel="stylesheet" href="../../../static/css/mealsHistoryPage.css"/>
<div id="content-wrap" class="container wrapper">
    <h2>Twoja historia posiłków</h2>
    <#list mealHistory as history>
        <div class="history-bar">
            <form action="/app/auth/mealHistory" method="POST">
                <input type="hidden" name="id" value="${history.id}"/>
                <button class="history-button">
                <span class="icon"><i class="bi bi-search"></i></span>
                    <span class="details">Data generowania: ${history.created}</span>
                    <span class="details">Przelicznik: ${history.multiplier}</span>
                </button>
            </form>
        </div>
    </#list>
</div>
<#include "../../webData/footer.ftlh">