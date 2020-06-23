const Anime = require('../models/anime')

const searchAnimes = async (search = '', skip = 0, limit = 0) => {
  return Anime.find({ title: new RegExp(search, 'i') }, null, { skip: skip, limit: limit })
}

const getAnime = async (id) => {
  return Anime.findById(id)
}

module.exports = {
  searchAnimes: searchAnimes,
  getAnime: getAnime
}
