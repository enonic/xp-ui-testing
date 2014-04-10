var component = portal.component;
var componentPath = component.path;
var componentType = component.type;
var imageUrl = component != null ? portal.url.createImageByIdUrl(component.image) : '';

var html = '<div data-live-edit-type="'+componentType+'" data-live-edit-component="'+componentPath+'">' +
               '<img style="margin-bottom:15px;width:100%;" src="'+imageUrl+'"/>' +
           '</div>';

portal.response.body = html;
portal.response.contentType = 'text/html';
portal.response.status = 200;
