var libs = {
    portal: require('/lib/xp/portal'),
    util: require('/lib/enonic/util')
};

var appNamePath = libs.util.app.getJsonName();
var mixinPath = 'meta-data';

function getConfig() {
    return libs.portal.getSiteConfig();
}

function commaStringToArray(str) {
    var commas = str || '';
    var arr = commas.split(',');
    if (arr) {
        arr = arr.map(function (s) {
            return s.trim()
        });
    } else {
        arr = libs.util.data.forceArray(str); // Make sure we always work with an array
    }
    return arr;
}

function findValueInJson(json, paths) {
    var value = null;
    var pathLength = paths.length;
    var jsonPath = ";"

    for (var i = 0; i < pathLength; i++) {
        if (paths[i]) {
            jsonPath = 'json.data["' + paths[i].split('.').join('"]["') + '"]'; // Wrap property so we can have dashes in it
            try {
                value = eval(jsonPath);
            } catch (e) {
                // Noop
            }
            if (value) {
                if (value.trim() === "") {
                    value = null;
                }// Reset value if empty string (skip empties)
                else {
                    break;
                } // Expect the first property in the string is the most important one to use
            } // if value
        } // if paths[i]
    } // for
    return value;
} // function findValueInJson

function isString(o) {
    return typeof o === 'string' || o instanceof String;
}

function stringOrNull(o) {
    return isString(o) ? o : null;
}


exports.getBlockRobots = function (content) {
    var setInMixin = content.x[appNamePath]
                     && content.x[appNamePath][mixinPath]
                     && content.x[appNamePath][mixinPath].blockRobots;
    return setInMixin;
};

exports.getPageTitle = function (content, site) {
    var siteConfig = getConfig();

    var setInMixin = content.x[appNamePath]
                     && content.x[appNamePath][mixinPath]
                     && content.x[appNamePath][mixinPath].seoTitle;

    var userDefinedPaths = siteConfig.pathsTitles;
    var userDefinedArray = userDefinedPaths ? commaStringToArray(userDefinedPaths) : [];
    var userDefinedValue = userDefinedPaths ? findValueInJson(content, userDefinedArray) : null;


    var metaTitle = setInMixin ? stringOrNull(content.x[appNamePath][mixinPath].seoTitle) // Get from mixin
        : stringOrNull(userDefinedValue) // json property defined by user as important
          || stringOrNull(content.data.title) || stringOrNull(content.data.heading) || stringOrNull(content.data.header) // Use other typical content titles (overrides displayName)
          || stringOrNull(content.displayName) // Use content's display name
          || stringOrNull(siteConfig.seoTitle) // Use default og-title for site
          || stringOrNull(site.displayName) // Use site default
          || ''

    return metaTitle;
};

exports.getMetaDescription = function (content, site) {
    var siteConfig = getConfig();

    var userDefinedPaths = siteConfig.pathsDescriptions;
    var userDefinedArray = userDefinedPaths ? commaStringToArray(userDefinedPaths) : [];
    var userDefinedValue = userDefinedPaths ? findValueInJson(content, userDefinedArray) : null;

    var setWithMixin = content.x[appNamePath]
                       && content.x[appNamePath][mixinPath]
                       && content.x[appNamePath][mixinPath].seoDescription;
    var metaDescription = setWithMixin ? content.x[appNamePath][mixinPath].seoDescription // Get from mixin
        : userDefinedValue
          || content.data.preface || content.data.description || content.data.summary // Use typical content summary names
          || siteConfig.seoDescription // Use default for site
          || site.description // Use bottom default
          || ''; // Don't crash plugin on clean installs

    // Strip away all html tags, in case there's any in the description.
    var regex = /(<([^>]+)>)/ig;
    metaDescription = metaDescription.replace(regex, "");

    return metaDescription;
};

exports.getOpenGraphImage = function (content, defaultImg, defaultImgPrescaled) {
    var siteConfig = getConfig();

    var userDefinedPaths = siteConfig.pathsImages || '';
    var userDefinedArray = userDefinedPaths ? commaStringToArray(userDefinedPaths) : [];
    var userDefinedValue = userDefinedPaths ? findValueInJson(content, userDefinedArray) : null;

    var ogImage;

    // Try to find an image in the content's image or images properties
    var imageArray = libs.util.data.forceArray(userDefinedValue || content.data.image || content.data.images || []);

    if (imageArray.length || (defaultImg && !defaultImgPrescaled)) {
        // Set basic image options
        var imageOpts = {
            scale: 'block(1200,630)', // Open Graph requires 600x315 for landscape format. Double that for retina display.
            quality: 85,
            format: 'jpg',
            type: 'absolute'
        };

        // Set the ID to either the first image in the set or use the default image ID
        imageOpts.id = imageArray.length ? imageArray[0] : defaultImg;

        ogImage = imageOpts.id ? libs.portal.imageUrl(imageOpts) : null;
    } else if (defaultImg && defaultImgPrescaled) {
        // Serve pre-optimized image directly
        ogImage = libs.portal.attachmentUrl({
            id: defaultImg,
            type: 'absolute'
        });
    }

    // Return the image URL or nothing
    return ogImage;
};
