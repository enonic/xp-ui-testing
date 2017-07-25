package com.enonic.wem.uitest

class LoginSpec
    extends BaseGebSpec
{
    def setup()
    {
        go "admin"
        getTestSession();
    }

    def "GIVEN login page When both username and password fields are empty THEN Login Button must not be displayed"()
    {
        expect:
        !$( 'button', class: contains( 'login-button' ) ).isDisplayed();
    }

    def "GIVEN login page AND only username field have value THEN 'Login Button' must not be displayed"()
    {
        when:
        $( 'input', placeholder: 'userid or e-mail' ) << 'user';
        report "login page, username is 'user', password is empty";
        saveScreenshot( "pass_empty" );
        then:
        !$( 'button', class: contains( 'login-button' ) ).isDisplayed();
    }

    def "GIVEN login page is opened AND only password field have value Then 'Login Button' must not be displayed"()
    {
        when:
        $( 'input', placeholder: 'password' ) << 'password';
        report "login page, username is empty, password is 'password'";
        saveScreenshot( "login_empty" )

        then:
        !$( 'button', class: contains( 'login-button' ) ).isDisplayed();
    }

    def "GIVEN login page is opened AND both username and password fields have value THEN 'Login Button' must be displayed"()
    {

        when:
        $( 'input', placeholder: 'userid or e-mail' ) << 'su';
        $( 'input', placeholder: 'password' ) << 'password';
        saveScreenshot( "login_pass" )

        then:
        waitFor { $( 'button', class: contains( 'login-button' ) ).isDisplayed() };
    }

    def "GIVEN login page is opened WHEN wrong username or password have been typed THEN error message should appear"()
    {
        when:
        $( 'input', placeholder: 'userid or e-mail' ) << 'su';
        $( 'input', placeholder: 'password' ) << 'password1';
        $( 'button', class: contains( 'login-button' ) ).click();

        then: "error message should be displayed"
        waitFor { $( 'div.message-container' ).text() == 'Login failed!' }
    }
}
