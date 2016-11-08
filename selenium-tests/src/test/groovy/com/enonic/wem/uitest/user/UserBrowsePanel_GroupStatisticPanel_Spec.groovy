package com.enonic.wem.uitest.user

import com.enonic.autotests.pages.usermanager.browsepanel.GroupStatisticsPanel
import com.enonic.autotests.pages.usermanager.browsepanel.UserBrowsePanel
import com.enonic.autotests.pages.usermanager.wizardpanel.GroupWizardPanel
import com.enonic.autotests.vo.usermanager.Group
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class UserBrowsePanel_GroupStatisticPanel_Spec
    extends BaseUsersSpec
{
    @Shared
    GroupStatisticsPanel groupStatisticsPanel;

    @Shared
    Group TEST_GROUP;

    @Shared
    String NEW_DISPLAY_NAME = "new display name";

    def "WHEN 'Groups' folder selected THEN correct info shown in a statistics panel"()
    {
        when: "'Groups' folder selected"
        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.SYSTEM.getValue() );
        userBrowsePanel.clickCheckboxAndSelectFolder( UserBrowsePanel.BrowseItemType.GROUPS_FOLDER );
        saveScreenshot( "groups-statistic-panel" );
        groupStatisticsPanel = new GroupStatisticsPanel( getSession() );

        then: "correct info shown in statistics panel"
        groupStatisticsPanel.getItemDisplayName() == "Groups";
    }

    def "GIVEN a new added group WHEN the group selected THEN empty members displayed in a statistics panel"()
    {
        given: "group saved and wizard closed"
        GroupWizardPanel groupWizardPanel = openSystemGroupWizard();
        TEST_GROUP = buildGroup( "group", "empty-group-statistics", "description" );
        groupWizardPanel.typeData( TEST_GROUP ).save().close( TEST_GROUP.getDisplayName() );
        sleep( 500 );
        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.GROUP.getValue() );
        userBrowsePanel.clickOnClearSelection();

        when: "the group selected in a grid"
        userBrowsePanel.clickCheckboxAndSelectGroup( TEST_GROUP.getName() );
        saveScreenshot( "empty-group-statistics" );

        then: "empty members displayed in statistics panel"
        groupStatisticsPanel.getMemberDisplayNames().size() == 0;

        and: "correct display name of group is shown"
        groupStatisticsPanel.getItemDisplayName() == TEST_GROUP.getDisplayName();
    }

    def "GIVEN changing a display name of existing group WHEN the group selected THEN group with new display name displayed in a statistics panel"()
    {
        given: "group saved and wizard closed"
        userBrowseFilterPanel.typeSearchText( TEST_GROUP.getName() );
        GroupWizardPanel groupWizardPanel = userBrowsePanel.clickCheckboxAndSelectGroup( TEST_GROUP.getName() ).clickToolbarEdit();

        when: "the group selected in a grid"
        groupWizardPanel.typeDisplayName( NEW_DISPLAY_NAME ).save().close( NEW_DISPLAY_NAME );
        saveScreenshot( "new-display-name-group-statistics" );

        then: "new display name of group is shown"
        groupStatisticsPanel.getItemDisplayName() == NEW_DISPLAY_NAME;
    }
}
