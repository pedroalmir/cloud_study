function getLeftPostTemplate(myprofile){
    if(myprofile){
        return '<li>' + getPostBodyWithoutBtns() + '</li>';
    }else{
        return '<li>' + getPostBody() + '</li>';
    }
};

function getRightPostTemplate(myprofile){
    if(myprofile){
        return '<li class="timeline-inverted">' + getPostBodyWithoutBtns() + '</li>';
    }else{
        return '<li class="timeline-inverted">' + getPostBody() + '</li>';
    }
};

function getPostBody(){
    return `
    <div class="timeline-badge primary">
        <a><i class="fas fa-camera" data-toggle="tooltip" data-placement="left" title="Posted on {{=it.postDate}}"></i></a>
    </div>
    <div class="timeline-panel">
        <div class="timeline-heading">
            <img class="img-responsive" src="{{=it.postURL}}" alt="Likes: {{=it.postLikes}}">
        </div>
        <div class="timeline-body"><p>{{=it.postMessage}}</p></div>
        
        <div class="timeline-footer">
            <div class="row">
                <div class="col-8">
                    <p style="margin: 0;padding: 8px 0px"><strong>Number of Likes</strong>: {{=it.postLikes}}</p>
                </div>
                <div class="col-4">
                {{? it.likedByLoggedUser == true }}
                    <a class="btn btn-secondary unlikeBTN" data-post-id="{{=it.postID}}" style="width: 100%;" href="#">Unlike</a>    
                {{??}}
                    <a class="btn btn-primary likeBTN" data-post-id="{{=it.postID}}" style="width: 100%;" href="#">Like</a>    
                {{?}}
                </div>
            </div>
        </div>
    </div>
    `;
};

function getPostBodyWithoutBtns(){
    return `
    <div class="timeline-badge primary">
        <a><i class="fas fa-camera" data-toggle="tooltip" data-placement="left" title="Posted on {{=it.postDate}}"></i></a>
    </div>
    <div class="timeline-panel">
        <div class="timeline-heading">
            <img class="img-responsive" src="{{=it.postURL}}" alt="Likes: {{=it.postLikes}}">
        </div>
        <div class="timeline-body"><p>{{=it.postMessage}}</p></div>
        
        <div class="timeline-footer">
            <div class="row">
                <div class="col-8">
                    <p style="margin: 0;padding: 8px 0px"><strong>Number of Likes</strong>: {{=it.postLikes}}</p>
                </div>
                <div class="col-4">
                </div>
            </div>
        </div>
    </div>
    `;
};

function fillDashboard(userLogged, userProfile, myprofile){
    $('#usernameSpan').html(userLogged.name);
    $('#userProfilePictureSmall').attr('src', userLogged.profileImg);

    $('#userRegisterDate').html(userLogged.registrationDate);
    $('#userPostNumber').html(userLogged.posts.length);
    $('#userEmail').html(userLogged.email);
    $('#totalUsers').html(userLogged.totalUsers);
    
    // fill posts
    var postLeftTmp = doT.template(getLeftPostTemplate(myprofile));
    var postRightTmp = doT.template(getRightPostTemplate(myprofile));
    var userTimelineHTML = '';
    
    if(userProfile.posts.length > 0){
        userProfile.posts.sort((post1, post2) => post2.date - post1.date);
        userProfile.posts.forEach(function(post, index){
            if(index % 2 == 0){
                userTimelineHTML += postLeftTmp({
                    'likedByLoggedUser': post.likedByLoggedUser,
                    'postID': post.id,
                    'postDate': post.date,
                    'postLikes': post.likes,
                    'postURL': post.imagePath,
                    'postMessage': post.message,
                });
            }else{
                userTimelineHTML += postRightTmp({
                    'likedByLoggedUser': post.likedByLoggedUser,
                    'postID': post.id,
                    'postDate': post.date,
                    'postLikes': post.likes,
                    'postURL': post.imagePath,
                    'postMessage': post.message,
                });
            }
        });
    }else{
        var sTemp = doT.template(getLeftPostTemplate(true))
        userTimelineHTML += sTemp({
            'postID': '-1',
            'postDate': new Date(),
            'postLikes': '',
            'postURL': 'https://abrilexame.files.wordpress.com/2018/01/tristeza.jpg',
            'postMessage': 'We did not find any posts registered in this profile. Encourage your friends to share great moments. The process is simple. Just log in to your profile and start posting. Enjoy it!',
        });
    }

    $('#userTimeline').html(userTimelineHTML);
    $('[data-toggle="tooltip"]').tooltip();
}