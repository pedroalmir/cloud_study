/** Check if is logged */
function isLogged(){
    return (localStorage.getItem('user') != null);
};

/** Login */
function login(user){
    localStorage.setItem('user', JSON.stringify(user));
};

/** Update Logged User */
function updateLoggedUser(newUserData){
    if(isLogged()){
        localStorage.setItem('user', JSON.stringify(newUserData));
    }
};

/** Logout */
function logout(){
    localStorage.removeItem('user');
};

/** Get logged user */
function getLoggedUser(){
    if(isLogged()){
        return JSON.parse(localStorage.getItem('user'));
    }
    return null;
};