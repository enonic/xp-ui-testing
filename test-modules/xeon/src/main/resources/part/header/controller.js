var xeon = require('xeon');
var thymeleaf = require('/lib/view/thymeleaf');

function handleGet(req) {
    var component = execute('portal.getComponent');
    var content = execute('portal.getContent');
    var page = content.page;

    var title = xeon.ifEmpty(component.config["title"], content.displayName);
    var text = xeon.ifEmpty(component.config["text"], "");

    var params = {
        context: req,
        component: component,
        title: title,
        text: text
    };

    var view = resolve('/view/header.html');
    var body = thymeleaf.render(view, params);

    return {
        body: body,
        contentType: 'text/html'
    };
}

exports.get = handleGet;
