<#include "../../webData/header.ftlh">
<#--<h1>Zalogowano, ${user.getNickName()}</h1>-->
<div class="col-xs-12 col-sm-12 col-md-10 col-md-offset-1 col-lg-8 col-lg-offset-2">
    <form action="/" method="post" id="formToSend">
        <@simpleInput name="Imie" placeholder="Imie" label="Imie"/>
    </form>
</div>
<#include "../../webData/footer.ftlh">