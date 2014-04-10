var editMode = portal.request.mode == 'edit';

var content = portal.content;
var pageRegions = portal.pageRegions;
var body = system.mustache.render('view/frogger.html', {
    baseUrl: portal.url.baseUrl,
    title: content.displayName,
    path: content.path,
    name: content.name,
    editable: editMode,
    resourcesPath: portal.url.createResourceUrl(''),
    mainRegion: pageRegions.getRegion("main"),
    leftRegion: pageRegions.getRegion("left"),
    rightRegion: pageRegions.getRegion("right")
});

portal.response.body = body;
portal.response.contentType = 'text/html';
portal.response.status = 200;
