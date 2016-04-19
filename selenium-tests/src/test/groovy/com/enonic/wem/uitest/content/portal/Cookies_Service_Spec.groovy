package com.enonic.wem.uitest.content.portal

import com.enonic.autotests.utils.TestUtils
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Shared

class Cookies_Service_Spec
    extends BaseContentSpec
{
    @Shared
    String COOKIES_SERVICE = "cookies";

    @Shared
    String COOKIE_NAME = "JSESSIONID";

    @Shared
    String APPLICATION_NAME = ALL_CONTENT_TYPES_APP_NAME;

    def "GIVEN existing app-service that returns a cookie WHEN request to service was sent THEN correct cookie present in the response"()
    {
        when: "request to service was sent"
        getService( COOKIES_SERVICE, APPLICATION_NAME );
        TestUtils.saveScreenshot( getSession(), "cookie_service" );

        then: "correct cookie present in the response"
        driver.manage().getCookieNamed( COOKIE_NAME ) != null;

        and:
        driver.manage().getCookies().size() == 1;
    }
}
