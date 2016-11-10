package com.enonic.wem.uitest.content.details_panel

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.ContentInfoTerms
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.ContentInfoWidget
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import com.enonic.xp.schema.content.ContentTypeName
import org.apache.commons.lang.StringUtils
import spock.lang.Shared
import spock.lang.Stepwise

import java.time.LocalDate

@Stepwise
class ContentInfoWidget_Spec
    extends BaseContentSpec
{

    @Shared
    Content FOLDER;

    def "GIVEN a existing folder WHEN content selected and Details panel opened THEN correct status and actual content-properties are present"()
    {
        given: "folder content added "
        FOLDER = buildFolderContent( "folder", "info_widget_test" );
        addContent( FOLDER );

        when: "details panel opened and widget is shown"
        filterPanel.typeSearchText( FOLDER.getName() );
        contentBrowsePanel.selectContentInTable( FOLDER.getName() ).clickOnDetailsToggleButton();
        ContentInfoWidget contentInfo = contentDetailsPanel.openInfoWidget();
        saveScreenshot( "folder_info_widget_opened1" );
        HashMap<String, String> props = contentInfo.getContentProperties();

        then: "Offline status displayed"
        contentInfo.getContentStatus().equalsIgnoreCase( ContentStatus.OFFLINE.getValue() );

        and: "correct number of properties present "
        props.size() == 7;

        and: "correct type present"
        props.get( ContentInfoTerms.TYPE.getValue() ).equals( ContentTypeName.folder().getLocalName() );

        and: "correct value for 'application' present"
        props.get( ContentInfoTerms.APPLICATION.getValue() ).equals( "base" );

        and: "correct owner is shown"
        props.get( ContentInfoTerms.OWNER.getValue() ).equals( "su" );

        and: "id value is not a null"
        props.get( ContentInfoTerms.ID.getValue() ) != null;

        and: "correct modified and created dates are shown"
        props.get( ContentInfoTerms.MODIFIED.getValue() ).contains( LocalDate.now().toString() );

        and:
        props.get( ContentInfoTerms.CREATED.getValue() ).contains( LocalDate.now().toString() );

        and: "Published from is White space"
        StringUtils.isWhitespace( props.get( ContentInfoTerms.PUBLISHED_FROM.getValue() ) );
    }

    def "GIVEN a existing 'unstructured' WHEN content selected and Details panel opened THEN correct status and actual content-properties are present"()
    {
        given: "folder content added "
        Content content = buildUnstructuredContent( "unstructured", "infowidget test" );
        addContent( content );

        when: "details panel opened and widget is shown"
        filterPanel.typeSearchText( content.getName() );
        contentBrowsePanel.selectContentInTable( content.getName() ).clickOnDetailsToggleButton();
        ContentInfoWidget contentInfo = contentDetailsPanel.openInfoWidget();
        HashMap<String, String> props = contentInfo.getContentProperties();
        saveScreenshot( "info-widget-opened" );

        then: "Offline status displayed"
        contentInfo.getContentStatus().equalsIgnoreCase( ContentStatus.OFFLINE.getValue() );

        and: "correct number of properties present "
        props.size() == 7;

        and: "correct type present"
        props.get( ContentInfoTerms.TYPE.getValue() ).equals( ContentTypeName.unstructured().getLocalName() );

        and: "correct value for 'application' present"
        props.get( ContentInfoTerms.APPLICATION.getValue() ).equals( "base" );

        and: "correct owner is shown"
        props.get( ContentInfoTerms.OWNER.getValue() ).equals( "su" );

        and: "id value is not a null"
        props.get( ContentInfoTerms.ID.getValue() ) != null;

        and: "correct modified and created dates are shown"
        props.get( ContentInfoTerms.MODIFIED.getValue() ).contains( LocalDate.now().toString() );

        and:
        props.get( ContentInfoTerms.CREATED.getValue() ).contains( LocalDate.now().toString() );
    }

    def "GIVEN existing folder that is 'Offline' WHEN folder published THEN new correct status is shown"()
    {
        given: "existing folder that is 'Offline'"
        filterPanel.typeSearchText( FOLDER.getName() )
        contentBrowsePanel.selectContentInTable( FOLDER.getName() )

        when: "the folder has been published"
        contentBrowsePanel.clickToolbarPublish().clickOnPublishNowButton();
        contentBrowsePanel.clickOnDetailsToggleButton();
        ContentInfoWidget contentInfo = contentDetailsPanel.openInfoWidget();
        saveScreenshot( "info_widget-content-published" );
        HashMap<String, String> props = contentInfo.getContentProperties();

        then: "'online' status is displayed on the widget"
        contentInfo.getContentStatus().equalsIgnoreCase( ContentStatus.ONLINE.getValue() );

        and: "Published from property is displayed correctly"
        props.get( ContentInfoTerms.PUBLISHED_FROM.getValue() ).contains( LocalDate.now().toString() );

    }

    def "GIVEN existing folder with 'Online' status  WHEN content edited THEN content has got a 'Modified' status"()
    {
        given:
        filterPanel.typeSearchText( FOLDER.getName() )
        ContentWizardPanel wizard = contentBrowsePanel.selectContentInTable( FOLDER.getName() ).clickToolbarEdit();

        when: "content updated"
        wizard.typeDisplayName( "new display name" ).save().close( "new display name" );
        contentBrowsePanel.clickOnDetailsToggleButton();
        ContentInfoWidget contentInfo = contentDetailsPanel.openInfoWidget();
        saveScreenshot( "info_widget_folder_modified" );

        then: "status is getting 'modified'"
        contentInfo.getContentStatus().equalsIgnoreCase( ContentStatus.MODIFIED.getValue() );
    }

    def "GIVEN existing folder with 'Modified' status  WHEN content deleted THEN 'Pending delete' status appears in the 'Detail Panel'"()
    {
        given: "existing folder with 'Modified' status"
        filterPanel.typeSearchText( FOLDER.getName() )

        when: "content deleted"
        contentBrowsePanel.selectContentInTable( FOLDER.getName() ).clickToolbarDelete().doDelete();
        contentBrowsePanel.clickOnDetailsToggleButton();
        ContentInfoWidget contentInfo = contentDetailsPanel.openInfoWidget();
        saveScreenshot( "det_panel_content_pending" )

        then: "'Pending delete' status appears in the 'Detail Panel'"
        contentInfo.getContentStatus().equalsIgnoreCase( ContentStatus.PENDING_DELETE.getValue() );
    }

}
