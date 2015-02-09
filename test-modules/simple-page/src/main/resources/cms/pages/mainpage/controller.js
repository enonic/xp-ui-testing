var thymeleaf = require('/cms/lib/view/thymeleaf');

exports.get = function(req) {

    var site = execute('portal.getSite');
   
    var content = execute('portal.getContent');  

    var params = {
       
        mainRegion: content.page.regions["main"]  
       
    }

    var view = resolve('../../view/mainpage.html');
   

   var body = thymeleaf.render(view,params);

    return {
        body: body,
        contentType: 'text/html'
    };
};

