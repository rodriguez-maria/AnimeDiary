const express = require('express');
const app = express();
const MongoClient = require('mongodb').MongoClient;
const bodyParser = require('body-parser');
const bcrypt = require('bcrypt');
const salt = 10;
var db;

// parse application/x-www-form-urlencoded
app.use(bodyParser.urlencoded({extended: false}))

// parse application/json
app.use(bodyParser.json())

MongoClient.connect('mongodb://testuser:testpassword@ds263988.mlab.com:63988/animediary', function (err, client) {
    if (err) {
        return console.log(err);
    }
    db = client.db('animediary');
    app.listen(3000, function () {
        console.log('listening on 3000');
    });
})


app.post('/register', function (req, res) {
    console.log('register/ ', req.body);
    if (!req.body || !req.body.user_name || !req.body.name || !req.body.password) {
        res.status(500).send({'error': 'user_name, name, and password are required.'});
        return;
    }
    var obj = {
        user_name: req.body.user_name,
        name: req.body.name,
        password_hash: bcrypt.hashSync(req.body.password, salt)
    };

    db.collection('users').save(obj, function (err, results) {
        if (err) {
            console.error(err);
            if (err.code === 11000) {
                res.status(500).send({'error': 'Username already exists.'});
            }
            else {
                res.status(500).send({'error': 'Something went wrong. Please try again.'});
            }

        }
        else {
            delete obj['password_hash'];
            obj['success'] = true;
            res.send(obj);
        }
    });
});

app.post('/login', function (req, res) {
    console.log('login/ ', req.body);
    if (!req.body || !req.body.user_name || !req.body.password) {
        res.status(500).send({'error': 'user_name and password are required.'});
        return;
    }

    db.collection('users').find({user_name: req.body.user_name}).toArray(function (err, results) {
        if (err) {
            console.error(err);
            res.status(500).send({'error': 'Something went wrong. Please try again.'});
            return;
        }

        console.log(results);
        var unauthorized = {"error": "Unauthorized"};
        if (!results || results.length == 0) {
            res.status(401).send(unauthorized);
        }
        else if (bcrypt.compareSync(req.body.password, results[0].password_hash)) {
            delete results[0]['password_hash'];
            results[0]['success'] = true;
            res.send(results[0]);
        }
        else {
            res.status(401).send(unauthorized);
        }
    });
});

