var xml =
    <mydoc>
        <content>Hello</content>
    </mydoc>;

var params = {
};

var body = system.xslt.render('view/sample1.xsl', xml, params);

portal.response.contentType = 'text/html';
portal.response.body = body;

