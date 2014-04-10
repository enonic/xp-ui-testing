var editMode = portal.request.mode == 'edit';

var content = portal.content;
var component = portal.component;
var layoutRegions = portal.layoutRegions;

var body = system.mustache.render('view/trampoline-70-30.html', {
    title: content.displayName,
    path: content.path,
    name: content.name,
    editable: editMode,
    resourcesPath: portal.url.createResourceUrl(''),
    component: component,
    leftRegion: layoutRegions.getRegion("left"),
    rightRegion: layoutRegions.getRegion("right")
});


portal.response.body = body;
portal.response.contentType = 'text/html';
portal.response.status = 200;
