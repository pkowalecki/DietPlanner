<#include "../../webData/header.ftlh">
<link rel="stylesheet" href="../../../static/css/foodBoardPage.css"/>
<div class="meal-form-container">
    <form id="mealBoardForm" name="form" action="/app/auth/generateMealBoard" method="POST">
        <div class="meal-board-container">
            <div class="meal-board-table">

                <div class="meal-type-header">Typ</div>
                <#list days as day>
                    <div class="day-header">${day}</div>
                </#list>

                <div class="meal-type-name">Śniadanie</div>
                <#assign idBre = 1 />
                <#list days as day>
                    <div class="meal-select">
                        <@simpleSelectPlain
                        name="meals"
                        id="mealsId_br${idBre}"
                        source=breakfastList
                        placeholder="-"
                        multiple=false
                        showMinus=true
                        selectWrapperStyle="overflow:hidden; width:200px;"
                        />
                    </div>
                    <#assign idBre = idBre + 1 />
                </#list>

                <div class="meal-type-name">Przekąska</div>
                <#assign idSna = 1 />
                <#list days as day>
                    <div class="meal-select">
                        <@simpleSelectPlain
                        name="meals"
                        id="mealsId_sn${idSna}"
                        source=snackList
                        placeholder="-"
                        multiple=false
                        showMinus=true
                        selectWrapperStyle="overflow:hidden; width:200px;"
                        />
                    </div>
                    <#assign idSna = idSna + 1 />
                </#list>

                <div class="meal-type-name">Obiad</div>
                <#assign idLun = 1 />
                <#list days as day>
                    <div class="meal-select">
                        <@simpleSelectPlain
                        name="meals"
                        id="mealsId_lu${idLun}"
                        source=lunchList
                        placeholder="-"
                        multiple=false
                        showMinus=true
                        selectWrapperStyle="overflow:hidden; width:200px;"
                        />
                    </div>
                    <#assign idLun = idLun + 1 />
                </#list>

                <div class="meal-type-name">Kolacja</div>
                <#assign idSup = 1 />
                <#list days as day>
                    <div class="meal-select">
                        <@simpleSelectPlain
                        name="meals"
                        id="mealsId_su${idSup}"
                        source=supperList
                        placeholder="-"
                        multiple=false
                        showMinus=true
                        selectWrapperStyle="overflow:hidden; width:200px;"
                        />
                    </div>
                    <#assign idSup = idSup + 1 />
                </#list>
            </div>
        </div>

        <div class="options">
            <label>Przelicznik:
                <input id="multiplier" name="multiplier" type="number" step="0.1" value="1">
            </label>
            <label>
                <input type="checkbox" name="isSnackMultiplied">
                Mnożyć przekąskę?
            </label>
        </div>
        <input type="submit" value="Wygeneruj listę zakupów">
    </form>
</div>
<#include "../../webData/footer.ftlh">