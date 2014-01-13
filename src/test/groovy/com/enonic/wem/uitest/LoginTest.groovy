package com.enonic.wem.uitest

import geb.spock.GebSpec

class LoginTest
    extends GebSpec
{
    def "Enable login button when form is filled out"()
    {
        given:
        go "admin"

        when:
        $( 'input.form-item', 0 ) << 'user'
        $( 'input.form-item', 1 ) << 'password'

        then:
        !$( 'button.login-button' ).classes().contains( 'disabled' )
    }
}
