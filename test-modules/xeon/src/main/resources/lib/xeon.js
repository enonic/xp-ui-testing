var menuService = require('menu');
var contentService = require('contentService');

exports.getLogoUrl = function (req, moduleConfig) {
    var logoContent;
    var logo = moduleConfig['logo'];
    if (logo) {
        logoContent = contentService.getContentById(logo);
    }

    if (logoContent) {
        return execute('portal.imageUrl', {
            id: logoContent.id,
            filter: 'scaleblock(115,26)'
        });
    } else {
        return execute('portal.assetUrl', {
            path: 'images/logo.png'
        });
    }
};

exports.defaultParams = function (req) {
    var editMode = req.mode == 'edit';

    var site = execute('portal.getSite');
    var moduleConfig = site.moduleConfigs['com.enonic.wem.modules.xeon'];
    var content = execute('portal.getContent');

    return {
        context: req,
        mainRegion: content.page.regions["main"],
        menuItems: menuService.getSiteMenu(3),
        editable: editMode,
        banner: false,
        site: site,
        moduleConfig: moduleConfig,
        content: content,
        logoUrl: this.getLogoUrl(req, moduleConfig)
    }
};

exports.merge = function (o1, o2) {
    for (var key in o2) {
        o1[key] = o2[key];
    }
    return o1;
};

exports.ifEmpty = function (string, defaultString) {
    if (Array.isArray(string) && string.length > 0) {
        return string[0] || defaultString;
    }
    return string || defaultString;
};