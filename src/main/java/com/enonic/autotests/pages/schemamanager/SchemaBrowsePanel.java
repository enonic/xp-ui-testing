package com.enonic.autotests.pages.schemamanager;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.BrowsePanel;
import com.enonic.autotests.pages.schemamanager.wizardpanel.ContentTypeWizardPanel;
import com.enonic.autotests.pages.schemamanager.wizardpanel.MixinWizardPanel;
import com.enonic.autotests.pages.schemamanager.wizardpanel.RelationshipWizardPanel;
import com.enonic.autotests.pages.schemamanager.wizardpanel.SchemaWizardPanel;
import com.enonic.autotests.utils.WaitHelper;
import com.enonic.autotests.vo.schemamanger.ContentType;

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

    private SchemaType selectedSchemaType;


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

    private String CONTENT_TYPE_NAME_AND_DISPLAY_NAME_IN_TABLE =
        "//table[contains(@class,'x-grid-table')]//div[@class='admin-tree-description' and descendant::h6[contains(.,'%s')] and descendant::p[contains(.,'%s')]]";

    private String THUMBNAIL_FOR_SCHEMA = CONTENT_TYPE_NAME_AND_DISPLAY_NAME_IN_TABLE + "/..//div/img";

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
        sleep( 500 );
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

        wizard.waitUntilWizardOpened();
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
     * Clicks on 'New' button on the toolbar and opens 'NewContentDialog'.
     *
     * @return {@link NewContentDialog}  instance.
     */
    public NewContentDialog clickToolbarNew()
    {
        newButton.click();
        NewContentDialog selectDialog = new NewContentDialog( getSession() );
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
    public boolean exists( ContentType contentType )
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

    public SchemaType getSelectedSchemaType()
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
            this.selectedSchemaType = SchemaType.CONTENT_TYPE;
        }
        else if ( srcAttr.contains( "RelationshipType" ) )
        {
            this.selectedSchemaType = SchemaType.RELATIONSHIP_TYPE;

        }
        else if ( srcAttr.contains( "Mixin" ) )
        {
            this.selectedSchemaType = SchemaType.MIXIN;
        }
    }
}
