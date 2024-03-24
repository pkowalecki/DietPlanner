<#include "../../webData/header.ftlh">
<link rel="stylesheet" href="../../../static/css/loginPage.css"/>
<div class="container wrapper">
    <div class="row mt-5">
        <img src = "../../../static/images/recipe-book-svgrepo-com.svg" alt="Happy book SVG">
        <div class="col-12 col-md-4 mx-auto py-4">
            <form class="row g-3 login-form" method="POST" name="loginForm" action="/login">
                <div class="mb-3">
                    <label for="email" class="form-label">Email</label>
                    <input type="email" class="form-control validation" id="email" name="email" placeholder="User email" required>
                </div>
                <div class="mb-3">
                    <label for="password" class="form-label">Password</label>
                    <input type="password" class="form-control validation" id="password" name="password" placeholder="Password" required>
                </div>
                <div class="d-grid gap-2">
                    <button type="submit" class="btn btn-primary btn-block">Login</button>
                </div>
            </form>
        </div>
    </div>

    <#if error?? && error != "">
        <script>
            document.addEventListener('DOMContentLoaded', function() {
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
</div>
<#include "../../webData/footer.ftlh">