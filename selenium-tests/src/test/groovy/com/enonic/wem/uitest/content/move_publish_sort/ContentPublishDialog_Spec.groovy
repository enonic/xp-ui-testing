package com.enonic.wem.uitest.content.move_publish_sort

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.ContentPublishDialog
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class ContentPublishDialog_Spec
    extends BaseContentSpec
{
    @Shared
    Content parentContent;

    @Shared
    Content childContent1;

    @Shared
    Content childInvalid;


    def "GIVEN Content BrowsePanel WHEN one content selected and 'Publish' button clicked THEN 'Content publish' appears with correct control elements"()
    {
        setup:
        parentContent = buildFolderContent( "publish_dialog", "content publish dialog" );
        addContent( parentContent );

        and:
        filterPanel.typeSearchText( parentContent.getName() );
        contentBrowsePanel.clickCheckboxAndSelectRow( parentContent.getName() )
        childContent1 = buildFolderContentWithParent( "publish_dialog", "child-folder1", parentContent.getName() );
        addContent( childContent1 );

        when: "parent content selected and 'Publish' button pressed"
        ContentPublishDialog contentPublishDialog = contentBrowsePanel.clickToolbarPublish().waitUntilDialogShowed(
            Application.EXPLICIT_NORMAL );

        then: "'ContentPublishDialog' dialog displayed"
        contentPublishDialog.isOpened();
        and: "dialog has a correct title"
        contentPublishDialog.getTitle() == ContentPublishDialog.DIALOG_TITLE;
        and: "dialog has 'Publish Now' and 'Cancel' buttons"
        contentPublishDialog.isPublishNowButtonEnabled();
        and:
        contentPublishDialog.isCancelButtonBottomEnabled();
        and:
        contentPublishDialog.isCancelButtonTopEnabled();
        and: "'Include Child' checkbox not checked"
        !contentPublishDialog.isIncludeChildCheckboxSelected();

    }

    def "GIVEN 'Content Publish' dialog shown WHEN the cancel button on the bottom clicked THEN dialog not present"()
    {
        given: "parent content selected and 'Publish' button pressed"
        contentBrowsePanel.clickCheckboxAndSelectRow( parentContent.getName() );
        ContentPublishDialog contentPublishDialog = contentBrowsePanel.clickToolbarPublish().waitUntilDialogShowed(
            Application.EXPLICIT_NORMAL );

        when: "button 'Cancel' on the bottom of dialog pressed"
        contentPublishDialog.clickOnCancelBottomButton();

        then: "dialog not present"
        !contentPublishDialog.isOpened();
    }

    def "GIVEN 'Content Publish' dialog shown WHEN the button cancel on the top clicked THEN dialog not present"()
    {
        given: "parent content selected and 'Publish' button pressed"
        contentBrowsePanel.clickCheckboxAndSelectRow( parentContent.getName() );
        ContentPublishDialog contentPublishDialog = contentBrowsePanel.clickToolbarPublish().waitUntilDialogShowed(
            Application.EXPLICIT_NORMAL );

        when: "button 'Cancel' on the top of dialog pressed"
        contentPublishDialog.clickOnCancelTopButton();

        then: "dialog not present"
        !contentPublishDialog.isOpened();
    }

    def "GIVEN one parent content on root selected WHEN 'Content Publish' dialog opened THEN correct name of content present in the dialog"()
    {
        given: "one parent content on the root selected"
        contentBrowsePanel.clickCheckboxAndSelectRow( parentContent.getName() );

        when: "'Content Publish' dialog opened"
        ContentPublishDialog contentPublishDialog = contentBrowsePanel.clickToolbarPublish().waitUntilDialogShowed(
            Application.EXPLICIT_NORMAL );
        List<String> names = contentPublishDialog.getNamesOfContentsToPublish();

        then:
        names.size() == 1;
        and: "correct name of content present in the dialog"
        names.get( 0 ).contains( parentContent.getName() );

    }

    def "GIVEN a parent content on root selected 'Content publish' dialog opened WHEN 'include child' checkbox set to true THEN correct text shown in the header of 'dependencies list'"()
    {
        given: "parent content selected and 'Publish' button pressed"
        contentBrowsePanel.clickCheckboxAndSelectRow( parentContent.getName() );
        ContentPublishDialog contentPublishDialog = contentBrowsePanel.clickToolbarPublish().waitUntilDialogShowed(
            Application.EXPLICIT_NORMAL );

        when:
        contentPublishDialog.setIncludeChildCheckbox( true );
        List<String> dependencies = contentPublishDialog.getDependencies();

        then: "The header of 'Dependencies list' appears"
        contentPublishDialog.isDependenciesListHeaderDisplayed();
        and: "correct text shown in the header"
        contentPublishDialog.getDependenciesListHeader() == ContentPublishDialog.DEPENDENCIES_LIST_HEADER_TEXT;
        and: "one correct dependency shown "
        dependencies.size() == 1 && dependencies.get( 0 ).contains( childContent1.getName() );
    }
}