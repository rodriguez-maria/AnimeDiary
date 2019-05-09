const config = require('./config')
const mongoose = require('mongoose')
const log = require('npmlog')

mongoose.connect(config.mongodbConnectionString, { useNewUrlParser: true, useCreateIndex: true })
  .then(() => log.info('db', 'MongoDB connected.'))
  .catch(err => log.error(err))

module.exports = mongoose
