package com.enonic.wem.uitest.content.details_panel

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.ContentInfoTerms
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.ContentInfoWidget
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import com.enonic.xp.schema.content.ContentTypeName
import spock.lang.Shared
import spock.lang.Stepwise

import java.time.LocalDate

/**
 *
 * Tasks:
 * xp-ui-testing#17 Add Selenium tests for "First Published" field in content
 * xp-ui-testing#44 Update tests for 'Details Panel' in order to new changes
 * */
@Stepwise
class ContentInfoWidget_Spec
    extends BaseContentSpec
{

    @Shared
    Content FOLDER;

    def "GIVEN existing folder WHEN the folder has been selected and Details panel opened THEN correct status and actual content-properties should be present"()
    {
        given: "existing folder"
        FOLDER = buildFolderContent( "folder", "info_widget_test" );
        addContent( FOLDER );

        when: "details panel opened and widget is shown"
        filterPanel.typeSearchText( FOLDER.getName() );
        contentBrowsePanel.selectContentInTable( FOLDER.getName() ).clickOnDetailsToggleButton();
        ContentInfoWidget contentInfo = contentDetailsPanel.openDetailsWidget();
        saveScreenshot( "folder_info_widget_opened1" );
        HashMap<String, String> props = contentInfo.getContentProperties();

        then: "'New' status should be displayed"
        contentInfo.getContentStatus().equalsIgnoreCase( ContentStatus.NEW.getValue() );

        and: "6 properties should be present on the details panel "
        props.size() == 6;

        and: "correct type should be present"
        props.get( ContentInfoTerms.TYPE.getValue() ).equals( ContentTypeName.folder().getLocalName() );

        and: "correct value for 'application' present"
        props.get( ContentInfoTerms.APPLICATION.getValue() ).equals( "base" );

        and: "correct owner should be displayed"
        props.get( ContentInfoTerms.OWNER.getValue() ).equals( "su" );

        and: "id value is not a null"
        props.get( ContentInfoTerms.ID.getValue() ) != null;

        and: "correct modified and created dates should be shown"
        props.get( ContentInfoTerms.MODIFIED.getValue() ).contains( LocalDate.now().toString() );

        and:
        props.get( ContentInfoTerms.CREATED.getValue() ).contains( LocalDate.now().toString() );
    }

    def "GIVEN existing 'folder' WHEN folder has been selected and Details panel opened THEN correct status and actual content-properties should be displayed"()
    {
        given: "existing 'folder'"
        Content folder = buildFolderContent( "folder", "infowidget test" );
        addContent( folder );

        when: "details panel has been opened and widget is shown"
        filterPanel.typeSearchText( folder.getName() );
        contentBrowsePanel.selectContentInTable( folder.getName() ).clickOnDetailsToggleButton();
        ContentInfoWidget contentInfo = contentDetailsPanel.openDetailsWidget();
        HashMap<String, String> props = contentInfo.getContentProperties();
        saveScreenshot( "info-widget-opened" );

        then: "New status should be displayed on the 'ContentInfo' widget"
        contentInfo.getContentStatus().equalsIgnoreCase( ContentStatus.NEW.getValue() );

        and: "6 properties should be present on the details panel "
        props.size() == 6;

        and: "correct type should be displayed"
        props.get( ContentInfoTerms.TYPE.getValue() ).equals( ContentTypeName.folder().getLocalName() );

        and: "correct value for 'application' should be displayed"
        props.get( ContentInfoTerms.APPLICATION.getValue() ).equals( "base" );

        and: "correct owner should be shown"
        props.get( ContentInfoTerms.OWNER.getValue() ).equals( "su" );

        and: "ID value is not a null"
        props.get( ContentInfoTerms.ID.getValue() ) != null;

        and: "correct modified and created dates are shown"
        props.get( ContentInfoTerms.MODIFIED.getValue() ).contains( LocalDate.now().toString() );

        and:
        props.get( ContentInfoTerms.CREATED.getValue() ).contains( LocalDate.now().toString() );
    }
    //xp-ui-testing#17 Add Selenium tests for "First Published" field in content
    def "GIVEN existing folder that is 'New' WHEN folder was published THEN new correct status is shown"()
    {
        given: "existing folder that is 'New'"
        filterPanel.typeSearchText( FOLDER.getName() )
        contentBrowsePanel.selectContentInTable( FOLDER.getName() )

        when: "the folder has been published"
        contentBrowsePanel.clickToolbarPublish().clickOnPublishNowButton();
        contentBrowsePanel.clickOnDetailsToggleButton();
        ContentInfoWidget contentInfo = contentDetailsPanel.openDetailsWidget();
        saveScreenshot( "info_widget-content-published" );
        HashMap<String, String> props = contentInfo.getContentProperties();

        then: "'online' status should be displayed on the widget"
        contentInfo.getContentStatus().equalsIgnoreCase( ContentStatus.ONLINE.getValue() );

        and: "'Published from' property should be correctly displayed"
        props.get( ContentInfoTerms.PUBLISHED_FROM.getValue() ).contains( LocalDate.now().toString() );
        and: "'First Published' should be displayed"
        props.get( ContentInfoTerms.FIRST_PUBLISHED.getValue() ).contains( LocalDate.now().toString() );
    }

    def "GIVEN existing folder with 'Online' status  WHEN the folder has been edited THEN content has got a 'Modified' status"()
    {
        given: "existing folder with 'Online' status is opened"
        ContentWizardPanel wizard = findAndSelectContent( FOLDER.getName() ).clickToolbarEditAndSwitchToWizardTab();

        when: "content has been updated"
        wizard.typeDisplayName( "new display name" ).save().closeBrowserTab().switchToBrowsePanelTab()
        contentBrowsePanel.clickOnDetailsToggleButton();
        ContentInfoWidget contentInfo = contentDetailsPanel.openDetailsWidget();
        saveScreenshot( "info_widget_folder_modified" );

        then: "status is getting 'modified'"
        contentInfo.getContentStatus().equalsIgnoreCase( ContentStatus.MODIFIED.getValue() );

        and: "'First Published' should be displayed"
        contentInfo.getContentProperties().get( ContentInfoTerms.FIRST_PUBLISHED.getValue() ).contains( LocalDate.now().toString() );
    }

    def "GIVEN existing folder with 'Modified' status  WHEN content has been deleted THEN 'Deleted' status appears on the 'Details Panel'"()
    {
        given: "existing folder with 'Modified' status"
        filterPanel.typeSearchText( FOLDER.getName() )

        when: "content has been deleted"
        contentBrowsePanel.selectContentInTable( FOLDER.getName() ).clickToolbarDelete().doDelete();
        contentBrowsePanel.clickOnDetailsToggleButton();
        ContentInfoWidget contentInfo = contentDetailsPanel.openDetailsWidget();
        saveScreenshot( "det_panel_content_deleted" )

        then: "'Deleted' status should appear on the 'Detail Panel'"
        contentInfo.getContentStatus().equalsIgnoreCase( ContentStatus.DELETED.getValue() );
    }
}
