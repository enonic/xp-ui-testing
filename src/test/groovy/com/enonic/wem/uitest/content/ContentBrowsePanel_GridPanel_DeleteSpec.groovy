package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.ItemViewPanelPage
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.api.content.ContentPath
import com.enonic.wem.api.schema.content.ContentTypeName
import com.enonic.wem.uitest.BaseGebSpec
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class ContentBrowsePanel_GridPanel_DeleteSpec
    extends BaseGebSpec
{

    @Shared
    ContentBrowsePanel contentBrowsePanel


    def setup()
    {
        go "admin"
        contentBrowsePanel = NavigatorHelper.openContentApp( getTestSession() );
    }

    def "GIVEN existing content, WHEN content opened and delete button pressed THEN the content should not be listed in the table"()
    {
        given:
        Content content = Content.builder().
            name( NameHelper.uniqueName( "deletecontent" ) ).
            displayName( "contenttodelete" ).
            contentType( ContentTypeName.folder() ).
            parent( ContentPath.ROOT ).
            build();
        contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder().toString() ).typeData( content ).save().close();

        when:
        ItemViewPanelPage contentInfoPage = contentBrowsePanel.selectRowByContentPath( content.getPath().toString() ).clickToolbarOpen()
        contentInfoPage.waitUntilOpened( content.getDisplayName(), 1 );
        contentInfoPage.openDeleteConfirmationDialog().doConfirm();

        then:
        !contentBrowsePanel.exists( content.getPath(), Application.DEFAULT_IMPLICITLY_WAIT );
    }

    def "GIVEN existing two contents, WHEN all content selected and delete button pressed THEN the content should not be listed in the table"()
    {
        given:
        Content content1 = Content.builder().
            name( NameHelper.uniqueName( "deletecontent" ) ).
            displayName( "contenttodelete" ).
            parent( ContentPath.ROOT ).
            contentType( ContentTypeName.folder() ).
            build();

        contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder().toString() ).typeData( content1 ).save().close();

        Content content2 = Content.builder().
            name( NameHelper.uniqueName( "deletecontent" ) ).
            displayName( "contenttodelete" ).
            contentType( ContentTypeName.folder() ).
            parent( ContentPath.ROOT ).
            build();

        contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder().toString() ).waitUntilWizardOpened().typeData(
            content2 ).save().close();

        List<Content> contentList = new ArrayList<>();
        contentList.add( content1 );
        contentList.add( content2 );
        contentBrowsePanel.waitsForSpinnerNotVisible();
        contentBrowsePanel.waituntilPageLoaded( 2 );
        contentBrowsePanel.doClearSelection();

        when:
        contentBrowsePanel.selectContentInTable( contentList ).clickToolbarDelete().doDelete();


        then:
        !contentBrowsePanel.exists( content1.getPath() ) && !contentBrowsePanel.exists( content2.getPath() );
    }

    def "GIVEN a Content on root WHEN deleted THEN deleted content is no longer listed at root"()
    {
        given:
        Content content = Content.builder().
            name( NameHelper.uniqueName( "deletecontent" ) ).
            displayName( "contenttodelete" ).
            contentType( ContentTypeName.folder() ).
            parent( ContentPath.ROOT ).
            build();

        contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder().toString() ).typeData( content ).save().close();
        List<Content> contents = new ArrayList<>();
        contents.add( content );
        contentBrowsePanel.waitsForSpinnerNotVisible();
        contentBrowsePanel.doClearSelection();

        when:
        contentBrowsePanel.clickCheckboxAndSelectRow( content.getPath() );
        contentBrowsePanel.deleteSelected();

        then:
        !contentBrowsePanel.exists( content.getPath() );
    }

    def "GIVEN a Content beneath an existing WHEN deleted THEN deleted Content is no longer listed beneath parent"()
    {
        given:
        Content parent = Content.builder().
            parent( ContentPath.ROOT ).
            name( NameHelper.uniqueName( "parent" ) ).
            displayName( "parent" ).
            contentType( ContentTypeName.folder() ).
            build();
        contentBrowsePanel.clickToolbarNew().selectContentType( parent.getContentTypeName() ).typeData( parent ).save().close();
        contentBrowsePanel.clickByParentCheckbox( parent.getPath() )
        Content contentToDelete = Content.builder().
            name( NameHelper.uniqueName( "archive" ) ).
            displayName( "delete content beneath parent" ).
            parent( ContentPath.from( parent.getName() ) ).
            contentType( ContentTypeName.archiveMedia() ).
            build();

        contentBrowsePanel.clickToolbarNew().selectContentType( contentToDelete.getContentTypeName() ).typeData( contentToDelete ).
            save().close();
        contentBrowsePanel.waituntilPageLoaded( 1 );

        List<Content> contentList = new ArrayList<>()
        contentList.add( contentToDelete );

        when:
        contentBrowsePanel.expandContent( contentToDelete.getParent() ).selectContentInTable( contentList ).clickToolbarDelete().doDelete();

        then:
        !contentBrowsePanel.exists( contentToDelete.getPath() );
    }


    def "GIVEN a one and only Content beneath an existing WHEN deleted THEN expand icon of parent is no longer shown "()
    {
        given:
        Content parent = Content.builder().
            parent( ContentPath.ROOT ).
            name( NameHelper.uniqueName( "parent" ) ).
            displayName( "expandicon-test" ).
            contentType( ContentTypeName.folder() ).
            build();
        contentBrowsePanel.clickToolbarNew().selectContentType( parent.getContentTypeName() ).typeData( parent ).save().close();

        contentBrowsePanel.clickByParentCheckbox( parent.getPath() )
        Content content = Content.builder().
            name( NameHelper.uniqueName( "archive" ) ).
            displayName( "archive" ).
            contentType( ContentTypeName.folder() ).
            parent( ContentPath.from( parent.getName() ) ).
            build();

        contentBrowsePanel.clickToolbarNew().selectContentType( content.getContentTypeName() ).typeData( content ).save().close();

        List<Content> contentList = new ArrayList<>();
        contentList.add( content );

        when:
        contentBrowsePanel.expandContent( content.getParent() )
        TestUtils.saveScreenshot( getTestSession(), "deletecontentbeneath" )
        contentBrowsePanel.selectContentInTable( contentList ).clickToolbarDelete().doDelete();

        then:
        !contentBrowsePanel.<ContentPath> isExpanderPresent( ContentPath.from( parent.getName() ) );
    }

    def "GIVEN a Content on root WHEN deleted THEN New-button is enabled"()
    {
        given:
        Content content = Content.builder().
            name( NameHelper.uniqueName( "archive" ) ).
            displayName( "archive" ).
            contentType( ContentTypeName.folder() ).
            parent( ContentPath.ROOT ).
            build();
        contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder().toString() ).typeData( content ).save().close();

        when:
        contentBrowsePanel.selectContentInTable( content.getPath() ).clickToolbarDelete().doDelete();

        then:
        contentBrowsePanel.isNewButtonEnabled();
    }

    def "GIVEN two Content on root WHEN both deleted THEN New-button is enabled"()
    {
        given:
        Content content1 = Content.builder().
            name( NameHelper.uniqueName( "archive" ) ).
            displayName( "archive" ).
            contentType( ContentTypeName.folder() ).
            parent( ContentPath.ROOT ).
            build();

        Content content2 = Content.builder().
            name( NameHelper.uniqueName( "archive" ) ).
            displayName( "archive" ).
            contentType( ContentTypeName.folder() ).
            parent( ContentPath.ROOT ).
            build();
        List<Content> contentList = new ArrayList<>();
        contentList.add( content2 );
        contentList.add( content1 );
        contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder().toString() ).typeData( content1 ).save().close();
        contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder().toString() ).typeData( content2 ).save().close();

        when:
        contentBrowsePanel.selectContentInTable( contentList ).clickToolbarDelete().doDelete();

        then:
        contentBrowsePanel.isNewButtonEnabled();
    }
}
