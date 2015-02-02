function handleGet(req) {
    var component = execute('portal.getComponent');
    var relatedPersonsId = component.config['person'] || [];
    relatedPersonsId = [].concat(relatedPersonsId);
    var persons = [];

    var defaultPersonImageUrl = execute('portal.assetUrl', {path: 'images/team1.jpg'});

    relatedPersonsId.forEach(function (relatedPersonId) {
        var personData = execute('content.get', {key: relatedPersonId});

        var imageContentId = personData.data['image'];
        var imageContentUrl = imageContentId ?
                              execute('portal.imageUrl', {
                                  id: imageContentId,
                                  filter: 'scaleblock(400,400)'
                              }) :
                              defaultPersonImageUrl;

        var personName = [
            personData.data['first-name'],
            personData.data['middle-name'],
            personData.data['last-name']
        ].join(' ').trim();

        var personTitle = personData.data['job-title'];

        persons.push({
            name:  personName || 'Test Testesen',
            title: personTitle || 'Sjefen over alle sjefer',
            image: imageContentUrl
        });
    });

    var data = {
        title: component.config['title'] || 'Please configure',
        text:  component.config['text'] || '',
        persons: persons
    };

    var params = {
        context: req,
        component: component,
        data: data
    };


    var body = execute('thymeleaf.render', {
        view: resolve('/view/person-list.html'),
        model: params
    });

    return {
        body: body,
        contentType: 'text/html'
    };
}

exports.get = handleGet;
