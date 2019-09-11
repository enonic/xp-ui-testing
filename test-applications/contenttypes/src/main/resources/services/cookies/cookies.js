var stk = require('/lib/stk/stk');

exports.get = function (req) {
    return {
        status: 200,
        body: "Hello World",
        cookies: {
            "plain": "value"
        }
    };
};
