var content = portal.content;
var page = portal.content.page;
var pageRegions = portal.pageRegions;
var site = portal.siteContent;
var editMode = portal.request.mode == 'edit';
var slides = page.config.dataSets("slide");

var params = {
	context: portal,
	pageRegions: pageRegions,
	mainRegion: pageRegions.getRegion("main"),
	contents: system.contentService.getRootContent(),
	editable: editMode,
	banner: true,
	slides: slides,
    site: site
};

var body = system.thymeleaf.render('view/page.html', params);

portal.response.contentType = 'text/html';
portal.response.body = body;

