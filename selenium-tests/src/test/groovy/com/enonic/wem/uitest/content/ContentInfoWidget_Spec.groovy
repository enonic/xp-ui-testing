package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentInfoTerms
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentInfoWidget
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
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

    def "GIVEN a existing folder WHEN content selected and Details panel opened THEN correct status and actual content-properties are present"()
    {
        given: "folder content added "
        FOLDER = buildFolderContent( "folder", "infowidget test" );
        addContent( FOLDER );

        when: "details panel opened and widget is shown"
        filterPanel.typeSearchText( FOLDER.getName() );
        contentBrowsePanel.selectContentInTable( FOLDER.getName() ).clickOnDetailsToggleButton();
        ContentInfoWidget contentInfo = contentDetailsPanel.openInfoWidget();
        HashMap<String, String> props = contentInfo.getContentProperties();

        then: "Offline status displayed"
        contentInfo.getContentStatus().equalsIgnoreCase( ContentStatus.OFFLINE.getValue() );

        and: "correct number of properties present "
        props.size() == 6;

        and: "correct type present"
        props.get( ContentInfoTerms.TYPE.getValue() ).equals( ContentTypeName.folder().getLocalName() );

        and: "correct value for 'application' present"
        props.get( ContentInfoTerms.APPLICATION.getValue() ).equals( "base" );

        and: "correct owner is shown"
        props.get( ContentInfoTerms.OWNER.getValue() ).equals( "su" );

        and: "id value is not a null"
        props.get( ContentInfoTerms.ID.getValue() ) != null;

        and: "correct modified and created dates are shown"
        props.get( ContentInfoTerms.MODIFIED.getValue() ).equals( LocalDate.now().toString() );

        and:
        props.get( ContentInfoTerms.CREATED.getValue() ).equals( LocalDate.now().toString() );
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

        then: "Offline status displayed"
        contentInfo.getContentStatus().equalsIgnoreCase( ContentStatus.OFFLINE.getValue() );

        and: "correct number of properties present "
        props.size() == 6;

        and: "correct type present"
        props.get( ContentInfoTerms.TYPE.getValue() ).equals( ContentTypeName.unstructured().getLocalName() );

        and: "correct value for 'application' present"
        props.get( ContentInfoTerms.APPLICATION.getValue() ).equals( "base" );

        and: "correct owner is shown"
        props.get( ContentInfoTerms.OWNER.getValue() ).equals( "su" );

        and: "id value is not a null"
        props.get( ContentInfoTerms.ID.getValue() ) != null;

        and: "correct modified and created dates are shown"
        props.get( ContentInfoTerms.MODIFIED.getValue() ).equals( LocalDate.now().toString() );

        and:
        props.get( ContentInfoTerms.CREATED.getValue() ).equals( LocalDate.now().toString() );
    }

    def "GIVEN existing folder that is 'Offline' WHEN folder published THEN new correct status is shown"()
    {
        given:
        filterPanel.typeSearchText( FOLDER.getName() )
        contentBrowsePanel.selectContentInTable( FOLDER.getName() )

        when:
        contentBrowsePanel.clickToolbarPublish().clickOnPublishNowButton();
        contentBrowsePanel.clickOnDetailsToggleButton();
        ContentInfoWidget contentInfo = contentDetailsPanel.openInfoWidget();
        then:
        contentInfo.getContentStatus().equalsIgnoreCase( ContentStatus.ONLINE.getValue() );
    }

    def "GIVEN existing folder with 'Online' status  WHEN content edited THEN  content has got a 'Modified' status"()
    {
        given:
        filterPanel.typeSearchText( FOLDER.getName() )
        ContentWizardPanel wizard = contentBrowsePanel.selectContentInTable( FOLDER.getName() ).clickToolbarEdit();

        when:
        wizard.typeDisplayName( "new display name" ).save().close( "new display name" );
        contentBrowsePanel.clickOnDetailsToggleButton();
        ContentInfoWidget contentInfo = contentDetailsPanel.openInfoWidget();

        then:
        contentInfo.getContentStatus().equalsIgnoreCase( ContentStatus.MODIFIED.getValue() );
    }

    def "GIVEN existing folder with 'Modified' status  WHEN content deleted THEN 'Pending delete' status appears in the 'Detail Panel'"()
    {
        given: "existing folder with 'Modified' status"
        filterPanel.typeSearchText( FOLDER.getName() )

        when: "content deleted"
        ContentWizardPanel wizard = contentBrowsePanel.selectContentInTable( FOLDER.getName() ).clickToolbarDelete().doDelete();
        contentBrowsePanel.clickOnDetailsToggleButton();
        ContentInfoWidget contentInfo = contentDetailsPanel.openInfoWidget();
        TestUtils.saveScreenshot( getSession(), "det_panel_delete" )

        then: "'Pending delete' status appears in the 'Detail Panel'"
        contentInfo.getContentStatus().equalsIgnoreCase( ContentStatus.PENDING_DELETE.getValue() );
    }

}
