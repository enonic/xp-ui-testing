package com.enonic.wem.uitest.contentimport com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanelimport com.enonic.autotests.pages.contentmanager.wizardpanel.ItemViewPanelPageimport com.enonic.autotests.services.NavigatorHelperimport com.enonic.autotests.utils.NameHelperimport com.enonic.autotests.vo.contentmanager.Contentimport com.enonic.wem.api.content.ContentPathimport com.enonic.wem.api.schema.content.ContentTypeNameimport com.enonic.wem.uitest.BaseGebSpecimport spock.lang.Sharedclass ContentBrowsePanel_ContextMenu_Spec    extends BaseGebSpec{    @Shared    ContentBrowsePanel contentBrowsePanel;    def setup()    {        go "admin"        contentBrowsePanel = NavigatorHelper.openContentApp( getTestSession() );    }    def "GIVEN a content WHEN content selected and 'Delete' selected from context menu  THEN content not exist in view"()    {        setup: "build a new folder-content"        String name = NameHelper.uniqueName( "folder" );        Content content = Content.builder().            name( name ).            displayName( "folderToDelete" ).            contentType( ContentTypeName.folder() ).            parent( ContentPath.ROOT ).            build();        and: "add new content: click on 'new' button, populate a wizard and close it"        contentBrowsePanel.clickToolbarNew().selectContentType( content.getContentTypeName() ).typeData( content ).save().close();        contentBrowsePanel.waitsForSpinnerNotVisible();        when: "right clicks on the selected folder and select 'Delete' from the context menu"        contentBrowsePanel.selectDeleteFromContextMenu( content.getPath() ).doDelete();        contentBrowsePanel.waitUntilPageLoaded( 3 );        then: "content deleted and  not exist in view"        !contentBrowsePanel.exists( content.getPath(), 1 );    }    def "GIVEN a content WHEN content selected and 'Edit' selected from context menu  THEN content is listed with it's new name"()    {        setup: "builds a new archive-content"        String name = NameHelper.uniqueName( "archive" );        Content content = Content.builder().            name( name ).            displayName( "archive" ).            contentType( ContentTypeName.archiveMedia() ).            parent( ContentPath.ROOT ).            build();        and: "add new content: click on 'new' button, populate a wizard and close it"        contentBrowsePanel.clickToolbarNew().selectContentType( content.getContentTypeName() ).typeData( content ).save().close();        contentBrowsePanel.waitsForSpinnerNotVisible();        Content newContent = cloneContentWithNewName( content )        when: "right clicks on the selected folder and select 'Edit' from the context menu, populate a new data and save it"        contentBrowsePanel.selectEditFromContextMenu( content.getPath() ).typeData( newContent ).save().close();        contentBrowsePanel.waitUntilPageLoaded( 3 );        then: "content is listed with it's new name"        contentBrowsePanel.exists( newContent.getPath(), 2 );    }    def "GIVEN a content WHEN content selected and 'Open' selected from a context menu THEN title with content display-name showed"()    {        setup: "builds a new archive-content"        String name = NameHelper.uniqueName( "archive" );        Content content = Content.builder().            name( name ).            displayName( "archive" ).            contentType( ContentTypeName.archiveMedia() ).            parent( ContentPath.ROOT ).            build();        and: "add new content: click on 'new' button, populate a wizard save and close it"        contentBrowsePanel.clickToolbarNew().selectContentType( content.getContentTypeName() ).typeData( content ).save().close();        contentBrowsePanel.waitsForSpinnerNotVisible();        when: "right clicks on the selected folder and select 'Open' from the context menu, 'item view' panel opened"        ItemViewPanelPage itemView = contentBrowsePanel.selectOpenFromContextMenu( content.getPath() );        itemView.waitUntilOpened( content.getDisplayName() );        then: "title with content display-name showed"        itemView.getTitle().equals( content.getDisplayName() );    }    def "GIVEN a content WHEN content selected and 'New' selected from a context menu THEN new Content should be listed beneath parent"()    {        setup: "builds a new archive-content"        String name = NameHelper.uniqueName( "archive" );        Content content = Content.builder().            name( name ).            displayName( "archive" ).            contentType( ContentTypeName.archiveMedia() ).            parent( ContentPath.ROOT ).            build();        and: "add new content: click on 'new' button, add a new content"        contentBrowsePanel.clickToolbarNew().selectContentType( content.getContentTypeName() ).typeData( content ).save().close();        contentBrowsePanel.waitsForSpinnerNotVisible();        and: "builds a new archive content, that will be child for first content"        Content newContent = Content.builder().            name( "child" ).            displayName( "child" ).            parent( ContentPath.from( name ) ).            contentType( ContentTypeName.archiveMedia() ).            build();        when: "right clicks on the first archive and select 'New' from the context menu,populate content wizard and save"        contentBrowsePanel.selectNewFromContextMenu( content.getPath() ).selectContentType( newContent.getContentTypeName() ).typeData(            newContent ) save().close();        contentBrowsePanel.waitUntilPageLoaded( 3 );        contentBrowsePanel.expandContent( content.getPath() );        then: "new Content should be listed beneath parent"        contentBrowsePanel.exists( newContent.getPath(), 1 );    }    Content cloneContentWithNewName( Content source )    {        String newName = NameHelper.uniqueName( "newname" );        return Content.builder().            name( newName ).            displayName( source.getDisplayName() ).            parent( source.getParent() ).            contentType( source.getContentTypeName() ).            build();    }}