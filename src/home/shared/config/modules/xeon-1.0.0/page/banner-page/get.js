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
	contents: system.contentService.getChildContent(site.path),
	editable: editMode,
	banner: true,
	slides: slides,
    site: site,
    content: content,
    logoUrl: getLogoUrl()
};

var body = system.thymeleaf.render('view/page.html', params);

portal.response.contentType = 'text/html';
portal.response.body = body;

function getLogoUrl() {
    var logoContent;
    var logo = site.contentData.getProperty('logo');
    if (logo) {
        logoContent = system.contentService.getContentById(logo.getString());
    }

    if (logoContent) {
        return portal.url.createImageByIdUrl(logoContent.id).filter("scaleblock(115,26)");
    } else {
        return portal.url.createResourceUrl('images/logo.png');
    }
}

