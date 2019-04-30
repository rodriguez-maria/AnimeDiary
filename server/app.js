const path = require('path');
const express = require('express');
require('express-async-errors');
const app = express();
const MongoClient = require('mongodb').MongoClient;
var ObjectId = require('mongodb').ObjectID;
const bodyParser = require('body-parser');
const bcrypt = require('bcrypt');
const salt = 10;
var db;
const PORT = process.env.PORT || 8080;
const animeController = require('./controllers/anime_controller')

// parse application/x-www-form-urlencoded
app.use(bodyParser.urlencoded({extended: false}))

// parse application/json
app.use(bodyParser.json())

app.use(express.static(path.join(__dirname, 'public')));

const mongoose = require('mongoose')
mongoose.connect('mongodb://localhost/animediary')
 .then(() => console.log('MongoDB connected…'))
 .catch(err => console.log(err))

app.listen(PORT, function () {
        console.log('App listening on port ' + PORT);
    });

/*
MongoClient.connect('mongodb://testuser:testpassword@ds263988.mlab.com:63988/animediary', function (err, client) {
    if (err) {
        return console.log(err);
    }
    db = client.db('animediary');
    app.listen(PORT, function () {
        console.log('App listening on port ' + PORT);
    });
})


app.post('/register', function (req, res) {
    console.log('POST /register/ ', JSON.stringify(req.body));
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
            db.collection('users').findOne({user_name: req.body.user_name}, function (err, user) {
                if (err) {
                    console.error(err);
                    res.status(500).send({'error': 'Something went wrong. Please try again.'});
                }
                else {
                    delete user['password_hash'];
                    user['success'] = true;
                    console.log('[Response] POST /register/ ', JSON.stringify(user));
                    res.send(user);
                }
            });
        }
    });
});

app.post('/login', function (req, res) {
    console.log('POST /login/ ', JSON.stringify(req.body));
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

        console.log(results.length + ' users found for username '+ req.body.user_name);
        var unauthorized = {"error": "Unauthorized"};
        if (!results || results.length == 0) {
            console.error(unauthorized);
            res.status(401).send(unauthorized);
        }
        else if (bcrypt.compareSync(req.body.password, results[0].password_hash)) {
            delete results[0]['password_hash'];
            results[0]['success'] = true;
            console.log('[Response] POST /login/ ', JSON.stringify(results[0]));
            res.send(results[0]);
        }
        else {
            console.error(unauthorized);
            res.status(401).send(unauthorized);
        }
    });
});
*/
app.get('/animes', animeController.getAnimes);
/*app.get('/animes', function (req, res) {
    console.log('GET /animes/ ', JSON.stringify(req.query));
    var search = req.query.search;
    db.collection('animes').find({title: new RegExp(search, 'i')}).toArray(function (err, results) {
        if (err) {
            console.error(err);
            res.status(500).send({'error': 'Something went wrong. Please try again.'});
            return;
        }

        console.log('Sending back ' + results.length + ' animes.');
        res.send(results);
    });
});

app.post('/notes', function (req, res) {
    console.log('GET /notes/ ', JSON.stringify(req.body));
    if (!req.body || !req.body.user_id || !req.body.anime_id || !req.body.note || !req.body.ratings) {
        res.status(500).send({'error': 'user_id, anime_id, ratings, and note are required.'});
        return;
    }
    db.collection('users').findOne({_id: new ObjectId(req.body.user_id)}, function (err, user) {
        if (err) {
            console.error(err);
            res.status(500).send({'error': 'Something went wrong. Please try again.'});
            return;
        }

        if (!user) {
            console.error('User not found.');
            res.status(500).send({'error': 'User not found.'});
            return;
        }

        db.collection('animes').findOne({_id: new ObjectId(req.body.anime_id)}, function (err, anime) {
            if (err) {
                console.error(err);
                res.status(500).send({'error': 'Something went wrong. Please try again.'});
                return;
            }

            if (!anime) {
                console.error('Anime not found.');
                res.status(500).send({'error': 'Anime not found.'});
                return;
            }

            // both found.
            var obj = {
                user_id: req.body.user_id,
                anime_id: req.body.anime_id,
                anime_title: anime.title,
                anime_image: anime.image,
                note: req.body.note,
                ratings: req.body.ratings
            };

            db.collection('notes').save(obj, function (err, result) {
                if (err) {
                    console.error(err);
                    if (err.code === 11000) {
                        res.status(500).send({'error': 'You have already created a note for this anime.'});
                    }
                    else {
                        res.status(500).send({'error': 'Something went wrong. Please try again.'});
                    }
                }
                else {
                    find_user_notes(req, res, req.body.user_id);
                }
            });
        });
    });
});

app.get('/notes', function (req, res) {
    console.log('GET /notes/ ', JSON.stringify(req.query));
    if (!req.query || !(req.query.user_id || req.query._id)) {
        res.status(500).send({'error': 'user_id or _id is required.'});
        return;
    }
    if (req.query.user_id) {
        find_user_notes(req, res, req.query.user_id);
    }
    else {
        find_one_note(req, res, req.query._id);
    }
});

app.put('/notes', function (req, res) {
    console.log('PUT /notes/ ', JSON.stringify(req.query), JSON.stringify(req.body));
    if (!req.query || !req.query._id || !req.body.note || !req.body.ratings) {
        res.status(500).send({'error': '_id, note, and ratings are required.'});
        return;
    }
    db.collection('notes').findAndModify(
        {_id: new ObjectId(req.query._id)}, // query
        [['_id', 'asc']],  // sort order
        {$set: {note: req.body.note, ratings: req.body.ratings}}, // replacement
        {}, // options
        function (err, object) {
            if (err) {
                console.error(err);
                res.status(500).send({'error': 'Unable to update. Please try again.'});
            } else {
                find_one_note(req, res, req.query._id);
            }
        }
    );
});

function find_user_notes(req, res, user_id) {
    db.collection('notes').find({user_id: user_id}).toArray(function (err, results) {
        if (err) {
            console.error(err);
            res.status(500).send({'error': 'Something went wrong. Please try again.'});
            return;
        }

        console.log('Sending back ' + results.length + ' notes.');

        res.send(results.map(function (n) {
            if (!n.ratings) {
                n.ratings = 0;
            }
            return n;
        }));
    });
}

function find_one_note(req, res, _id) {
    db.collection('notes').findOne({_id: new ObjectId(_id)}, function (err, note) {
        if (err) {
            console.error(err);
            res.status(500).send({'error': 'Something went wrong. Please try again.'});
        }
        else {
            if (!note.ratings) {
                note.ratings = 0;
            }

            res.send(note);
        }
    });
}
*/
app.get('/', function (req, res) {
    res.status(200).send('Hello, world!').end();
});

