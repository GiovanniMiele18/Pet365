$(document).ready(function () {
    let province = $("#dropdownProvince > .form-li");

    $("#cercaProvincia").keyup(function () {
        let value = $(this).val().toLowerCase();
        province.each(function () {
            $(this).toggle($(this).children("input").val().toLowerCase().includes(value));
        });
    });
});