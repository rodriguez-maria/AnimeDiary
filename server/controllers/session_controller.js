const log = require('npmlog')

const userRepository = require('../repositories/user_repository')
const jsonResponse = require('../utils/json_response')
const utils = require('../utils/utils')

const login = async (req, res) => {
  try {
    log.info('login', 'Body: %j', req.body)

    const userName = req.body.userName
    const password = req.body.password
    if (!userName || !password) {
      throw new Error(`Both userName and password needed. Username=${userName}, Password=${!!password}`)
    }

    let user = await userRepository.getUser(null, userName)
    if (!user) {
      throw new Error(`User not found with userName=${userName}.`)
    }

    if (!await utils.validateHash(password, user.password_hash)) {
      throw new Error(`Wrong password for userName=${userName}.`)
    }

    // User authenticated at this point.
    const token = utils.generateToken(user._id)
    user = jsonify(user, token)

    log.info('login', user)
    jsonResponse.success(res, user)
  } catch (err) {
    log.error('login', err)
    jsonResponse.error(res, 'Invalid Username/Password.', 401)
  }
}

const register = async (req, res) => {
  try {
    log.info('register', 'Body: %j', req.body)

    const name = req.body.name
    const userName = req.body.userName
    let password = req.body.password
    if (!userName || !password) {
      throw new Error('Both Username and password are needed.')
    }

    let user = await userRepository.getUser(null, userName)
    if (user) {
      throw new Error('Username is not available.')
    }
/*
    if (utils.validatePasswordStrength(password)) {
      throw new Error('Password is weak.')
    }
*/
    // Ready to create a new user.
    password = await utils.getHash(password)
    user = await userRepository.createUser(userName, password, name)
    if (!user) {
      throw new Error('Unable to create a user.')
    }
    const token = utils.generateToken(user._id)
    user = jsonify(user, token)

    log.info('register', user)
    jsonResponse.success(res, user)
  } catch (err) {
    log.error('register', err)
    jsonResponse.error(res, err.message, 400)
  }
}

const jsonify = (user, token = '') => {
  return {
    id: user._id,
    name: user.name,
    userName: user.user_name,
    token: token
  }
}

module.exports = {
  login: login,
  register: register
}
