package com.enonic.wem.uitest.application

import spock.lang.Stepwise

@Stepwise
class ApplicationBrowsePanel_ItemsSelectionPanel_Spec
    extends BaseApplicationSpec
{

    def "GIVEN existing application is selected WHEN selecting one more THEN two SelectionItem-s are listed"()
    {
        given: "existing application is selected"
        applicationBrowsePanel.clickCheckboxAndSelectRowByDisplayName( FIRST_APP_DISPLAY_NAME );

        when: "selected a one more application"
        applicationBrowsePanel.clickCheckboxAndSelectRowByDisplayName( SECOND_APP_DISPLAY_NAME );
        saveScreenshot( "test_two_app_selected" );

        then:
        itemsSelectionPanel.getSelectedItemCount() == 2;
    }

    def "GIVEN 2 existing applications are selected WHEN selecting one more app THEN three SelectionItem-s should be listed"()
    {
        given: "2 existing applications are selected"
        applicationBrowsePanel.clickCheckboxAndSelectRowByDisplayName( FIRST_APP_DISPLAY_NAME );
        applicationBrowsePanel.clickCheckboxAndSelectRowByDisplayName( SECOND_APP_DISPLAY_NAME );

        when: "one more application has been selected"
        applicationBrowsePanel.clickCheckboxAndSelectRowByDisplayName( THIRD_APP_DISPLAY_NAME );
        saveScreenshot( "test_three_app_selected1" );

        then: "three SelectionItem-s should be displayed"
        itemsSelectionPanel.getSelectedItemCount() == 3;
    }

    def "GIVEN three application are selected WHEN deselecting one THEN two SelectionItem-s should be listed"()
    {
        given: "there are three selected application in browse panel"
        applicationBrowsePanel.clickCheckboxAndSelectRowByDisplayName( FIRST_APP_DISPLAY_NAME );
        applicationBrowsePanel.clickCheckboxAndSelectRowByDisplayName( SECOND_APP_DISPLAY_NAME );
        applicationBrowsePanel.clickCheckboxAndSelectRowByDisplayName( THIRD_APP_DISPLAY_NAME );
        saveScreenshot( "test_three_app_selected2" );

        when: "one application was deselected"
        applicationBrowsePanel.deSelectAppInTable( THIRD_APP_DISPLAY_NAME );
        saveScreenshot( "test_three_app_one_deselected" );

        then: "only two items should be listed in the browse panel"
        itemsSelectionPanel.getSelectedItemCount() == 2;
    }

    def "WHEN two application are selected THEN two SelectionItem-s with correct  names should be displayed"()
    {
        when: "two applications are selected"
        applicationBrowsePanel.clickCheckboxAndSelectRowByDisplayName( FIRST_APP_DISPLAY_NAME );
        applicationBrowsePanel.clickCheckboxAndSelectRowByDisplayName( SECOND_APP_DISPLAY_NAME );

        then: "correct display names should be displayed on the SelectionItem panel"
        List actualNames = itemsSelectionPanel.getSelectedItemDisplayNames();
        actualNames.size() == 2;

        and:
        actualNames.contains( FIRST_APP_DISPLAY_NAME );

        and:
        actualNames.contains( SECOND_APP_DISPLAY_NAME );
    }
}
