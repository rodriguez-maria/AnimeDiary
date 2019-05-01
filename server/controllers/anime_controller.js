const Anime = require('../models/anime');
const log = require('npmlog');

// Get all cars
exports.getAnimes = async (req, res) => {
    try {
        log.info('getAnimes', 'Params: %j', req.query);
        var search = req.query.search;
        const animes = await Anime.find({title: new RegExp(search, 'i')})
        log.info('getAnimes', 'Returned %j results.', animes.length);
        res.send(animes);
    } catch (err) {
        console.log(err)
        throw Error(err)
    }
}