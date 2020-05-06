/** Check if is logged */
function isLogged(){
    return (localStorage.getItem('userSSNetGAE') != null);
};

/** Login */
function login(user){
    localStorage.setItem('userSSNetGAE', JSON.stringify(user));
};

/** Update Logged User */
function updateLoggedUser(newUserData){
    if(isLogged()){
        localStorage.setItem('userSSNetGAE', JSON.stringify(newUserData));
    }
};

/** Logout */
function logout(){
    localStorage.removeItem('userSSNetGAE');
};

/** Get logged user */
function getLoggedUser(){
    if(isLogged()){
        return JSON.parse(localStorage.getItem('userSSNetGAE'));
    }
    return null;
};