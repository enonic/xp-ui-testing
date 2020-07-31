exports.create = function () {

    //Documentation BEGIN
    var contentLib = require('/lib/xp/content');

    // var result = contentLib.create({
    //     name: '_attachments',
    //     parentPath: "/",
    //     displayName: '_attachments',
    //     draft: true,
    //     contentType: 'base:folder',
    //     data: {},
    //     branch: 'draft'
    // });

    var result = contentLib.create({
        name: 'myContent',
        parentPath: '/',
        displayName: 'My Content',
        contentType: app.name + ':date0_0',
        data: {
            "date": ["2020-06-02", "2020-06-03", "2022-10-03"]
        }
    });

    log.info('Content created with id ' + result._id);
    //Documentation END

    log.info('Create result: ' + JSON.stringify(result, null, 4));

    return result;
};


exports.get = function () {

    //Documentation BEGIN
    var contentLib = require('/lib/xp/content');

    var result = contentLib.get({
        key: '/features/js-libraries/mycontent'
    });

    if (result) {
        log.info('Display Name = ' + result.displayName);
    } else {
        log.info('Content was not found');
    }
    //Documentation END

    log.info('Get result: ' + JSON.stringify(result, null, 4));

    return result;
};

exports.exists = function (key) {

    //Documentation BEGIN
    var contentLib = require('/lib/xp/content');

    var result = contentLib.exists({
        key: key
    });

    if (result) {
        log.info('Content found');
    } else {
        log.info('Content was not found');
    }
    //Documentation END

    return result;
};

exports.getChildren = function () {

    //Documentation BEGIN
    var contentLib = require('/lib/xp/content');

    var result = contentLib.getChildren({
        key: '/features/js-libraries/houses',
        start: 0,
        count: 2,
        sort: '_modifiedTime ASC'
    });

    log.info('Found ' + result.total + ' number of contents');

    for (var i = 0; i < result.hits.length; i++) {
        var content = result.hits[i];
        log.info('Content ' + content._name + ' loaded');
    }
    //Documentation END

    log.info('Get children result: ' + JSON.stringify(result, null, 4));

    return result;
};

exports.query = function () {

    //Documentation BEGIN
    var contentLib = require('/lib/xp/content');

    var result = contentLib.query({
        start: 0,
        count: 2,
        sort: "modifiedTime DESC, geoDistance('data.location', '59.91,10.75')",
        query: "data.city = 'Oslo' AND fulltext('data.description', 'garden', 'AND') ",
        contentTypes: [
            app.name + ":house",
            app.name + ":apartment"
        ],
        aggregations: {
            floors: {
                terms: {
                    field: "data.number_floor",
                    order: "_count asc"
                },
                aggregations: {
                    prices: {
                        histogram: {
                            field: "data.price",
                            interval: 1000000,
                            extendedBoundMin: 1000000,
                            extendedBoundMax: 3000000,
                            minDocCount: 0,
                            order: "_key desc"
                        }
                    }
                }
            },
            by_month: {
                dateHistogram: {
                    field: "data.publish_date",
                    interval: "1M",
                    minDocCount: 0,
                    format: "MM-yyyy"
                }
            },
            price_ranges: {
                range: {
                    field: "data.price",
                    ranges: [
                        {to: 2000000},
                        {from: 2000000, to: 3000000},
                        {from: 3000000}
                    ]
                }
            },
            my_date_range: {
                dateRange: {
                    field: "data.publish_date",
                    format: "MM-yyyy",
                    ranges: [
                        {to: "now-10M/M"},
                        {from: "now-10M/M"}
                    ]
                }
            },
            price_stats: {
                stats: {
                    field: "data.price"
                }
            }
        },
        highlight: {
            properties: {
                "data.city": {},
                "data.description": {
                    preTag: "<b>",
                    postTag: "</b>"
                }
            }
        }
    });

    log.info('Found ' + result.total + ' number of contents');

    for (var i = 0; i < result.hits.length; i++) {
        var content = result.hits[i];
        log.info('Content ' + content._name + ' found');
    }
    //Documentation END

    log.info('Query result: ' + JSON.stringify(result, null, 4));

    return result;
};



exports.modify = function () {

    //Documentation BEGIN
    function editor(c) {
        c.displayName = 'Modified';
        c.language = 'en';
        c.data.myCheckbox = false;
        c.data["myTime"] = "11:00";
        c.data.checkOptionSet = {
            _selected: ["option_2"],
            option_2: {
                contentSelector: '5a5fc786-a4e6-4a4d-a21a-19ac6fd4784b'
            }
        };
        return c;
    }

    var contentLib = require('/lib/xp/content');

    var result = contentLib.modify({
        key: '/features/js-libraries/mycontent',
        editor: editor
    });

    if (result) {
        log.info('Content modified. New title is ' + result.displayName);
    } else {
        log.info('Content not found');
    }
    //Documentation END

    log.info('Modify result: ' + JSON.stringify(result, null, 4));

    return result;
};

exports.setPermissions = function () {

    //Documentation BEGIN
    var contentLib = require('/lib/xp/content');

    var result = contentLib.setPermissions({
        key: '/features/js-libraries/mycontent',
        inheritPermissions: false,
        overwriteChildPermissions: true,
        permissions: [{
            principal: 'user:system:anonymous',
            allow: ['READ'],
            deny: []
        }]
    });

    if (result) {
        log.info('Content permissions updated.');
    } else {
        log.info('Content not found');
    }
    //Documentation END

    log.info('SetPermissions result: ' + JSON.stringify(result, null, 4));

    return result;
};

exports.getPermissions = function () {

    //Documentation BEGIN
    var contentLib = require('/lib/xp/content');

    var result = contentLib.getPermissions({
        key: '/features/js-libraries/mycontent'
    });

    if (result) {
        log.info('Content inherits permissions: ' + result.inheritPermissions);
    } else {
        log.info('Content not found');
    }
    //Documentation END

    log.info('GetPermissions result: ' + JSON.stringify(result, null, 4));

    return result;
};

exports.publish = function () {
    var contentLib = require('/lib/xp/content');
    var result = contentLib.publish({
        keys: ['/features/js-libraries/mycontent'],
        sourceBranch: 'draft',
        targetBranch: 'master'
    });
    if (result) {
        log.info('Pushed ' + result.pushedContents.length + " content.");
        log.info('Deleted ' + result.deletedContents.length + " content.");
        log.info('Content that failed operation: ' + result.failedContents.length);
    } else {
        log.info('Operation failed.');
    }
    return result;
};
