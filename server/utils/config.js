require('dotenv').config();

const config = {
    port: process.env.PORT,
    mongodbConnectionString: process.env.MONGODB_CONNECTION_STRING,
    passwordSalt: parseInt(process.env.PASSWORD_SALT),
    tokenSecret: process.env.TOKEN_SECRET
};

module.exports = config;