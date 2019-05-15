module.exports.success = (res, data, cursor = null) => {
  let toSend = {
    'success': true,
    'data': data
  }

  if (cursor) {
    toSend['cursor'] = cursor
  }

  res.send(toSend)
}

module.exports.error = (res, message, code = 500) => {
  res.status(code).send({
    'success': false,
    'error': message
  })
}
