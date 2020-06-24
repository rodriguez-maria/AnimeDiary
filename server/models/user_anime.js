const db = require('../utils/db')

const schema = new db.Schema({
  anime: { type: String, ref: 'anime' },
  user: { type: db.Schema.Types.ObjectId, ref: 'user' },
  notes: String,
  rating: Number,
  tags: [String],
  search: String,
})
schema.index({ anime: 1, user: 1, tags: 1})

module.exports = db.model('user_anime', schema)
