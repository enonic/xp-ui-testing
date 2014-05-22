var content = portal.content;
var pageRegions = portal.pageRegions;
var contents = system.contentService.getRootContent();
var editMode = portal.request.mode == 'edit';
var site = portal.siteContent;

var params = {
	context: portal,
	pageRegions: pageRegions,
	mainRegion: pageRegions.getRegion("main"),
	contents: contents,
	editable: editMode,
	banner: false,
    site: site
};

var body = system.thymeleaf.render('view/page.html', params);

portal.response.contentType = 'text/html';
portal.response.body = body;

