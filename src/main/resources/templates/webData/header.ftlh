<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=0.9">
    <#include "starter.ftlh">

    <link rel="stylesheet" href="/static/css/headerNavbar.css">
    
</head>
<body>
<header>
    <div class="main-bar">
        <nav>
            <input type="checkbox" id="check">
            <label for="check" class="checkbtn">
                <span></span>
                <span></span>
                <span></span>
            </label>

            <div class="logo-header">
                <a href="#">DietPlanner</a>
            </div>

            <#if logged?? && logged==false>

            <#else >
                <ul>
                    <li class="active"><a href="#" onclick="location.href='/auth/mainBoard'">Strona główna</a>
                    </li>
                    <li class="dropdown">
                        <a href="#">Posiłek</a>
                        <ul class="dropdown-content">
                            <li><a href="#" onclick="location.href='/auth/meals/'">Dodaj posiłek</a></li>
                            <li><a href="#" onclick="location.href='/auth/ingredient/'">Dodaj składnik</a></li>
                            <li><a href="#" onclick="location.href='/auth/meal-board/'">Wygeneruj plan
                                    żywienia</a></li>
                            <li><a href="#" onclick="location.href='/auth/meal-history/'">Historia posiłków</a></li>
                        </ul>
                    </li>

                </ul>
            </#if>
        </nav>
    </div>

</header>
<#--<div class="container-fluid" id="tresc">-->
<div id="page-container">