/*
app.get('/insert', function (req, res) {
    var j1 = [{"title":"Attack on Titan","image":"https://cdn.anilist.co/img/dir/anime/reg/16498.jpg"},{"title":"Sword Art Online","image":"https://cdn.anilist.co/img/dir/anime/reg/11757.jpg"},{"title":"Death Note","image":"https://cdn.anilist.co/img/dir/anime/reg/1535.jpg"},{"title":"Steins;Gate","image":"https://cdn.anilist.co/img/dir/anime/reg/9253.jpg"},{"title":"Angel Beats!","image":"https://cdn.anilist.co/img/dir/anime/reg/6547.jpg"},{"title":"Fullmetal Alchemist: Brotherhood","image":"https://cdn.anilist.co/img/dir/anime/reg/5114-Z9wXkxBfC2W3.jpg"},{"title":"The Future Diary","image":"https://cdn.anilist.co/img/dir/anime/reg/10620.jpg"},{"title":"No Game No Life","image":"https://cdn.anilist.co/img/dir/anime/reg/19815-bIo51RMWWhLv.jpg"},{"title":"Kill la Kill","image":"https://cdn.anilist.co/img/dir/anime/reg/18679.jpg"},{"title":"Toradora!","image":"https://cdn.anilist.co/img/dir/anime/reg/4224.jpg"},{"title":"Code Geass: Lelouch of the Rebellion","image":"https://cdn.anilist.co/img/dir/anime/reg/1575.jpg"},{"title":"Durarara!!","image":"https://cdn.anilist.co/img/dir/anime/reg/6746.jpg"},{"title":"Noragami","image":"https://cdn.anilist.co/img/dir/anime/reg/20447.jpg"},{"title":"Bakemonogatari","image":"https://cdn.anilist.co/img/dir/anime/reg/5081.jpg"},{"title":"Psycho-Pass","image":"https://cdn.anilist.co/img/dir/anime/reg/13601.jpg"},{"title":"Tokyo Ghoul","image":"https://cdn.anilist.co/img/dir/anime/reg/20605-F3PbLuMn6ui9.png"},{"title":"Gurren Lagann","image":"https://cdn.anilist.co/img/dir/anime/reg/2001.jpg"},{"title":"Puella Magi Madoka Magica","image":"https://cdn.anilist.co/img/dir/anime/reg/9756.jpg"},{"title":"Sword Art Online II","image":"https://cdn.anilist.co/img/dir/anime/reg/20594-OGVNWwWPa9m4.jpg"},{"title":"Blue Exorcist","image":"https://cdn.anilist.co/img/dir/anime/reg/9919.jpg"},{"title":"Anohana: The Flower We Saw That Day","image":"https://cdn.anilist.co/img/dir/anime/reg/9989.jpg"},{"title":"Another","image":"https://cdn.anilist.co/img/dir/anime/reg/11111.jpg"},{"title":"The Devil is a Part-Timer!","image":"https://cdn.anilist.co/img/dir/anime/reg/15809.jpg"},{"title":"Clannad","image":"https://cdn.anilist.co/img/dir/anime/reg/2167-EgfXVBt6MztP.png"},{"title":"Fate/Zero","image":"https://cdn.anilist.co/img/dir/anime/reg/10087.jpg"},{"title":"Love, Chunibyo & Other Delusions","image":"https://cdn.anilist.co/img/dir/anime/reg/14741-bJiBlamtxo9o.png"},{"title":"Hyouka","image":"https://cdn.anilist.co/img/dir/anime/reg/12189.jpg"},{"title":"Beyond the Boundary","image":"https://cdn.anilist.co/img/dir/anime/reg/18153.jpg"},{"title":"Naruto","image":"https://cdn.anilist.co/img/dir/anime/reg/20.jpg"},{"title":"Guilty Crown","image":"https://cdn.anilist.co/img/dir/anime/reg/10793-LbNrRNEjwCFn.jpg"},{"title":"Elfen Lied","image":"https://cdn.anilist.co/img/dir/anime/reg/226-U1VLbgT5f4ZG.jpg"},{"title":"Akame ga Kill!","image":"https://cdn.anilist.co/img/dir/anime/reg/20613.jpg"},{"title":"Code Geass: Lelouch of the Rebellion R2","image":"https://cdn.anilist.co/img/dir/anime/reg/2904.jpg"},{"title":"Soul Eater","image":"https://cdn.anilist.co/img/dir/anime/reg/3588.jpg"},{"title":"High School of the Dead","image":"https://cdn.anilist.co/img/dir/anime/reg/8074-kbBWOjSLMN86.jpg"},{"title":"Neon Genesis Evangelion","image":"https://cdn.anilist.co/img/dir/anime/reg/30.jpg"},{"title":"My Teen Romantic Comedy SNAFU","image":"https://cdn.anilist.co/img/dir/anime/reg/14813.jpg"},{"title":"Log Horizon","image":"https://cdn.anilist.co/img/dir/anime/reg/17265.jpg"},{"title":"Fairy Tail","image":"https://cdn.anilist.co/img/dir/anime/reg/6702.jpg"},{"title":"The Melancholy of Haruhi Suzumiya","image":"https://cdn.anilist.co/img/dir/anime/reg/849-63USQ1xklBjM.jpg"}];
    var j2 = [{"title":"Cowboy Bebop","image":"https://cdn.anilist.co/img/dir/anime/reg/1.jpg"},{"title":"One Punch Man","image":"https://cdn.anilist.co/img/dir/anime/reg/21087-Du1v8UgbGITB.jpg"},{"title":"Nisekoi","image":"https://cdn.anilist.co/img/dir/anime/reg/18897-xeKYqBdXevus.png"},{"title":"Fullmetal Alchemist","image":"https://cdn.anilist.co/img/dir/anime/reg/121-lcStiEjYkFuH.jpg"},{"title":"The Pet Girl of Sakurasou","image":"https://cdn.anilist.co/img/dir/anime/reg/13759.jpg"},{"title":"Spirited Away","image":"https://cdn.anilist.co/img/dir/anime/reg/199.jpg"},{"title":"Bleach","image":"https://cdn.anilist.co/img/dir/anime/reg/269.jpg"},{"title":"Deadman Wonderland","image":"https://cdn.anilist.co/img/dir/anime/reg/6880.jpg"},{"title":"Terror in Resonance","image":"https://cdn.anilist.co/img/dir/anime/reg/20661-sN5ovQxZZD9j.jpg"},{"title":"K-ON!","image":"https://cdn.anilist.co/img/dir/anime/reg/5680.jpg"},{"title":"OreImo","image":"https://cdn.anilist.co/img/dir/anime/reg/8769.jpg"},{"title":"Clannad After Story","image":"https://cdn.anilist.co/img/dir/anime/reg/4181-uF6SwWrcGQ6l.png"},{"title":"Naruto: Shippuuden","image":"https://cdn.anilist.co/img/dir/anime/reg/1735.jpg"},{"title":"Darker than Black","image":"https://cdn.anilist.co/img/dir/anime/reg/2025.jpg"},{"title":"Your Lie in April","image":"https://cdn.anilist.co/img/dir/anime/reg/20665-98nNcQRVguFE.jpg"},{"title":"Parasyte -the maxim-","image":"https://cdn.anilist.co/img/dir/anime/reg/20623-ZWo5xhTkEoNu.jpg"},{"title":"Fate/Zero 2nd Season","image":"https://cdn.anilist.co/img/dir/anime/reg/11741.jpg"},{"title":"Baccano!","image":"https://cdn.anilist.co/img/dir/anime/reg/2251.jpg"},{"title":"Hunter x Hunter (2011)","image":"https://cdn.anilist.co/img/dir/anime/reg/11061.jpg"},{"title":"Death Parade","image":"https://cdn.anilist.co/img/dir/anime/reg/20931-AWa51FoEzSs9.jpg"},{"title":"My Little Monster","image":"https://cdn.anilist.co/img/dir/anime/reg/14227.jpg"},{"title":"From the New World","image":"https://cdn.anilist.co/img/dir/anime/reg/13125.jpg"},{"title":"Haganai","image":"https://cdn.anilist.co/img/dir/anime/reg/10719.jpg"},{"title":"Nisemonogatari","image":"https://cdn.anilist.co/img/dir/anime/reg/11597.jpg"},{"title":"Black Bullet","image":"https://cdn.anilist.co/img/dir/anime/reg/20457.jpg"},{"title":"High School DxD","image":"https://cdn.anilist.co/img/dir/anime/reg/11617.jpg"},{"title":"5 Centimeters per Second","image":"https://cdn.anilist.co/img/dir/anime/reg/1689.jpg"},{"title":"The Irregular at Magic High School","image":"https://cdn.anilist.co/img/dir/anime/reg/20458.jpg"},{"title":"Magi: The Labyrinth of Magic","image":"https://cdn.anilist.co/img/dir/anime/reg/14513.jpg"},{"title":"Tokyo Ghoul √A","image":"https://cdn.anilist.co/img/dir/anime/reg/20850-rgprgcyacc6P.jpg"},{"title":"A Certain Magical Index","image":"https://cdn.anilist.co/img/dir/anime/reg/4654.jpg"},{"title":"Accel World","image":"https://cdn.anilist.co/img/dir/anime/reg/11759-K242H45xCzYP.png"},{"title":"ERASED","image":"https://cdn.anilist.co/img/dir/anime/reg/21234-9y65nOtJwK2G.jpg"},{"title":"Kokoro Connect","image":"https://cdn.anilist.co/img/dir/anime/reg/11887.jpg"},{"title":"Is It Wrong to Try and Pick Up Girls in a Dungeon?","image":"https://cdn.anilist.co/img/dir/anime/reg/20920-W0MQotcMHHpv.jpg"},{"title":"Watamote: No Matter How I Look at It, It’s You Guys' Fault I’m Not Popular","image":"https://cdn.anilist.co/img/dir/anime/reg/16742.jpg"},{"title":"Btooom!","image":"https://cdn.anilist.co/img/dir/anime/reg/14345.jpg"},{"title":"Monthly Girls' Nozaki-kun","image":"https://cdn.anilist.co/img/dir/anime/reg/20668.jpg"},{"title":"Danganronpa: The Animation","image":"https://cdn.anilist.co/img/dir/anime/reg/16592.jpg"},{"title":"Spice and Wolf","image":"https://cdn.anilist.co/img/dir/anime/reg/2966.jpg"}];
    var j3 = [{"title":"Food Wars! Shokugeki no Soma","image":"https://cdn.anilist.co/img/dir/anime/reg/20923-pAB59gMZom6X.jpg"},{"title":"K","image":"https://cdn.anilist.co/img/dir/anime/reg/14467.jpg"},{"title":"The World God Only Knows","image":"https://cdn.anilist.co/img/dir/anime/reg/8525.jpg"},{"title":"Golden Time","image":"https://cdn.anilist.co/img/dir/anime/reg/17895.jpg"},{"title":"Date A Live","image":"https://cdn.anilist.co/img/dir/anime/reg/15583.jpg"},{"title":"Fate/stay night","image":"https://cdn.anilist.co/img/dir/anime/reg/356.jpg"},{"title":"Daily Lives of High School Boys","image":"https://cdn.anilist.co/img/dir/anime/reg/11843.jpg"},{"title":"Assassination Classroom","image":"https://cdn.anilist.co/img/dir/anime/reg/20755-dd7F1pv6z1y9.jpg"},{"title":"Nichijou - My Ordinary Life","image":"https://cdn.anilist.co/img/dir/anime/reg/10165-CRkmJYnqIrO2.jpg"},{"title":"Samurai Champloo","image":"https://cdn.anilist.co/img/dir/anime/reg/205-U1pjyWo0dzUb.jpg"},{"title":"Kuroko's Basketball","image":"https://cdn.anilist.co/img/dir/anime/reg/11771.jpg"},{"title":"My Hero Academia","image":"https://cdn.anilist.co/img/dir/anime/reg/21459-M5Q3Q5qAFZEe.jpg"},{"title":"The Girl Who Leapt Through Time","image":"https://cdn.anilist.co/img/dir/anime/reg/2236.jpg"},{"title":"Seraph of the End: Vampire Reign","image":"https://cdn.anilist.co/img/dir/anime/reg/20829-cDkQPmYZ6O0I.jpg"},{"title":"MUSHI-SHI","image":"https://cdn.anilist.co/img/dir/anime/reg/457-rpIYktJBidec.jpg"},{"title":"Welcome to the N.H.K.","image":"https://cdn.anilist.co/img/dir/anime/reg/1210.jpg"},{"title":"Re:ZERO -Starting Life in Another World-","image":"https://cdn.anilist.co/img/dir/anime/reg/21355-3phFoi6x3Qwf.jpg"},{"title":"FLCL","image":"https://cdn.anilist.co/img/dir/anime/reg/227-xOQLeS9JPdaE.jpg"},{"title":"Gintama","image":"https://cdn.anilist.co/img/dir/anime/reg/918.jpg"},{"title":"Maid-Sama!","image":"https://cdn.anilist.co/img/dir/anime/reg/7054.jpg"},{"title":"When They Cry","image":"https://cdn.anilist.co/img/dir/anime/reg/934.jpg"},{"title":"Is this a Zombie?","image":"https://cdn.anilist.co/img/dir/anime/reg/8841.jpg"},{"title":"Haikyu!!","image":"https://cdn.anilist.co/img/dir/anime/reg/20464-MfIvvWdoLhbs.jpg"},{"title":"Aldnoah Zero","image":"https://cdn.anilist.co/img/dir/anime/reg/20632.jpg"},{"title":"Charlotte","image":"https://cdn.anilist.co/img/dir/anime/reg/20997-8uqxQpmR1laU.jpg"},{"title":"The Familiar of Zero","image":"https://cdn.anilist.co/img/dir/anime/reg/1195.jpg"},{"title":"Monogatari Series: Second Season","image":"https://cdn.anilist.co/img/dir/anime/reg/17074.jpg"},{"title":"Black Butler","image":"https://cdn.anilist.co/img/dir/anime/reg/4898.jpg"},{"title":"The Seven Deadly Sins","image":"https://cdn.anilist.co/img/dir/anime/reg/20789.jpg"},{"title":"Fate/stay night: Unlimited Blade Works","image":"https://cdn.anilist.co/img/dir/anime/reg/19603.jpg"},{"title":"Black Lagoon","image":"https://cdn.anilist.co/img/dir/anime/reg/889.jpg"},{"title":"Baka and Test - Summon the Beasts","image":"https://cdn.anilist.co/img/dir/anime/reg/6347.jpg"},{"title":"Free! – Iwatobi Swim Club","image":"https://cdn.anilist.co/img/dir/anime/reg/18507.jpg"},{"title":"Eden of The East","image":"https://cdn.anilist.co/img/dir/anime/reg/5630.jpg"},{"title":"Nagi-Asu: A Lull in the Sea","image":"https://cdn.anilist.co/img/dir/anime/reg/16067.jpg"},{"title":"Blood Lad","image":"https://cdn.anilist.co/img/dir/anime/reg/11633.jpg"},{"title":"Howl‘s Moving Castle","image":"https://cdn.anilist.co/img/dir/anime/reg/431.jpg"},{"title":"Blast of Tempest","image":"https://cdn.anilist.co/img/dir/anime/reg/14075.jpg"},{"title":"Ouran High School Host Club","image":"https://cdn.anilist.co/img/dir/anime/reg/853.jpg"},{"title":"Princess Mononoke","image":"https://cdn.anilist.co/img/dir/anime/reg/164.jpg"}];
    var j4 = [{"title":"The Disappearance of Haruhi Suzumiya","image":"https://cdn.anilist.co/img/dir/anime/reg/7311-3r3FWKkLzIzR.jpg"},{"title":"Attack on Titan 2","image":"https://cdn.anilist.co/img/dir/anime/reg/20958-tV4N22giRdga.jpg"},{"title":"A Certain Scientific Railgun","image":"https://cdn.anilist.co/img/dir/anime/reg/6213.jpg"},{"title":"Mekakucity Actors","image":"https://cdn.anilist.co/img/dir/anime/reg/20541.jpg"},{"title":"The Melancholy of Haruhi Suzumiya (2009)","image":"https://cdn.anilist.co/img/dir/anime/reg/4382-P9KWZR2Szm4u.jpg"},{"title":"Love, Chunibyo & Other Delusions - Heart Throb -","image":"https://cdn.anilist.co/img/dir/anime/reg/18671-VFDCSRqfgVlh.jpg"},{"title":"Oreimo 2","image":"https://cdn.anilist.co/img/dir/anime/reg/13659.jpg"},{"title":"Infinite Stratos","image":"https://cdn.anilist.co/img/dir/anime/reg/9041.jpg"},{"title":"Barakamon","image":"https://cdn.anilist.co/img/dir/anime/reg/20722.jpg"},{"title":"Summer Wars","image":"https://cdn.anilist.co/img/dir/anime/reg/5681.jpg"},{"title":"Blood Blockade Battlefront","image":"https://cdn.anilist.co/img/dir/anime/reg/20727-bWfzRH9aIz8x.jpg"},{"title":"K-ON! Season 2","image":"https://cdn.anilist.co/img/dir/anime/reg/7791.jpg"},{"title":"Konosuba -God's Blessing On This Wonderful World!","image":"https://cdn.anilist.co/img/dir/anime/reg/21202-PRIdvS9HW8oF.jpg"},{"title":"Kimi ni Todoke","image":"https://cdn.anilist.co/img/dir/anime/reg/6045.jpg"},{"title":"Lucky☆Star","image":"https://cdn.anilist.co/img/dir/anime/reg/1887.jpg"},{"title":"Amagi Brilliant Park","image":"https://cdn.anilist.co/img/dir/anime/reg/20602-MYYnqsqQCaHg.jpg"},{"title":"Shana of the Burning Eyes","image":"https://cdn.anilist.co/img/dir/anime/reg/355.jpg"},{"title":"The Garden of Words","image":"https://cdn.anilist.co/img/dir/anime/reg/16782.jpg"},{"title":"Nekomonogatari: Kuro","image":"https://cdn.anilist.co/img/dir/anime/reg/15689.jpg"},{"title":"Noragami Aragoto","image":"https://cdn.anilist.co/img/dir/anime/reg/21128-PNfKHjRSzBCs.jpg"},{"title":"HENNEKO – The Hentai Prince and the Stony Cat -","image":"https://cdn.anilist.co/img/dir/anime/reg/15225.jpg"},{"title":"Strike the Blood","image":"https://cdn.anilist.co/img/dir/anime/reg/18277.jpg"},{"title":"Problem Children Are Coming From Another World, Aren't They?","image":"https://cdn.anilist.co/img/dir/anime/reg/15315.jpg"},{"title":"Wolf Children","image":"https://cdn.anilist.co/img/dir/anime/reg/12355.jpg"},{"title":"Dragon Ball Z","image":"https://cdn.anilist.co/img/dir/anime/reg/813.jpg"},{"title":"Gargantia on the Verdurous Planet","image":"https://cdn.anilist.co/img/dir/anime/reg/16524.jpg"},{"title":"Katanagatari","image":"https://cdn.anilist.co/img/dir/anime/reg/6594.jpg"},{"title":"Haganai Next","image":"https://cdn.anilist.co/img/dir/anime/reg/14967.jpg"},{"title":"Sankarea","image":"https://cdn.anilist.co/img/dir/anime/reg/11499.jpg"},{"title":"Seitokai Yakuindomo","image":"https://cdn.anilist.co/img/dir/anime/reg/8675.jpg"},{"title":"Psycho-Pass 2","image":"https://cdn.anilist.co/img/dir/anime/reg/20513.jpg"},{"title":"My Teen Romantic Comedy SNAFU TOO!","image":"https://cdn.anilist.co/img/dir/anime/reg/20698-fmsrWtjtSAYi.jpg"},{"title":"School Days","image":"https://cdn.anilist.co/img/dir/anime/reg/2476.jpg"},{"title":"Steins;Gate The Movie – Load Region of Déjà Vu","image":"https://cdn.anilist.co/img/dir/anime/reg/11577.jpg"},{"title":"Gosick","image":"https://cdn.anilist.co/img/dir/anime/reg/8425.jpg"},{"title":"Shiki","image":"https://cdn.anilist.co/img/dir/anime/reg/7724.jpg"},{"title":"High School DxD New","image":"https://cdn.anilist.co/img/dir/anime/reg/15451.jpg"},{"title":"Gate","image":"https://cdn.anilist.co/img/dir/anime/reg/20994-91wYl3U53oBV.png"},{"title":"Neon Genesis Evangelion: The End of Evangelion","image":"https://cdn.anilist.co/img/dir/anime/reg/32-b2CcYhpRgLoQ.jpg"},{"title":"Ergo Proxy","image":"https://cdn.anilist.co/img/dir/anime/reg/790-HSnRB9kmLtoL.jpg"}];
    var j5 = [{"title":"Magi: The Kingdom of Magic","image":"https://cdn.anilist.co/img/dir/anime/reg/18115.jpg"},{"title":"JoJo's Bizarre Adventure","image":"https://cdn.anilist.co/img/dir/anime/reg/14719.jpg"},{"title":"Serial Experiments Lain","image":"https://cdn.anilist.co/img/dir/anime/reg/339-briieUdxv4ZB.png"},{"title":"My Neighbor Totoro","image":"https://cdn.anilist.co/img/dir/anime/reg/523-IVqJyeKqpdzy.png"},{"title":"Log Horizon 2","image":"https://cdn.anilist.co/img/dir/anime/reg/20671.jpg"},{"title":"Bakuman.","image":"https://cdn.anilist.co/img/dir/anime/reg/7674.jpg"},{"title":"Overlord","image":"https://cdn.anilist.co/img/dir/anime/reg/20832-NYbZ7GW33tyz.jpg"},{"title":"Bunny Drop","image":"https://cdn.anilist.co/img/dir/anime/reg/10162-6McSrF9zeE2g.jpg"},{"title":"D.Gray-man","image":"https://cdn.anilist.co/img/dir/anime/reg/1482.jpg"},{"title":"Hellsing Ultimate","image":"https://cdn.anilist.co/img/dir/anime/reg/777.jpg"},{"title":"Eureka Seven","image":"https://cdn.anilist.co/img/dir/anime/reg/237.jpg"},{"title":"Evangelion: 1.0 You Are (Not) Alone","image":"https://cdn.anilist.co/img/dir/anime/reg/2759.jpg"},{"title":"The World God Only Knows II","image":"https://cdn.anilist.co/img/dir/anime/reg/10080.jpg"},{"title":"Kuroko's Basketball 2","image":"https://cdn.anilist.co/img/dir/anime/reg/16894.jpg"},{"title":"My Mental Choices Are Completely Interfering With My School Romantic Comedy","image":"https://cdn.anilist.co/img/dir/anime/reg/19221.jpg"},{"title":"Natsume's Book of Friends","image":"https://cdn.anilist.co/img/dir/anime/reg/4081.jpg"},{"title":"Claymore","image":"https://cdn.anilist.co/img/dir/anime/reg/1818.jpg"},{"title":"Chaika -The Coffin Princess-","image":"https://cdn.anilist.co/img/dir/anime/reg/20462.jpg"},{"title":"Wagnaria!!","image":"https://cdn.anilist.co/img/dir/anime/reg/6956.jpg"},{"title":"Trigun","image":"https://cdn.anilist.co/img/dir/anime/reg/6.jpg"},{"title":"Steins;Gate: Egoistic Poriomania","image":"https://cdn.anilist.co/img/dir/anime/reg/10863.jpg"},{"title":"Durarara!! X2","image":"https://cdn.anilist.co/img/dir/anime/reg/20652-WVzT1gWTdkPt.png"},{"title":"Penguindrum","image":"https://cdn.anilist.co/img/dir/anime/reg/10721.jpg"},{"title":"Say \"I Love You\".","image":"https://cdn.anilist.co/img/dir/anime/reg/14289.jpg"},{"title":"your name.","image":"https://cdn.anilist.co/img/dir/anime/reg/21519-4I676N0vzuek.jpg"},{"title":"the Garden of sinners Chapter 1: Thanatos. (Overlooking View)","image":"https://cdn.anilist.co/img/dir/anime/reg/2593.jpg"},{"title":"Plastic Memories","image":"https://cdn.anilist.co/img/dir/anime/reg/20872-MTigyWeI0g4V.jpg"},{"title":"Maoyu: Archenemy & Hero","image":"https://cdn.anilist.co/img/dir/anime/reg/14833.jpg"},{"title":"Black★Rock Shooter (TV)","image":"https://cdn.anilist.co/img/dir/anime/reg/11285.jpg"},{"title":"Blue Spring Ride","image":"https://cdn.anilist.co/img/dir/anime/reg/20596.jpg"},{"title":"Inu X Boku Secret Service","image":"https://cdn.anilist.co/img/dir/anime/reg/11013.jpg"},{"title":"To Love Ru","image":"https://cdn.anilist.co/img/dir/anime/reg/3455.jpg"},{"title":"Waiting in the Summer","image":"https://cdn.anilist.co/img/dir/anime/reg/11433.jpg"},{"title":"A Certain Magical Index II","image":"https://cdn.anilist.co/img/dir/anime/reg/8937.jpg"},{"title":"Brynhildr in the Darkness","image":"https://cdn.anilist.co/img/dir/anime/reg/20534.jpg"},{"title":"Kabaneri Of The Iron Fortress","image":"https://cdn.anilist.co/img/dir/anime/reg/21196-0dhwtTGZwTYg.jpg"},{"title":"Ghost in the Shell","image":"https://cdn.anilist.co/img/dir/anime/reg/43.jpg"},{"title":"Dragon Ball","image":"https://cdn.anilist.co/img/dir/anime/reg/223.jpg"},{"title":"Rokka -Braves of the Six Flowers-","image":"https://cdn.anilist.co/img/dir/anime/reg/20955-O5WXIusW8wfC.jpg"},{"title":"Rosario + Vampire","image":"https://cdn.anilist.co/img/dir/anime/reg/2993.jpg"}];
    var arr = [j1, j2, j3, j4, j5];
    for (var i=0; i<arr.length;i++){
        for(var j=0; j<arr[i].length;j++){
            db.collection('animes').save(arr[i][j], function (err, results) {});
        }
    }
});
*/
