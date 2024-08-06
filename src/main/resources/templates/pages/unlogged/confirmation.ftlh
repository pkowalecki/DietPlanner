<#include "../../webData/header.ftlh">
<div class="container wrapper">
    <#if message??>
        <#if message?size gt 0>
            <p>Błędy:</p>
            <ul>
                <#list message?values as msg>
                    <li>${msg}</li>
                </#list>
            </ul>
        </#if>
    </#if>
    <#if activated?? && activated>
    <p>Konto zostało pomyślnie aktywowane!</p>
    </#if>
</div>
<#include "../../webData/footer.ftlh">