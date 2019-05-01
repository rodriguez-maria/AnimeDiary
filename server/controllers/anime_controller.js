const log = require('npmlog');
const _ = require('underscore');

const Anime = require('../models/anime');
const constants = require('../utils/constants');
const jsonResponse = require('../utils/json_response');
const utils = require('../utils/utils');

exports.getAnimes = async (req, res) => {
    try {
        log.info('getAnimes', 'Params: %j', req.query);

        const search = req.query.search;
        let limit = req.query.limit || 10;
        limit = Math.min(limit, constants.MAX_SEARCH_RESULTS);
        const skip = utils.decodeCursor(req.query.cursor);

        let animes = await Anime.find({title: new RegExp(search, 'i')}, null, {skip: skip, limit: limit});
        animes = _.map(animes, a => {
            if (a.image && a.image.startsWith('/')) {
                // Locally hosted image. Return full url.
                a.image = utils.getBaseUrl(req) + a.image;
            }
            return a;
        });

        log.info('getAnimes', 'Returned %j results.', animes.length);
        jsonResponse.success(res, animes, utils.getCursor(skip + limit));
    } catch (err) {
        console.log(err);
        jsonResponse.error(res, 'Server error.')
    }
};