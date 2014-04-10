var editMode = portal.request.mode == 'edit';

var content = portal.content;
var component = portal.component;

var body = system.mustache.render('view/trampoline-buy.html', {
    title: content.displayName,
    path: content.path,
    name: content.name,
    editable: editMode,
    resourcesPath: portal.url.createResourceUrl(''),
    component: component
});


portal.response.body = body;
portal.response.contentType = 'text/html';
portal.response.status = 200;
