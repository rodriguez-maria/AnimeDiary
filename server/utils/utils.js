const {base64encode, base64decode} = require('nodejs-base64');

module.exports.getBaseUrl = req => req.protocol + '://' + req.get('host');

module.exports.getCursor = skip => base64encode(JSON.stringify({skip: skip, time: Date.now()}));
module.exports.decodeCursor = cursor => {
    if (!cursor) {
        return 0;
    }

    let decoded = base64decode(cursor);
    decoded = JSON.parse(decoded);

    if (!decoded || !decoded.skip) {
        return 0;
    }

    return decoded.skip;
};