const UserAnime = require('../models/user_anime')

const searchUserAnimes = async (user_id, search = '', skip = 0, limit = 0) => {
  return UserAnime.find({ user: user_id, search: new RegExp(search, 'i') }, null, { skip: skip, limit: limit }).populate('anime')
}

const updateUserAnimes = async (anime_id, user_id, rating, notes, tags, search) => {
  return UserAnime.findOneAndUpdate({ anime: anime_id,  user: user_id},
  {
    anime: anime_id,
    user: user_id,
    notes: notes,
    rating: rating,
    tags: tags,
    search: search,
  },
  {upsert: true, new:true}).populate('anime')
}

module.exports = {
  searchUserAnimes: searchUserAnimes,
  updateUserAnimes: updateUserAnimes,
}
