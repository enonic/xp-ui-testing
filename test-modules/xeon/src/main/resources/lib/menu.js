function isInteger(x) {
    return Math.round(x) === x;
}


/**
 * Checks if the content is a menu item.
 * @param {Content} content object obtained with 'portal.getContent', 'portal.getSite' or any 'content.*' commands
 * @return {Boolean} true if the content is marked as menu item
 */
function isMenuItem(content) {
    var meta = content.meta;
    if (!meta) {
        return false;
    }
    var menuItemMetadata = meta['system:menu-item'] || {};
    var menuItemValue = menuItemMetadata['menuItem'];
    return menuItemValue;
}

/**
 * Returns submenus of a parent menuitem.
 * @param {Content} content object obtained with 'portal.getContent', 'portal.getSite' or any 'content.*' commands
 * @param {Integer} The number of submenus to retrieve
 * @return {Array} Array of submenus
 */

function getSubMenus(parentContent, levels) {
    var children = execute('content.getChildren', {
        key: parentContent._id,
        count: 100
    });

    levels--;

    var subMenus = [];
    children.contents.forEach(function (child) {
        if (isMenuItem(child)) {
            subMenus.push(menuItemToJson(child, levels));
        }
    });

    return subMenus;
}

/**
 * Returns JSON data for a menuitem.
 * @param {Content} content object obtained with 'portal.getContent', 'portal.getSite' or any 'content.*' commands
 * @param {Integer} The number of submenus to retrieve
 * @return {Object} Menuitem JSON data
 */

function menuItemToJson(content, levels) {
    var subMenus = [];
    if (levels > 0) {
        subMenus = getSubMenus(content, levels);
    }

    return {
        displayName: content.displayName,
        menuName: content.meta['system:menu-item'].menuName || '',
        path: content._path,
        name: content._name,
        id: content._id,
        hasChildren: subMenus.length > 0,
        children: subMenus
    };
}


exports.getSiteMenu = function (levels) {
    levels = (isInteger(levels) ? levels : 1);
    var site = execute('portal.getSite');

    if (!site) {
        return [];
    }
    var menu = getSubMenus(site, levels);

    log.info('Site Menu: \r\n %s', JSON.stringify(menu, null, 4));

    return menu;
};
