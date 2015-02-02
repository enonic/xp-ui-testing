var thymeleaf = require('/lib/view/thymeleaf');

function handleGet(req) {
    var component = execute('portal.getComponent');

    var params = {
        context: req,
        component: component
    };

    var view = resolve('/view/portfolio.html');
    var body = thymeleaf.render(view, params);

    return {
        body: body,
        contentType: 'text/html'
    };
}

exports.get = handleGet;

