package com.enonic.autotests.pages.usermanager.browsepanel;


import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.BaseDeleteDialog;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class DeleteUserItemDialog
    extends BaseDeleteDialog
{
    private final String TITLE_XPATH =
        "//div[contains(@id,'ConfirmationDialog')]//div[contains(@id,'api.ui.dialog.ModalDialogHeader') and child::h2[text()='Confirmation']]";

    private final String YES_BUTTON_XPATH =
        "//div[contains(@id,'ModalDialogButtonRow')]//button[contains(@id,'DialogButton') and child::span[text()='Yes']]";

    @FindBy(xpath = YES_BUTTON_XPATH)
    private WebElement yesButton;

    /**
     * The constructor.
     *
     * @param session
     */
    public DeleteUserItemDialog( TestSession session )
    {
        super( session );

    }

    public void doDelete()
    {
        yesButton.click();
        boolean isClosed = waitForClosed();
        if ( !isClosed )
        {
            throw new TestFrameworkException( "Confirm 'delete content' dialog was not closed!" );
        }
        sleep( 1000 );
    }

    @Override
    public String getTitleXpath()
    {
        return TITLE_XPATH;
    }

}
