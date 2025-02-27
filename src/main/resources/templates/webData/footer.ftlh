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
                    if (response.redirectUrl) {
                        window.location.href = response.redirectUrl;
                    } else if (response && response.type) {
                        showSwalMessage(response.type, response.message, response.icon);
                    } else if (response && response.errorMsg) {
                        showSwalMessage('Błąd!', response.errorMsg, 'error');
                    } else {
                        showSwalMessage('Sukces', 'Operacja zakończona pomyślnie.', 'success');
                        if (shouldReloadOnSuccess) {
                            location.reload();
                        }
                    }
                },
                error: function (xhr, status, error) {
                    try {
                        const errorResponse = JSON.parse(xhr.responseText);
                        if (xhr.status === 400 && typeof errorResponse === 'object' && !Array.isArray(errorResponse)) {
                            handleFieldErrors(errorResponse);
                        } else if (errorResponse) {
                            showSwalMessage(errorResponse.type, errorResponse.message, errorResponse.icon);
                        } else {
                            showSwalMessage('Error', 'Coś poszło nie tak. Spróbuj ponownie.', 'error');
                        }
                    } catch (e) {
                        showSwalMessage('Error', 'Nie można przetworzyć odpowiedzi. Spróbuj ponownie.', 'error');
                    }
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