var xeon = require('/lib/xeon');
var thymeleaf = require('/lib/view/thymeleaf');

function handleGet(req) {
    var params = xeon.defaultParams(req);

    var view = resolve('../../view/page.html');
    var body = thymeleaf.render(view, params);

    return {
        body: body,
        contentType: 'text/html'
    };
}

exports.get = handleGet;
