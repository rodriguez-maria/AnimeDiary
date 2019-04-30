const Anime = require('../models/anime')

// Get all cars
exports.getAnimes = async (req, res) => {
    try {
        console.log(req.query.search)
        var search = req.query.search;
        const animes = await Anime.find({title: new RegExp(search, 'i')})
        console.log(animes)
        res.send(animes);
    } catch (err) {
        console.log(err)
        throw Error(err)
    }
}