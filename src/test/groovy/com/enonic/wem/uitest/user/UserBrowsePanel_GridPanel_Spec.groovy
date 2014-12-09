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

    def "GIVEN a 'roles' folder on root  WHEN folder expanded THEN 'superuser'  shown"()
    {
        when: " a 'roles' folder expanded"
        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.ROLES.getValue() );

        then: "'superuser' should be shown"
        userBrowsePanel.exists( "superuser", true );
    }

    def "GIVEN a 'system' folder with an open expander WHEN closed THEN no children are listed beneath"()
    {
        given:
        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.SYSTEM.getValue() );
        TestUtils.saveScreenshot( getTestSession(), "system-expanded" );
        when:
        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.SYSTEM.getValue() );
        TestUtils.saveScreenshot( getTestSession(), "system-collapsed" );

        then:
        userBrowsePanel.getChildNames( UserBrowsePanel.BrowseItemType.SYSTEM.getValue() ).size() == 0;
    }

    def "GIVEN a 'system' folder selected WHEN arrow down is typed THEN next row is selected"()
    {
        given: "a 'system folder is selected'"
        userBrowsePanel.clickCheckboxAndSelectRow( UserBrowsePanel.BrowseItemType.SYSTEM );
        int before = userBrowsePanel.getSelectedRowsNumber();

        when: "'arrow down' typed"
        userBrowsePanel.pressKeyOnRow( UserBrowsePanel.BrowseItemType.SYSTEM.getValue(), Keys.ARROW_DOWN );
        TestUtils.saveScreenshot( getTestSession(), "arrow_down_user" );

        then: "'system' is not selected now and another folder in the root directory is selected"
        !userBrowsePanel.isRowSelected( UserBrowsePanel.BrowseItemType.SYSTEM.getValue() ) && userBrowsePanel.getSelectedRowsNumber() ==
            before;
    }

    def "GIVEN a 'roles' selected WHEN arrow up is typed THEN another row is selected"()
    {
        given: "'roles folder is selected'"
        userBrowsePanel.clickCheckboxAndSelectRow( UserBrowsePanel.BrowseItemType.ROLES );
        int before = userBrowsePanel.getSelectedRowsNumber();

        when: "arrow up typed"
        userBrowsePanel.pressKeyOnRow( UserBrowsePanel.BrowseItemType.ROLES.getValue(), Keys.ARROW_UP );
        TestUtils.saveScreenshot( getTestSession(), "arrow_up_user" );

        then: "roles is not selected now, another folder in the root directory is selected"
        !userBrowsePanel.isRowSelected( UserBrowsePanel.BrowseItemType.ROLES.getValue() ) && userBrowsePanel.getSelectedRowsNumber() ==
            before;
    }


}
