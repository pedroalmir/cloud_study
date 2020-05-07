var wait = false;
var BACKEND_URL = "http://18.229.202.214:8080";

var eventRegister = {
};

function clearEventRegister(route){
    eventRegister[route] = [];
};

function addEvent(route, id, type, func){
    eventRegister[route].push({
        'id'    : id,
        'type'  : type,
        'func'  : func
    });
};

/** 
 * Asyncrhonously fetch the html template partial from 
 * the file directory, then set its contents to the html 
 * of the parent element.
*/
function loadHTML(url, id) {
    req = new XMLHttpRequest();
    req.open('GET', url);
    req.send();
    req.onload = () => {
        $(id).html(req.responseText);

        var events = eventRegister[router._lastRouteResolved.url];
        //console.log(events);
        if(events && events.length > 0){
            events.forEach(function(event){
                if(event.type == 'now'){
                    event.func();
                }else{
                    $(event.id).off(event.type);
                    $(event.id).on(event.type, event.func);
                }
            });
        }
    };
};

/** Load template by key */
function loadTemplate(tKey){
    if(templates[tKey].restricted){
        if(isLogged()){
            loadHTML(templates[tKey].path, '#main-view');
        }else{
            redirectTo('login');
        }
    }else{
        loadHTML(templates[tKey].path, '#main-view');
    }
};
/** Utils to define body class */
function defineBodyClass(cssClass){
    $("#bodyID").removeClass();
    $("#bodyID").addClass(cssClass);
};

/** Redirect function */
function redirectTo(route){
    $(location).attr('href', '#!' + route);
};

const templates = {
    '404'       : {restricted: false, path: './templates/public/404.html'},
    'login'     : {restricted: false, path: './templates/public/login.html'},
    'register'  : {restricted: false, path: './templates/public/register.html'},
    /* Restricted Access */
    'home'      : {restricted: true, path: './templates/restricted/home.html'},
};

/** Index controller */
function indexController(){
    if(isLogged()){
        redirectTo('home');
    }else{
        redirectTo('login');
    }
};

/** Page not found controller */
function notFoundController(){
    loadTemplate('404');
    $(document).ready(function() {
        defineBodyClass('');
    });
};

/* ######################################################### */

/** Login controller */
function loginController(){
    if(isLogged()){
        redirectTo('home');
    }

    var route = router._lastRouteResolved.url;
    
    clearEventRegister(route);
    addEvent(route, '#loginBTN', 'click', loginBtnEvent);
    addEvent(route, '#loginInputPassword', 'keypress', function(event){
        if (event.which == 13) {
            event.preventDefault();
            loginBtnEvent(event);
         }
    });
    addEvent(route, '', 'now', loginAndRegisterBodyAdjust);
    addEvent(route, '', 'now', function(){$('.modal-backdrop').remove()});
    loadTemplate('login');
};

/** #loginBTN event */
function loginBtnEvent(event){
    event.preventDefault();
    if(wait) return;

    $.ajax({
        type: "POST",
        url: BACKEND_URL + "/login",
        data: {
            'email'     : $('#loginInputEmail').val(),
            'password'  : $('#loginInputPassword').val()
        },
        timeout: 600000,
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
                login(result.data);
                redirectTo('home');
            }
        },error: function (error) {
            $("#msgError").html('<strong>Error!</strong> ' + error.responseText);
            $("#msgError").show();
        }, complete: function() {
            $('.loader').hide();
            wait = false;
        }
    });
};

/* ######################################################### */

/** Register controller */
function registerController(){
    var route = router._lastRouteResolved.url;

    clearEventRegister(route);
    addEvent(route, '#submitBTN', 'click', registerUserSubmitBtnEvent);
    addEvent(route, '', 'now', loginAndRegisterBodyAdjust);
    
    loadTemplate('register');
};

/** #submitBTN event */
function registerUserSubmitBtnEvent(event){
    event.preventDefault();
    if(wait) return;
    
    var form = $('#userRegisterForm')[0];
    var data = new FormData(form);

    $.ajax({
        type:        "POST",
        enctype:     "multipart/form-data",
        url:         BACKEND_URL + "/user",
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
                redirectTo('login');
            }
        },error: function (error) {
            $("#msgError").html('<strong>Error!</strong> ' + error.responseText);
            $("#msgError").show();
        }, complete: function() {
            $('.loader').hide();
            wait = false;
        }
    });
};

