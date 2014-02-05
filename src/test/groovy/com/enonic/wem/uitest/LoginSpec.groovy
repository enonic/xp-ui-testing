package com.enonic.wem.uitest

import spock.lang.Shared;
import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.contentmanager.wizardpanel.ItemViewPanelPage;
import com.enonic.autotests.services.ContentService;
import com.enonic.autotests.vo.contentmanager.BaseAbstractContent;
import com.enonic.autotests.vo.contentmanager.FolderContent;
import geb.spock.GebSpec

class LoginSpec
    extends BaseGebSpec
{
		
	def "Given login page When both username and password fields is empty Then Login Button must be disabled"()
	{
		when:
		go "admin"
		
		then:
		$( 'button.login-button' ).classes().contains( 'disabled' )
	}
	
    def "Given login page When both username and password fields have value Then Login Button must be enabled"()
    {
        given:
        go "admin"

        when:
        $( 'input.form-item', 0 ) << 'user'
        $( 'input.form-item', 1 ) << 'password'
		report "login page, username is 'user', password is 'password'"
        then:
        !$( 'button.login-button' ).classes().contains( 'disabled' )
    }
	
	def "Given login page When only username field have value Then Login Button must be disabled"()
	{
		given:
		go "admin"

		when:
		$( 'input.form-item', 0 ) << 'user'
		report "login page, username is 'user', password is empty"
		then:
		$( 'button.login-button' ).classes().contains( 'disabled' )
	}
	
	def "Given login page When only password field have value Then Login Button must be disabled"()
	{
		given:
		go "admin"

		when:
		$( 'input.form-item', 1 ) << 'password'
		report "login page, username is empty, password is 'password'"
		then:
		$( 'button.login-button' ).classes().contains( 'disabled' )
	}

}
