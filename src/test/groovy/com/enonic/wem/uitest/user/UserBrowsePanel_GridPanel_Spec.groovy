package com.enonic.wem.uitest.user

import com.enonic.autotests.pages.usermanager.browsepanel.UserBrowsePanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.wem.uitest.BaseGebSpec
import org.openqa.selenium.Keys
import spock.lang.Shared

class UserBrowsePanel_GridPanel_Spec
    extends BaseGebSpec
{
    @Shared
    UserBrowsePanel userBrowsePanel;


    def setup()
    {
        go "admin"
        userBrowsePanel = NavigatorHelper.openUserManager( getTestSession() );
    }

    def "GIVEN user browse panel opened WHEN no selection THEN all rows are white"()
    {
        given:
        int rowNumber = userBrowsePanel.getRowNumber();

        expect:
        userBrowsePanel.getSelectedRowsNumber() == 0 && rowNumber > 0;

    }

    def "GIVEN user browse panel opened WHEN first is clicked THEN first row is blue"()
    {
        when:
        userBrowsePanel.clickCheckboxAndSelectRow( UserBrowsePanel.BrowseItemType.SYSTEM );

        then:
        userBrowsePanel.getSelectedRowsNumber() == 1;
    }

    def "GIVEN a Content selected WHEN spacebar is typed THEN row is no longer selected"()
    {
        given:
        userBrowsePanel.clickCheckboxAndSelectRow( UserBrowsePanel.BrowseItemType.SYSTEM );
        TestUtils.saveScreenshot( getTestSession(), "spacebar-system1" );

        when:
        userBrowsePanel.pressKeyOnRow( "system", Keys.SPACE );

        then:
        TestUtils.saveScreenshot( getTestSession(), "spacebar-system2" );
        userBrowsePanel.getSelectedRowsNumber() == 0;
    }

    def "GIVEN a 'system' folder selected WHEN 'Clear selection'-link is clicked THEN row is no longer selected"()
    {
        given:
        List<String> contentNames = userBrowsePanel.getNamesFromBrowsePanel();
        userBrowsePanel.clickCheckboxAndSelectRow( UserBrowsePanel.BrowseItemType.SYSTEM );

        when:
        userBrowsePanel.clickOnClearSelection();

        then:
        userBrowsePanel.getSelectedRowsNumber() == 0 && contentNames.size() > 0;
    }

    def "GIVEN no items selected WHEN 'Select all'-link is clicked THEN all rows are selected"()
    {
        given:
        userBrowsePanel.clickOnClearSelection();

        when: "'Select all'-link is clicked"
        int selectedNumber = userBrowsePanel.clickOnSelectAll();

        then: "the number of rows in the grid the same as number in the 'Select All' link"
        userBrowsePanel.getRowNumber() == selectedNumber;
    }

    def "GIVEN a 'system' folder on root having a child WHEN listed THEN expander is shown"()
    {
        expect: " expander is shown near the 'system' folder"
        userBrowsePanel.isExpanderPresent( UserBrowsePanel.BrowseItemType.SYSTEM.getValue() )
    }

    def "GIVEN a 'system' folder on root  WHEN folder expanded THEN 'Users' and 'Groups' shown"()
    {
        when: " a 'system' folder expanded"
        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.SYSTEM.getValue() );

        then: "'users' should be shown"
        userBrowsePanel.exists( "users", true );
    }


}
