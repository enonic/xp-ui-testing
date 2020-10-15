var portal = require('/lib/xp/portal');
var thymeleaf = require('/lib/thymeleaf');
var contentJsLib = require('/lib/jslibraries/content');
var view = resolve('part-with-error.html');

function handlePost(req) {

    var createResult = JSON.stringify(contentJsLib.create(), null, 4);

    var params = {
        createResult: createResult,

    };

    var body = thymeleaf.render(view, params);

    return {
        contentType: 'text/html',
        body: body
    };
}

exports.get = handlePost;