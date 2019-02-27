package com.enonic.autotests.pages.contentmanager.browsepanel;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.contentmanager.ConfirmContentDeleteDialog;
import com.enonic.autotests.utils.TestUtils;

import static com.enonic.autotests.utils.SleepHelper.sleep;

/**
 * This Dialog appears, when user tries to delete a content.
 */
public class DeleteContentDialog
    extends Application
{
    public static final String CHECKBOX_LABEL_TEXT = "Instantly delete published items";

    public static final String OTHER_ITEMS_WILL_BE_DELETED_TEXT = "Other items that will be deleted";

    public static final String DELETE_ITEM_WITH_CHILD_TITLE = "Delete selected items and its child content";

    private final String CONTAINER_DIV = "//div[contains(@id,'ContentDeleteDialog')]";

    private String CONTENT_STATUS = CONTAINER_DIV +
        "//div[contains(@id,'StatusSelectionItem') and descendant::h6[contains(@class,'main-name') and contains(.,'%s')]]//div[contains(@class,'status')][2]";

    private final String DEPENDANT_LIST = CONTAINER_DIV + "//ul[contains(@id,'DialogDependantList')]";

    private final String HIDE_DEPENDANT_ITEM_LINK = CONTAINER_DIV + "//div[@class='dependants']//h6[@class='dependants-header' and contains(.,'Hide dependent items')]";

    private final String SHOW_DEPENDANT_ITEM_LINK =
        CONTAINER_DIV + "//div[@class='dependants']//h6[@class='dependants-header' and contains(.,'Show dependent items')]";

    private final String OTHER_ITEMS_WILL_DELETED = CONTAINER_DIV + "//div[@class='dependants-body']";

    private final String DEPENDANT_NAMES = DEPENDANT_LIST + "//div[contains(@id,'DependantItemViewer')]" + H6_MAIN_NAME;

    private final String CHECKBOX_DELETE_PUBLISHED_ITEMS =
        CONTAINER_DIV + "//div[contains(@id,'Checkbox') and contains(@class,'instant-delete-check')]";

    private final String CHECKBOX_LABEL = CHECKBOX_DELETE_PUBLISHED_ITEMS + "//label";

    protected Logger logger = Logger.getLogger( this.getClass() );

    private final String ITEMS_TO_DELETE_BY_DISPLAY_NAME = CONTAINER_DIV + "//ul[contains(@id,'DialogItemList')]" + H6_DISPLAY_NAME;

    private final String TITLE_HEADER_XPATH =
        CONTAINER_DIV + "//div[contains(@id,'ModalDialogHeader') and child::h2[contains(.,'Delete item')]]";

    private final String TITLE_TEXT = CONTAINER_DIV + "//div[contains(@id,'ModalDialogHeader')]//h2[@class='title']";

    protected final String DELETE_BUTTON_XPATH = CONTAINER_DIV + "//button/span[contains(.,'Delete')]";

    protected final String CANCEL_BUTTON = CONTAINER_DIV + "//button/span[text()='Cancel']";

    private final String CANCEL_BUTTON_TOP = CONTAINER_DIV + APP_CANCEL_BUTTON_TOP;

    @FindBy(xpath = DELETE_BUTTON_XPATH)
    private WebElement deleteButton;

    @FindBy(xpath = CANCEL_BUTTON)
    private WebElement cancelButton;

    @FindBy(xpath = CHECKBOX_DELETE_PUBLISHED_ITEMS)
    private WebElement checkBoxDiv;

    @FindBy(xpath = CANCEL_BUTTON_TOP)
    private WebElement cancelButtonTop;

    /**
     * The constructor.
     *
     * @param session
     */
    public DeleteContentDialog( TestSession session )
    {
        super( session );
    }

    public boolean isDeleteButtonPresent()
    {
        return deleteButton.isDisplayed();
    }

    public boolean isCancelButtonPresent()
    {
        return cancelButton.isDisplayed();
    }

    public boolean isCheckboxForDeletePublishedItemsDisplayed()
    {
        return isElementDisplayed( CHECKBOX_DELETE_PUBLISHED_ITEMS );
    }

    public String getCheckboxLabelText()
    {
        return getDisplayedString( CHECKBOX_LABEL );
    }

    /**
     * Clocks on the Delete button and waits until the wizard will be closed
     */
    public void doDelete()
    {
        deleteButton.click();
        waitForClosed();
        sleep( 1000 );
    }

    /**
     * If content is not 'online', so need to switch to the browse panel and do not need to wait of closing the wizard
     */
    public void doDeleteAndSwitchToBrowsePanel()
    {
        deleteButton.click();
        switchToBrowsePanelTab();
    }

    public ContentBrowsePanel switchToBrowsePanelTab()
    {
        getDriver().switchTo().window( getSession().getHandleForContentBrowseTab() );
        return new ContentBrowsePanel( getSession() );
    }

    public DeleteContentDialog clickOnShowDependentItemsLink()
    {
        if ( !isShowDependantItemsLinkDisplayed() )
        {
            this.saveScreenshot( "err_show_dependents_items_link" );
            throw new TestFrameworkException( "'Show dependent items' link is not displayed " );
        }
        getDisplayedElement( By.xpath( SHOW_DEPENDANT_ITEM_LINK ) ).click();
        sleep( 1000 );
        return this;
    }
    public DeleteContentDialog clickOnHideDependentItemsLink()
    {
        if ( !isHideDependantItemsLinkDisplayed() )
        {
            this.saveScreenshot( "err_show_dependents_items_link" );
            throw new TestFrameworkException( "'Show dependent items' link is not displayed " );
        }
        getDisplayedElement( By.xpath( HIDE_DEPENDANT_ITEM_LINK ) ).click();
        sleep( 1000 );
        return this;
    }

    public DeleteContentDialog clickOnDeleteButton()
    {
        deleteButton.click();
        return this;
    }

    public void clickOnDeleteButtonAndConfirm( String numberOfContent )
    {
        deleteButton.click();
        ConfirmContentDeleteDialog confirmContentDeleteDialog = new ConfirmContentDeleteDialog( getSession() );
        confirmContentDeleteDialog.waitUntilDialogShown( Application.EXPLICIT_NORMAL );
        confirmContentDeleteDialog.typeNumber( numberOfContent ).clickOnConfirmButton().waitUntilDialogClosed(
            Application.EXPLICIT_NORMAL );
    }

    public DeleteContentDialog clickOnInstantlyCheckbox()
    {
        checkBoxDiv.click();
        return this;
    }

    public boolean isInstantlyDeleteCheckboxChecked()
    {
        String id = checkBoxDiv.getAttribute( "id" );
        return TestUtils.isCheckBoxChecked( getSession(), id );
    }

    public void clickOnCancelButton()
    {
        cancelButton.click();
        waitForClosed();
    }

    public void clickOnCancelTop()
    {
        cancelButtonTop.click();
        waitForClosed();
    }

    public boolean waitForClosed()
    {
        return waitsElementNotVisible( By.xpath( TITLE_HEADER_XPATH ), Application.EXPLICIT_NORMAL );
    }

    public boolean waitForOpened()
    {
        return waitUntilVisibleNoException( By.xpath( TITLE_HEADER_XPATH ), Application.EXPLICIT_NORMAL );
    }

    public boolean isOpened()
    {
        return isElementDisplayed( TITLE_HEADER_XPATH );
    }

    public List<String> getDisplayNamesToDelete()
    {
        List<String> names = new ArrayList<>();
        List<WebElement> itemsToDelete = findElements( By.xpath( ITEMS_TO_DELETE_BY_DISPLAY_NAME ) );
        for ( WebElement el : itemsToDelete )
        {
            names.add( el.getText() );
            logger.info( "this item present in the confirm-delete dialog and will be deleted:" + el.getText() );
        }
        return names;
    }

    public String getContentStatus( String displayName )
    {
        String status = String.format( CONTENT_STATUS, displayName );
        if ( !isElementDisplayed( status ) )
        {
            saveScreenshot( "err_status_" + displayName );
            throw new TestFrameworkException( "status was not found! " + displayName );
        }
        return getDisplayedString( status );
    }

    public boolean isHideDependantItemsLinkDisplayed()
    {
        return isElementDisplayed( HIDE_DEPENDANT_ITEM_LINK );

    }

    public boolean isShowDependantItemsLinkDisplayed()
    {
        return isElementDisplayed( SHOW_DEPENDANT_ITEM_LINK );

    }

    public boolean isDependantListDisplayed()
    {
        return isElementDisplayed( OTHER_ITEMS_WILL_DELETED );

    }

    public String getTitle()
    {
        return getDisplayedString( TITLE_TEXT );
    }

    public List<String> getDependantList()
    {
        return getDisplayedStrings( By.xpath( DEPENDANT_NAMES ) );
    }
}
