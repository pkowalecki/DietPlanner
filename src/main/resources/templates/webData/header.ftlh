<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=0.9">
    <#include "starter.ftlh">

    <link rel="stylesheet" href="/static/css/headerNavbar.css">

    <#if webMsg?? && webMsg?has_content>
        <script>
            document.addEventListener('DOMContentLoaded', function () {
                var swalContent = {};
                swalContent = {
                    title: '${webMsg.type?capitalize}',
                    text: '${webMsg.message}',
                    icon: '${webMsg.icon}',
                    confirmButtonColor: "#052d73"
                };
                Swal.fire(swalContent);
            });
        </script>
    </#if>
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
                    <li class="active"><a href="#" onclick="location.href='/app/auth/loggedUserBoard'">Strona główna</a>
                    </li>
                    <li class="dropdown">
                        <a href="#">Posiłek</a>
                        <ul class="dropdown-content">
                            <li><a href="#" onclick="location.href='/app/auth/addMeal'">Dodaj posiłek</a></li>
                            <li><a href="#" onclick="location.href='/app/auth/addIngredient'">Dodaj składnik</a></li>
                            <li><a href="#" onclick="location.href='/app/auth/generateMealBoard'">Wygeneruj plan
                                    żywienia</a></li>
                            <li><a href="#" onclick="location.href='/app/auth/mealsHistory'">Historia posiłków</a></li>
                        </ul>
                    </li>

                </ul>
            </#if>
        </nav>
    </div>

</header>
<div id="page-container">