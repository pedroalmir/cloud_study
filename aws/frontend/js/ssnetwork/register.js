var wait = false;
var BACKEND_URL = "http://localhost:8081/";

$(document).ready(function() {
    $('#submitBTN').on('click', function(event){
        if(wait){
            event.preventDefault();
            return;
        }

        var form = $('#userRegisterForm')[0];
        var data = new FormData(form);

        $.ajax({
            type:        "POST",
            enctype:     "multipart/form-data",
            url:         BACKEND_URL + "ssnetwork/user",
            data:        data,
            processData: false,
            contentType: false,
            cache:       false,
            timeout:     600000,
            beforeSend: function() {
                $('.loader').show();
                $("#msgError").hide();
                wait = true;
            }, success: function (result) {
                //console.log(result);
                if(result.statusCode == 500){
                    $("#msgError").html('<strong>Error!</strong> ' + result.message);
                    $("#msgError").show();
                }else if(result.statusCode == 200){
                    $(location).attr('href', '/pages/home.html?nickname=' + result.data.nickname);
                }
            },error: function (error) {
                $("#msgError").html('<strong>Error!</strong> ' + error);
                $("#msgError").show();
            }, complete: function() {
                $('.loader').hide();
                wait = false;
            }
        });
    });
});