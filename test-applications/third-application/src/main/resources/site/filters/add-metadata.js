var libs = {
    portal: require('/lib/xp/portal'),
    thymeleaf: require('/lib/thymeleaf'),
    util: require('/lib/enonic/util'),
    local: require('/lib/local')
};

var view = resolve('add-metadata.html');

// Format locale into the ISO format that Open Graph wants
var localeMap = {
    da: 'da_DK',
    sv: 'sv_SE',
    pl: 'pl_PL',
    no: 'nb_NO',
    en: 'en_US'
};

exports.responseFilter = function (req, res) {
    var site = libs.portal.getSite();
    var content = libs.portal.getContent();
    var siteConfig = libs.portal.getSiteConfig();

    var lang = content.language || site.language || 'en';
    var frontpage = site._path === content._path;
    var pageTitle = libs.local.getPageTitle(content, site);

    // Concat site title? Trigger if set to true in settings, or if not set at all (default = true)
    var titleAppendix = '';
    if (siteConfig.titleBehaviour || !siteConfig.hasOwnProperty("titleBehaviour")) {
        var separator = siteConfig.titleSeparator || '-';
        var titleRemoveOnFrontpage = siteConfig.hasOwnProperty("titleFrontpageBehaviour") ? siteConfig.titleFrontpageBehaviour : true; // Default true needs to be respected
        if (!frontpage || !titleRemoveOnFrontpage) {
            titleAppendix = ' ' + separator + ' ' + site.displayName;
        }
    }

    var siteVerification = siteConfig.siteVerification || null;

    var url = libs.portal.pageUrl({path: content._path, type: "absolute"});
    var isFrontpage = site._path === content._path;
    var fallbackImage = siteConfig.seoImage;
    var fallbackImageIsPrescaled = siteConfig.seoImageIsPrescaled;
    if (isFrontpage && siteConfig.frontpageImage) {
        fallbackImage = siteConfig.frontpageImage;
        fallbackImageIsPrescaled = siteConfig.frontpageImageIsPrescaled;
    }
    var image = libs.local.getOpenGraphImage(content, fallbackImage, fallbackImageIsPrescaled);

    var params = {
        title: pageTitle,
        description: libs.local.getMetaDescription(content, site),
        siteName: site.displayName,
        locale: localeMap[lang] || localeMap.en,
        type: isFrontpage ? 'website' : 'article',
        url: url,
        image: image,
        imageWidth: 1200, // Twice of 600x315, for retina
        imageHeight: 630,
        blockRobots: siteConfig.blockRobots || libs.local.getBlockRobots(content),
        siteVerification: siteVerification,
        canonical: siteConfig.canonical,
        twitterUserName: siteConfig.twitterUsername

    };

    var metadata = libs.thymeleaf.render(view, params);

    // Force arrays since single values will be return as string instead of array
    res.pageContributions.headEnd = libs.util.data.forceArray(res.pageContributions.headEnd);
    res.pageContributions.headEnd.push(metadata);

    // Handle injection of title - use any existing tag by replacing its content.
    var titleHtml = '<title>' + pageTitle + titleAppendix + '</title>';
    var titleAdded = false;
    if (res.contentType === 'text/html') {
        if (res.body) {
            if (typeof res.body === 'string') {
                // Find a title in the html and use that instead of adding our own title
                var hasIndex = res.body.indexOf('<title>') > -1;
                if (hasIndex) {
                    res.body = res.body.replace(/(<title>)(.*?)(<\/title>)/i, titleHtml);
                    titleAdded = true;
                }
            }
        }
    }
    if (!titleAdded) {
        res.pageContributions.headEnd.push(titleHtml);
    }

    if (req.params) {
        if (req.params.debug === 'true') {
            res.applyFilters = false; // Skip other filters
        }
    }

    return res;
};
