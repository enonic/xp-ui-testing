var editMode = portal.request.mode == 'edit';

var content = portal.content;
var component = portal.component;

var body = system.mustache.render('view/trampoline-image.html', {
    title: content.displayName,
    path: content.path,
    name: content.name,
    editable: editMode,
    resourcesPath: portal.url.createResourceUrl(''),
    content: content,
    component: component,
    imageUrl: component.image != null ? portal.url.createImageByIdUrl(component.image) : null
});


portal.response.body = body;
portal.response.contentType = 'text/html';
portal.response.status = 200;
