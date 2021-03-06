package com.enonic.wem.uitest.content.move_publish_sort.issue

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.BaseContentType
import com.enonic.autotests.pages.contentmanager.ConfirmValueDialog
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowseFilterPanel
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
import com.enonic.autotests.pages.contentmanager.browsepanel.DeleteContentDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.DoubleFormViewPanel
import com.enonic.autotests.pages.form.SiteFormViewPanel
import com.enonic.autotests.pages.usermanager.browsepanel.UserBrowsePanel
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.autotests.vo.contentmanager.Issue
import com.enonic.wem.uitest.BaseGebSpec
import com.enonic.xp.content.ContentPath
import com.enonic.xp.data.PropertyTree
import spock.lang.Shared

/**
 * Created on 7/11/2017.*/
class BaseIssueSpec
    extends BaseGebSpec
{

    @Shared
    UserBrowsePanel userBrowsePanel;

    @Shared
    ContentBrowseFilterPanel filterPanel;

    @Shared
    ContentBrowsePanel contentBrowsePanel;


    protected Issue buildIssue( String description, List<String> assigneesDisplayName, List<String> itemsToPublish )
    {
        String generated = NameHelper.uniqueName( "task" );
        Issue issue = Issue.builder().title( generated ).description( description ).assignees( assigneesDisplayName ).items(
            itemsToPublish ).build();
        return issue;
    }

    ContentBrowsePanel findAndSelectContent( String name )
    {
        filterPanel.typeSearchText( name );
        selectContentByName( name );
        return contentBrowsePanel;
    }

    protected ContentBrowsePanel selectContentByName( String name )
    {
        if ( !contentBrowsePanel.isRowSelected( name ) )
        {
            contentBrowsePanel.clickCheckboxAndSelectRow( name );
        }
        return contentBrowsePanel;
    }

    Content buildFolderContent( String name, String displayName )
    {
        String generated = NameHelper.uniqueName( name );
        Content content = Content.builder().
            name( generated ).
            displayName( displayName ).
            contentType( BaseContentType.FOLDER.getDisplayName() ).
            parent( ContentPath.ROOT ).
            build();
        return content;
    }

    protected Content buildDouble1_1_Content( String doubleValue )
    {
        PropertyTree data = new PropertyTree();
        String name = "double";
        if ( doubleValue != null )
        {
            data.addStrings( DoubleFormViewPanel.DOUBLE_VALUES, doubleValue );
        }

        Content dateContent = Content.builder().
            name( NameHelper.uniqueName( name ) ).
            displayName( "double content" ).
            parent( ContentPath.ROOT ).
            contentType( ALL_CONTENT_TYPES_APP_NAME + "double1_1" ).data( data ).
            build();
        return dateContent;
    }

    void addReadyContent( Content content )
    {
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( content.getContentTypeName() );
        wizard.typeData( content ).clickOnMarkAsReadyButton();
        wizard.closeBrowserTab().switchToBrowsePanelTab();
        contentBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
    }

    void addContent( Content content )
    {
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( content.getContentTypeName() );
        wizard.typeData( content ).save();
        wizard.closeBrowserTab().switchToBrowsePanelTab();
        contentBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
    }

    protected Content buildSiteWithAllTypes()
    {
        String siteName = NameHelper.uniqueName( "site" )
        PropertyTree data = new PropertyTree();
        data.addString( SiteFormViewPanel.APP_KEY, APP_CONTENT_TYPES_DISPLAY_NAME );
        data.addStrings( "description", "all content types site " )
        Content site = Content.builder().
            parent( ContentPath.ROOT ).
            name( siteName ).
            displayName( "site-contenttypes" ).
            parent( ContentPath.ROOT ).
            contentType( "Site" ).data( data ).
            build();
        return site;
    }

    protected ConfirmValueDialog openConfirmDeleteDialog( String siteName )
    {
        DeleteContentDialog deleteContentDialog = findAndSelectContent( siteName ).clickToolbarDelete()
        deleteContentDialog.waitForOpened();
        deleteContentDialog.clickOnDeleteButton();
        return new ConfirmValueDialog( getSession() );
    }
}
