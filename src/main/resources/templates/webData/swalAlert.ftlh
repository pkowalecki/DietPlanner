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