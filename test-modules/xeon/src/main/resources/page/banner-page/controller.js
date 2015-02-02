var xeon = require('/lib/xeon');
var thymeleaf = require('/lib/view/thymeleaf');

function handleGet(req) {
    var page = execute('portal.getContent').page;
    var slides = page ? page.config && page.config.slide : [];
    slides = [].concat(slides); // ensure it's an array, even if single element

    var pageParams = {
        slides: slides,
        banner: true
    };
    var params = xeon.merge(xeon.defaultParams(req), pageParams);

    var view = resolve('../../view/page.html');
    var body = thymeleaf.render(view, params);

    return {
        body: body,
        contentType: 'text/html'
    };
}

exports.get = handleGet;
