var portal = require('/lib/xp/portal');
var contentSvc = require('/lib/xp/content');

function isInteger(x) {
    return Math.round(x) === x;
}

function isMenuItem(content) {
    var extraData = content.x;
    if (!extraData) {
        return false;
    }
    // var extraDataModule = extraData['com-enonic-xp-modules-features'];
    // if (!extraDataModule || !extraDataModule['menu-item']) {
    //     return false;
    // }
    //var menuItemMetadata = extraDataModule['menu-item'] || {};
    var menuItemMetadata = {};
    return menuItemMetadata.menuItem;
}

function getChildMenuItems(parentContent, levels) {
    var childrenResult = contentSvc.getChildren({
        key: parentContent._id,
        count: 100
    });

    levels--;

    var childMenuItems = [];


    childrenResult.hits.forEach(function (child) {
        if (isMenuItem(child)) {
            childMenuItems.push(menuItemToJson(child, levels));
        }
    });

    return childMenuItems;
}

function menuItemToJson(content, levels) {
    var subMenus = [];
    if (levels > 0) {
        subMenus = getChildMenuItems(content, levels);
    }

    return {
        displayName: content.displayName,
        //menuName: content.x['com-enonic-xp-modules-features']['menu-item'].menuName,
        path: content._path,
        name: content._name,
        id: content._id,
        hasChildren: subMenus.length > 0,
        children: subMenus
    };
}

exports.getSiteMenu = function (levels) {
    levels = (isInteger(levels) ? levels : 1);
    var site = portal.getSite();
    if (!site) {
        return [];
    }

    return getChildMenuItems(site, levels);
};
