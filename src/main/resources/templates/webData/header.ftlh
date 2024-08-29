<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=0.9">
    <#include "starter.ftlh">
    <link rel="icon" type="image/x-icon" href="/favicon.ico" />
    <link rel="stylesheet" href="/static/css/headerNavbar.css">

    <#if errorMsg?? && errorMsg != "">
        <script>
            document.addEventListener('DOMContentLoaded', function () {
                var swalContent = {};
                swalContent = {
                    title: "Error",
                    text: '${errorMsg}',
                    icon: "error",
                    confirmButtonColor: "#052d73"
                };
                Swal.fire(swalContent);
            });
        </script>
    </#if>
</head>
<body>
<header>
    <#if user??>
        <div class="nav-container">
            <button class="dropbtn" onclick="location.href='/app/auth/loggedUserBoard'">Strona główna</button>
            <div class="dropdown">
                <button class="dropbtn">Posiłek</button>
                <div class="dropdown-content">
                    <a href="addMeal">Dodaj posiłek</a>
                    <a href="#">Dodaj składnik</a>
                    <a href="generateMealBoard">Wygeneruj plan żywienia</a>
                </div>
            </div>
        </div>
    </#if>
</header>
<div id="page-container">