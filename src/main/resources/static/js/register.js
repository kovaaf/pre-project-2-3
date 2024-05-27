$(document).ready(function () {
    const registerUserForm = $('#register-user-form');

    const restAPIUrl = '/api/v2';
    const registerUserUrl = restAPIUrl + '/register';
    const loginPageUrl = '/login';

    $('#submit-register-user').click(function (e) {
        e.preventDefault();
        let registerData = JSON.stringify(objectifyForm($(registerUserForm).serializeArray()));
        $.ajax({
            url: registerUserUrl,
            method: 'POST',
            data: registerData,
            dataType: 'json',
            contentType: 'application/json',
            success: function (response) {
                window.location.href = loginPageUrl;
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(jqXHR.responseText);
            }
        });
    });

    function objectifyForm(x) {
        //serialize data function
        const formData = {};
        $.each(x, function(i, field){
            if(field.value.trim() != ""){
                if(formData[field.name] != undefined){
                    const val = formData[field.name];
                    if(!Array.isArray(val)){
                        arr = [val];
                    }
                    arr.push(field.value.trim());
                    formData[field.name] = arr;
                }else{
                    formData[field.name] = field.value;
                }
            }
        });
        return formData;
    }
});