const mongoose = require('mongoose');
mongoose.connect('your URL connection', {useNewUrlParser: true});
mongoose.set('useFindAndModify', false);

const Schema = mongoose.Schema;

const ContactSchema = new Schema(
    {        
        name : String,
        nickName : String,
        email : String,
        phone : String,
        birthday : Date,
        photo : String
    }
);

module.exports = mongoose.model('Contact', ContactSchema);

