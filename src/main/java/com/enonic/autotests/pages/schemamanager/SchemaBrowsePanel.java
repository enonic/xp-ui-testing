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
        "//div[contains(@class,'x-toolbar-item')]//button[contains(@class,'x-btn-center') and descendant::span[contains(.,'Re-index')]]";

    @FindBy(xpath = REINDEX_BUTTON_XPATH)
    private WebElement reindexButton;

    private final String EXPORT_BUTTON_XPATH =
        "//div[contains(@class,'x-toolbar-item')]//button[contains(@class,'x-btn-center') and descendant::span[contains(.,'Export')]]";

    @FindBy(xpath = EXPORT_BUTTON_XPATH)
    private WebElement exportButton;

    private String CONTENTTYPE_NAME_AND_DISPLAYNAME_IN_TABLE =
        "//table[contains(@class,'x-grid-table')]//div[@class='admin-tree-description' and descendant::h6[contains(.,'%s')] and descendant::p[contains(.,'%s')]]";

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
     * @param contentTypeToEdit
     * @param newContentType
     */
    public void doEditContentType( ContentType contentTypeToEdit, ContentType newContentType )
    {
        ContentTypeWizardPanel wizard = doOpenContentTypeForEdit( contentTypeToEdit );
        getLogger().info( "## ContentTypeWizardPanel  should be opened, waits title: " + contentTypeToEdit.getName() );
        wizard.waitUntilWizardOpened( 1 );
        wizard.doTypeDataSaveAndClose( newContentType );
    }

    /**
     * Expands a folder, that is supertype for a content.
     *
     * @param superTypeName
     */
    private void doExpandSuperTypeFolder( String superTypeName )
    {
        if ( superTypeName != null )
        {
            doExpandFolder( superTypeName );
        }
    }

    /**
     * Selects a row with contenttype: clicks by row with content
     *
     * @param contentName        the name of content
     * @param contentDisplayName the display-name of content
     */
    private void selectRowWithContentType( String contentName, String contentDisplayName )
    {
        String contentTypeXpath = String.format( CONTENTTYPE_NAME_AND_DISPLAYNAME_IN_TABLE, contentDisplayName, contentName );
        getLogger().info( "Check that a Content Type to edit is present in the table: " + contentName );
        //3. click by 'Edit' button on toolbar
        WebElement elem = getDynamicElement( By.xpath( contentTypeXpath ), 3 );
        if ( elem == null )
        {
            throw new TestFrameworkException( "element was not found:" + contentTypeXpath );
        }
        sleep( 500 );
        elem.click();
        getLogger().info( "content type with name:" + contentName + " was selected in the table!" );
    }

    /**
     * Expands a supertype folder, click by row with contentype and clicks by 'Edit' button.
     *
     * @param contentTypeToEdit
     * @return {@ContentTypeWizardPanel} insatnce.
     */
    public ContentTypeWizardPanel doOpenContentTypeForEdit( ContentType contentTypeToEdit )
    {
        String superTypeName = contentTypeToEdit.getSuperTypeNameFromConfig();
        doExpandSuperTypeFolder( superTypeName );
        //2. select a content type in a grid
        selectRowWithContentType( contentTypeToEdit.getName(), contentTypeToEdit.getDisplayNameFromConfig() );
        WaitHelper.waitUntilElementEnabled( getSession(), By.xpath( EDIT_BUTTON_XPATH ) );
        editButton.click();
        ContentTypeWizardPanel wizard = new ContentTypeWizardPanel( getSession() );
        wizard.waitUntilWizardOpened( 1 );
        return wizard;
    }

    /**
     * Select a contentype or mixin or relationship and click by 'Delete' button in toolbar.
     *
     * @param contentTypeToDelete
     */
    public void doDeleteContentType( ContentType contentTypeToDelete )
    {
        String supertype = contentTypeToDelete.getSuperTypeNameFromConfig();
        doExpandSuperTypeFolder( supertype );

        selectRowWithContentType( contentTypeToDelete.getName(), contentTypeToDelete.getDisplayNameFromConfig() );

        confirmAndDelete( contentTypeToDelete.getName() );

    }

    /**
     * Clicks by 'Delete' button confirms and delete a contentype.
     *
     * @param contentTypeName
     */
    private void confirmAndDelete( String contentTypeName )
    {
        // wait for deleteButton(in toolbar) is enabled
        WaitHelper.waitUntilElementEnabled( getSession(), By.xpath( DELETE_BUTTON_XPATH ) );
        // click by 'delete' button
        deleteButton.click();

        DeleteContentTypeDialog dialog = new DeleteContentTypeDialog( getSession() );
        boolean result = dialog.isOpened();
        if ( !result )
        {
            throw new TestFrameworkException( "Confirm delete space dialog was not opened!" );
        }
        //confirm deleting
        dialog.doDelete();
        boolean isClosed = dialog.verifyIsClosed();
        if ( !isClosed )
        {
            throw new TestFrameworkException( "Confirm delete space dialog was not closed!" );
        }
        getLogger().info( "The Contentent Type  with name: " + contentTypeName + " was deleted!" );
    }

    /**
     * @param contentType
     * @param isWizardShouldBeClosed
     */
    public void doAddContentType( ContentType contentType, boolean isWizardShouldBeClosed )
    {
        ContentTypeWizardPanel wizard = doOpenContentTypeWizard( contentType.getKind().getValue() );
        if ( isWizardShouldBeClosed )
        {
            wizard.doTypeDataSaveAndClose( contentType );
        }
        else
        {
            wizard.doTypeDataAndSave( contentType );
        }
    }

    /**
     * Clicks by 'New' button from the toolbar, opens modal dialog with title: 'Select Kind', selects
     * <br>a kind and opens a contentype wizard.
     *
     * @param kind
     * @return {@ContentTypeWizardPanel} instance.
     */
    public ContentTypeWizardPanel doOpenContentTypeWizard( String kind )
    {
        newButton.click();
        SelectKindDialog selectDialog = new SelectKindDialog( getSession() );
        boolean isOpened = selectDialog.verifyIsOpened();
        if ( !isOpened )
        {
            logError( "SelectKindDialog was not opened!" );
            throw new TestFrameworkException( String.format( "Error during add new content type  %s, dialog was not opened!", kind ) );
        }
        getLogger().info( "SelectKindDialog, content type should be selected:" + kind );
        ContentTypeWizardPanel wizard = selectDialog.doSelectKind( kind );
        return wizard;
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
     *         otherwise false.
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
    public boolean isContentTypePresentInTable( ContentType contentType )
    {
        String superTypeName = contentType.getSuperTypeNameFromConfig();
        doExpandSuperTypeFolder( superTypeName );
        String contentTypeXpath =
            String.format( CONTENTTYPE_NAME_AND_DISPLAYNAME_IN_TABLE, contentType.getDisplayNameFromConfig(), contentType.getName() );
        getLogger().info( "Check is Space present in table: " + contentTypeXpath );

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
}
