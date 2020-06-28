const bcrypt = require('bcrypt')
const jwt = require('jsonwebtoken')
const {
  base64encode,
  base64decode
} = require('nodejs-base64')
const config = require('./config')
const log = require('npmlog')

const getBaseUrl = req => req.protocol + '://' + req.get('host')

const getCursor = skip => base64encode(JSON.stringify({
  skip: skip,
  time: Date.now()
}))

const decodeCursor = cursor => {
  if (!cursor) {
    return 0
  }

  let decoded = base64decode(cursor)
  decoded = JSON.parse(decoded)

  if (!decoded || !decoded.skip) {
    return 0
  }

  return decoded.skip
}

const getHash = async original => bcrypt.hash(original, config.passwordSalt)

const validateHash = async (original, hashed) => bcrypt.compare(original, hashed)

const generateToken = (id, expiresInSec = 86400) => jwt.sign({
  id: id
}, config.tokenSecret, {
  expiresIn: expiresInSec
})

const validateToken = (token) => {
  try {
    return jwt.verify(token, config.tokenSecret)
  } catch (err) {
    log.error('validateToken', err)
    return null
  }
}

// TODO: Use additional rules.
const validatePasswordStrength = password => password && password.length > 5

module.exports = {
  getBaseUrl: getBaseUrl,
  getCursor: getCursor,
  decodeCursor: decodeCursor,
  getHash: getHash,
  validateHash: validateHash,
  generateToken: generateToken,
  validateToken: validateToken,
  validatePasswordStrength: validatePasswordStrength
}