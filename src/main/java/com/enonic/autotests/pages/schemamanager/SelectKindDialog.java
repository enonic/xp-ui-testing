package com.enonic.autotests.pages.schemamanager;

import org.openqa.selenium.By;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.BaseModalDialog;

/**
 *
 *
 */
public class SelectKindDialog
    extends BaseModalDialog
{

    private String KIND = "//div[@class='modal-dialog new-schema-dialog']//h6[text()='%s']";

    /**
     * The Constructor.
     *
     * @param session
     */
    public SelectKindDialog( TestSession session )
    {
        super( session );
    }

    /**
     * @param kind
     * @return
     */
    public ContentTypeWizardPanel doSelectKind( String kind )
    {
        String kindXpath = String.format( KIND, kind );
        boolean isPpresent = waitAndFind( By.xpath( kindXpath ) );

        if ( !isPpresent )
        {
            throw new TestFrameworkException( "The kind of contentype" + kind + " was not found!!!" );
        }
        getDriver().findElement( By.xpath( kindXpath ) ).click();
        ContentTypeWizardPanel wizard = new ContentTypeWizardPanel( getSession() );
        wizard.waitUntilWizardOpened();
        return wizard;
    }

    /**
     * @return
     */
    public boolean verifyIsOpened()
    {
        String title = "//div[@class='modal-dialog new-schema-dialog']/div[contains(.,'Select Kind')]";
        return waitUntilVisibleNoException( By.xpath( title ), 2 );
    }

}
