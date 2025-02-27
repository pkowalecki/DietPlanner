<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=0.9">
    <#include "../../webData/starter.ftlh">
    <#include "../../webData/swalAlert.ftlh">
    <link rel="stylesheet" href="../../../static/css/loginPage.css"/>
</head>
<div class="container-fluid">
    <div class="center-content">
        <img class="row g-3" src="../../../static/images/recipe-book-svgrepo-com.svg" alt="Happy book SVG">
        <form class="row g-3 login-form" style="padding-left: 42px; padding-right: 42px;" id="loginForm">
            <div class="mb-3">
                <label for="email" class="form-label">Email</label>
                <input type="email" class="form-control" id="email" name="email" placeholder="User email"
                       autocomplete="on" required>
            </div>
            <div class="mb-3">
                <label for="password" class="form-label">Password</label>
                <input type="password" class="form-control" id="password" name="password"
                       placeholder="Password" autocomplete="on" required>
            </div>
            <div class="mb-3 group form-link">
                <a id="registerBtn" href="#">Create account</a>
            </div>
            <div class="d-grid gap-2">
                <button type="submit" class="btn btn-primary btn-block">Zaloguj</button>
            </div>
        </form>
    </div>
    <div id="registerModalContainer"></div>
</div>
<script>
    function setupPasswordStrengthIndicator() {
        let pswd = document.getElementById("passwordReg");
        let strength = document.getElementById("power-point");

        //tooltip
        let minLengthText = document.getElementById("minLength");
        let lowercaseText = document.getElementById("lowercase");
        let uppercaseText = document.getElementById("uppercase");
        let numberText = document.getElementById("number");
        let specialCharText = document.getElementById("specialChar");


        pswd.oninput = function () {
            let value = pswd.value;

            let minLengthMet = value.length >= 8;
            let lowercaseMet = /[a-z]/.test(value);
            let uppercaseMet = /[A-Z]/.test(value);
            let numberMet = /[0-9]/.test(value);
            let specialCharMet = /[!@#&()–[\]{}:;',?/*~$^+=<>]/.test(value);

            minLengthText.style.textDecoration = minLengthMet ? "line-through" : "none";
            lowercaseText.style.textDecoration = lowercaseMet ? "line-through" : "none";
            uppercaseText.style.textDecoration = uppercaseMet ? "line-through" : "none";
            numberText.style.textDecoration = numberMet ? "line-through" : "none";
            specialCharText.style.textDecoration = specialCharMet ? "line-through" : "none";

            let point = 0;
            let maxPoints = 7;

            if (minLengthMet) point += 1;
            if (lowercaseMet) point += 1;
            if (uppercaseMet) point += 1;
            if (numberMet) point += 1;
            if (specialCharMet) point += 1;

            if (value.length >= 16) point += 2;
            else if (value.length >= 12) point += 1;


            point = Math.min(point, maxPoints);

            let widthPercentage = (point / maxPoints) * 100;
            let colorPower = ["#D73F40", "#F2B84F", "#F2B84F", "#F2B84F", "#BDE952", "#BDE952", "#3ba62f"];

            strength.style.width = widthPercentage + "%";
            strength.style.backgroundColor = colorPower[Math.floor(point)];
        };
    }

    $(document).ready(function () {
        $("#registerBtn").click(function (e) {
            e.preventDefault();
            $.ajax({
                url: '/registerModal',
                method: 'GET',
                success: function (response) {
                    $('#registerModalContainer').html(response);
                    $("#registerModal").modal("show");
                    $('#registerForm')[0].reset();
                    setupPasswordStrengthIndicator();
                },
                error: function () {
                    showSwalMessage('Błąd', 'Nie udało się załadować okna rejestracji', 'error');
                }
            });
        });

        $(document).on('click', '#closeModal', function (e) {
            e.preventDefault();
            $("#registerModal").modal("hide");
        });


        $(document).on('submit', '#registerForm', function (e) {
            e.preventDefault();
            $(".error-message").text("");
            $(".form-control").removeClass('is-invalid');

            var formData = {
                nickname: $('#nickname').val(),
                name: $('#name').val(),
                surname: $('#surname').val(),
                emailReg: $('#emailReg').val(),
                passwordReg: $('#passwordReg').val(),
                passwordReg2: $('#passwordReg2').val()
            };

            $.ajax({
                type: 'POST',
                url: '/register',
                contentType: 'application/json',
                data: JSON.stringify(formData),
                success: function (response) {
                    if (response.status === 'ERROR' && response.additionalData) {
                        var errors = response.additionalData;
                        for (var field in errors) {
                            $('#' + field + 'Error').text(errors[field]);
                            $('#' + field).addClass('is-invalid');
                        }
                    } else if (response.status === 'SUCCESS') {
                        showSwalMessage("Sukces", response.message, response.icon);
                        $('#registerModal').modal('hide');
                        $('#registerForm')[0].reset();
                    } else if (response.status === 'ERROR') {
                        showSwalMessage("Błąd", response.message, response.icon);
                    }
                },
                error: function () {
                    showSwalMessage('Błąd', 'Wystąpił nieoczekiwany błąd', 'error');
                }
            });
        });

        $(document).on('submit', '#loginForm', function (e) {
            e.preventDefault();

            var formData = {
                email: $('#email').val(),
                password: $('#password').val(),
            };

            $.ajax({
                type: 'POST',
                url: '/login',
                contentType: 'application/json',
                data: JSON.stringify(formData),
                success: function (response) {
                    if (response.status === 'REDIRECT' && response.redirectUrl) {
                        window.location.href=response.redirectUrl;
                    } else if (response.status === 'ERROR') {
                        showSwalMessage("Błąd", response.message, response.icon);
                    }
                },
                error: function () {
                    showSwalMessage('Błąd', 'Wystąpił nieoczekiwany błąd', 'error');
                }
            });
        });
    });
</script>

</html>