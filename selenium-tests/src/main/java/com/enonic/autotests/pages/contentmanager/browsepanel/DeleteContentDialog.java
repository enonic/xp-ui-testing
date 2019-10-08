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
    public static final String OTHER_ITEMS_WILL_BE_DELETED_TEXT = "Other items that will be deleted";

    public static final String DELETE_ITEM_WITH_CHILD_TITLE = "Delete selected items and its child content";

    private final String CONTAINER_DIV = "//div[contains(@id,'ContentDeleteDialog')]";

    private String CONTENT_STATUS = CONTAINER_DIV +
        "//div[contains(@id,'StatusSelectionItem') and descendant::h6[contains(@class,'main-name') and contains(.,'%s')]]//div[contains(@class,'status')][2]";

    private final String DEPENDANT_LIST = CONTAINER_DIV + "//ul[contains(@id,'DialogDependantList')]";

    private final String HIDE_DEPENDANT_ITEM_LINK =
        CONTAINER_DIV + "//div[@class='dependants']//h6[@class='dependants-header' and contains(.,'Hide dependent items')]";

    private final String SHOW_DEPENDANT_ITEM_LINK =
        CONTAINER_DIV + "//div[@class='dependants']//h6[@class='dependants-header' and contains(.,'Show dependent items')]";

    private final String OTHER_ITEMS_WILL_DELETED = CONTAINER_DIV + "//div[@class='dependants-body']";

    private final String DEPENDANT_NAMES = DEPENDANT_LIST + "//div[contains(@id,'DependantItemViewer')]" + H6_MAIN_NAME;

    protected Logger logger = Logger.getLogger( this.getClass() );

    private final String ITEMS_TO_DELETE_BY_DISPLAY_NAME = CONTAINER_DIV + "//ul[contains(@id,'DialogItemList')]" + H6_DISPLAY_NAME;

    private final String TITLE_HEADER_XPATH =
        CONTAINER_DIV + "//div[contains(@id,'ModalDialogHeader') and child::h2[contains(.,'Delete item')]]";

    private final String TITLE_TEXT = CONTAINER_DIV + "//div[contains(@id,'ModalDialogHeader')]//h2[@class='title']";

    protected final String DELETE_NOW_BUTTON_XPATH = CONTAINER_DIV + "//button/span[contains(.,'Delete Now')]";

    protected final String CANCEL_BUTTON = CONTAINER_DIV + "//button/span[text()='Cancel']";

    private final String CANCEL_BUTTON_TOP = CONTAINER_DIV + APP_CANCEL_BUTTON_TOP;

    private final String MARK_AS_DELETED_MENU_ITEM = "//li[contains(@id,'MenuItem') and text()='Mark As Deleted']";

    private final String DELETE_MENU = "//div[contains(@id,'MenuButton')]";

    private final String DELETE_MENU_DROPDOWN = CONTAINER_DIV + DELETE_MENU + DROP_DOWN_HANDLE_BUTTON;

    @FindBy(xpath = DELETE_NOW_BUTTON_XPATH)
    private WebElement deleteNowButton;

    @FindBy(xpath = CANCEL_BUTTON)
    private WebElement cancelButton;

    @FindBy(xpath = DELETE_MENU_DROPDOWN)
    private WebElement menuDropDown;

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

    public boolean isDeleteNowButtonPresent()
    {
        return deleteNowButton.isDisplayed();
    }

    public boolean isCancelButtonPresent()
    {
        return cancelButton.isDisplayed();
    }

    /**
     * Clicks on 'Delete' button and waits until the wizard will be closed
     */
    public void doDelete()
    {
        deleteNowButton.click();
        waitForClosed();
        sleep( 1000 );
    }

    /**
     * Delete the content, close the wizard and switch to the grid:
     */
    public void doDeleteAndSwitchToBrowsePanel()
    {
        deleteNowButton.click();
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

    public DeleteContentDialog clickOnMarkAsDeletedMenuItem()
    {
        waitUntilVisible( By.xpath( DELETE_MENU_DROPDOWN ) );
        menuDropDown.click();
        sleep( 300 );
        String menuItem = CONTAINER_DIV + MARK_AS_DELETED_MENU_ITEM;
        findElement( By.xpath( menuItem ) ).click();
        this.waitForClosed();
        return this;
    }

    public DeleteContentDialog clickOnDeleteNowButton()
    {
        deleteNowButton.click();
        sleep( 1000 );
        return this;
    }

    public void clickOnDeleteNowButtonAndConfirm( String numberOfContent )
    {
        deleteNowButton.click();
        ConfirmContentDeleteDialog confirmContentDeleteDialog = new ConfirmContentDeleteDialog( getSession() );
        confirmContentDeleteDialog.waitUntilDialogShown( Application.EXPLICIT_NORMAL );
        confirmContentDeleteDialog.typeNumber( numberOfContent ).clickOnConfirmButton().waitUntilDialogClosed(
            Application.EXPLICIT_NORMAL );
    }

    public void clickOnMarkAsDeletedMenuItemAndConfirm( String numberOfContent )
    {
        clickOnMarkAsDeletedMenuItem();
        ConfirmContentDeleteDialog confirmContentDeleteDialog = new ConfirmContentDeleteDialog( getSession() );
        confirmContentDeleteDialog.waitUntilDialogShown( Application.EXPLICIT_NORMAL );
        confirmContentDeleteDialog.typeNumber( numberOfContent ).clickOnConfirmButton().waitUntilDialogClosed(
            Application.EXPLICIT_NORMAL );
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

    public void waitForOpened()
    {
        waitUntilVisible( By.xpath( TITLE_HEADER_XPATH ) );
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
