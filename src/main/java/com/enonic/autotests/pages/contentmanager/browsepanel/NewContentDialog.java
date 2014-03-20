package com.enonic.autotests.pages.contentmanager.browsepanel;

import org.openqa.selenium.By;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.BaseModalDialog;
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel;
import com.enonic.autotests.utils.TestUtils;

import static com.enonic.autotests.utils.SleepHelper.sleep;

/**
 * Content Manager application/add new content/select content type
 */
public class NewContentDialog
    extends BaseModalDialog
{
    private final static String DIALOG_TITLE_XPATH =
        "//div[contains(@class,'modal-dialog')]/div[contains(@class,'dialog-header') and contains(.,'What do you want to create?')]";

    public static String CONTENT_TYPE_NAME = "//li[contains(@class,'content-type-list-item')]//p[text()='%s']";

    private String INPUT_SEARCH = "//div[contains(@class,'column-right')]/input";

    /**
     * The constructor.
     *
     * @param session
     */
    public NewContentDialog( TestSession session )
    {
        super( session );
    }

    /**
     * Checks that 'AddNewContentWizard' is opened.
     *
     * @return true if dialog opened, otherwise false.
     */
    public boolean isOpened()
    {
        return waitUntilVisibleNoException( By.xpath( DIALOG_TITLE_XPATH ), 1 );
    }

    /**
     * Select content type by name.
     *
     * @param contentTypeName the name of a content type.
     */
    public ContentWizardPanel selectContentType( String contentTypeName )
    {

        String ctypeXpath = String.format( CONTENT_TYPE_NAME, contentTypeName );
        boolean isContentNamePresent = waitElementExist( ctypeXpath, 3 );

        if ( !isContentNamePresent )
        {
            throw new TestFrameworkException( "content type with name " + contentTypeName + " was not found!" );
        }

        TestUtils.clickByElement( By.xpath( ctypeXpath ), getDriver() );
        ContentWizardPanel wizard = new ContentWizardPanel( getSession() );
        sleep( 500 );
        return wizard;

    }
}
