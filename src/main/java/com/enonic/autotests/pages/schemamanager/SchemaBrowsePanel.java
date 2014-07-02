package com.enonic.autotests.pages.schemamanager;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.BrowsePanel;
import com.enonic.autotests.pages.contentmanager.browsepanel.NewContentDialog;
import com.enonic.autotests.pages.contentmanager.wizardpanel.ItemViewPanelPage;
import com.enonic.autotests.pages.schemamanager.wizardpanel.ContentTypeWizardPanel;
import com.enonic.autotests.pages.schemamanager.wizardpanel.MixinWizardPanel;
import com.enonic.autotests.pages.schemamanager.wizardpanel.RelationshipWizardPanel;
import com.enonic.autotests.pages.schemamanager.wizardpanel.SchemaWizardPanel;
import com.enonic.autotests.utils.WaitHelper;
import com.enonic.autotests.vo.schemamanger.ContentType;
import com.enonic.autotests.vo.schemamanger.Schema;

import static com.enonic.autotests.utils.SleepHelper.sleep;

/**
 * 'Schema Manager' application, the dashboard page.
 */
public class SchemaBrowsePanel
    extends BrowsePanel
{
    private static String titleXpath = "//button[contains(@class,'home-button') and contains(.,'Schema Manager')]";

    public static final String SCHEMAS_TABLE_CELLS_XPATH = "//table[contains(@class,'x-grid-table')]//td[contains(@class,'x-grid-cell')]";

    private final String REINDEX_BUTTON_XPATH =
        "//div[contains(@id,'app.browse.SchemaBrowseToolbar')]/*[contains(@id, 'api.ui.ActionButton') and child::span[text()='Re-index']]";

    private final String EDIT_BUTTON_XPATH =
        "//div[contains(@id,'app.browse.SchemaBrowseToolbar')]/*[contains(@id, 'api.ui.ActionButton') and child::span[text()='Edit']]";

    private final String DELETE_BUTTON_XPATH =
        "//div[contains(@id,'app.browse.SchemaBrowseToolbar')]/*[contains(@id, 'api.ui.ActionButton') and child::span[text()='Delete']]";

    private String CONTENT_TYPE_NAME_AND_DISPLAY_NAME_IN_TABLE =
        "//table[contains(@class,'x-grid-table')]//div[@class='admin-tree-description' and descendant::h6[contains(.,'%s')] and descendant::p[contains(.,'%s')]]";

    private String THUMBNAIL_FOR_SCHEMA = CONTENT_TYPE_NAME_AND_DISPLAY_NAME_IN_TABLE + "/..//div/img";

    private String CONTEXT_MENU_ITEM = "//li[contains(@id,'api.ui.menu.MenuItem') and text()='%s']";


    private SchemaKindUI selectedSchemaType;


    @FindBy(
        xpath = "//div[contains(@id,'app.browse.SchemaBrowseToolbar')]/*[contains(@id, 'api.ui.ActionButton') and child::span[text()='New']]")
    protected WebElement newButton;

    @FindBy(xpath = DELETE_BUTTON_XPATH)
    protected WebElement deleteButton;

    @FindBy(xpath = EDIT_BUTTON_XPATH)
    protected WebElement editButton;


    @FindBy(xpath = REINDEX_BUTTON_XPATH)
    private WebElement reindexButton;

    private final String EXPORT_BUTTON_XPATH =
        "//div[@id='app.browse.SchemaBrowseToolbar']/*[contains(@id, 'api.ui.ActionButton') and child::span[text()='Export']]";

    @FindBy(xpath = EXPORT_BUTTON_XPATH)
    private WebElement exportButton;


    /**
     * The constructor
     *
     * @param session
     */
    public SchemaBrowsePanel( TestSession session )
    {
        super( session );
    }

    /**
     * Expands a folder, that is supertype for a content.
     *
     * @param superTypeName
     */
    public SchemaBrowsePanel expandSuperTypeFolder( String superTypeName )
    {
        waitsForSpinnerNotVisible();
        if ( superTypeName != null )
        {
            clickByExpander( superTypeName );
        }
        sleep( 1500 );
        return this;
    }

    /**
     * Selects a row with contenttype: clicks by row with content
     *
     * @param contentName        the name of content
     * @param contentDisplayName the display-name of content
     */
    public SchemaBrowsePanel selectRowWithContentType( String contentName, String contentDisplayName )
    {
        String contentTypeXpath = String.format( CONTENT_TYPE_NAME_AND_DISPLAY_NAME_IN_TABLE, contentDisplayName, contentName );
        getLogger().info( "Check that a Content Type to edit is present in the table: " + contentName );
        //3. press the 'Edit' button on the toolbar
        WebElement elem = getDynamicElement( By.xpath( contentTypeXpath ), 3 );
        if ( elem == null )
        {
            throw new TestFrameworkException( "element was not found:" + contentTypeXpath );
        }
        sleep( 500 );
        elem.click();
        WaitHelper.waitUntilElementEnabled( getSession(), By.xpath( EDIT_BUTTON_XPATH ) );
        getLogger().info( "content type with name:" + contentName + " was selected in the table!" );
        specifySelectedSchemaType( contentName, contentDisplayName );
        return this;
    }

    /**
     * Clicks on 'Edit' button on the toolbar.
     *
     * @return {@link ContentTypeWizardPanel} instance.
     */
    public SchemaWizardPanel<?> clickToolbarEdit()
    {
        SchemaWizardPanel<?> wizard = null;
        editButton.click();
        switch ( this.selectedSchemaType )
        {
            case CONTENT_TYPE:
                wizard = new ContentTypeWizardPanel( getSession() );
                break;
            case MIXIN:
                wizard = new MixinWizardPanel( getSession() );
                break;

            case RELATIONSHIP_TYPE:
                wizard = new RelationshipWizardPanel( getSession() );
                break;

            default:
                break;
        }
        sleep( 500 );
        wizard.waitForTextAreaLoaded();
        return wizard;
    }

    /**
     * Clicks on 'Delete' button on the toolbar.
     *
     * @return {@link DeleteContentTypeDialog} instance.
     */
    public DeleteContentTypeDialog clickToolbarDelete()
    {
        deleteButton.click();
        DeleteContentTypeDialog dialog = new DeleteContentTypeDialog( getSession() );
        boolean result = dialog.waitForOpened();
        if ( !result )
        {
            throw new TestFrameworkException( "Confirm delete space dialog was not opened!" );
        }
        return dialog;
    }

    /**
     * Clicks on 'New' button on the toolbar and opens 'NewSchemaDialog'.
     *
     * @return {@link NewSchemaDialog}  instance.
     */
    public NewSchemaDialog clickToolbarNew()
    {
        newButton.click();
        NewSchemaDialog selectDialog = new NewSchemaDialog( getSession() );
        boolean isOpened = selectDialog.verifyIsOpened();
        if ( !isOpened )
        {
            logError( "SelectKindDialog was not opened!" );
            throw new TestFrameworkException( " 'Select content type' dialog was not opened!" );
        }
        return selectDialog;
    }

    /**
     * Waits until page loaded.
     *
     * @param timeout
     */
    public void waituntilPageLoaded( long timeout )
    {
        new WebDriverWait( getSession().getDriver(), timeout ).until(
            ExpectedConditions.visibilityOfElementLocated( By.xpath( SCHEMAS_TABLE_CELLS_XPATH ) ) );
    }

    /**
     * @param session
     * @return true if 'Content Manager' opened and CMSpacesPage showed,
     * otherwise false.
     */
    public static boolean isOpened( TestSession session )
    {
        List<WebElement> elems = session.getDriver().findElements( By.xpath( titleXpath ) );
        if ( elems.size() == 0 )
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     * Returns true if a contenttype is present in the Browse Panel, otherwise returns false
     *
     * @param contentType
     * @return
     */
    public boolean exists( Schema contentType )
    {
        String contentTypeXpath =
            String.format( CONTENT_TYPE_NAME_AND_DISPLAY_NAME_IN_TABLE, contentType.getDisplayNameFromConfig(), contentType.getName() );
        getLogger().info( "Check is contenttype is  present in the  table: " + contentTypeXpath );
        waitsForSpinnerNotVisible();
        List<WebElement> elems = findElements( By.xpath( contentTypeXpath ) );
        if ( elems.size() > 0 )
        {
            getLogger().info( "Content type  was found in the Table! " + "Name:" + contentType.getName() );
            return true;
        }
        else
        {
            getLogger().info( "Content type  was not found in the Table!  " + "Name:" + contentType.getName() );
            return false;
        }
    }

    public SchemaKindUI getSelectedSchemaType()
    {
        return selectedSchemaType;
    }

    public void specifySelectedSchemaType( String contentName, String contentDisplayName )
    {
        String contentTypeThumbnail = String.format( THUMBNAIL_FOR_SCHEMA, contentDisplayName, contentName );
        List<WebElement> elems = getDriver().findElements( By.xpath( contentTypeThumbnail ) );
        if ( elems.size() == 0 )
        {
            throw new TestFrameworkException(
                "The thumbnail for schema element was not found!, please checkout the xpath: " + THUMBNAIL_FOR_SCHEMA );
        }
        String srcAttr = elems.get( 0 ).getAttribute( "src" );
        if ( srcAttr.contains( "ContentType" ) )
        {
            this.selectedSchemaType = SchemaKindUI.CONTENT_TYPE;
        }
        else if ( srcAttr.contains( "RelationshipType" ) )
        {
            this.selectedSchemaType = SchemaKindUI.RELATIONSHIP_TYPE;

        }
        else if ( srcAttr.contains( "Mixin" ) )
        {
            this.selectedSchemaType = SchemaKindUI.MIXIN;
        }
    }

    /**
     * Start to delete a content type from menu in context menu.
     *
     * @param ctype
     * @return {@link DeleteContentTypeDialog} instance.
     */
    public DeleteContentTypeDialog selectDeleteFromContextMenu( ContentType ctype )
    {
        openContextMenu( ctype );
        findElements( By.xpath( String.format( CONTEXT_MENU_ITEM, "Delete" ) ) ).get( 0 ).click();
        DeleteContentTypeDialog dialog = new DeleteContentTypeDialog( getSession() );
        dialog.waitForOpened();
        return dialog;
    }

    /**
     * clicks on a  content type and selects 'Edit' from a context menu.
     *
     * @param ctype
     * @return {@link ContentTypeWizardPanel} instance.
     */
    public ContentTypeWizardPanel selectEditFromContextMenu( ContentType ctype )
    {
        openContextMenu( ctype );
        findElements( By.xpath( String.format( CONTEXT_MENU_ITEM, "Edit" ) ) ).get( 0 ).click();
        ContentTypeWizardPanel wizard = new ContentTypeWizardPanel( getSession() );
        wizard.waitUntilWizardOpened();
        return wizard;
    }


    /**
     * clicks on a  content type and selects 'Open' from a context menu.
     *
     * @param ctype
     * @return
     */
    public ItemViewPanelPage selectOpenFromContextMenu( ContentType ctype )
    {
        openContextMenu( ctype );
        findElements( By.xpath( String.format( CONTEXT_MENU_ITEM, "Open" ) ) ).get( 0 ).click();
        ItemViewPanelPage cinfo = new ItemViewPanelPage( getSession() );
        return cinfo;
    }

    /**
     * clicks on a  content type and selects 'New' from a context menu.
     *
     * @param ctype
     * @return
     */
    public NewContentDialog selectNewFromContextMenu( ContentType ctype )
    {
        openContextMenu( ctype );
        findElements( By.xpath( String.format( CONTEXT_MENU_ITEM, "New" ) ) ).get( 0 ).click();
        NewContentDialog newContentDialog = new NewContentDialog( getSession() );
        newContentDialog.waituntilDialogShowed( Application.EXPLICIT_3 );
        return newContentDialog;
    }

    /**
     * Clicks on content type and opens context menu.
     *
     * @param ctype
     */
    private void openContextMenu( ContentType ctype )
    {
        getLogger().info( "open a context menu on : " + ctype.getName() );
        String contentTypeDescriptionXpath =
            String.format( CONTENT_TYPE_NAME_AND_DISPLAY_NAME_IN_TABLE, ctype.getDisplayNameFromConfig(), ctype.getName() );
        WebElement element = findElement( By.xpath( contentTypeDescriptionXpath ) );
        Actions action = new Actions( getDriver() );

        action.contextClick( element ).build().perform();
        sleep( 500 );
    }

}
