const log = require('npmlog')

exports.home = (req, res) => {
  log.info('home', 'Params: %j', req.query)
  res.status(200).send('Welcome to Anime Diary!').end()
}