/* ######################################################### */

/** Home controller */
function homeController(){
    var route = router._lastRouteResolved.url;
    clearEventRegister(route);
    addEvent(route, "#sidebarToggle, #sidebarToggleTop", 'click', sideBarEvent);
    addEvent(route, "#logoutBTN", 'click', logoutBtnEvent);
    addEvent(route, "#submitPostBTN", 'click', submitPostBtnEvent);
    addEvent(route, '#message-text', 'keypress', function(event){
        if (event.which == 13) {
            event.preventDefault();
            submitPostBtnEvent(event);
         }
    });
    addEvent(route, "#updateProfileBTN", 'click', updateProfileBtnEvent);

    addEvent(route, '', 'now', searchUsersBtnEvent);
    addEvent(route, '', 'now', dashboardBodyAdjust);
    addEvent(route, '', 'now', loadUserData);
    
    addEvent(route, '', 'now', function(){$('.modal-backdrop').remove()});
    addEvent(route, '#scroll-to-top-link', 'click', function(e){
        e.preventDefault();
        var $anchor = $(this);
        $('html, body').stop().animate({
            scrollTop: ($($anchor.attr('anchor')).offset().top)
        }, 1000, 'easeInOutExpo');
        
    });
    addEvent(route, '#postLink', 'click', function(e){
        e.preventDefault();
        $('#postPhotoModal').modal();
    });
    addEvent(route, '#profileBTN', 'click', function(e){
        e.preventDefault();
        $('#updateProfileModal').modal();
    });

    addEvent(route, '', 'now', function(){

        var interval = getInterval(router);
        if(interval){
            $('#dateFilterInputID').daterangepicker({
                startDate: new Date(interval[0] + 'T00:00'),
                endDate: new Date(interval[1] + 'T00:00'),
                locale: {
                    cancelLabel: 'Clear',
                    format: 'YYYY-MM-DD',
                    "separator": " to "
                }
            });
        }else{
            $('#dateFilterInputID').daterangepicker({
                autoUpdateInput: false,
                locale: {
                    cancelLabel: 'Clear',
                    format: 'YYYY-MM-DD',
                    "separator": " to "
                }
            });
        }

        $('#dateFilterInputID').on('apply.daterangepicker', function(ev, picker) {
            console.log('aqui');
            var begin = picker.startDate.format('YYYY-MM-DD');
            var end = picker.endDate.format('YYYY-MM-DD');
            $(this).val(begin + ' to ' + end);

            redirectTo(router._lastRouteResolved.url + "?begin=" + begin + "&end=" + end);
            console.log();
        });
      
        $('#dateFilterInputID').on('cancel.daterangepicker', function(ev, picker) {
            $(this).val('');
        });
    });

    loadTemplate('home');
};

function viewProfileController(params){
    if(getLoggedUser().nickname != params.nickname){
        var route = 'home/' + params.nickname;

        clearEventRegister(route);
        addEvent(route, "#sidebarToggle, #sidebarToggleTop", 'click', sideBarEvent);
        addEvent(route, "#logoutBTN", 'click', logoutBtnEvent);
        addEvent(route, "#updateProfileBTN", 'click', updateProfileBtnEvent);
        addEvent(route, '', 'now', searchUsersBtnEvent);
        addEvent(route, '', 'now', dashboardBodyAdjustToViewOtherProfile);
        addEvent(route, '', 'now', loadOtherProfileData);

        addEvent(route, '#scroll-to-top-link', 'click', function(e){
            e.preventDefault();
            var $anchor = $(this);
            $('html, body').stop().animate({
                scrollTop: ($($anchor.attr('anchor')).offset().top)
            }, 1000, 'easeInOutExpo');
            
        });

        addEvent(route, '#profileBTN', 'click', function(e){
            e.preventDefault();
            $('#updateProfileModal').modal();
        });

        addEvent(route, '','now', function(){
            $($('#dashLink').parent()[0]).removeClass('active');
        });

        addEvent(route, '', 'now', function(){

            var interval = getInterval(router);
            if(interval){
                $('#dateFilterInputID').daterangepicker({
                    startDate: new Date(interval[0] + 'T00:00'),
                    endDate: new Date(interval[1] + 'T00:00'),
                    locale: {
                        cancelLabel: 'Clear',
                        format: 'YYYY-MM-DD',
                        "separator": " to "
                    }
                });
            }else{
                $('#dateFilterInputID').daterangepicker({
                    autoUpdateInput: false,
                    locale: {
                        cancelLabel: 'Clear',
                        format: 'YYYY-MM-DD',
                        "separator": " to "
                    }
                });
            }
    
            $('#dateFilterInputID').on('apply.daterangepicker', function(ev, picker) {
                console.log('aqui');
                var begin = picker.startDate.format('YYYY-MM-DD');
                var end = picker.endDate.format('YYYY-MM-DD');
                $(this).val(begin + ' to ' + end);
    
                redirectTo(router._lastRouteResolved.url + "?begin=" + begin + "&end=" + end);
                console.log();
            });
          
            $('#dateFilterInputID').on('cancel.daterangepicker', function(ev, picker) {
                $(this).val('');
            });
        });

        loadTemplate('home');
    }
};

