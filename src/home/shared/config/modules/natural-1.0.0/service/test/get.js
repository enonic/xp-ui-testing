var parentList = portal.request.params['parent'];

var contents = null;

if (parentList) {
    contents = system.contentService.getChildContent(parentList.get(0));
}
else {
    contents = system.contentService.getRootContent();
}

var params = {
    title: 'Hello Dynamic :-)',
    contents: contents,
    context: portal
};

var body = system.thymeleaf.render('view/sample.html', params);

portal.response.contentType = 'text/html';
portal.response.body = body;

