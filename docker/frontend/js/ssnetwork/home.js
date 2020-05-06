var BACKEND_URL = "http://localhost:8081/";

function getPostTemplate(){
    return `
<div class="card shadow mb-4">
    <div class="card-header py-3">
        <h6 class="m-0 font-weight-bold text-primary">Posted on {{=it.postDate}}</h6>
    </div>
    <div class="card-body">
        <div class="text-center">
        <img class="img-fluid px-3 px-sm-4 mt-3 mb-4" style="width: 25rem;" src="{{=it.postURL}}" alt="Likes: {{=it.postLikes}}">
        </div>
        <p class="text-justify">{{=it.postMessage}}</p>
        <p class="text-right" style="margin-bottom: 0px;">
        <a class="btn btn-primary likeBTN" data-post-id="{{=it.postID}}" style="width: 150px;" href="#">Like</a>
        <a class="btn btn-secondary unlikeBTN" data-post-id="{{=it.postID}}" style="width: 150px;" href="#">Unlike</a>
        </p>
    </div>
</div>
    `;
}

function getURLParameter(sParam){
    var sPageURL = window.location.search.substring(1);

    var sURLVariables = sPageURL.split('&');

    for (var i = 0; i < sURLVariables.length; i++){
        var sParameterName = sURLVariables[i].split('=');
        if (sParameterName[0] == sParam){
            return sParameterName[1];
        }
    }
}

function loadAllUserData(nickname){
    $.ajax({
        type: "GET",
        url: BACKEND_URL + "ssnetwork/user",
        data: {'nickname': nickname},
        timeout: 600000,
        beforeSend: function() {
            $('.loader').show();
        }, success: function (result) {
            console.log(result);
            if(result.statusCode == 500){
               
            }else if(result.statusCode == 200){
                fillDashboard(result.data);
            }
        },error: function (error) {
            
        }, complete: function() {
            $('.loader').hide();
        }
    });
}

function fillDashboard(user){
    $('#usernameSpan').html(user.name);
    $('#userProfilePictureSmall').attr('src', user.profileImg);

    $('#userRegisterDate').html(user.registrationDate);
    $('#userPostNumber').html(user.posts.length);
    $('#userEmail').html(user.email);
    $('#totalUsers').html(user.totalUsers);
    // fill posts

    var postTmp = doT.template(getPostTemplate());
    var left = [], middle = [], right = []; var count = 0;
    user.posts.forEach(function(post){
        var result = postTmp({
            'postID': post.id,
            'postDate': post.date,
            'postLikes': post.likes,
            'postURL': post.imagePath,
            'postMessage': post.message,
        });
        if(count % 3 == 0){
            left.push(result);
        }else if(count % 3 == 1){
            middle.push(result);
        }else{
            right.push(result);
        }
        count++;
    });

    $('#leftPosts').html(left.join('\n'));
    $('#middlePosts').html(middle.join('\n'));
    $('#rightPosts').html(right.join('\n'));
}

function organizeLinksEvents(){
    $('#dashLink').on('click', function(){
        var nickname = getURLParameter('nickname');
        $(location).attr('href', '/pages/home.html?nickname=' + result.data.nickname);
    });
    $('#postLink').on('click', function(){
        var nickname = getURLParameter('nickname');
        $(location).attr('href', '/pages/home.html?nickname=' + result.data.nickname);
    });
    $('#searchLink').on('click', function(){
        var nickname = getURLParameter('nickname');
        $(location).attr('href', '/pages/home.html?nickname=' + result.data.nickname);
    });
}

// usernameSpan
// userProfilePictureSmall
// profileBTN
//userPostNumber
// userRegisterDate
//userEmail
//totalUsers

$(document).ready(function() {
    var nickname = getURLParameter('nickname');
    if(nickname && nickname.length > 0){
        console.log('oi');
        loadAllUserData(nickname);
    }

    $('#submitBTN').on('click', function(event){
        if(wait){
            event.preventDefault();
            return;
        }

        var form = $('#userRegisterForm')[0];
        var data = new FormData(form);

        
    });
});