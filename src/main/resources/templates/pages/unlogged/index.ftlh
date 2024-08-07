<#include "../../webData/header.ftlh">
<link rel="stylesheet" href="../../../static/css/loginPage.css"/>
<div class="container wrapper">
    <div class="row col-12 col-md-6 mx-auto py-4 center-content">
        <img class="row g-3" src="../../../static/images/recipe-book-svgrepo-com.svg" alt="Happy book SVG">
        <form class="row g-3 login-form" style="padding-left: 42px; padding-right: 42px;" method="POST" name="loginForm"
              action="/login">
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
                <span><input type="checkbox" name="rememberMe" autocomplete="off">Remember me</span>
                <a href="/forgotPassword">Forogot password?</a>
            </div>
            <div class="mb-3 goCenter">
                <a id="registerBtn" href="#">Create account</a>
            </div>
            <div class="d-grid gap-2">
                <button type="submit" class="btn btn-primary btn-block">Login</button>
            </div>
        </form>
    </div>

    <div class="modal fade" id="registerModal">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title">Register</h4>
                    <button id="closeModal" type="button" class="close" data-dismiss="modal">&times;</button>
                </div>
                <div class="modal-body">
                    <form id="registerForm">
                        <div data-mdb-input-init class="form-outline mb-4">
                            <label class="form-label" for="nickname">Nickname*</label>
                            <input type="text" id="nickname" name="nickname" class="form-control" required minlength="1" maxlength="50"/>
                            <div class="error-message" id="nicknameError"></div>
                        </div>
                        <div class="row">
                            <div data-mdb-input-init class="form-outline mb-4 col-md-6">
                                <label class="form-label" for="name">Name</label>
                                <input type="text" id="name" name="name" class="form-control" minlength="5" maxlength="50"/>
                                <div class="error-message" id="nameError"></div>
                            </div>
                            <div data-mdb-input-init class="form-outline mb-4 col-md-6">
                                <label class="form-label" for="surname">Surname</label>
                                <input type="text" id="surname" name="surname" class="form-control" minlength="5" maxlength="50"/>
                                <div class="error-message" id="surnameError"></div>
                            </div>
                        </div>
                        <div data-mdb-input-init class="form-outline mb-4">
                            <label class="form-label" for="emailReg">Email address*</label>
                            <input type="email" id="emailReg" name="emailReg" class="form-control" required minlength="10" maxlength="80"/>
                            <div class="error-message" id="emailRegError"></div>
                        </div>
                        <div data-mdb-input-init class="form-outline mb-4">
                            <label class="form-label" for="passwordReg">Password*</label>
                            <div class="hover-text">
                                <i class="bi bi-info-circle"></i>
                            <span class="tooltip-text" id="bottom">
                                Wymagania dotyczące mocy hasła:<br/>
                                <br/>
                                <br/>
                                <br/>
                                <br/>
                            </span>
                            </div>
                            <input type="password" id="passwordReg" name="passwordReg" class="form-control" required minlength="8" maxlength="255"/>
                            <div class="error-message" id="passwordRegError"></div>
                        </div>
                        <div data-mdb-input-init class="form-outline mb-4">
                            <label>Strength of password</label>
                            <div class="group-pass">
                                <div class="power-container">
                                    <div id="power-point"></div> <div id="minRequirementsDone"></div>
                                </div>
                            </div>
                        </div>
                        <div data-mdb-input-init class="form-outline mb-4">
                            <label class="form-label" for="passwordReg2">Confirm password*</label>
                            <input type="password" id="passwordReg2" name="passwordReg2" class="form-control" required minlength="8" maxlength="255"/>
                            <div class="error-message" id="passwordReg2Error"></div>
                        </div>
                        <div data-mdb-input-init class="d-grid gap-2">
                            <button type="submit" class="btn btn-primary btn-block">Register</button>
                        </div>
                    </form>

                </div>
            </div>
        </div>
    </div>

    <#if error?? && error != "">
        <script>
            document.addEventListener('DOMContentLoaded', function () {
                var swalContent = {};
                swalContent = {
                    title: "Error",
                    text: '${error}',
                    icon: "error",
                    confirmButtonColor: "#052d73"
                };
                Swal.fire(swalContent);
            });
        </script>
    </#if>

    <script>
        $(document).ready(function () {
            $("#registerBtn").click(function (e) {
                e.preventDefault();
                $("#registerModal").modal("show");
                $('#registerForm')[0].reset();
            });
            $("#closeModal").click(function (e) {
                e.preventDefault();
                $("#registerModal").modal("hide");
                $('#registerForm')[0].reset();
            });

            $("#registerForm").submit(function (e) {
                e.preventDefault();
                $(".error-message").text("");
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
                        alert('Registration successful');
                        $('#registerModal').modal('hide');
                        $('#registerForm')[0].reset();
                    },
                    error: function (jqXHR) {
                        if (jqXHR.responseJSON && jqXHR.responseJSON.errors) {
                            var errors = jqXHR.responseJSON.errors;
                            for (var field in errors) {
                                if (field !== ""){
                                    $("#" + field + "Error").text(errors[field]);
                                }
                            }
                        }
                    }
                });
            });
        });
    </script>
</div>

<script>
    let password = document.getElementById("passwordReg");
    let strength = document.getElementById("power-point");
    password.oninput = function () {
        let point = 0;
        let value = password.value;
        let widthPower = ["0%", "50%", "75%", "100%"];
        let colorPower = ["#D73F40", "#F2B84F", "#BDE952", "#3ba62f"];
        let minRequirements = /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[\]{}:;',?/*~$^+=<>]).{8,255}$/.test(value);

        if (minRequirements) {
            point = 1;
            if (value.length >= 12) {
                point += 1;
            }
            if (value.length >= 16) {
                point += 1;
            }
        }

        point = Math.min(point, widthPower.length - 1);

        strength.style.width = widthPower[point];
        strength.style.backgroundColor = colorPower[Math.min(point, colorPower.length - 1)];
    };
</script>
<#include "../../webData/footer.ftlh">