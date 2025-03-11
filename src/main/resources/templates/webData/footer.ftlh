<footer id="footer">
    <#include "swalAlert.ftlh">
    <script>
        function ajaxCall(url, method, contentType, data, shouldReloadOnSuccess = false) {
            $.ajax({
                url: url,
                type: method,
                contentType: contentType,
                data: data,
                success: function (response) {
                    if (response && response.type) {
                        showSwalMessage(response.type, response.message, response.icon);
                        if (shouldReloadOnSuccess) {
                            location.reload();
                        }
                    } else if (response.status === 'REDIRECT' && response.redirectUrl) {
                        window.location.href = response.redirectUrl;
                    } else if (response.status === 'ERROR' && response.additionalData) {
                        var errors = response.additionalData;
                        handleFieldErrors(errors);
                    }
                    else{
                        alert("nieznany typ odpowiedzi ale sukces - do sprawdzenia");
                    }
                },
                error: function (xhr, status, error) {
                    showSwalMessage('Error', 'Nie można przetworzyć odpowiedzi. Spróbuj ponownie.', 'error');
                }
            });
        }

        function handleFieldErrors(errors) {
            $('.text-danger').text('');
            $('.is-invalid').removeClass('is-invalid');
            for (const field in errors) {
                const errorMessage = errors[field];
                if (errorMessage) {
                    const inputField = $('#' + field);
                    const labelField = $('#labelFor' + field);

                    $('#' + field + 'Error').text(errorMessage);
                    inputField.addClass('is-invalid');
                    labelField.css({
                        background: '#ff0018',
                        color: '#ffffff'
                    });
                }
            }
        }

        function resetErrors(selector = "") {
            const scope = selector ? $(selector) : $(document);
            scope.find(".error-message").text("");
            scope.find(".form-control").removeClass("is-invalid");
            scope.find(".floating-label").removeAttr("style");
        }

    </script>
</footer>
</div>
</body>
</html>