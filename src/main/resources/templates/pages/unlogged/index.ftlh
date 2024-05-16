<#include "../../webData/header.ftlh">
<link rel="stylesheet" href="../../../static/css/loginPage.css"/>
<div class="container wrapper">
        <div class="row col-12 col-md-6 mx-auto py-4 center-content">
            <img class="row g-3" src="../../../static/images/recipe-book-svgrepo-com.svg" alt="Happy book SVG">
            <form class="row g-3 login-form" style="padding-left: 42px; padding-right: 42px;" method="POST" name="loginForm" action="/login">
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
                    <a id="registerBtn" href="#" >Create account</a>
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
                            <label class="form-label" for="nickname2">Nickname</label>
                            <input type="text" id="nickname2" class="form-control" autocomplete="off" required/>
                        </div>
                        <div data-mdb-input-init class="form-outline mb-4">
                            <label class="form-label" for="name2">Name</label>
                            <input type="text" id="name2" class="form-control" autocomplete="off"/>
                        </div>
                        <div data-mdb-input-init class="form-outline mb-4">
                            <label class="form-label" for="surname2">Surname</label>
                            <input type="text" id="surname2" class="form-control" autocomplete="off"/>
                        </div>
                        <div data-mdb-input-init class="form-outline mb-4">
                            <label class="form-label" for="email2">Email address</label>
                            <input type="email" id="email2" class="form-control" autocomplete="off" required />
                        </div>
                        <div data-mdb-input-init class="form-outline mb-4">
                            <label class="form-label" for="password2">Password</label>
                            <input type="password" id="password2" class="form-control" autocomplete="off" required/>
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
        $(document).ready(function(){
            $("#registerBtn").click(function(e){
                e.preventDefault();
                $("#registerModal").modal("show");
            });
            $("#closeModal").click(function(e){
                e.preventDefault();
                $("#registerModal").modal("hide");
            });

            $("#registerForm").submit(function(e){
                e.preventDefault();
                var formData = {
                    nickname: $('#nickname2').val(),
                    name: $('#name2').val(),
                    surname: $('#surname2').val(),
                    email: $('#email2').val(),
                    password: $('#password2').val()
                };
                $.ajax({
                    type: 'POST',
                    url: '/register',
                    contentType: 'application/json',
                    data: JSON.stringify(formData),
                    success: function(response) {
                        alert('Registration successful');
                        $('#registerModal').modal('hide'); // Zamknij modal
                    },
                    error: function(jqXHR) {
                        alert('Registration failed: ' + jqXHR.responseText);
                    }
                });
            });
        });
    </script>
</div>
<#include "../../webData/footer.ftlh">