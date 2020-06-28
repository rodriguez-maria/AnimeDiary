const log = require('npmlog')
const _ = require('underscore')

const animeRepository = require('../repositories/anime_repository')
const userAnimeRepository = require('../repositories/user_anime_repository')
const constants = require('../utils/constants')
const jsonResponse = require('../utils/json_response')
const utils = require('../utils/utils')

const getAnimes = async (req, res) => {
  try {
    log.info('getAnimes', 'Params: %j', req.query)

    const search = req.query.search
    let limit = req.query.limit || 10
    limit = Math.min(limit, constants.MAX_SEARCH_RESULTS)
    const skip = utils.decodeCursor(req.query.cursor)

    const baseUrl = utils.getBaseUrl(req)
    if (!req.query.tags) {
      let animes = await animeRepository.searchAnimes(search, skip, limit)
      animes = _.map(animes, a => jsonify(a, baseUrl))
      log.info('getAnimes', 'Returned %j results.', animes.length)
      jsonResponse.success(res, animes, utils.getCursor(skip + limit))
    } else {
      let animes = await userAnimeRepository.searchUserAnimes(req.authUser._id, search, skip, limit)
      animes = _.map(animes, ua => jsonify(ua.anime, baseUrl, ua.rating, ua.notes, ua.tags))
      log.info('getAnimes', 'Returned %j results.', animes.length)
      jsonResponse.success(res, animes, utils.getCursor(skip + limit))
    }


  } catch (err) {
    log.error('getAnimes', err)
    jsonResponse.error(res, 'Server error.')
  }
}

const updateAnime = async (req, res) => {
  try {
    log.info('updateAnime', 'Params: %j Body: %j', req.query, req.body)

    const animeId = req.query.animeId
    const rating = req.body.rating
    const notes = req.body.notes
    const tags = req.body.tags

    validateUpdateAnimeInput(animeId, rating, notes, tags)

    let anime = await animeRepository.getAnime(animeId)
    if (!anime) {
      log.info('updateAnime', 'Not found with animeId %j.', animeId)
      jsonResponse.error(res, 'Not found.', 404)
      return
    }

    log.info('updateAnime', 'Found with animeId %j.', animeId)
    userAnime = await userAnimeRepository.updateUserAnimes(animeId, req.authUser._id, rating, notes, tags, anime.title)
    const baseUrl = utils.getBaseUrl(req)
    jsonResponse.success(res, jsonify(userAnime.anime, baseUrl, userAnime.rating, userAnime.notes, userAnime.tags))
  } catch (err) {
    log.error('updateAnime', err)
    jsonResponse.error(res, 'Server error.')
  }
}

const validateUpdateAnimeInput = (animeId, rating, notes, tags) => {
  if (!animeId) {
    throw new Error('The param animeId is needed.')
  }
  //TODO
}

const jsonify = (anime, baseUrl = '', rating = null, notes = null, tags = null) => {
  const anime_json = {
    id: anime._id,
    desc: anime.desc,
    title: anime.title
  }

  if (anime.image && anime.image.startsWith('/')) {
    // Locally hosted image. Return full url.
    anime_json.image = baseUrl + anime.image
  }

  json = {
    anime: anime_json
  }

  if (rating) {
    json.rating = rating
  }

  if (notes) {
    json.notes = notes
  }

  if (tags) {
    json.tags = tags
  }

  return json
}

module.exports = {
  getAnimes: getAnimes,
  updateAnime: updateAnime,
}