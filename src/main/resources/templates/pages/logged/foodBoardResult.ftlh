<#include "../../webData/header.ftlh">
<style>
    table {
        border-collapse: collapse;
        width: 100%;
    }

    th, td {
        border: 1px solid #dddddd;
        text-align: left;
        padding: 8px;
    }

    th {
        background-color: #f2f2f2;
    }

    .foodType {
        background-color: #f2f2f2;
    }
</style>
    <div class="container">
        <p>ResultPage</p>
        <#list result as food>
                ${food.getName()} ${food.getIngredientAmount()}${food.getIngredientUnit()}
                ${food.getMeasurementAmount()} ${food.getMeasurementUnit()}
                <br/>
        </#list>


        <br/>
        <#assign id = 1/>
        <#list mealNames as mealName>
            <#if id%8==0><br/></#if>
            <td>${id}.${mealName}</td>
            <#assign id = id + 1/>
        </#list>
    </div>
<#include "../../webData/footer.ftlh">