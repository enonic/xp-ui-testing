var component = portal.component;
var slides = component.config.dataSets("slide");

var params = {
	context: portal,
	component: component,
    slides: slides
};

var body = system.thymeleaf.render('view/banner.html', params);

portal.response.contentType = 'text/html';
portal.response.body = body;

