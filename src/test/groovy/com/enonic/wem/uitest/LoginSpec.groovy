package com.enonic.wem.uitest

import spock.lang.Shared;
import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.contentmanager.wizardpanel.ItemViewPanelPage;
import com.enonic.autotests.services.ContentManagerService;
import com.enonic.autotests.vo.contentmanager.BaseAbstractContent;
import com.enonic.autotests.vo.contentmanager.FolderContent;
import geb.spock.GebSpec

class LoginSpec
    extends BaseGebSpec
{
	@Shared ContentManagerService cManagerService = new ContentManagerService();
	
	String CHECK_TITLE_TEST_FOLDER_DISPLAYNAME = "BildeArkiv";
	
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
