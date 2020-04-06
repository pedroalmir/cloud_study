const Contact = require("../models/contact");
const AWS_S3 = require("../util/aws_s3_test");
const fs = require('fs');
const path = require('path');


module.exports.listAll = function(req, res){
    Contact.find({})
    .then(contact_list => {
        res.json(contact_list);
    })
    .catch(err => console.log(err));
}

module.exports.getContact = function(req, res){
    var id = req.params.id;    
    Contact.findById(id)
    .then(contact => {
        res.status(200).send(contact)
    })
    .catch(err => {
        console.log(err);
        res.status(404).send("Contact not found");
    });
}

const criarDiretorio = (diretorio) => {
    return new Promise((resolve, reject) => {
        fs.mkdir(diretorio, { recursive: true }, (err) => {
            if (err){
                reject(err)
            } else{
                resolve(diretorio)
            }
        })
    })
}

const moverArquivo = (old_filename, new_filename) => {
    return new Promise((resolve, reject) => {
        fs.rename(old_filename, new_filename, _err => {
            if (_err){
                reject(_err)
            } else{
                resolve(new_filename)
            }
        })
    })
}

module.exports.addContact_noS3 = function(req, res){
    const newContact = new Contact(req.body);
    console.log(req.body);
    console.log(req.file);
    newContact.photo = "temp";
    newContact.save()
    .then(newContact => {
        criarDiretorio(req.file.destination+"/"+newContact._id)
        .then(() => {
            const new_filename = req.file.destination+"/"+newContact._id+"/"+ req.file.originalname
            moverArquivo(req.file.path, new_filename)
            .then((new_filename ) => {
                var _filename = new_filename.substring(new_filename.lastIndexOf("/")+1);
                Contact.findByIdAndUpdate(newContact._id, {$set:{photo:_filename}},{new:true})
                .then(old_contact => {
                    res.send(old_contact)        
                })
                .catch(err => {
                    res.status(404).send("Contact not found");
                });
            })
        })
        .catch(_err => {
            res.status(405).send("_err");
        })
    })
}

module.exports.addContact = function(req, res){
    const newContact = new Contact(req.body);
    newContact.save()
        .then(newContact => {
            const new_filename = newContact._id+"/"+ req.file.originalname;
            AWS_S3.uploadFileS3(new_filename, req.file.path)
            .then((aws_s3_file ) => {
                Contact.findByIdAndUpdate(newContact._id, {$set:{photo:aws_s3_file.Location}},{new:true})
                .then(old_contact => {
                    deleteFile(req.file.destination, req.file.originalname)
                    .then(()=>{
                        res.send(old_contact)        
                    })                    
                })
            })
        })
    .catch(_err => {
        res.status(405).send(_err);
    })    
}



module.exports.updateContact = function(req, res){
    Contact.findByIdAndUpdate(req.params.id, {$set:{name:req.body.name, nickName:req.body.nickName, email:req.body.email, phone:req.body.phone, birthday:req.body.birthday}},{new:true})
    .then(old_contact => {
        res.send(old_contact)        
    })
    .catch(err => {
        res.status(404).send("Contact not found");
    });
}

module.exports.updatePhoto = (req, res) => {
    const id = req.params.id;
    deleteDirectory('./public/uploads/'+id)
    .then(() => {
        criarDiretorio(req.file.destination+"/"+id)
        .then(() => {
            const new_filename = req.file.destination+"/"+id+"/"+ req.file.originalname
            moverArquivo(req.file.path, new_filename)
            .then((new_filename ) => {
                var _filename = new_filename.substring(new_filename.lastIndexOf("/")+1);
                Contact.findByIdAndUpdate(id, {$set:{photo:_filename}},{new:true})
                .then(old_contact => {
                    res.send(old_contact)        
                })
                .catch(err => {
                    res.status(404).send("Contact not found");
                })
            })  
        })
    }).catch(_err => {
        res.status(405).send("_err");
    })
}


function deleteFile(dir, file) {
    return new Promise(function (resolve, reject) {
        var filePath = path.join(dir, file);
        fs.lstat(filePath, function (err, stats) {
            if (err) {
                return reject(err);
            }
            if (stats.isDirectory()) {
                resolve(deleteDirectory(filePath));
            } else {
                fs.unlink(filePath, function (err) {
                    if (err) {
                        return reject(err);
                    }
                    resolve();
                });
            }
        });
    });
};

function deleteDirectory(dir) {
    return new Promise(function (resolve, reject) {
        fs.access(dir, function (err) {
            if (err) {
                return reject(err);
            }
            fs.readdir(dir, function (err, files) {
                if (err) {
                    return reject(err);
                }
                Promise.all(files.map(function (file) {
                    return deleteFile(dir, file);
                })).then(function () {
                    fs.rmdir(dir, function (err) {
                        if (err) {
                            return reject(err);
                        }
                        resolve();
                    });
                }).catch(reject);
            });
        });
    });
};


module.exports.deleteContact = function(req, res){
    Contact.findByIdAndRemove({_id:req.params.id})
    .then(old_contact => {
        AWS_S3.removeFileS3(old_contact.photo)
        .then(
            res.status(200).send(old_contact)
        )
        .catch(_err => {
            res.status(405).send("_err");
        })
    })
    .catch(err => {
        res.status(404).send("Contact not found");
    });
}

module.exports.handleFile = function(req, res){
    res.status(200).send();
}

