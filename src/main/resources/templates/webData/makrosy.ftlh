<#macro onClick objId url urlParams formId >

</#macro>
<#macro simpleInput name style="" class="" minLength="" maxLength="" type="text" id="" disabled=false placeholder="" divWrapperId="" divWrapperStyle="" label="" step="" value="">
    <#assign idPolka = id/><#rt>
    <#assign classPolka = class/>
    <div class="form-group-container" id="${divWrapperId}" style="">
        <div class="floating-group">
            <@simpleInputPlain name=name style=style class=classPolka minLength=minLength maxLength=maxLength type=type id=idPolka disabled=false placeholder=placeholder step=step value=value/>
            <label class="floating-label" for="${idPolka}" id="labelFor${idPolka}">${label}</label>
            <div class="error-message" id="${idPolka}Error">
                <i class="error-icon fa-solid fa-circle-exclamation" id="${idPolka}Error" style="display: none"></i>
            </div>
        </div>
    </div>
</#macro>

<#macro simpleInputPlain name style="" class="" minLength="" maxLength="" type="text" id="" disabled=false placeholder="" step="" value="">
    <#assign idPolka = id/><#rt>
    <#assign classPolka = class/>
    <input <#if disabled>disabled="disabled"</#if>
           type="${type}" name="${name}"
           class="floating-input form-control ${classPolka}" <#if style != "">style="floating-input ${style}"</#if>
            <#if step!="">step="${step}"</#if>
            <#if minLength != "">minlength="${minLength}"</#if> <#if idPolka!="">id="${idPolka}"</#if>
            <#if maxLength != "">maxlength="${maxLength}"</#if>
           autocomplete="off"
            <#if placeholder !="">placeholder="${placeholder}"</#if>
            <#if value!=""> value="${value}" </#if>
    />
</#macro>

<#macro simpleTextArea name style="" class="" minLength="" maxLength="" rows="" id="" disabled=false placeholder="" divWrapperId="" divWrapperStyle="" label="">
    <#assign idPolka = id/><#rt>
    <#assign classPolka = class/>
    <div class="form-group-container" id="${divWrapperId}" style="">
        <div class="floating-group">
            <@simpleTextAreaPlain name=name style=style class=classPolka minLength=minLength maxLength=maxLength rows=rows id=idPolka disabled=false placeholder=placeholder/>
            <label class="floating-label" for="${idPolka}" id="labelFor${idPolka}">${label}</label>
            <div class="error-message" id="${idPolka}Error">
                <i class="error-icon fa-solid fa-circle-exclamation" id="${idPolka}Error" style="display: none"></i>
            </div>
        </div>
    </div>
</#macro>

<#macro simpleTextAreaPlain name style="" class="" minLength="" maxLength="" rows="" id="" disabled=false placeholder="">
    <#assign idPolka = id/><#rt>
    <#assign classPolka = class/>
    <textarea
            <#if disabled>disabled="disabled"</#if>
           class="floating-textarea form-control ${classPolka} <#if rows??> no-rows </#if>"
            <#if style != "">style="floating-textarea ${style}"</#if>
            <#if idPolka!="">id="${idPolka}"</#if>
            <#if minLength != "">minlength="${minLength}"</#if>
            <#if maxLength != "">maxlength="${maxLength}"</#if>
           autocomplete="off"
            <#if placeholder !="">placeholder="${placeholder}"</#if>
            <#if rows !="">rows="${rows}"</#if>
    ></textarea>
</#macro>

<#macro simpleSelect name source style="" class="" liveSearch=true id="" disabled=false placeholder="" divWrapperId="" divWrapperStyle="" label="" multiple=true showMinus=false>
    <#assign idPolka = id/><#rt>
    <#assign classPolka = class/>
    <div class="form-group-container" id="${divWrapperId}" style="">
        <div class="floating-group">
            <@simpleSelectPlain name=name style=style class=classPolka liveSearch=liveSearch id=idPolka disabled=false placeholder=placeholder multiple=multiple source=source showMinus=showMinus/>
            <label class="floating-label" for="${idPolka}" id="labelFor${idPolka}">${label}</label>
            <div class="error-message" id="${idPolka}Error">
                <i class="error-icon f a-solid fa-circle-exclamation" id="${idPolka}Error" style="display: none"></i>
            </div>
        </div>
    </div>
</#macro>

<#macro simpleSelectPlain name source style="" class="" liveSearch=true id="" disabled=false placeholder="" divWrapperId="" divWrapperStyle="" multiple=true emptyValue="-" showMinus=false>
    <#assign idPolka = id/><#rt>
    <#assign classPolka = class/>
    <select
            <#if disabled>disabled="disabled"</#if>
            class="selectpicker form-control floating-select ${classPolka}"
            <#if liveSearch> data-live-search="true"</#if>
            <#if style != "">style="floating-select ${style}"</#if>
            <#if idPolka!="">id="${idPolka}"</#if>
            <#if placeholder !="">title="${placeholder}"</#if>
            <#if multiple>multiple</#if>
            onchange="toggleLabelActive('${idPolka}')"
    >
        <#if source?exists && 0 < source?size>
            <#if showMinus>
                <option value="-1">${emptyValue}</option>
            </#if>
            <#list source as val>
                <option value="${val.value}"
                        <#if val.blokada?? && val.blokada == "true">disabled</#if>>${val.name}</option>
            </#list>
        <#else>
            <option value="-1">Brak</option>
        </#if>
    </select>

</#macro>


<#macro sendFormButton id, formToSend, url, method="post" buttonText="Wyślij" contentType="application/json">
    <button type="button" id="${id}" class="btn btn-primary mt-3">${buttonText}</button>
    <script>
        document.getElementById("${id}").addEventListener("click", function () {
            const form = document.getElementById("${formToSend}");
            const formData = new FormData(form);
            const jsonData = Object.fromEntries(formData.entries());

            ajaxCall("${url}", "${method}", "${contentType}", JSON.stringify(jsonData));
        });
    </script>
</#macro>

<script>

    function toggleLabelActive(selectId) {
        const selectElement = document.getElementById(selectId);
        const labelElement = document.getElementById("labelFor"+selectId);
        const selectedValues = Array.from(selectElement.selectedOptions).map(option => option.value);

        if (selectedValues.length > 0) {
            labelElement.classList.add('active');
        } else {
            labelElement.classList.remove('active');
        }
    }

</script>