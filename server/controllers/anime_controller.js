const log = require('npmlog')
const _ = require('underscore')

const animeRepository = require('../repositories/anime_repository')
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
    let animes = await animeRepository.searchAnimes(search, skip, limit)
    animes = _.map(animes, a => jsonify(a, baseUrl))

    log.info('getAnimes', 'Returned %j results.', animes.length)
    jsonResponse.success(res, animes, utils.getCursor(skip + limit))
  } catch (err) {
    log.error('getAnimes', err)
    jsonResponse.error(res, 'Server error.')
  }
}

const getAnime = async (req, res) => {
  try {
    log.info('getAnime', 'Params: %j', req.query)

    const animeId = req.query.animeId
    if (!animeId) {
      throw new Error('The param animeId is needed.')
    }

    let anime = await animeRepository.getAnime(animeId)
    if(anime){
      log.info('getAnime', 'Found with animeId %j.', animeId)
      const baseUrl = utils.getBaseUrl(req)
      jsonResponse.success(res, jsonify(anime, baseUrl))
    } else{
      log.info('getAnime', 'Not found with animeId %j.', animeId)
      jsonResponse.error(res, 'Not found.', 404)
    }
  } catch (err) {
    log.error('getAnimes', err)
    jsonResponse.error(res, 'Server error.')
  }
}

const jsonify = (anime, baseUrl = '') => {
  const json = {
    id: anime._id,
    desc: anime.desc,
    title: anime.title
  }

  if (anime.image && anime.image.startsWith('/')) {
    // Locally hosted image. Return full url.
    anime.image = baseUrl + anime.image
  }

  return json
}

module.exports = {
  getAnimes: getAnimes,
  getAnime: getAnime,
}
