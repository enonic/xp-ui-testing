package com.enonic.wem.uitest

import com.enonic.autotests.utils.TestUtils

class LoginSpec
    extends BaseGebSpec
{
    def setup()
    {
        go "admin"
        getTestSession();
    }

    def "Given login page When both username and password fields is empty Then Login Button must not  be disabled"()
    {
        expect:
        !$( 'button', class: 'button login-button' ).isDisplayed();
    }

    def "Given login page When only username field have value Then Login Button must not be disabled"()
    {
        when:
        $( 'input', placeholder: 'userid or e-mail' ) << 'user';
        report "login page, username is 'user', password is empty";
        TestUtils.saveScreenshot( getSession(), "pass_empty" );
        then:
        !$( 'button', class: 'button login-button' ).isDisplayed();
    }

    def "Given login page When only password field have value Then Login Button must not  be disabled"()
    {
        when:
        $( 'input', placeholder: 'password' ) << 'password';
        report "login page, username is empty, password is 'password'";
        TestUtils.saveScreenshot( getSession(), "login_empty" )

        then:
        !$( 'button', class: 'button login-button' ).isDisplayed();
    }

    def "Given login page When both username and password fields have value Then Login Button must be enabled"()
    {

        when:
        $( 'input', placeholder: 'userid or e-mail' ) << 'su';
        $( 'input', placeholder: 'password' ) << 'password';
        TestUtils.saveScreenshot( getSession(), "login_pass" )

        then:
        $( 'button', class: 'button login-button' ).isDisplayed();
    }

    //there was issue: XP-1432
    def "Given login page When wrong username or password typed Then error message appears"()
    {
        when:
        $( 'input', placeholder: 'userid or e-mail' ) << 'su';
        $( 'input', placeholder: 'password' ) << 'password1';
        $( 'button', class: 'button login-button' ).click();

        then:
        waitFor { $( 'div.message-container' ).text() == 'Login failed!' }
    }
}
