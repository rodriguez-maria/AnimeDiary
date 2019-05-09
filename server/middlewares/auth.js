const utils = require('../utils/utils')
const log = require('npmlog')
const jsonResponse = require('../utils/json_response')
const userRepository = require('../repositories/user_repository')

let authorize = async (req, res, next) => {
  let token = req.headers['authorization'] // Express headers are auto converted to lowercase
  if (!token || !token.startsWith('Bearer ')) {
    log.error('authorize', 'Invalid token: %j', token)
    jsonResponse.error(res, 'Unauthorized.', 401)
    return
  }

  // Remove Bearer from token
  token = token.slice(7, token.length)
  token = utils.validateToken(token)
  if (!token || !token['id']) {
    log.error('authorize', 'Invalid decoded token: %j', token)
    jsonResponse.error(res, 'Unauthorized.', 401)
    return
  }

  const user = await userRepository.getUser(token['id'])
  if (!user) {
    log.error('authorize', 'User not found: %j', token)
    jsonResponse.error(res, 'Unauthorized.', 401)
    return
  }

  // Patch request to add auth user info.
  req.authUser = user
  next()
}

module.exports = {
  authorize: authorize
}
