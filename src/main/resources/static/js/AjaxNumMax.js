$(document).ready(function () {
        $("#listavisite").change(function () {

            if ($("#listavisite").val() != "Seleziona una visita") {
                $("#prenota").attr({
                    "disabled": false
                });
            }

            if ($("#listavisite").val() == "Seleziona una visita") {
                $("#prenota").attr({
                    "disabled": true
                });
            }

            var mydata = {dataVisita: $("#dataVisita").val(), idVisita: $("#listavisite").val()};

            $.ajax({
                url: '/numMaxPersone',
                method: 'Post',
                data: mydata,
                success: function (data) {
                    $("#numpersone").val(1);
                    $("#numpersone").attr({
                        "max": data
                    });
                }
            });
        });
    }
);