function processLikesAndUnline(event){
    event.preventDefault();
    var postID = $($(this)[0]).attr('data-post-id');
    var like = $($(this)[0]).hasClass("likeBTN");

    if(wait) return;
    
    
    $.ajax({
        type: "POST",
         url:  BACKEND_URL + "/post/like",
        data: {
            email : getLoggedUser().email, 
            post  : postID, 
            isLike: like
        },
        timeout:     600000,
        beforeSend: function() {
            $('.loader').show();
            wait = true;
        }, success: function (result) {
            console.log(result);
            if(result.statusCode == 500){
                
            }else if(result.statusCode == 200){
                location.reload();
            }
        },error: function (error) {
            console.log(error.responseText);
        }, complete: function() {
            $('.loader').hide();
            wait = false;
        }
    });
};

function loadOtherProfileData(){
    var interval = getInterval(router);
    var dataForm = null; 
    if(interval){
        dataForm = {
            'nickname': localStorage.getItem('nickname'), 
            'loggedUser': getLoggedUser().nickname,
            'begin'     : interval[0],
            'end'       : interval[1]
        };
    }else{
        dataForm = {
            'nickname': localStorage.getItem('nickname'), 
            'loggedUser': getLoggedUser().nickname
        };
    }

    $.ajax({
        type: "GET",
        url: BACKEND_URL + "/user",
        data: dataForm,
        timeout: 600000,
        beforeSend: function() {
            $('.loader').show();
        }, success: function (result) {
            //console.log(result);
            if(result.statusCode == 500){
                console.log(result.message);
            }else if(result.statusCode == 200){
                // template-service.js
                //console.log(result.data);
                fillDashboard(getLoggedUser(), result.data, false);
                $('.likeBTN').on('click', processLikesAndUnline);
                $('.unlikeBTN').on('click', processLikesAndUnline);
            }
        },error: function (error) {
            console.log(error.responseText);
        }, complete: function() {
            $('.loader').hide();
        }
    });
}

function loadUserData(){
    var user = getLoggedUser();
    var interval = getInterval(router);
    var dataForm = null; 
    if(interval){
        dataForm = {
            'nickname'  : user.nickname, 
            'loggedUser': user.nickname,
            'begin'     : interval[0],
            'end'       : interval[1]
        };
    }else{
        dataForm = {'nickname': user.nickname, 'loggedUser': user.nickname};
    }

    $.ajax({
        type: "GET",
        url: BACKEND_URL + "/user",
        data: dataForm,
        timeout: 600000,
        beforeSend: function() {
            $('.loader').show();
        }, success: function (result) {
            //console.log(result);
            if(result.statusCode == 500){
                console.log(result.message);
            }else if(result.statusCode == 200){
                // template-service.js
                fillDashboard(result.data, result.data, true);
            }
        },error: function (error) {
            console.log(error.responseText);
        }, complete: function() {
            $('.loader').hide();
        }
    });
};

