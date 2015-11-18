package com.enonic.wem.uitest.application

import spock.lang.Stepwise

@Stepwise
class ApplicationBrowsePanel_ItemsSelectionPanel_Spec
    extends BaseApplicationSpec
{

    def "GIVEN one selected application WHEN selecting one more THEN two SelectionItem-s are listed"()
    {
        given: " there is a one selected application"
        applicationBrowsePanel.selectRowByName( FIRST_APP_NAME );

        when: "selected a one more application"
        applicationBrowsePanel.clickCheckboxAndSelectRow( SECOND_APP_NAME );
        then:
        itemsSelectionPanel.getSelectedItemCount() == 2;
    }

    def "GIVEN two selected application WHEN selecting one more THEN three SelectionItem-s are listed"()
    {
        given: " there is a two selected module"
        applicationBrowsePanel.selectRowByName( FIRST_APP_NAME );
        applicationBrowsePanel.clickCheckboxAndSelectRow( SECOND_APP_NAME );

        when: "selected a one more application"
        applicationBrowsePanel.clickCheckboxAndSelectRow( THIRD_APP_NAME );

        then: "three SelectionItem-s are listed"
        itemsSelectionPanel.getSelectedItemCount() == 3;
    }

    def "GIVEN three selected application WHEN deselecting one THEN two SelectionItem-s are listed"()
    {
        given: "there are three selected application in browse panel"
        applicationBrowsePanel.clickCheckboxAndSelectRow( FIRST_APP_NAME );
        applicationBrowsePanel.clickCheckboxAndSelectRow( SECOND_APP_NAME );
        applicationBrowsePanel.clickCheckboxAndSelectRow( THIRD_APP_NAME );

        when: "one application was deselected"
        applicationBrowsePanel.deSelectAppInTable( THIRD_APP_NAME );

        then: "only two items are listed in the browse panel"
        itemsSelectionPanel.getSelectedItemCount() == 2;

    }

    def "WHEN two selected application THEN two SelectionItem-s with the same name are listed"()
    {
        when: "two applications are selected"
        applicationBrowsePanel.clickCheckboxAndSelectRow( FIRST_APP_NAME );
        applicationBrowsePanel.clickCheckboxAndSelectRow( SECOND_APP_NAME );

        then: "three SelectionItem-s are listed"
        List actualNames = itemsSelectionPanel.getSelectedItemNames();
        actualNames.size() == 2 && actualNames.contains( FIRST_APP_NAME ) && actualNames.contains( SECOND_APP_NAME );
    }
}
