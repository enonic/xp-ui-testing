exports.view = {};
var thymeleaf = require('/lib/thymeleaf');

// Render Thymeleaf view
exports.view.render = function(view, params) {
    return {
        body: thymeleaf.render(view, params)
    };
};