function submitPostBtnEvent(event){
    event.preventDefault();
    if(wait) return;
    
    var form = $('#postFormID')[0];
    var data = new FormData(form);
    data.append('email', getLoggedUser().email);

    $.ajax({
        type:        "POST",
        enctype:     "multipart/form-data",
        url:         BACKEND_URL + "/post",
        data:        data,
        processData: false,
        contentType: false,
        cache:       false,
        timeout:     600000,
        beforeSend: function() {
            $('.loader').show();
            $("#postModalMsgError").hide();
            wait = true;
        }, success: function (result) {
            console.log(result);
            if(result.statusCode == 500){
                $("#postModalMsgError").html('<strong>Error!</strong> ' + result.message);
                $("#postModalMsgError").show();
            }else if(result.statusCode == 200){
                // close modal and refresh the page
                $('#postPhotoModal').modal('hide');
                location.reload();
            }
        },error: function (error) {
            $("#postModalMsgError").html('<strong>Error!</strong> ' + error.responseText);
            $("#postModalMsgError").show();
        }, complete: function() {
            $('.loader').hide();
            wait = false;
        }
    });
};

function searchUsersBtnEvent(){
    //event.preventDefault();
    $('.basicAutoComplete').on('autocomplete.select', function(event, item){
        event.preventDefault();
        //console.log(item);
        redirectTo('home/' + item.nickname);
    });
    
    $('.basicAutoComplete').autoComplete({
        formatResult: function(item){
            return {
                value: item.id,
                text: item.name,
                html: [
                    $('<img>')
                    .attr('src', item.profileImg)
                    .css({"height": "18px", "margin-right": "5px"}), item.name]
            };
        }, events: {
            search: function(query, callback){
                $.ajax({
                    type: "GET",
                    url: BACKEND_URL + "/find/user",
                    data: {'nickname': query},
                    timeout: 600000,
                    success: function (result) {
                        if(result.statusCode == 500){
                            console.log(result.message);
                        }else if(result.statusCode == 200){
                            var indexToRemove = -1;
                            result.data.forEach(function(user, index){
                                if(user.nickname == getLoggedUser().nickname){
                                    indexToRemove = index;
                                    return;
                                }
                            });
                            if (indexToRemove > -1) {
                                result.data.splice(indexToRemove, 1);
                            }
                            callback(result.data);
                        }
                    },error: function (error) {
                        console.log(error.responseText);
                    }
                });
            }
        }
    });
};

function updateProfileBtnEvent(event){
    event.preventDefault();
    if(wait) return;

    var form = $('#userUpdateForm')[0];
    var data = new FormData(form);
    data.append('email', getLoggedUser().email);

    $.ajax({
        type:        "POST",
        enctype:     "multipart/form-data",
        url:         BACKEND_URL + "/user/update",
        data:        data,
        processData: false,
        contentType: false,
        cache:       false,
        timeout:     600000,
        beforeSend: function() {
            $('.loader').show();
            $("#updateUserModalMsgError").hide();
            wait = true;
        }, success: function (result) {
            //console.log(result);
            if(result.statusCode == 500){
                $("#updateUserModalMsgError").html('<strong>Error!</strong> ' + result.message);
                $("#updateUserModalMsgError").show();
            }else if(result.statusCode == 200){
                // close modal and refresh the page
                updateLoggedUser(result.data);
                $('#updateProfileModal').modal('hide');
                location.reload();
            }
        },error: function (error) {
            $("#updateUserModalMsgError").html('<strong>Error!</strong> ' + error.responseText);
            $("#updateUserModalMsgError").show();
        }, complete: function() {
            $('.loader').hide();
            wait = false;
        }
    });
};

/** Logout event */
function logoutBtnEvent(event){
    event.preventDefault();
    logout();
    $('#logoutModal').modal('hide');
    redirectTo('login');
};

function sideBarEvent(){
    $("body").toggleClass("sidebar-toggled");
    $(".sidebar").toggleClass("toggled");
    if ($(".sidebar").hasClass("toggled")) {
        $('.sidebar .collapse').collapse('hide');
    };
};

function loginAndRegisterBodyAdjust(){
    defineBodyClass("bg-gradient-primary");
};

function dashboardBodyAdjust(){
    defineBodyClass('');
    $('#postPhotoLI').show();
    $('#cardsRowID').show();
    $('#pageTitleID').html('Dashboard');
};

function dashboardBodyAdjustToViewOtherProfile(){
    defineBodyClass('');
    $('#postPhotoLI').hide();
    $('#cardsRowID').hide();
    $('#pageTitleID').html('@' + localStorage.getItem('nickname'));
};