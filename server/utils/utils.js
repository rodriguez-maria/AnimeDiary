const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');
const {base64encode, base64decode} = require('nodejs-base64');
const config = require('./config')

const getBaseUrl = req => req.protocol + '://' + req.get('host');

const getCursor = skip => base64encode(JSON.stringify({skip: skip, time: Date.now()}));

const decodeCursor = cursor => {
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

const getHash = async original => await bcrypt.hash(original, config.passwordSalt);

const validateHash = async (original, hashed) => await bcrypt.compare(original, hashed);

const generateToken = (id, expiresInSec = 86400) => jwt.sign({id: id}, config.tokenSecret, {expiresIn: expiresInSec});

// TODO: Use additional rules.
const validatePasswordStrength = password => password && password.length > 5;

module.exports = {
    getBaseUrl: getBaseUrl,
    getCursor: getCursor,
    decodeCursor: decodeCursor,
    getHash: getHash,
    validateHash: validateHash,
    generateToken: generateToken,
    validatePasswordStrength: validatePasswordStrength
};