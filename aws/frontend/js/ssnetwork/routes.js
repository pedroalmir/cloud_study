

// use #! to hash
router = new Navigo(null, true, '#!');
router.on({
    'login'     : () => { loginController(); },
    'notFound'  : () => { notFoundController(); },
    'register'  : () => { registerController(); },
});

router.on('home', function(params, query){
    homeController();
});

router.on('home/:nickname', function(params, query){
    viewProfileController(params);
},{
    before: function (done, params) {
        localStorage.setItem('nickname', params.nickname);
        done();
    }, leave: function (params) {
        localStorage.removeItem('nickname');
    }
});
  
// Set the default route
router.on(() => { indexController(); });
  
// set the 404 route
router.notFound((query) => { notFoundController(); });

router.hooks({
    before: function(done, params) {
        done();
     },
    after: function(params) {
    }
});
  
router.resolve();