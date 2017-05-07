var express = require('express');
var mongoose = require('mongoose');
var connect = require('connect');
var fs = require('fs');
var jwt = require('jwt-simple');

var config = require('./config/config');

const Product = require('./model/product');
const User = require('./model/user');

mongoose.connect(config.database);

var app = express();

app.use(connect.cookieParser());
app.use(connect.logger('dev'));
app.use(connect.bodyParser());

app.use(connect.json());
app.use(connect.urlencoded());
app.use(express.static(__dirname + '/public'));

app.get('/',function (req,res) {
    res.end('home');
});
app.post('/login',function (req,res) {
    User.findOne({email:req.body.email,password:req.body.password},function (err,user) {
            if (err) {
                return handleError(err);
            }
        var token=jwt.encode(user,config.secret);
            res.set("token",token);
            res.status(200).send(user);
        });
});
app.post('/get-user',function (req,res) {
    var userId;
    if(req.body._id){
        userId=req.body._id;
    }else{
        var token = req.headers['token'];
        var decoded = jwt.decode(token, 'superSecret');
        userId=decoded._id;
    }
    User.findById(userId,function (err,user) {
            if (err) {
                return handleError(err);
            }
            res.status(200).send(user);
        });
});
app.post('/user-register', function (req, res) {
    var user = new User();
    user.user_name = req.body.user_name;
    user.name = req.body.name;
    user.password = req.body.password;
    user.email = req.body.email;

    User.findOne({email:user.email,password:user.password},function (err,userres) {
        if (err) {
            return handleError(err);
        }
        if(userres){
            res.status(500).send({message: 'Error already user'});
        }else{
            user.save(function (err, userStored) {
                if (err) res.status(500).send({message: 'Error DB: ${err}'});
                var token=jwt.encode(user,config.secret);
                res.set("token",token);
                res.status(200).send(userStored);
            });
        }
    });

});
app.post('/user-update', function (req, res) {
        var token = req.headers['token'];
        var decoded = jwt.decode(token, 'superSecret');

        User.update({_id: decoded._id}, {'name':req.body.name,'password':req.body.password,'email':req.body.email,'address':req.body.address,'number_phone':req.body.number_phone,'image':req.body.image}, function (err, user) {
            if (err) {
                return handleError(err);
            }
            res.status(200).send(user);
        });
    });
app.post('/product-create', function (req, res) {
    var token = req.headers['token'];
    var decoded = jwt.decode(token, 'superSecret');

    User.findById(decoded._id,function (err,user) {
        if (err) {
            return handleError(err);
        }

        var product = new Product();
        product.name = req.body.name;
        product.description = req.body.description;
        product.old_price = req.body.old_price;
        product.new_price = req.body.new_price;
        product.category = req.body.category;
        product.expiration_date = req.body.expiration_date;
        product.up_date = req.body.up_date;
        product.amount = req.body.amount;
        product.address = req.body.address;
        product.image = req.body.image;
        product._user=user;

        product.save(function (err, productStored) {
            if (err) {
                res.status(500).send({message: 'Error DB: ${err}'});
            }

            res.status(200).send(productStored);
        });
    });
});

app.post('/add-favorite', function (req, res) {
    var token = req.headers['token'];
    var decoded = jwt.decode(token, 'superSecret');

    Product.findById(req.body._id, function (err, product) {
        User.update({_id: decoded._id}, {$push:{'_product': [product]}}, function (err, user) {
                if (err) {
                    return handleError(err);
                }
                res.status(200).send(user);
            }

        );
    });

});

app.get('/product-list', function (req, res) {
var query={};
    Product.find(query, {}, {
        // "skip":0,
        // "limit": 20,
        // "sort":{up_date:1}
    }, function (err, products) {
        if (err) {
            return handleError(err);
        }
        res.status(200).send(products);
    });

});
app.post('/product-list', function (req, res) {
    var token = req.headers['token'];

    var query={};
    //fragment person
    if(token) {
        var decoded = jwt.decode(token, 'superSecret');
        query['_user']=decoded._id;
        if(req.body.selling!=null){
            query['selling']=req.body.selling;
        }
    }else{
        query['selling']=1;
    }
    if(req.body.category!=null){
        query['category']=req.body.category;
    }
    Product.find(query, {}, {
        // "skip":0,
        // "limit": 20,
        "sort":{up_date:-1}
    }, function (err, products) {
        if (err) {
            return handleError(err);
        }
        res.status(200).send(products);
    });

});
app.post('/product-list-favorite', function (req, res) {
    var token = req.headers['token'];
    var decoded = jwt.decode(token, 'superSecret');
    User.findById(decoded._id,function (err,user) {
        if (err) {
            return handleError(err);
        }

        var query = {};
        query['selling']=1;
        query['_id'] = { $in :  user._product  };
        Product.find(query, {}, {
            // "skip":0,
            // "limit": 20,
            // "sort":{up_date:-1}
        }, function (err, products) {
            if (err) {
                return handleError(err);
            }
            res.status(200).send(products);
        });

    });


});
app.post('/product-detail', function (req, res) {
    Product.findById(req.body._id, function (err, product) {
        if (err) {
            return handleError(err);
        }
        res.status(200).send(product);
    }).populate("_user");

});
app.post('/update-product', function (req, res) {
    Product.update({_id: req.body._id}, {'selling': req.body.selling}, function (err, product) {
            if (err) {
                return handleError(err);
            }
            res.status(200).send(product);
        }

    );

});
app.post('/upload', function(req, res) {
    console.log(req.files.picture.originalFilename);
    console.log(req.files.picture.path);
    fs.readFile(req.files.picture.path, function (err, data){
        var newPath = "public/images/" +     req.files.picture.originalFilename;
        console.log(newPath);
        fs.writeFile(newPath, data, function (err) {
            if(err){
                res.json({'response':"Error"});
            }else {
                res.json("Saved");
            }
        });
    });
});
app.listen(config.port, function () {
    console.log("Start server ok!");
});