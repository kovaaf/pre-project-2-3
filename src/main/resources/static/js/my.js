$(document).ready(function () {
    const editButton = '#submit-edit-user';
    const submitButton = '#submit-new-user';
    const all_users_table = '#all-users-table';
    const editUserForm = $('#edit-form');
    const newUserForm = $('#new-user-form');

    const restAPIUrl = '/api/v2';
    const adminPath = '/admin';
    const crudUrl = restAPIUrl + adminPath + '/user';
    const usersUrl = restAPIUrl + '/users';
    const rolesUrl = restAPIUrl + '/roles';
    const authUser = restAPIUrl + '/authUser';


    // Получаем для логин текущего пользователя и его роли и вставляем в хэдэр страницы
    $.getJSON(authUser, function(user) {
        $('#navbar-login').text(user.name.toUpperCase());
        $('#auth-roles').text(user.rolesList);

        // заполняем таблицу текущего пользователя
        $('#about-user-table')
            .append('<tr>')
            .append('<td>' + user.name + '</td>')
            .append('<td>' + user.email + '</td>')
            .append('<td>' + user.rolesList + '</td>')
            .append('<td>' + user.homeAddress + '</td>')
            .append('<td>' + user.jobAddress + '</td>')
            .append('</tr>');

        // скрываем админскую панель, если пользователь не админ
        if (!user.rolesList.includes('ADMIN')) {
            $('#admin-panel').hide();
            $('#admin-panel-tab').hide();


            $("#user-page").tab('show');
            $("#user-page-tab").tab('show');
        }
    });


    // create table of all users
    $.getJSON(usersUrl, function (users) {
        users.forEach((user) => {
            addNewUserToTable(user);
        });
    });


    // append roles to new user
    $.getJSON(rolesUrl, function (roles) {
        roles.forEach((role) => {
            $('#role-selector')
                .append('<option value=' + role.id + '>' + role.role.replace('ROLE_', '') + '</option>');
        });
        roles.forEach((role) => {
            $('#role-selector-edit-user')
                .append('<option value=' + role.id + '>' + role.role.replace('ROLE_', '') + '</option>');
        });
    });


    $(submitButton).click(function (e) {
        e.preventDefault();
        let newUserData = JSON.stringify(objectifyForm($(newUserForm).serializeArray()));
        $.ajax({
            url: crudUrl,
            type: 'POST',
            data: newUserData,
            contentType: 'application/json',
            success: function (response) {
                addNewUserToTable(response);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(jqXHR.responseText);
            }
        });
    });


    $(all_users_table).on('click', 'button.delete-button', function(events){
        // get id
        let id = $(this).attr('id').replace('delete-user', '');

        $.ajax({
            url: crudUrl,
            type: 'DELETE',
            data: id,
            contentType: 'application/json',
            success: function (response) {
                $('[id=row' + id + ']').remove();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(jqXHR.responseText);
            }
        });
    });

    function addNewUserToTable(user) {
        $(all_users_table)
            .append('<tr id="row' + user.id + '">')
                .append('<td id="row' + user.id + '">' + user.name + '</td>')
                .append('<td id="row' + user.id + '">' + user.email + '</td>')
                .append('<td id="row' + user.id + '">' + user.rolesList + '</td>')
                .append('<td id="row' + user.id + '">' + user.homeAddress + '</td>')
                .append('<td id="row' + user.id + '">' + user.jobAddress + '</td>')
                .append('<td id="row' + user.id + '">' + user.departureTime + '</td>')
                .append('<td id="row' + user.id + '">' + user.travelTime + '</td>')
                .append('<td id="row' + user.id + '">' +
                    '       <button type="button" ' +
                    '               class="btn btn-info" ' +
                    '               data-toggle="modal" ' +
                    '               data-target="#exampleModal" ' +
                    '               id="edit-user' + user.id +'" ' +
                    '               data-id="' + user.id + '" ' +
                    '               data-name="' + user.name + '"' +
                    '               data-homeaddress="' + user.homeAddress + '"' +
                    '               data-jobaddress="' + user.jobAddress + '"' +
                    '               data-email="' + user.email + '">Edit</button></td>' +
                        '<td id="row' + user.id + '"><button type="button" class="btn btn-danger delete-button" id="delete-user' + user.id +'">Delete</button></td>')
            .append('</tr>');
    }

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

    let userId;

    $('#exampleModal').on('show.bs.modal', function (event) {
        const modal = $(this);

        const button = $(event.relatedTarget); // Кнопка, запускающая модальное окно
        userId = button.data('id'); // Извлечь информацию из атрибутов data- *
        const userName = button.data('name'); // Извлечь информацию из атрибутов data- *
        const userEmail = button.data('email'); // Извлечь информацию из атрибутов data- *
        const homeAddress = button.data('homeaddress'); // Извлечь информацию из атрибутов data- *
        const jobAddress = button.data('jobaddress'); // Извлечь информацию из атрибутов data- *
        modal.find('.modal-title').text('Edit user ' + userName);
        modal.find('.modal-body input#edit-name').val(userName);
        modal.find('.modal-body input#edit-email').val(userEmail);
        modal.find('.modal-body input#edit-homeAddress').val(homeAddress);
        modal.find('.modal-body input#edit-jobAddress').val(jobAddress);
        // При необходимости Вы можете инициировать здесь запрос AJAX (а затем выполнить обновление в обратном вызове).
        // Обновите содержимое модального окна.


    });

    $(editButton).on('click', function(events){
        // events.preventDefault();

        // get id
        let patchUrl = crudUrl + '/' + userId;
        let editData = JSON.stringify(objectifyForm($(editUserForm).serializeArray()));

        $.ajax({
            url: patchUrl,
            type: 'PATCH',
            data: editData,
            contentType: 'application/json',
            success: function (response) {
                $('[id=row' + userId + ']').remove();
                addNewUserToTable(response);
                $('#exampleModal').modal('hide');
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(jqXHR.responseText);
            }
        });
    });


    // Для валидации
    // Пример стартового JavaScript для отключения отправки форм при наличии недопустимых полей
    // (function() {
    //     'use strict';
    //     window.addEventListener('load', function() {
    //         // Получите все формы, к которым мы хотим применить пользовательские стили проверки Bootstrap
    //         var forms = document.getElementsByClassName('needs-validation');
    //         // Зацикливайтесь на них и предотвращайте подчинение
    //         var validation = Array.prototype.filter.call(forms, function(form) {
    //             form.addEventListener('submit', function(event) {
    //                 if (form.checkValidity() === false) {
    //                     event.preventDefault();
    //                     event.stopPropagation();
    //                 }
    //                 form.classList.add('was-validated');
    //             }, false);
    //         });
    //     }, false);
    // })();
});