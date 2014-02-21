package com.enonic.autotests.services;

import java.util.List;

import org.apache.log4j.Logger;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.Page;
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel;
import com.enonic.autotests.pages.contentmanager.browsepanel.DeleteContentDialog;
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel;
import com.enonic.autotests.pages.contentmanager.wizardpanel.ItemViewPanelPage;
import com.enonic.autotests.utils.TestUtils;
import com.enonic.autotests.vo.contentmanager.BaseAbstractContent;
import com.enonic.wem.api.content.ContentPath;

/**
 * Service for 'Content Manager' application.
 */
public class ContentService
{

    private Logger logger = Logger.getLogger( this.getClass() );

    public enum HowOpenContent
    {
        TOOLBAR, CONTEXT_MENU
    }

    public boolean openContentManagerAppAndVerify( TestSession session )
    {
        // 1. open a 'content manager'
        ContentBrowsePanel cmPage = NavigatorHelper.openContentApp( session );
        TestUtils.saveScreenshot( session );
        boolean result = true;
        result &= cmPage.verifyTitle();
        //TODO should specified: what need to check?
        //result &=cmPage.verifyAllControls();
        return result;
    }

    
    public ContentWizardPanel openContentWizardPanel(TestSession session, String contentTypeName, ContentPath contentPath)
    {
    	 ContentBrowsePanel cmPage = NavigatorHelper.openContentApp( session );

         //select a folder and open the 'add content wizard' (click by 'New')
         ContentWizardPanel wizardPage = cmPage.openContentWizardPanel( contentTypeName, contentPath );
         wizardPage.waitUntilWizardOpened(1);
         return wizardPage;
    }

    /**
     * Selects all content in the table.
     *
     * @param session
     * @return
     */
    public int doSelectAll( TestSession session )
    {

        // 1. open a 'content manager'
        ContentBrowsePanel cmPage = NavigatorHelper.openContentApp( session );
        // click by "Select All" link and get a number of selected rows:
        return cmPage.doSelectAll();

    }

    /**
     * Selects a parent folder or space in the table of content and adds new content.
     *
     * @param session
     * @param newcontent
     * @param isCloseWizard
     * @return
     */
    public Page addContent( TestSession session, BaseAbstractContent newcontent, boolean isCloseWizard )
    {
        // 1. open a 'content manager'
        ContentBrowsePanel cmPage = NavigatorHelper.openContentApp( session );
        cmPage.doAddContent( newcontent, isCloseWizard );
        if ( isCloseWizard )
        {
            return cmPage;
        }
        else
        {
            return new ContentWizardPanel( session );
        }

    }

    /**
     * Finds a content, open preview page and verify this page.
     *
     * @param session
     * @param content
     * @return
     */
    public boolean doOpenContentVerifyPage( TestSession session, BaseAbstractContent content )
    {
        // 1. open a 'content manager'
        ContentBrowsePanel cmPage = NavigatorHelper.openContentApp( session );
        ItemViewPanelPage contentInfoPage = cmPage.doOpenContent( content );

        boolean result = contentInfoPage.verifyContentInfoPage( content );
        result &= contentInfoPage.verifyToolbar( content );
        return result;
    }

    public void doOpenContent( TestSession session, BaseAbstractContent content )
    {
        // 1. open a 'content manager'
        ContentBrowsePanel cmPage = NavigatorHelper.openContentApp( session );
        cmPage.doOpenContent( content );
    }

    /**
     * Finds a content, open preview for this content and click by "Edit" and updates.
     *
     * @param session
     * @param contentToEdit
     * @param newcontent
     * @return {@link com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel} instance. Table of content.
     */
    public ContentBrowsePanel doOpenContentAndEdit( TestSession session, BaseAbstractContent contentToEdit, BaseAbstractContent newcontent )
    {
        // 1. open a 'content manager'
        ContentBrowsePanel cmPage = NavigatorHelper.openContentApp( session );
        //cmPage.doClearSelection();

        ItemViewPanelPage contentInfoPage = cmPage.doOpenContent( contentToEdit );
        TestUtils.saveScreenshot( session );
        contentInfoPage.doEditContentAndCloseWizard( contentToEdit.getDisplayName(), newcontent );
        contentInfoPage.doCloseContentInfoView();
        cmPage.waituntilPageLoaded( Application.PAGELOAD_TIMEOUT );
        return cmPage;
    }

    /**
     * @param session
     * @param content
     * @param how
     * @return
     */
    public ItemViewPanelPage doOpenContent( TestSession session, BaseAbstractContent content, HowOpenContent how )
    {
        ItemViewPanelPage contentInfoPage;
        switch ( how )
        {
            case TOOLBAR:
            {
                contentInfoPage = doOpenContentUseToolbar( session, content );
                break;
            }
            case CONTEXT_MENU:
            {
                contentInfoPage = doOpenContentUseContextMenu( session, content );
                break;
            }
            default:
                contentInfoPage = doOpenContentUseToolbar( session, content );
                break;
        }

        return contentInfoPage;
    }

    /**
     * Opens a content-info page
     *
     * @param session
     * @param content
     * @return
     */
    public ItemViewPanelPage doOpenContentUseToolbar( TestSession session, BaseAbstractContent content )
    {
        // 1. open a 'content manager'
        ContentBrowsePanel cmPage = NavigatorHelper.openContentApp( session );
        ItemViewPanelPage contentInfoPage = cmPage.doOpenContent( content );
        return contentInfoPage;
    }

    public ItemViewPanelPage doOpenContentUseContextMenu( TestSession session, BaseAbstractContent contentToDelete )
    {
        // 1. open a 'content manager'
        ContentBrowsePanel cmPage = NavigatorHelper.openContentApp( session );
        ItemViewPanelPage contentInfoPage = cmPage.doOpenContentFromContextMenu( contentToDelete );
        return contentInfoPage;
    }

    /**
     * Expand a space, selects a checkbox, press 'Delete' button from a toolbar and delete content from space.
     *
     * @param session
     * @param contents
     * @return {@link com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel} instance. Table of content.
     */
    public ContentBrowsePanel deleteContentUseToolbar( TestSession session, List<BaseAbstractContent> contents )
    {
        // 1. open a 'content manager'
        ContentBrowsePanel cmPage = NavigatorHelper.openContentApp( session );
        // expand a space and selects contents, clicks by 'Delete' button from a toolbar and confirm deletion.
        cmPage.doClearSelection();
        cmPage.doDeleteContent( contents );
        return cmPage;
    }

    public DeleteContentDialog selectContentClickDeleteInToolbar( TestSession session, List<BaseAbstractContent> contents )
    {
        // 1. open a 'content manager'
        ContentBrowsePanel cmPage = NavigatorHelper.openContentApp( session );
        // expand a space and selects contents, clicks by 'Delete' button from a toolbar and confirm deletion.
        return cmPage.openDeleteContentDialog( contents );
    }
}
