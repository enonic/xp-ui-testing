var component = portal.component;
var content = portal.content;
var page = portal.content.page;
var relatedPerson = component.config.getProperty('person');
var person,personData;

if (content.type == 'person') {
    personData = content;
} else {
    var personId;
    if (relatedPerson) {
        personId = relatedPerson.getString();
        if (personId) {
            personData = system.contentService.getContentById(personId);
        }
    }
}

if (personData) {
    var imageContent = system.contentService.getContentById(personData.contentData.getProperty('image').getString());
    person = {
        name: personData.contentData.getProperty('first-name').getString() + ' ' + personData.contentData.getProperty('middle-name').getString() + ' ' + personData.contentData.getProperty('last-name').getString(),
        title: personData.contentData.getProperty('job-title').getString(),
        image: portal.url.createImageByIdUrl(imageContent.id).filter("scaleblock(400,400)")
    };
} else {
    person = {
        name: 'Test Testesen',
        title: 'Sjefen over alle sjefer',
        image: portal.url.createResourceUrl('images/team1.jpg')
    };
}

var params = {
	context: portal,
	component: component,
    content: content,
    person: person
};

var body = system.thymeleaf.render('view/person.html', params);

portal.response.contentType = 'text/html';
portal.response.body = body;
