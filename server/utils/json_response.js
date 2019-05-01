module.exports.success = (res, data, cursor = null) => {
    var to_send = {
        'success': true,
        'data': data
    };

    if (cursor) {
        to_send['cursor'] = cursor;
    }

    res.send(to_send);
};

module.exports.error = (res, message, code = 500) => {
    res.status(code).send({
        'success': false,
        'error': message
    });
};