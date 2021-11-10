package com.enonic.wem.uitest.content

import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared

class ContentBrowsePanel_DetailsPanel_ContentDetails_Spec
    extends BaseContentSpec
{
    @Shared
    Content folderContent;

    def "WHEN folder has been selected AND widget dropdown selector has been clicked THEN expected options should be displayed"()
    {
        given: "new folder has been added"
        folderContent = buildFolderContent( "folder", "details_panel_test" );
        addContent( folderContent );
        contentBrowsePanel.openContentDetailsPanel();

        when: "when the folder has been selected and widgets dropdown selector has been clicked"
        findAndSelectContent( folderContent.getName() );
        contentDetailsPanel.getContentDisplayName() == folderContent.getDisplayName();

        then: "expected display name should be shown on the Details Panel"
        List<String> widgetMenuOptions = contentDetailsPanel.getMenuOptions();
        and: "three items should be present"
        widgetMenuOptions.size() == 4;
        and: "'Details' menu option should be present"
        widgetMenuOptions.contains( "Details" );
        and: "'Dependencies' menu option should be present"
        widgetMenuOptions.contains( "Dependencies" );
        and: "Version history menu option should be present"
        widgetMenuOptions.contains( "Version history" );
        and:
        widgetMenuOptions.contains( "Emulator" );
    }

    def "GIVEN existing folder is selected AND 'Content Details Panel' is opened WHEN Toggle Content Details button has been clicked THEN 'Content Details Panel' closes"()
    {
        given: "content has been selected and the 'Content Details Panel' is opened"
        findAndSelectContent( folderContent.getName() );
        contentBrowsePanel.openContentDetailsPanel();
        saveScreenshot( "detail-panel-opened" );

        when: "'Toggle' button has been clicked"
        contentBrowsePanel.clickOnDetailsToggleButton();
        saveScreenshot( "detail-panel-closed" );

        then: "'Content Details Panel' should be closed"
        !contentBrowsePanel.getContentDetailsPanel().isOpened( folderContent.getDisplayName() );
    }
}
