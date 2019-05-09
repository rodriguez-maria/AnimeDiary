const UserAnime = require('../models/user_anime')

const searchUserAnimes = async (search = '', skip = 0, limit = 0) => {
  return await UserAnime.find({ search: new RegExp(search, 'i') }, null, { skip: skip, limit: limit }).populate('anime')
}

module.exports = {
  searchUserAnimes: searchUserAnimes
}
