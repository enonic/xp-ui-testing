package com.enonic.wem.uitest.application

class ApplicationBrowsePanel_ItemStatisticPanel_Spec
    extends BaseApplicationSpec
{
    //1) WHEN One module is selected THEN The details panel should show Build date, version, key and System requirement
    //2)WHEN One module is installed and stopped THEN detail page should not show any content types
    //3)WHEN One module is installed and stopped THEN detail page should not show any mixins
    //4)WHEN One module is installed and stopped THEN detail page should not show any relationship types
    //5) WHEN One module is installed and stopped THEN detail page should not show any pages
    //6)WHEN One module is installed and stopped THEN detail page should not show any parts
    //7) WHEN One module is installed and stopped THEN detail page should not show any layout
    //8)WHEN One module is installed and started THEN detail page should not show all its content types
    //9) WHEN One module is installed and started THEN detail page should not show all its mixins
    //10) WHEN One module is installed and started THEN detail page should not show all its relationship types
    //11)WHEN One module is installed and started THEN detail page should not show all its pages
    //12) WHEN One module is installed and started THEN detail page should not show all its parts
    //13)WHEN One module is installed and started THEN detail page should not show all its layouts

    def "WHEN One application is selected THEN The details panel should show Build date, version, key and System requirement"()
    {
        when:
        applicationBrowsePanel.clickCheckboxAndSelectRow( FIRST_APP_NAME );

        then: "the Build date shown"
        applicationBrowsePanel.getItemStatisticPanel().isBuildDateVisible();
        and: "the version shown"
        applicationBrowsePanel.getItemStatisticPanel().isVersionVisible();
        and: "the key shown"
        applicationBrowsePanel.getItemStatisticPanel().isKeyVisible();
        and: "the system required shown"
        applicationBrowsePanel.getItemStatisticPanel().isSystemRequiredVisible();
    }

    def "WHEN One application is installed and stopped THEN detail page should not show any content types"()
    {
        when:
        applicationBrowsePanel.clickCheckboxAndSelectRow( FIRST_APP_NAME );
        then: ""
        true;

    }

}
