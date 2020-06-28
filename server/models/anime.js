const db = require('../utils/db')

const schema = new db.Schema({
  _id: String,
  desc: String,
  title: String,
  image: String
})
schema.index({
  title: 1
})

module.exports = db.model('anime', schema)