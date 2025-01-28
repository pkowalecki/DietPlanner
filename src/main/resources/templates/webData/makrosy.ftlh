<#macro onClick objId url urlParams formId >

</#macro>
<#macro simpleInput name inputStyle="" class="" min="" max="" type="text" id="" disabled=false placeholder="" divWrapperId="" divWrapperStyle="" label="" labelStyle="" step="" value="">
    <#assign idPolka = id/><#rt>
    <#assign classPolka = class/>
    <div class="form-group-container" id="${divWrapperId}" style="">
        <div class="floating-group">
            <@simpleInputPlain name=name inputStyle=inputStyle class=classPolka min=min max=max type=type id=idPolka disabled=false placeholder=placeholder step=step value=value/>
            <#if label!=""><label class="floating-label" for="${idPolka}" id="labelFor${idPolka}"
                                  style="${labelStyle}">${label}</label></#if>
            <div class="error-message" id="${idPolka}Error">
                <i class="error-icon fa-solid fa-circle-exclamation" id="${idPolka}Error" style="display: none"></i>
            </div>
        </div>
    </div>
</#macro>

<#macro simpleInputPlain name inputStyle="" class="" min="" max="" type="text" id="" disabled=false placeholder="" step="" value="">
    <#assign idPolka = id/><#rt>
    <#assign classPolka = class/>
    <input <#if disabled>disabled="disabled"</#if>
           type="${type}" name="${name}"
           class="floating-input form-control ${classPolka}" <#if inputStyle != "">style="floating-input ${inputStyle}"</#if>
            <#if step!="">step="${step}"</#if>
            <#if idPolka!="">id="${idPolka}"</#if>
            <#if type == "number">
                <#if min != "">min="${min}"</#if>
                <#if max != "">max="${max}"</#if>
            </#if>
            <#if type == "text">
                <#if min != "">minlength="${min}"</#if>
                <#if max != "">maxlength="${max}"</#if>
            </#if>
           autocomplete="off"
            <#if placeholder !="">placeholder="${placeholder}"</#if>
            <#if value!=""> value="${value}" </#if>
    />
</#macro>

<#macro simpleTextArea name style="" class="" minLength="" maxLength="" rows="" id="" disabled=false placeholder="" divWrapperId="" divWrapperStyle="" label="" value="">
    <#assign idPolka = id/><#rt>
    <#assign classPolka = class/>
    <div class="form-group-container" id="${divWrapperId}" style="">
        <div class="floating-group">
            <@simpleTextAreaPlain name=name style=style class=classPolka minLength=minLength maxLength=maxLength rows=rows id=idPolka disabled=false placeholder=placeholder value=value/>
            <label class="floating-label" for="${idPolka}" id="labelFor${idPolka}">${label}</label>
            <div class="error-message" id="${idPolka}Error">
                <i class="error-icon fa-solid fa-circle-exclamation" id="${idPolka}Error" style="display: none"></i>
            </div>
        </div>
    </div>
</#macro>

<#macro simpleTextAreaPlain name style="" class="" minLength="" maxLength="" rows="" id="" disabled=false placeholder="" value="">
    <#assign idPolka = id/><#rt>
    <#assign classPolka = class/>
    <textarea
            name="${name}"
            <#if disabled>disabled="disabled"</#if>
           class="floating-textarea form-control ${classPolka} <#if rows??> no-rows </#if>"
            <#if style != "">style="floating-textarea ${style}"</#if>
            <#if idPolka!="">id="${idPolka}"</#if>
            <#if minLength != "">minlength="${minLength}"</#if>
            <#if maxLength != "">maxlength="${maxLength}"</#if>
           autocomplete="off"
            <#if placeholder !="">placeholder="${placeholder}"</#if>
            <#if rows !="">rows="${rows}"</#if>
    ><#if value?has_content>${value}</#if></textarea>
</#macro>

