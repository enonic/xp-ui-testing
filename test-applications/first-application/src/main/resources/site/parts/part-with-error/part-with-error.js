var portal = require('/lib/xp/portal');
var thymeleaf = require('/lib/thymeleaf');
var contentJsLib = require('/lib/jslibraries/content');
var view = resolve('part-with-error.html');

function handlePost(req) {

     throw 'Error';

    var body = thymeleaf.render(view, {});

    return {
        contentType: 'text/html',
        body: body
    };
}

exports.get = handlePost;