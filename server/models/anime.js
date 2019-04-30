const mongoose = require('mongoose')

const schema = new mongoose.Schema({
    _id: String,
    desc: String,
    title: String,
    image: String,
});
schema.index({title: 1});

module.exports = mongoose.model('anime', schema)