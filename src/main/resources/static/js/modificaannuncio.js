$(document).ready(function () {


    $(".delete").click(function () {

        $(this).parent().remove();

    })

    var wrapper0 = $(".container0");
    var wrapper1 = $(".container1");
    var wrapper2 = $(".container2");
    var wrapper3 = $(".container3");
    var wrapper4 = $(".container4");
    var wrapper5 = $(".container5");
    var wrapper6 = $(".container6");

    var lunedi = $(".add_form_field0");
    var martedi = $(".add_form_field1");
    var mercoledi = $(".add_form_field2");
    var giovedi = $(".add_form_field3");
    var venerdi = $(".add_form_field4");
    var sabato = $(".add_form_field5");
    var domenica = $(".add_form_field6");
    var count = 0;


    $(lunedi).click(function (e) {
        e.preventDefault();

        $(wrapper0).append('<div><input class="inputGiorno" type="hidden" value="MONDAY" id="visite' + count + '.giorno" name="visite[' + count + '].giorno"><input type="time" id="visite' + count + '.orarioInizio" name="visite[' + count + '].orarioInizio" value=""><input type="time" id="visite' + count + '.orarioFine" name="visite[' + count + '].orarioFine" value=""><a href="#" class="delete"> x</a></div>'); //add input box
        count++;
    });

    $(martedi).click(function (e) {
        e.preventDefault();

        $(wrapper1).append('<div><input class="inputGiorno" type="hidden" value="TUESDAY" id="visite' + count + '.giorno" name="visite[' + count + '].giorno"><input type="time" id="visite' + count + '.orarioInizio" name="visite[' + count + '].orarioInizio" value=""><input type="time" id="visite' + count + '.orarioFine" name="visite[' + count + '].orarioFine" value=""><a href="#" class="delete"> x</a></div>'); //add input box
        count++;

    });

    $(mercoledi).click(function (e) {
        e.preventDefault();

        $(wrapper2).append('<div><input class="inputGiorno" type="hidden" value="WEDNESDAY" id="visite' + count + '.giorno" name="visite[' + count + '].giorno"><input type="time" id="visite' + count + '.orarioInizio" name="visite[' + count + '].orarioInizio" value=""><input type="time" id="visite' + count + '.orarioFine" name="visite[' + count + '].orarioFine" value=""><a href="#" class="delete"> x</a></div>'); //add input box
        count++;

    });

    $(giovedi).click(function (e) {
        e.preventDefault();

        $(wrapper3).append('<div><input class="inputGiorno" type="hidden" value="THURSDAY" id="visite' + count + '.giorno" name="visite[' + count + '].giorno"><input type="time" id="visite' + count + '.orarioInizio" name="visite[' + count + '].orarioInizio" value=""><input type="time" id="visite' + count + '.orarioFine" name="visite[' + count + '].orarioFine" value=""><a href="#" class="delete"> x</a></div>'); //add input box
        count++;

    });

    $(venerdi).click(function (e) {

        e.preventDefault();

        $(wrapper4).append('<div><input class="inputGiorno" type="hidden" value="FRIDAY" id="visite' + count + '.giorno" name="visite[' + count + '].giorno"><input type="time" id="visite' + count + '.orarioInizio" name="visite[' + count + '].orarioInizio" value=""><input type="time" id="visite' + count + '.orarioFine" name="visite[' + count + '].orarioFine" value=""><a href="#" class="delete"> x</a></div>'); //add input box
        count++;

    });

    $(sabato).click(function (e) {
        e.preventDefault();

        $(wrapper5).append('<div><input class="inputGiorno" type="hidden" value="SATURDAY" id="visite' + count + '.giorno" name="visite[' + count + '].giorno"><input type="time" id="visite' + count + '.orarioInizio" name="visite[' + count + '].orarioInizio" value=""><input type="time" id="visite' + count + '.orarioFine" name="visite[' + count + '].orarioFine" value=""><a href="#" class="delete"> x</a></div>'); //add input box
        count++;

    });

    $(domenica).click(function (e) {
        e.preventDefault();

        $(wrapper6).append('<div><input class="inputGiorno" type="hidden" value="SUNDAY" id="visite' + count + '.giorno" name="visite[' + count + '].giorno"><input type="time" id="visite' + count + '.orarioInizio" name="visite[' + count + '].orarioInizio" value=""><input type="time" id="visite' + count + '.orarioFine" name="visite[' + count + '].orarioFine" value=""><a href="#" class="delete"> x</a></div>'); //add input box
        count++;

    });

    $("#bottonSubmit").click(function () {
        $(".inputGiorno").each(function (index) {
            $(this).attr("name", "visite[" + index + "].giorno")
                .attr("id", "visite" + index + ".giorno").attr("disabled", false)
                .next().attr("name", "visite[" + index + "].orarioInizio").attr("id", "visite" + index + ".orarioInizio").attr("disabled", false)
                .next().attr("name", "visite[" + index + "].orarioFine").attr("id", "visite" + index + ".orarioFine").attr("disabled", false);

        })
    })

    $(wrapper0).on("click", ".delete", function (e) {
        e.preventDefault();
        $(this).parent('div').remove();
    })
    $(wrapper1).on("click", ".delete", function (e) {
        e.preventDefault();
        $(this).parent('div').remove();
    })
    $(wrapper2).on("click", ".delete", function (e) {
        e.preventDefault();
        $(this).parent('div').remove();
    })
    $(wrapper3).on("click", ".delete", function (e) {
        e.preventDefault();
        $(this).parent('div').remove();
    })
    $(wrapper4).on("click", ".delete", function (e) {
        e.preventDefault();
        $(this).parent('div').remove();
    })
    $(wrapper5).on("click", ".delete", function (e) {
        e.preventDefault();
        $(this).parent('div').remove();
    })
    $(wrapper6).on("click", ".delete", function (e) {
        e.preventDefault();
        $(this).parent('div').remove();
    })

});

