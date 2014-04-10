function log(text) {
    java.lang.System.out.println(text);
}

var xml = <dummy/>;
var editMode = portal.request.mode == 'edit';
var params = {
    title: portal.content.displayName,
    editable: editMode
};

var body = system.xslt.render('view/frogger.xsl', xml, params);

portal.response.contentType = 'text/html';
portal.response.body = body;
