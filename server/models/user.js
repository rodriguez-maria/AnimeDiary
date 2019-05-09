const db = require('../utils/db')

const schema = new db.Schema({
  name: String,
  user_name: { type: String, unique: true },
  password_hash: String
})
schema.index({ user_name: 1 })

module.exports = db.model('user', schema)
