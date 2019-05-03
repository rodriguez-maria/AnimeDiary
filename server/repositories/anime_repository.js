const Anime = require('../models/anime');

const searchAnimes = async (search = '', skip = 0, limit = 0) => {
    return await Anime.find({title: new RegExp(search, 'i')}, null, {skip: skip, limit: limit});
};

module.exports = {
    searchAnimes: searchAnimes
};