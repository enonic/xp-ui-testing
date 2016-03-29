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

import static com.enonic.autotests.utils.SleepHelper.sleep;

/**
 * This Dialog appears, when customer try to delete a content.
 */
public class DeleteContentDialog
    extends Application
{
    private final String CONTAINER_DIV = "//div[contains(@id,'ContentDeleteDialog')]";

    protected Logger logger = Logger.getLogger( this.getClass() );

    private final String ITEMS_TO_DELETE = CONTAINER_DIV + "//div[@class='item-list']//h6";

    private final String TITLE_HEADER_XPATH =
        CONTAINER_DIV + "//div[contains(@id,'ModalDialogHeader') and child::h2[text()='Delete item']]";

    private final String TITLE_TEXT = CONTAINER_DIV + "//div[contains(@id,'ModalDialogHeader')]//h2[@class='title']";

    protected final String DELETE_BUTTON_XPATH = CONTAINER_DIV + "//button/span[contains(.,'Delete')]";

    protected final String CANCEL_BUTTON = CONTAINER_DIV + "//button/span[text()='Cancel']";

    @FindBy(xpath = DELETE_BUTTON_XPATH)
    private WebElement deleteButton;

    @FindBy(xpath = CANCEL_BUTTON)
    private WebElement cancelButton;

    /**
     * The constructor.
     *
     * @param session
     */
    public DeleteContentDialog( TestSession session )
    {
        super( session );

    }

    public void doDelete()
    {
        deleteButton.click();
        boolean isClosed = waitForClosed();
        if ( !isClosed )
        {
            throw new TestFrameworkException( "Confirm 'delete content' dialog was not closed!" );
        }
        sleep( 1000 );
    }

    public void pressCancelButton()
    {
        cancelButton.click();
        waitForClosed();
    }

    public boolean waitForClosed()
    {
        return waitElementNotVisible( By.xpath( TITLE_HEADER_XPATH ), Application.EXPLICIT_NORMAL );
    }

    public boolean waitForOpened()
    {
        return waitUntilVisibleNoException( By.xpath( TITLE_HEADER_XPATH ), Application.EXPLICIT_NORMAL );
    }

    public boolean isOpened()
    {
        return isElementDisplayed( TITLE_HEADER_XPATH );
    }

    public List<String> getContentNameToDelete()
    {
        List<String> names = new ArrayList<>();
        List<WebElement> itemsToDelete = getDriver().findElements( By.xpath( ITEMS_TO_DELETE ) );

        for ( WebElement el : itemsToDelete )
        {
            names.add( el.getText() );
            logger.info( "this item present in the confirm-delete dialog and will be deleted:" + el.getText() );
        }
        return names;
    }

    public String getTitle()
    {
        return getDisplayedString( TITLE_TEXT );
    }
}
