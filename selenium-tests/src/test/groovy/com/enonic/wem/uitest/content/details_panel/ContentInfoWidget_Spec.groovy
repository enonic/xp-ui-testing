package com.enonic.wem.uitest.content.details_panel

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.ContentInfoTerms
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.ContentInfoWidget
import com.enonic.autotests.pages.contentmanager.wizardpanel.ConfirmationDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import com.enonic.xp.schema.content.ContentTypeName
import spock.lang.Shared
import spock.lang.Stepwise

import java.time.LocalDate

@Stepwise
class ContentInfoWidget_Spec
    extends BaseContentSpec
{

    @Shared
    Content FOLDER;

    def "WHEN existing folder has been selected and Details panel opened THEN expected status and expected content-properties should be present"()
    {
        given: "existing folder"
        FOLDER = buildFolderContent( "folder", "info_widget_test" );
        addContent( FOLDER );

        when: "details panel has been opened and info-widget opened"
        filterPanel.typeSearchText( FOLDER.getName() );
        contentBrowsePanel.selectContentInGrid( FOLDER.getName() ).openContentDetailsPanel();
        ContentInfoWidget contentInfo = contentDetailsPanel.openDetailsWidget();
        saveScreenshot( "folder_info_widget_opened1" );
        HashMap<String, String> props = contentInfo.getContentProperties();

        then: "'New' status should be displayed"
        contentInfo.getContentStatus().equalsIgnoreCase( ContentStatus.NEW.getValue() );

        and: "6 properties should be present on the details panel "
        props.size() == 6;

        and: "correct type should be present"
        props.get( ContentInfoTerms.TYPE.getValue() ) == ContentTypeName.folder().getLocalName();

        and: "'Base' should be present in 'application'"
        props.get( ContentInfoTerms.APPLICATION.getValue() ) == "base";

        and: "SU owner should be displayed"
        props.get( ContentInfoTerms.OWNER.getValue() ) == "su";

        and: "id value is not a null"
        props.get( ContentInfoTerms.ID.getValue() ) != null;

        and: "correct modified and created dates should be shown"
        props.get( ContentInfoTerms.MODIFIED.getValue() ).contains( LocalDate.now().toString() );

        and:
        props.get( ContentInfoTerms.CREATED.getValue() ).contains( LocalDate.now().toString() );
    }

    def "GIVEN existing folder that is 'New' WHEN folder was published THEN new correct status is shown"()
    {
        given: "existing folder that is 'New' and 'Marked as ready'"
        filterPanel.typeSearchText( FOLDER.getName() )
        contentBrowsePanel.selectContentInGrid( FOLDER.getName() );
        contentBrowsePanel.showPublishMenu().clickOnMarkAsReadyMenuItem();

        when: "the folder has been published"
        contentBrowsePanel.clickToolbarPublish().clickOnPublishNowButton();
        contentBrowsePanel.openContentDetailsPanel();
        ContentInfoWidget contentInfo = contentDetailsPanel.openDetailsWidget();
        saveScreenshot( "info_widget-content-published" );
        HashMap<String, String> props = contentInfo.getContentProperties();

        then: "'published' status should be displayed on the widget"
        contentInfo.getContentStatus().equalsIgnoreCase( ContentStatus.PUBLISHED.getValue() );

        and: "'Published from' property should be correctly displayed"
        props.get( ContentInfoTerms.PUBLISHED_FROM.getValue() ).contains( LocalDate.now().toString() );
        and: "'First Published' should be displayed"
        props.get( ContentInfoTerms.FIRST_PUBLISHED.getValue() ).contains( LocalDate.now().toString() );
    }

    def "GIVEN existing folder with 'Published' status  WHEN the folder has been changed THEN content has got a 'Modified' status"()
    {
        given: "existing folder with 'Published' status is opened"
        ContentWizardPanel wizard = findAndSelectContent( FOLDER.getName() ).clickToolbarEditAndSwitchToWizardTab();

        when: "content has been updated"
        wizard.typeDisplayName( "new display name" ).save().closeBrowserTab().switchToBrowsePanelTab()
        contentBrowsePanel.openContentDetailsPanel();
        ContentInfoWidget contentInfo = contentDetailsPanel.openDetailsWidget();
        saveScreenshot( "info_widget_folder_modified" );

        then: "status is getting 'modified'"
        contentInfo.getContentStatus().equalsIgnoreCase( ContentStatus.MODIFIED.getValue() );

        and: "'First Published' should be displayed"
        contentInfo.getContentProperties().get( ContentInfoTerms.FIRST_PUBLISHED.getValue() ).contains( LocalDate.now().toString() );
    }
}
