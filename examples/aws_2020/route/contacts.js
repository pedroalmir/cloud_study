const controller = require("../controller/contacts.js");
const multer = require('multer');
const storage = multer.diskStorage({
    destination: function(req, file, cb) {
        cb(null, './public/uploads');
     },
    filename: function (req, file, cb) {
        cb(null , file.originalname);
    }
});
const parser = multer({ storage: storage });

module.exports = function(app){
    app.get("/api/contacts", controller.listAll);
    app.get("/api/contacts/:id", controller.getContact);
    app.put("/api/contacts/:id", controller.updateContact);
    app.delete("/api/contacts/:id", controller.deleteContact);
    app.post("/api/contacts", parser.single('photo'), controller.addContact);
    app.put("/api/contacts/update_photo/:id", parser.single('photo'), controller.updatePhoto);
    app.post("/api/contacts/teste_arquivo", parser.single('file'), controller.handleFile);

}

