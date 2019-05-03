const User = require('../models/user');

const getUser = async (id = '', userName = '') => {
    // id takes preference.
    if (id) {
        return await User.findById(id);
    }

    if (userName) {
        return await User.findOne({user_name: userName});
    }

    return null;
};

const createUser = async (userName, passwordHash, name = '') => {
    if (!userName || !passwordHash) {
        return null;
    }

    // Db would ensure unique username.
    return await User.create({
        name: name,
        user_name: userName,
        password_hash: passwordHash
    });
};

module.exports = {
    getUser: getUser,
    createUser: createUser
};