<#include "../webData/header.ftlh">
    <div class="container">
        <p>ResultPage</p>
        <#list result as food>
                ${food.getName()} ${food.getIngredientAmount()}${food.getIngredientUnit()}
                ${food.getMeasurementAmount()} ${food.getMeasurementUnit()}
                <br/>
        </#list>


        <br/>
        <#list idsList as od>
            ${od}
        </#list>
        <br/>
        <#list meals as meal>
            ${meal.getId()} - ${meal.getName()}<br/>
        </#list>
    </div>
<#include "../webData/footer.ftlh">