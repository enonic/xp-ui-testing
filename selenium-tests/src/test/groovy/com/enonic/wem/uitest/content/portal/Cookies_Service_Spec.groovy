package com.enonic.wem.uitest.content.portal

import com.enonic.wem.uitest.content.BaseContentSpec
import org.openqa.selenium.Cookie
import spock.lang.Ignore
import spock.lang.Shared

@Ignore
class Cookies_Service_Spec
    extends BaseContentSpec
{
    @Shared
    String COOKIES_SERVICE = "cookies";

    @Shared
    String COOKIE_NAME = "JSESSIONID";

    @Shared
    String APPLICATION_NAME = "com.enonic.xp.ui_testing.contenttypes";

    def "GIVEN existing app-service that returns a cookie WHEN request was sent to service THEN correct cookie should be present in the response"()
    {
        when: "request to service was sent"
        getService( COOKIES_SERVICE, APPLICATION_NAME );
        saveScreenshot( "cookie_service" );

        then: "correct cookie should be present in the response"
        Cookie cookie = driver.manage().getCookieNamed( COOKIE_NAME );
        cookie.getName() == COOKIE_NAME;
    }
}
