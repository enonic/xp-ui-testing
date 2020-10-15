exports.create = function () {

    //Documentation BEGIN
    var contentLib = require('/lib/xp/content');

    var result = contentLib.create({
        name: 'myContent',
        parentPath: '/',
        displayName: 'My Content',
        contentType: app.name + ':date0_00',
        data: {
            "date": ["2020-06-02", "2020-06-03", "2022-10-03"]
        }
    });
    log.info('Content created with id ' + result._id);
    log.info('Create result: ' + JSON.stringify(result, null, 4));
    return result;
};