<#macro simpleSelect name source style="" class="" liveSearch=true id="" disabled=false placeholder="" divWrapperId="" divWrapperStyle="" selectWrapperStyle="" label="" multiple=true showMinus=false divWrapperClass="" selectedValues=[]>
    <#assign idPolka = id/><#rt>
    <#assign classPolka = class/>
    <#assign isLabel = (label?has_content)>
    <div id="${divWrapperId}" class="${divWrapperClass} style="${divWrapperStyle}">
        <div class="floating-group">
            <@simpleSelectPlain name=name style=style class=classPolka liveSearch=liveSearch id=idPolka disabled=false placeholder=placeholder multiple=multiple source=source showMinus=showMinus selectWrapperStyle=selectWrapperStyle labelled=isLabel selectedValues=selectedValues/>
            <#if label!=""><label class="floating-label" for="${idPolka}" id="labelFor${idPolka}">${label}</label></#if>
            <div class="error-message" id="${idPolka}Error">
                <i class="error-icon f a-solid fa-circle-exclamation" id="${idPolka}Error" style="display: none"></i>
            </div>
        </div>
    </div>
</#macro>

<#macro simpleSelectPlain name source style="" class="" liveSearch=true id="" disabled=false placeholder="" divWrapperId="" divWrapperStyle="" multiple=true emptyValue="-" showMinus=false selectWrapperStyle="" labelled=false selectedValues=[]>
    <#assign idPolka = id/><#rt>
    <#assign classPolka = class/>
    <div style="${selectWrapperStyle}">
        <select
                name="${name}"
                <#if disabled>disabled="disabled"</#if>
                class="selectpicker form-control floating-select ${classPolka}"
                <#if liveSearch> data-live-search="true"</#if>
                <#if style != "">style="floating-select ${style}"</#if>
                <#if idPolka!="">id="${idPolka}"</#if>
                <#if placeholder !="">title="${placeholder}"</#if>
                <#if multiple>multiple</#if>
                onchange="<#if labelled>toggleLabelActive('${idPolka}')</#if>"
        >
            <#if source?exists && 0 < source?size>
                <#if showMinus>
                    <option value="-1">${emptyValue}</option>
                </#if>
                <#list source as val>
                    <#if val?has_content>
                        <option value="${val['value']}"
                                <#if val['blokada']?? && val['blokada'] == "true">disabled</#if>
                                <#if selectedValues?seq_contains(val['value']?number)>selected</#if>
                        >${val['name']}</option>
                    </#if>
                </#list>
            <#else>
                <option value="-1">Brak</option>
            </#if>
        </select>
    </div>
    <#if selectedValues?has_content>
        <script>
            $(document).ready(function() {
                $('#${idPolka}').selectpicker('refresh').change();
            });
        </script>
    </#if>
</#macro>

<#macro simpleButton id, url, buttonText="Wyślij" class="">
    <a id="${id}" href="${url}" class="btn ${class}">${buttonText}</a>
</#macro>

<#macro sendFormButton id, formToSend, url, method="post" buttonText="Wyślij" contentType="application/json" reloadPageAfterSuccess=false >
    <button type="button" id="${id}" class="btn btn-primary mt-3">${buttonText}</button>
    <script>
        document.getElementById("${id}").addEventListener("click", function () {
            resetErrors("#${formToSend}");
            const form = document.getElementById("${formToSend}");
            const formData = new FormData(form);
            const jsonData = Object.fromEntries(formData.entries());

            ajaxCall("${url}", "${method}", "${contentType}", JSON.stringify(jsonData), ${reloadPageAfterSuccess?c});
        });
    </script>
</#macro>

<script>

    function toggleLabelActive(selectId) {
        const selectElement = document.getElementById(selectId);
        const labelElement = document.getElementById("labelFor" + selectId);
        const selectedValues = Array.from(selectElement.selectedOptions).map(option => option.value);

        if (selectedValues.length > 0) {
            labelElement.classList.add('active');
        } else {
            labelElement.classList.remove('active');
        }
    }

</script>