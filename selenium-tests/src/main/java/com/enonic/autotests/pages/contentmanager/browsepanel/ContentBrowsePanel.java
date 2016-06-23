package com.enonic.autotests.pages.contentmanager.browsepanel;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.SaveOrUpdateException;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.BaseBrowseFilterPanel;
import com.enonic.autotests.pages.BrowsePanel;
import com.enonic.autotests.pages.contentmanager.ContentPublishDialog;
import com.enonic.autotests.pages.contentmanager.ContentUnpublishDialog;
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel;
import com.enonic.autotests.pages.contentmanager.wizardpanel.ItemViewPanelPage;
import com.enonic.autotests.utils.NameHelper;
import com.enonic.autotests.utils.TestUtils;
import com.enonic.autotests.vo.contentmanager.Content;
import com.enonic.xp.content.ContentPath;

import static com.enonic.autotests.utils.SleepHelper.sleep;

/**
 * 'Content Studio' application, the Browse Panel page.
 */
public class ContentBrowsePanel
    extends BrowsePanel
{
    public final String NOTIFICATION_MESSAGE = "//div[contains(@id,'NotificationContainer')]//div[@class='notification-content']//span";

    protected final String CONTENT_BROWSE_TOOLBAR_XPATH = "//div[contains(@id,'ContentBrowseToolbar')]";

    public static final String CONTENT_STUDIO_BUTTON = "//button[@id='api.app.bar.HomeButton' ]//span[text()='Content Studio']";

    private final String BASE_PANEL_XPATH = "//div[contains(@id,'ContentBrowsePanel')]";

    protected final String ALL_CONTENT_NAMES_FROM_BROWSE_PANEL_XPATH = BASE_PANEL_XPATH + ALL_NAMES_FROM_BROWSE_PANEL_XPATH;

    private final String NEW_BUTTON_XPATH = BROWSE_TOOLBAR_XPATH + "//*[contains(@id, 'ActionButton') and child::span[text()='New']]";

    private final String DUPLICATE_BUTTON_XPATH =
        BROWSE_TOOLBAR_XPATH + "//*[contains(@id, 'ActionButton') and child::span[text()='Duplicate']]";

    private final String PREVIEW_BUTTON_XPATH =
        BROWSE_TOOLBAR_XPATH + "//*[contains(@id, 'ActionButton') and child::span[text()='Preview']]";

    private final String MOVE_BUTTON_XPATH = BROWSE_TOOLBAR_XPATH + "//*[contains(@id, 'ActionButton') and child::span[text()='Move']]";

    private final String EDIT_BUTTON_XPATH =
        CONTENT_BROWSE_TOOLBAR_XPATH + "//*[contains(@id, 'ActionButton') and child::span[text()='Edit']]";

    private final String DELETE_BUTTON_XPATH = BROWSE_TOOLBAR_XPATH + "//*[contains(@id, 'ActionButton') and child::span[text()='Delete']]";

    private final String SORT_BUTTON_XPATH = BROWSE_TOOLBAR_XPATH + "//*[contains(@id, 'ActionButton') and child::span[text()='Sort']]";

    private final String MORE_BUTTON_XPATH = BROWSE_TOOLBAR_XPATH + "//div[contains(@id,'FoldButton')]";

    private final String PUBLISH_BUTTON_XPATH =
        BROWSE_TOOLBAR_XPATH + "//*[contains(@id, 'ActionButton') and child::span[text()='Publish']]";

    private final String PUBLISH_MENU_DROPDOWN_HANDLER =
        BROWSE_TOOLBAR_XPATH + "//div[contains(@id,'MenuButton')]//button[contains(@id,'DropdownHandle')]";

    protected final String DETAILS_TOGGLE_BUTTON = BASE_PANEL_XPATH + "//div[contains(@class,'details-panel-toggle-button')]";

    private final String UNPUBLISH_MENU_ITEM = "//ul[contains(@id,'Menu')]//li[contains(@id,'MenuItem') and text()='Unpublish']";

    private final String PUBLISH_TREE_MENU_ITEM = "//ul[contains(@id,'Menu')]//li[contains(@id,'MenuItem') and text()='Publish Tree']";

    @FindBy(xpath = DELETE_BUTTON_XPATH)
    protected WebElement deleteButton;

    @FindBy(xpath = NEW_BUTTON_XPATH)
    private WebElement newButton;

    @FindBy(xpath = EDIT_BUTTON_XPATH)
    private WebElement editButton;

    @FindBy(xpath = DUPLICATE_BUTTON_XPATH)
    private WebElement duplicateButton;

    @FindBy(xpath = CONTENT_STUDIO_BUTTON)
    private WebElement contentManagerButton;

    @FindBy(xpath = PREVIEW_BUTTON_XPATH)
    private WebElement previewButton;

    @FindBy(xpath = SORT_BUTTON_XPATH)
    private WebElement sortButton;

    @FindBy(xpath = MOVE_BUTTON_XPATH)
    private WebElement moveButton;

    @FindBy(xpath = PUBLISH_BUTTON_XPATH)
    private WebElement publishButton;

    @FindBy(xpath = PUBLISH_MENU_DROPDOWN_HANDLER)
    private WebElement publishMenuDropDownHandler;


    @FindBy(xpath = DETAILS_TOGGLE_BUTTON)
    WebElement detailsToggleButton;

    private ContentBrowseFilterPanel filterPanel;

    private ContentBrowseItemsSelectionPanel itemsSelectionPanel;

    private ContentBrowseItemPanel contentBrowseItemPanel;

    private ContentDetailsPanel contentDetailsPanel;

    /**
     * The constructor.
     *
     * @param session {@link TestSession} instance
     */
    public ContentBrowsePanel( TestSession session )
    {
        super( session );
    }

    @Override
    public void waitsForSpinnerNotVisible()
    {
        boolean result = waitsElementNotVisible( By.xpath( BASE_PANEL_XPATH + SPINNER_XPATH ), Application.EXPLICIT_NORMAL );
        if ( !result )
        {
            throw new TestFrameworkException( "after " + EXPLICIT_NORMAL + " second, spinner still present" );
        }
    }

    public ContentBrowsePanel showPublishMenu()
    {
        publishMenuDropDownHandler.click();
        sleep( 400 );
        return this;
    }

    public ContentPublishDialog selectPublishTreeMenuItem()
    {
        showPublishMenu();
        if ( !isElementDisplayed( PUBLISH_TREE_MENU_ITEM ) )
        {
            TestUtils.saveScreenshot( getSession(), "err_publish_menu_item" );
            throw new TestFrameworkException( "menu item was not found!" + "Publish Tree" );
        }
        getDisplayedElement( By.xpath( PUBLISH_TREE_MENU_ITEM ) ).click();
        ContentPublishDialog dialog = new ContentPublishDialog( getSession() );
        dialog.waitUntilDialogShown( Application.EXPLICIT_NORMAL );
        return dialog;
    }

    public ContentUnpublishDialog selectUnPublishMenuItem()
    {
        if ( !isUnPublishMenuItemEnabled() )
        {
            TestUtils.saveScreenshot( getSession(), "err_unpublish_menu_item" );
            throw new TestFrameworkException( "menu item was not found!" + "unpublish_item" );
        }
        getDisplayedElement( By.xpath( UNPUBLISH_MENU_ITEM ) ).click();
        ContentUnpublishDialog dialog = new ContentUnpublishDialog( getSession() );
        dialog.waitUntilDialogShown( Application.EXPLICIT_NORMAL );
        return dialog;
    }

    public boolean isPublishMenuAvailable()
    {
        if ( !isElementDisplayed( PUBLISH_MENU_DROPDOWN_HANDLER ) )
        {
            TestUtils.saveScreenshot( getSession(), "err_publish_dropdown_handler" );
            throw new TestFrameworkException( "dropdown handler for publish menu is not displayed" );
        }
        return !getAttribute( getDisplayedElement( By.xpath( PUBLISH_MENU_DROPDOWN_HANDLER ) ), "class",
                              Application.EXPLICIT_NORMAL ).contains( "disabled" );
    }

    public boolean isPublishTreeMenuItemEnabled()
    {
        if ( !isElementDisplayed( PUBLISH_TREE_MENU_ITEM ) )
        {
            TestUtils.saveScreenshot( getSession(), "err_publish_tree_menu_item_not_visible " );
            throw new TestFrameworkException( "'publish tree' menu item is not visible!" );
        }
        return !getAttribute( getDisplayedElement( By.xpath( PUBLISH_TREE_MENU_ITEM ) ), "class", Application.EXPLICIT_NORMAL ).contains(
            "disabled" );
    }

    public boolean isUnPublishMenuItemEnabled()
    {
        if ( !isElementDisplayed( UNPUBLISH_MENU_ITEM ) )
        {
            TestUtils.saveScreenshot( getSession(), "err_unpublish_menu_item_not_visible " );
            throw new TestFrameworkException( "'unpublish' menu item is not visible!" );
        }
        return !getAttribute( getDisplayedElement( By.xpath( UNPUBLISH_MENU_ITEM ) ), "class", Application.EXPLICIT_NORMAL ).contains(
            "disabled" );
    }

    public ContentDetailsPanel getContentDetailsPanel()
    {
        if ( contentDetailsPanel == null )
        {
            contentDetailsPanel = new ContentDetailsPanel( getSession() );
        }
        return contentDetailsPanel;
    }

    public ContentBrowseItemPanel getContentBrowseItemPanel()
    {
        if ( contentBrowseItemPanel == null )
        {
            contentBrowseItemPanel = new ContentBrowseItemPanel( getSession() );
        }
        return contentBrowseItemPanel;
    }

    public ContentDetailsPanel openContentDetailsPanel()
    {
        if ( !getContentDetailsPanel().isDisplayed() )
        {
            clickOnDetailsToggleButton();
        }
        return contentDetailsPanel;
    }

    public ContentBrowsePanel clickOnDetailsToggleButton()
    {
        boolean result = waitUntilClickableNoException( By.xpath( DETAILS_TOGGLE_BUTTON ), Application.EXPLICIT_NORMAL );
        if ( !result )
        {
            TestUtils.saveScreenshot( getSession(), "err_toggle_button" );
            throw new TestFrameworkException( "DetailsToggle button is not clickable!" );
        }
        detailsToggleButton.click();
        sleep( 1000 );
        return this;
    }

    public boolean isDetailsPanelToggleButtonDisplayed()
    {
        return isElementDisplayed( DETAILS_TOGGLE_BUTTON );
    }

    public boolean isFilterPanelShown()
    {
        return getFilterPanel().isFilterPanelDisplayed();
    }

    public String waitNotificationMessage()
    {
        if ( !waitUntilVisibleNoException( By.xpath( NOTIFICATION_MESSAGE ), Application.EXPLICIT_NORMAL ) )
        {
            return null;
        }
        return findElements( By.xpath( NOTIFICATION_MESSAGE ) ).get( 0 ).getText();
    }

    public boolean isContentInvalid( String contentName )
    {
        String contentInGrid = String.format( CONTENT_SUMMARY_VIEWER, contentName );
        if ( !isElementDisplayed( contentInGrid ) )
        {
            TestUtils.saveScreenshot( getSession(), "err_" + contentName );
            throw new TestFrameworkException( "content with path was not found or browsePanel was not displayed!" + contentName );
        }
        WebElement element = getDisplayedElement( By.xpath( contentInGrid ) );
        return waitAndCheckAttrValue( element, "class", "invalid", Application.EXPLICIT_NORMAL );
    }

    public ContentWizardPanel selectAndOpenContentFromToolbarMenu( Content content )
    {
        clickOnClearSelection();
        if ( findElements( By.linkText( BaseBrowseFilterPanel.CLEAR_FILTER_LINK ) ).size() > 0 )
        {
            findElements( By.linkText( BaseBrowseFilterPanel.CLEAR_FILTER_LINK ) ).get( 0 ).click();
        }

        filterPanel.typeSearchText( content.getName() );
        clickCheckboxAndSelectRow( content.getName() ).clickToolbarEdit();
        ContentWizardPanel wizard = new ContentWizardPanel( getSession() );
        wizard.waitUntilWizardOpened();
        return wizard;
    }

    public String getContentStatus( String contentName )
    {
        String statusXpath = String.format(
            "//div[contains(@class,'slick-row') and descendant::p[contains(.,'%s')]]//div[contains(@class,'slick-cell')][4]/span",
            contentName );
        if ( findElements( By.xpath( statusXpath ) ).size() == 0 )
        {
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "status_not_found" ) );
            if ( !getContentDetailsPanel().isDisplayed() )
            {
                clickOnDetailsToggleButton();
                selectContentInTable( contentName );
                TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "details_panel" ) );
            }
            return getContentDetailsPanel().openInfoWidget().getContentStatus();
        }
        return findElement( By.xpath( statusXpath ) ).getText();
    }

    public ContentBrowseFilterPanel getFilterPanel()
    {
        if ( filterPanel == null )
        {
            filterPanel = new ContentBrowseFilterPanel( getSession() );
        }
        return filterPanel;
    }

    public ContentBrowseItemsSelectionPanel getItemSelectionPanel()
    {
        if ( itemsSelectionPanel == null )
        {
            itemsSelectionPanel = new ContentBrowseItemsSelectionPanel( getSession() );
        }
        return itemsSelectionPanel;
    }

    public ContentBrowsePanel pressAppHomeButton()
    {
        contentManagerButton.click();
        sleep( 500 );
        waitUntilPageLoaded( Application.EXPLICIT_NORMAL );
        return this;
    }

    /**
     * Gets all content names, showed in the browse panel.
     *
     * @return list of names.
     */
    public List<String> getContentNamesFromBrowsePanel()
    {
        List<WebElement> rows = getDriver().findElements( By.xpath( ALL_CONTENT_NAMES_FROM_BROWSE_PANEL_XPATH ) );
        return rows.stream().filter( e -> !e.getText().isEmpty() ).map( WebElement::getText ).collect( Collectors.toList() );
    }

    public List<String> getChildContentNamesFromBrowsePanel( String parentName )
    {
        List<WebElement> rows = getDriver().findElements( By.xpath( ALL_CONTENT_NAMES_FROM_BROWSE_PANEL_XPATH ) );
        return rows.stream().filter( e -> !e.getText().contains( parentName ) && !e.getText().isEmpty() ).map(
            WebElement::getText ).collect( Collectors.toList() );
    }

    public boolean exists( ContentPath contentPath, boolean saveScreenshot )
    {
        return exists( contentPath.toString(), saveScreenshot );
    }


    public ContentBrowsePanel unExpandContent( ContentPath contentPath )
    {
        if ( !doScrollAndFindGridItem( contentPath.toString() ) )
        {
            throw new TestFrameworkException( "unExpandContent: content was not found! " + contentPath );
        }
        if ( isRowExpanded( contentPath.toString() ) )
        {
            this.clickOnExpander( contentPath.toString() );
            getLogger().info( "content have been unexpanded: " + contentPath.toString() );
        }
        else
        {
            getLogger().info( "content was not expanded: " + contentPath.toString() );
        }
        sleep( 500 );
        return this;
    }

    /**
     * Clicks on expander icon near the content.
     *
     * @param contentPath
     */
    public ContentBrowsePanel expandContent( ContentPath contentPath )
    {
        if ( !doScrollAndFindGridItem( contentPath.getName() ) )
        {
            TestUtils.saveScreenshot( getSession(), "err_" + contentPath.getName() );
            throw new TestFrameworkException( "expandContent: content was not found! " + contentPath );
        }
        ContentPath path = null;
        if ( contentPath != null )
        {
            for ( int i = 0; i < contentPath.elementCount(); i++ )
            {
                String parentContent = contentPath.getElement( i );
                if ( path == null )
                {
                    path = ContentPath.from( parentContent );
                }
                else
                {
                    path = ContentPath.from( path, parentContent );
                }
                getLogger().info( "expandContent,  name of folder : " + path.getName() );
                if ( isRowExpanded( path.getName() ) == null )
                {
                    throw new TestFrameworkException( "Expander for the " + path.getName() + "  was not found! Or content has no child" );
                }
                if ( !isRowExpanded( path.getName() ) )
                {
                    this.clickOnExpander( path.getName() );
                    getLogger().info( "content with name " + parentContent + "was expanded! " );
                }
            }
        }
        waitsForSpinnerNotVisible();
        sleep( 700 );
        return this;
    }

    /**
     * Clicks on 'Delete' button on toolbar.
     *
     * @return {@link DeleteContentDialog} instance.
     */
    public DeleteContentDialog clickToolbarDelete()
    {
        if ( !isButtonDisplayed( DELETE_BUTTON_XPATH ) )
        {
            clickOnFoldButton();
        }
        boolean isEnabledDeleteButton = waitUntilElementEnabledNoException( By.xpath( DELETE_BUTTON_XPATH ), 2l );
        if ( !isEnabledDeleteButton )
        {
            TestUtils.saveScreenshot( getSession(), "err_delete_button" );
            throw new SaveOrUpdateException( "Impossible to delete content, because the 'Delete' button is disabled!" );
        }
        deleteButton.click();
        DeleteContentDialog dialog = new DeleteContentDialog( getSession() );
        dialog.waitForOpened();
        return dialog;
    }

    public ContentPublishDialog clickToolbarPublish()
    {
        if ( !isButtonDisplayed( PUBLISH_BUTTON_XPATH ) )
        {
            clickOnFoldButton();
        }
        publishButton.click();
        ContentPublishDialog dialog = new ContentPublishDialog( getSession() );
        dialog.waitUntilDialogShown( Application.EXPLICIT_NORMAL );
        return dialog;
    }

    public ContentBrowsePanel selectContentInTable( List<String> contentNames )
    {
        contentNames.stream().forEach( name -> selectContentInTable( name ) );
        return this;
    }

    public ContentBrowsePanel selectContentInTable( String... contentNames )
    {
        Arrays.asList( contentNames ).forEach( name -> selectContentInTable( name ) );
        return this;
    }

    public ContentBrowsePanel selectContentInTable( String contentName )
    {
        boolean exist = doScrollAndFindGridItem( contentName );
        if ( !exist )
        {
            TestUtils.saveScreenshot( getSession(), "err_content_not_found" + contentName );
        }
        if ( !isRowSelected( contentName ) )
        {
            clickCheckboxAndSelectRow( contentName );
        }
        return this;
    }

    public ContentBrowsePanel deSelectContentInTable( String contentName )
    {
        boolean exist = doScrollAndFindGridItem( contentName );
        if ( isRowSelected( contentName ) )
        {
            clickCheckboxAndSelectRow( contentName );
        }
        return this;
    }


    public BrowsePanel pressKeyOnRow( ContentPath path, Keys key )
    {
        boolean result = doScrollAndFindGridItem( path.toString() );
        if ( !result )
        {
            throw new TestFrameworkException( "grid item was not found! " + path.toString() );
        }
        return pressKeyOnRow( path.toString(), key );
    }

    public boolean isExpanderPresent( ContentPath contentPath )
    {
        return isExpanderPresent( contentPath.toString() );
    }

    /**
     * Clicks on 'New' button and opens NewContentDialog
     *
     * @return {@link NewContentDialog} instance.
     */
    public NewContentDialog clickToolbarNew()
    {
        newButton.click();
        sleep( 500 );
        NewContentDialog newContentDialog = new NewContentDialog( getSession() );
        boolean isLoaded = newContentDialog.waitUntilDialogShowed( Application.EXPLICIT_NORMAL );
        if ( !isLoaded )
        {
            throw new TestFrameworkException( "Error during add content, NewContentDialog dialog was not showed!" );
        }
        return newContentDialog;
    }

    public ContentBrowsePanel clickToolbarDuplicate()
    {
        duplicateButton.click();
        sleep( 1000 );
        return this;
    }

    public SortContentDialog clickToolbarSort()
    {
        if ( !isButtonDisplayed( SORT_BUTTON_XPATH ) )
        {
            clickOnFoldButton();
        }
        sortButton.click();
        sleep( 500 );
        SortContentDialog sortContentDialog = new SortContentDialog( getSession() );
        sortContentDialog.waitForLoaded( Application.EXPLICIT_NORMAL );
        return sortContentDialog;
    }

    private boolean isButtonDisplayed( String xpath )
    {
        return findElements( By.xpath( xpath ) ).stream().filter( WebElement::isDisplayed ).count() == 1;
    }

    private void clickOnFoldButton()
    {
        if ( !isElementDisplayed( MORE_BUTTON_XPATH ) )
        {
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "err_fold_button" ) );
            throw new TestFrameworkException( "'More' button not found on the toolbar" );
        }
        getDisplayedElement( By.xpath( MORE_BUTTON_XPATH ) ).click();
        sleep( 500 );
    }

    public MoveContentDialog clickToolbarMove()
    {
        if ( !isButtonDisplayed( MOVE_BUTTON_XPATH ) )
        {
            clickOnFoldButton();
        }
        moveButton.click();
        sleep( 500 );
        MoveContentDialog moveContentDialog = new MoveContentDialog( getSession() );
        moveContentDialog.waitUntilDialogShown( Application.EXPLICIT_NORMAL );
        return moveContentDialog;
    }

    /**
     * Expands all contents from 'content path' and clicks on a checkbox near the content, that will be parent for new content.
     *
     * @param contentPath {@link ContentPath} instance.
     */
    public ContentBrowsePanel clickOnParentCheckbox( ContentPath contentPath )
    {
        if ( contentPath.elementCount() == 0 )
        {
            return this;
        }
        if ( !doScrollAndFindGridItem( contentPath.toString() ) )
        {
            throw new TestFrameworkException( "content was not found: " + contentPath.toString() );
        }

        String checkBoxXpath = String.format( ROW_CHECKBOX_BY_NAME, contentPath.toString() );
        boolean isPresentCheckbox = isDynamicElementPresent( By.xpath( checkBoxXpath ), 3 );
        if ( !isPresentCheckbox )
        {
            TestUtils.saveScreenshot( getSession(), "checkbox" + contentPath.getName() );
            throw new TestFrameworkException( "wrong xpath:" + checkBoxXpath + " or item with name " + contentPath.getName() +
                                                  " was not found!" );
        }
        getDriver().findElement( By.xpath( checkBoxXpath ) ).click();
        sleep( 200 );
        boolean isNewEnabled = waitUntilElementEnabledNoException( By.xpath( NEW_BUTTON_XPATH ), 2l );
        if ( !isNewEnabled )
        {
            throw new SaveOrUpdateException( "Impossible to open 'ContentWizardPanel', because the 'New' button is disabled!" );
        }
        return this;
    }

    /**
     * Clicks on 'Open' button in toolbar.
     *
     * @return {@link ItemViewPanelPage} instance.
     */
    public ItemViewPanelPage clickToolbarPreview()
    {
        if ( !isButtonDisplayed( PREVIEW_BUTTON_XPATH ) )
        {
            clickOnFoldButton();
        }
        previewButton.click();
        sleep( 1000 );
        return new ItemViewPanelPage( getSession() );
    }

    public ContentWizardPanel clickToolbarEdit()
    {
        editButton.click();
        sleep( 1000 );
        ContentWizardPanel wizard = new ContentWizardPanel( getSession() );
        wizard.waitUntilWizardOpened();
        return wizard;
    }

    /**
     * Selects 'Delete' in a context menu.
     *
     * @param contentName
     * @return {@link DeleteContentDialog} instance.
     */
    public DeleteContentDialog selectDeleteFromContextMenu( String contentName )
    {
        if ( !doScrollAndFindGridItem( contentName ) )
        {
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "err_" + contentName ) );
            throw new TestFrameworkException( "content was not found: " + contentName );
        }
        openContextMenu( contentName );
        String deleteMenuItem = String.format( CONTEXT_MENU_ITEM, "Delete" );
        if ( !isElementDisplayed( deleteMenuItem ) )
        {
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "err_context-delete" ) );
            throw new TestFrameworkException( "Delete context-menu item is not visible!" );
        }
        getDisplayedElement( By.xpath( deleteMenuItem ) ).click();
        DeleteContentDialog dialog = new DeleteContentDialog( getSession() );
        dialog.waitForOpened();
        return dialog;
    }

    public ContentPublishDialog selectPublishFromContextMenu( String contentName )
    {
        getFilterPanel().clickOnCleanFilter().typeSearchText( contentName );
        sleep( 1000 );
        openContextMenu( contentName );
        String publishMenuItem = String.format( CONTEXT_MENU_ITEM, "Publish" );
        if ( !isElementDisplayed( publishMenuItem ) )
        {
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "err_context-publish" ) );
            throw new TestFrameworkException( "publish context-menu item is not visible!" );
        }
        getDisplayedElement( By.xpath( publishMenuItem ) ).click();
        ContentPublishDialog dialog = new ContentPublishDialog( getSession() );
        dialog.waitUntilDialogShown( Application.EXPLICIT_NORMAL );
        sleep( 500 );
        return dialog;
    }

    public ContentUnpublishDialog selectUnPublishFromContextMenu( String contentName )
    {
        openContextMenu( contentName );
        String publishMenuItem = String.format( CONTEXT_MENU_ITEM, "Unpublish" );
        if ( !isElementDisplayed( publishMenuItem ) )
        {
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "err_context-unpublish" ) );
            throw new TestFrameworkException( "unpublish context-menu item is not visible!" );
        }
        getDisplayedElement( By.xpath( publishMenuItem ) ).click();
        ContentUnpublishDialog dialog = new ContentUnpublishDialog( getSession() );
        dialog.waitUntilDialogShown( Application.EXPLICIT_NORMAL );
        sleep( 500 );
        return dialog;
    }


    public ContentBrowsePanel selectDuplicateFromContextMenu( String contentName )
    {
        getFilterPanel().clickOnCleanFilter().typeSearchText( contentName );
        sleep( 1000 );
        openContextMenu( contentName );
        String duplicateMenuItem = String.format( CONTEXT_MENU_ITEM, "Duplicate" );
        if ( !isElementDisplayed( duplicateMenuItem ) )
        {
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "err_context-duplicate" ) );
            throw new TestFrameworkException( "duplicate context-menu item is not visible!" );
        }
        getDisplayedElement( By.xpath( duplicateMenuItem ) ).click();
        sleep( 1000 );
        return this;
    }

    /**
     * Selects 'Edit' in a context menu.
     *
     * @param contentName
     * @return {@link ContentWizardPanel} instance.
     */
    public ContentWizardPanel selectEditFromContextMenu( String contentName )
    {
        openContextMenu( contentName );
        String editMenuItem = String.format( CONTEXT_MENU_ITEM, "Edit" );
        if ( !isElementDisplayed( editMenuItem ) )
        {
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "err_context-edit" ) );
            throw new TestFrameworkException( "'edit' context-menu item is not visible!" );
        }
        getDisplayedElement( By.xpath( editMenuItem ) ).click();
        ContentWizardPanel wizard = new ContentWizardPanel( getSession() );
        wizard.waitUntilWizardOpened();
        return wizard;
    }

    public SortContentDialog selectSortInContextMenu( String contentName )
    {
        openContextMenu( contentName );
        if ( !waitUntilVisibleNoException( By.xpath( String.format( CONTEXT_MENU_ITEM, "Sort" ) ), Application.EXPLICIT_NORMAL ) )
        {
            throw new TestFrameworkException( "Sort item was not found in the context menu" );
        }
        findElement( By.xpath( String.format( CONTEXT_MENU_ITEM, "Sort" ) ) ).click();
        SortContentDialog sortContentDialog = new SortContentDialog( getSession() );
        sortContentDialog.waitForLoaded( Application.EXPLICIT_NORMAL );
        return sortContentDialog;
    }

    public boolean isItemDisabledInContextMenu( String contentName, String menuItem )
    {
        openContextMenu( contentName );
        if ( !waitUntilVisibleNoException( By.xpath( String.format( CONTEXT_MENU_ITEM, menuItem ) ), Application.EXPLICIT_NORMAL ) )
        {
            TestUtils.saveScreenshot( getSession(), "err_" + menuItem );
            throw new TestFrameworkException( menuItem + "  item was not found in the context menu" );
        }
        WebElement previewItem = getDisplayedElement( By.xpath( String.format( CONTEXT_MENU_ITEM, menuItem ) ) );
        return waitAndCheckAttrValue( previewItem, "class", "disabled", 1 );
    }

    public ContentBrowsePanel selectPreviewInContextMenu( String contentName )
    {
        openContextMenu( contentName );
        if ( !waitUntilVisibleNoException( By.xpath( String.format( CONTEXT_MENU_ITEM, "Preview" ) ), Application.EXPLICIT_NORMAL ) )
        {
            TestUtils.saveScreenshot( getSession(), "err_" + "preview" );
            throw new TestFrameworkException( "'Preview' menu item is not visible" );
        }
        findElements( By.xpath( String.format( CONTEXT_MENU_ITEM, "Preview" ) ) ).get( 0 ).click();
        return this;
    }

    /**
     * Opens context menu and select a 'New' item
     *
     * @param contentName
     * @return {@link NewContentDialog} instance.
     */
    public NewContentDialog selectNewFromContextMenu( String contentName )
    {
        getFilterPanel().clickOnCleanFilter().typeSearchText( contentName );
        sleep( 1000 );
        openContextMenu( contentName );
        sleep( 500 );
        String newMenuItem = String.format( CONTEXT_MENU_ITEM, "New" );
        if ( !isElementDisplayed( newMenuItem ) )
        {
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "err_context-edit" ) );
            throw new TestFrameworkException( "'New' context-menu item is not visible!" );
        }
        getDisplayedElement( By.xpath( newMenuItem ) ).click();
        NewContentDialog newContentDialog = new NewContentDialog( getSession() );
        newContentDialog.waitUntilDialogShowed( Application.EXPLICIT_NORMAL );
        return newContentDialog;
    }

    public String waitPublishNotificationMessage( long timeout )
    {
        if ( !waitUntilVisibleNoException( By.xpath( PUBLISH_SUCCESS_NOTIFICATION_MESSAGE_XPATH ), timeout ) )
        {
            return null;
        }
        String message = findElement( By.xpath( PUBLISH_SUCCESS_NOTIFICATION_MESSAGE_XPATH ) ).getText();
        getLogger().info( "Publish Notification message " + message );
        return message;
    }

    public boolean waitExpectedNotificationMessage( String message, long timeout )
    {
        String expectedMessage = String.format( EXPECTED_NOTIFICATION_MESSAGE_XPATH, message );
        return waitUntilVisibleNoException( By.xpath( expectedMessage ), timeout );
    }

    /**
     * @return true if 'Delete' button enabled, otherwise false.
     */
    public boolean isDeleteButtonEnabled()
    {
        return deleteButton.isEnabled();
    }

    public boolean isNewButtonEnabled()
    {
        return newButton.isEnabled();
    }

    public boolean isSortButtonEnabled()
    {
        return sortButton.isEnabled();
    }

    public boolean isDuplicateButtonEnabled()
    {
        return duplicateButton.isEnabled();
    }

    public boolean isMoveButtonEnabled()
    {
        return moveButton.isEnabled();
    }

    public boolean isEditButtonEnabled()
    {
        return editButton.isEnabled();
    }

    public boolean isPublishButtonEnabled()
    {
        return publishButton.isEnabled();
    }

    public boolean isPreviewButtonEnabled()
    {
        return previewButton.isEnabled();
    }
}
