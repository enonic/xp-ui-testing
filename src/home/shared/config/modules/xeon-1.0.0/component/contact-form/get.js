var component = portal.component;
var site = portal.siteContent;

var social = {
    facebook: site.contentData.getProperty('facebook').getString(),
    twitter: site.contentData.getProperty('twitter').getString(),
    linkedin: site.contentData.getProperty('linkedin').getString(),
    google: site.contentData.getProperty('google').getString(),
    pintrest: site.contentData.getProperty('pintrest').getString(),
    youtube: site.contentData.getProperty('youtube').getString()
};

var addresses = site.contentData.dataSets('location');

var data = {
    social: social,
    addresses: addresses
};

var params = {
	context: portal,
	component: component,
    site: site,
    data: data
};

var body = system.thymeleaf.render('view/contact-form.html', params);

portal.response.contentType = 'text/html';
portal.response.body = body;

