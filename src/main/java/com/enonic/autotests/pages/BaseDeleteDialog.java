package com.enonic.autotests.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;

/**
 * Base confirm dialog for deleting spaces, contents, accounts
 */
public abstract class BaseDeleteDialog
    extends BaseModalDialog
{
    protected Logger logger = Logger.getLogger( this.getClass() );

    private final String ITEMS_TO_DELETE = "//div[contains(@class,'modal-dialog delete-dialog')]//div[@class='item-list']//h4";

    public static final String DELETE_BUTTON_XPATH =
        "//div[@class='modal-dialog delete-dialog']//div[@class='dialog-buttons']//button[text()='Delete']";

    @FindBy(xpath = DELETE_BUTTON_XPATH)
    private WebElement deleteButton;

    /**
     * The constructor
     *
     * @param session
     * @param spacesToDelete
     */
    public BaseDeleteDialog( TestSession session )
    {
        super( session );
    }

    /**
     * Checks items to delete and click "Delete" button. throws
     * DeleteSpaceException, if list of names for deleting does not contains
     * expected names.
     */
    public void doDelete()
    {

        deleteButton.click();
    }

    public List<String> getContentNameToDelete()
    {
        List<String> names = new ArrayList<>();
        List<WebElement> itemsTodelete = getDriver().findElements( By.xpath( ITEMS_TO_DELETE ) );

        for ( WebElement el : itemsTodelete )
        {
            names.add( el.getText() );
            logger.info( "this item present in the confirm-delete dialog and will be deleted:" + el.getText() );
        }
        return names;
    }

    /**
     * Checks that 'DeleteSpaceDialog' is opened.
     *
     * @return true if dialog opened, otherwise false.
     */
    public boolean isOpened()
    {
        return waitUntilVisibleNoException( By.xpath( getTitleXpath() ), Application.IMPLICITLY_WAIT );
    }

    public boolean verifyIsClosed()
    {
        return waitElementNotVisible( By.xpath( getTitleXpath() ), Application.IMPLICITLY_WAIT );
    }

    public abstract String getTitleXpath();


}
