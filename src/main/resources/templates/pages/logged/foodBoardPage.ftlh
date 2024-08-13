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

<div class="">
    <form name="form" action="/generateMealBoard" method="POST">
        <table>
            <thead>
            <tr>
                <th>Typ</th>
                <th>Poniedziałek</th>
                <th>Wtorek</th>
                <th>Środa</th>
                <th>Czwartek</th>
                <th>Piątek</th>
                <th>Sobota</th>
                <th>Niedziela</th>
            </tr>
            </thead>
            <tbody>
            <#list 1..4 as row>
                <tr>
                    <#list 1..7 as column>
                        <#if row ==1 && column ==1>
                            <td class="foodType">Śnidadanie</td></#if>
                        <#if row ==2 && column ==1>
                            <td class="foodType">Przekąska</td></#if>
                        <#if row ==3 && column ==1>
                            <td class="foodType">Obiad</td></#if>
                        <#if row ==4 && column ==1>
                            <td class="foodType">Kolacja</td></#if>
                        <td>
                            <select class="selectpicker" data-live-search="true" name="mealValues">
                                <option id="0" name="0" value="0">-</option>
                                <#list mealList as meal>
                                    <#if row == 1>
                                        <#list meal.getMealTypes() as type>
                                            <#if type.getMealTypenEn() == "breakfast" || type.getMealTypenEn() == "supper">
                                                <option value="${meal.getId()}">${meal.getName()}</option>
                                            </#if>
                                        </#list>
                                    </#if>
                                    <#if row == 2>
                                        <#list meal.getMealTypes() as type>
                                            <#if type.getMealTypenEn() == "snack">
                                                <option value="${meal.getId()}">${meal.getName()}</option>
                                            </#if>
                                        </#list>
                                    </#if>
                                    <#if row == 3>
                                        <#list meal.getMealTypes() as type>
                                            <#if type.getMealTypenEn() == "lunch">
                                                <option value="${meal.getId()}">${meal.getName()}</option>
                                            </#if>
                                        </#list>
                                    </#if>
                                    <#if row == 4>
                                        <#list meal.getMealTypes() as type>
                                            <#if type.getMealTypenEn() == "breakfast" || type.getMealTypenEn() == "supper">
                                                <option value="${meal.getId()}">${meal.getName()}</option>
                                            </#if>
                                        </#list>
                                    </#if>
                                </#list>
                            </select>
                        </td>
                    </#list>
                </tr>
            </#list>
            </tbody>
        </table>
        <span>Przelicznik: <input id="multiplier" name="multiplier" type="number" step="0.1"></span>
        <span>Mnożyć przekąskę?<input type="checkbox" name="isSnackMultiplied"></span>
        <input type="submit" value="Wygeneruj listę zakupów">
    </form>
</div>
<#include "../../webData/footer.ftlh">