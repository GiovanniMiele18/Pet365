$(document).ready(function () {

        $("#dataVisita").blur(function () {
            var listaVisite = $("#listavisite");
            listaVisite.empty();

            if ($(this).val() === "") {
                return;
            }

            var data = Date.parse($(this).val());
            var today = new Date().getTime();

            if (isNaN(data)) {
                listaVisite.append('<option>Formato data non valido</option>');
                return;
            }

            if (today >= data) {
                listaVisite.append('<option>Data nel passato</option>');
                return;
            }

            var mydata = {dataVisita: $(this).val(), idAnnuncio: $("#idAnnuncio").val()};
            $.ajax({
                url: '/ricercaVisita',
                method: 'Post',
                data: mydata,
                success: function (data) {


                    if (data.length == 0) {
                        $("#listavisite").append('<option>Nessuna visita</option>');
                    } else {
                        $("#listavisite").append('<option>Seleziona una visita</option>');
                        data.map(visita => {
                            $("#listavisite").append('<option value="' + visita.id + '">' + visita.orarioInizio + '-' + visita.orarioFine + '</option>');

                        })
                    }
                }
            });
        });
    }
);