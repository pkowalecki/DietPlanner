<#include "../webData/header.ftlh">
    <div class="container">
        <p>ResultPage</p>
        <#list result as food>
                ${food.getName()} ${food.getIngredientAmount()}${food.getIngredientUnit()}
                ${food.getMeasurementAmount()} ${food.getMeasurementUnit()}
                <br/>
        </#list>
    </div>
<#include "../webData/footer.ftlh">