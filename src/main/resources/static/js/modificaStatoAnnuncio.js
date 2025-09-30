$(document).ready(function () {

    $("#nuovoStato").change(function () {
        if ($("#nuovoStato option:selected").val() == "RIFIUTATO") {
            $("#divMotivo").show();
        } else {
            $("#divMotivo").hide();
        }
    });
});