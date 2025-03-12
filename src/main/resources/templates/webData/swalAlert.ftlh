<script>
    function showSwalMessage(type, message, icon) {
        Swal.fire({
            title: type,
            text: message,
            icon: icon,
            confirmButtonColor: "#052d73"
        });
    }

    function showSwalMessageWithRedirDialog(message, redirectUrl) {
        Swal.fire({
            title: 'Sukces',
            text: message,
            icon: 'success',
            timer: 2500,
            timerProgressBar: true,
            confirmButtonText: 'Kontynuuj',
            showConfirmButton: true
        }).then(function() {
            window.location.href = redirectUrl;
        });
    }
</script>
