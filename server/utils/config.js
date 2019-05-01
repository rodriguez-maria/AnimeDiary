require('dotenv').config();

const config = {
    port: process.env.PORT,
    mongodbConnectionString: process.env.MONGODB_CONNECTION_STRING
};

module.exports = config;