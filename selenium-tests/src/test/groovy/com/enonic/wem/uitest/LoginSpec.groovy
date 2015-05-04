package com.enonic.wem.uitest

import com.enonic.autotests.utils.TestUtils
import spock.lang.Ignore

class LoginSpec
    extends BaseGebSpec
{
    def setup()
    {
        go "admin"
        getTestSession();

    }

    def "Given login page When both username and password fields is empty Then Login Button must be disabled"()
    {
        when:
        go "admin"

        then:
        $( 'button.login-button' ).getAttribute( "style" ).contains( 'display: none' );
    }


    def "Given login page When only username field have value Then Login Button must be disabled"()
    {
        given:
        go "admin"

        when:
        $( 'input.form-item', 0 ) << 'user';
        report "login page, username is 'user', password is empty";
        TestUtils.saveScreenshot( getSession(), "pass_empty" );
        then:
        $( 'button.login-button' ).getAttribute( "style" ).contains( 'display: none' );
    }


    def "Given login page When only password field have value Then Login Button must be disabled"()
    {
        given:
        go "admin"

        when:
        $( 'input.form-item', 1 ) << 'password';
        report "login page, username is empty, password is 'password'";
        TestUtils.saveScreenshot( getSession(), "login_empty" )

        then:
        $( 'button.login-button' ).getAttribute( "style" ).contains( 'display: none' );
    }

    def "Given login page When both username and password fields have value Then Login Button must be enabled"()
    {

        when:
        $( 'input.input-view', 0 ) << 'admin';
        $( 'input.input-view', 1 ) << 'password';
        TestUtils.saveScreenshot( getSession(), "login_pass" )

        then:
        !$( 'button.login-button' ).getAttribute( "style" ).contains( 'display: none' );
    }

    @Ignore
    def "Given login page When wrong username or password typed Then error message appears"()
    {
        when:
        $( 'input.form-item', 0 ) << 'admin';
        $( 'input.form-item', 1 ) << 'password';

        then:
        waitFor { $( 'div.message-container' ).text() == 'Login failed!' }

    }


}
