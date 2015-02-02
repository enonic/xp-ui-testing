var thymeleaf = require('/lib/view/thymeleaf');

function handleGet(req) {
    var component = execute('portal.getComponent');
    var site = execute('portal.getSite');

    var xeonConfig = site.getModuleConfig(req.module.key);

    var social = {
        facebook: xeonConfig.getString('facebook'),
        twitter: xeonConfig.getString('twitter'),
        linkedin: xeonConfig.getString('linkedin'),
        google: xeonConfig.getString('google'),
        pintrest: xeonConfig.getString('pintrest'),
        youtube: xeonConfig.getString('youtube')
    };

    var addresses = site.data.getSets('location');

    var data = {
        social: social,
        addresses: addresses
    };

    var params = {
        context: req,
        component: component,
        site: site,
        data: data
    };

    var view = resolve('/view/contact-form.html');
    var body = thymeleaf.render(view, params);

    return {
        body: body,
        contentType: 'text/html'
    };
}

exports.get = handleGet;

