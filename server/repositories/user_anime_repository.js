const UserAnime = require('../models/user_anime')
const _ = require('underscore')

const searchUserAnimes = async (user_id, search = '', tags = [], skip = 0, limit = 0) => {
  let queryObj = {
    user: user_id,
    search: new RegExp(search, 'i'),
  }
  if (!_.isEmpty(tags)) {
    queryObj.tags = {
      $in: tags
    }
  }
  return UserAnime.find(
    queryObj,
    null, {
      skip: skip,
      limit: limit
    }).populate('anime')
}

const updateUserAnimes = async (anime_id, user_id, rating, notes, tags, search) => {
  return UserAnime.findOneAndUpdate({
    anime: anime_id,
    user: user_id
  }, {
    anime: anime_id,
    user: user_id,
    notes: notes,
    rating: rating,
    tags: tags,
    search: search,
  }, {
    upsert: true,
    new: true
  }).populate('anime')
}

module.exports = {
  searchUserAnimes: searchUserAnimes,
  updateUserAnimes: updateUserAnimes,
}