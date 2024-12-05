<#macro dataInput name style="" label="" labelStyle="" divWrapperId="" divWrapperStyle="" class="simpleInput" value="" onchange="" minLength="" maxLength="" type="text" id="" required="" disabled=false placeholder="" help="" hasLabel=false>
    <#assign idPolka = id/><#rt>
    <div class="form-group" id="${divWrapperId}" style="${divWrapperStyle}">
        <label class="control-label" <#if labelStyle!="">style="${labelStyle}"</#if> for="${idPolka}"
               id="label4${idPolka}">${label}</label>
        <div class="field">
            <@dataInputPlain name=name style=style class=class value=value onchange=onchange maxLength=maxLength type=type id=idPolka required=required hasLabel=true help="" disabled=disabled placeholder=placeholder/>
            <#if errMsg?? && errMsg?length > 0>
                <div class="input-group-addon alert-danger" data-html="true" data-toggle="tooltip" data-placement="left"
                     title="${errMsg}"><i class="fa fa-info-circle info"></i></div>

            <#elseif 0 < help?length><#if help!="">
            <div class="input-group-addon" data-toggle="tooltip" data-placement="left" title="${help}"><i
                            class="fa fa-info-circle info"></i></div></#if></#if>
            <#if required !="">
                <div class="input-group-addon"><i class="warning fa fa-asterisk"></i><#t>
                <#if required?length != 1><i class="warning fa fa-asterisk"></i></#if></div></#if>
        </div>
    </div>
</#macro>

<#macro dataInputPlain name style="" class="simpleInput" minLength="" maxLength="" type="text" id="" required="" disabled=false placeholder="">
    <#assign idPolka = id/><#rt>
    <#assign classPolka = class/>
    <input <#if disabled?? && disabled == true>disabled="disabled"</#if>
           type="${type}" name="${name}" class="formInActive ${classPolka}" <#if style != "">style="${style}"</#if>
            <#if minLength != "">minlength="${minLength}"</#if> <#if idPolka!="">id="${idPolka}"</#if>
            <#if maxLength != "">maxlength="${maxLength}"</#if>
           autocomplete="off"
            <#if placeholder !="">placeholder="${placeholder}"</#if>
    />
</#macro>

<#macro onClick objId url urlParams formId >

</#macro>

<#macro getErrorForName name>
    <#list errorsList as error>
        <#assign errMsg = ""/>
        <#if error.name = name>
            <#list error.messages as msg>
                <#if 0 < errMsg?length><#assign errMsg = errMsg + "<br />"></#if>
                <#assign errMsg = errMsg + msg/>
            </#list>${errMsg}<#return>
        </#if>
    </#list>
</#macro>

<#macro simpleInput name style="" class="form-control" minLength="" maxLength="" type="text" id="" disabled=false placeholder="" divWrapperId="" divWrapperStyle="" label="">
    <#assign idPolka = id/><#rt>
    <#assign classPolka = class/>
    <div class="form-floating mb-3" id="${divWrapperId}"
         style="margin-bottom: 30px; height: 34px !important; ${divWrapperStyle}">
        <label class="" for="${idPolka}" id="labelFor${idPolka}">${label}</label>
        <div class="input-group">
            <@simpleInputPlain name=name style=style class=classPolka minLength=minLength maxLength=maxLength type=type id=idPolka disabled=false placeholder=""/>
        </div>
    </div>

</#macro>
<#macro simpleInputPlain name style="" class="form-control" minLength="" maxLength="" type="text" id="" disabled=false placeholder="">
    <#assign idPolka = id/><#rt>
    <#assign classPolka = class/>
    <input <#if disabled?? && disabled == true>disabled="disabled"</#if>
           type="${type}" name="${name}" class="formInActive ${classPolka}" <#if style != "">style="${style}"</#if>
            <#if minLength != "">minlength="${minLength}"</#if> <#if idPolka!="">id="${idPolka}"</#if>
            <#if maxLength != "">maxlength="${maxLength}"</#if>
           autocomplete="off"
            <#if placeholder !="">placeholder="${placeholder}"</#if>
    />
</#macro>