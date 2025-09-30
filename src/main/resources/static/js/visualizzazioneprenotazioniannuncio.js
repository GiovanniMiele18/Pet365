$("#dateStandard").blur(function () {
    console.log($("#dateStandard").val());

    if ($("#dateStandard").val())
        $("#formdata").submit();
})