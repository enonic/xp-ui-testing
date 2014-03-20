package com.enonic.autotests.pages.contentmanager.wizardpanel;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.vo.contentmanager.BaseAbstractContent;

public class ItemViewPanelPage
    extends Application
{

    public static String RED_CIRCLE_XPATH = "//span[@class='tabcount' and contains(.,'%s')]";

    public static String VERIFY_TITLE_SPAN_XPATH = "//span[@class='label' and @title='%s']";

    public static String TITLE_SPAN_XPATH = "//span[@class='label' and @title]";

    public static final String HOME_BUTTON_XPATH = "//div[contains(@class,'x-btn start-button')]";

    private static final String TOOLBAR_EDIT_BUTTON_XPATH =
        "//div[contains(@class,'panel item-view-panel')]//div[@class='toolbar']/button[text()='Edit']";

    private static final String TOOLBAR_DELETE_BUTTON_XPATH =
        "//div[contains(@class,'panel item-view-panel')]//div[@class='toolbar']/button[text()='Delete']";

    @FindBy(xpath = TOOLBAR_EDIT_BUTTON_XPATH)
    private WebElement editButtonToolbar;

    @FindBy(xpath = TOOLBAR_DELETE_BUTTON_XPATH)
    private WebElement deleteButtonToolbar;

    @FindBy(xpath = "//div[contains(@class,'panel item-view-panel')]//div[@class='toolbar']/button[text()='Close']")
    protected WebElement closeButton;

    /**
     * The constructor.
     *
     * @param session
     */
    public ItemViewPanelPage( TestSession session )
    {
        super( session );
    }

    /**
     * Verify that red circle and "New Space" message presented on the top of
     * Page.
     *
     */
    public void waitUntilOpened( String displayName, Integer numberPage )
    {
        String circleXpath = String.format( RED_CIRCLE_XPATH, numberPage.toString() );
        String titleXpath = String.format( VERIFY_TITLE_SPAN_XPATH, displayName );
        waitUntilVisible( By.xpath( circleXpath ) );
        waitUntilVisible( By.xpath( titleXpath ) );
    }

    public void close()
    {
        closeButton.click();
    }

    public ContentWizardPanel clickToolbarEdit()
    {
        editButtonToolbar.click();
        ContentWizardPanel wizard = new ContentWizardPanel( getSession() );
        wizard.waitUntilWizardOpened();
        return wizard;
    }

    public ConfirmationDialog openDeleteConfirmationDialog()
    {
        deleteButtonToolbar.click();
        ConfirmationDialog dialog = new ConfirmationDialog( getSession() );
        boolean isOpened = dialog.verifyIsOpened();
        if ( !isOpened )
        {
            throw new TestFrameworkException( "Confirm 'delete content' dialog was not opened!" );
        }
        return dialog;
    }


    /**
     * @param content
     * @return
     */
    public boolean verifyToolbar( BaseAbstractContent content )
    {
        boolean result = true;
        boolean tmp;

        tmp = deleteButtonToolbar.isDisplayed() && deleteButtonToolbar.isEnabled();
        if ( !tmp )
        {
            getLogger().info( "the delete button has wrong state" );
        }
        result &= tmp;
        tmp = editButtonToolbar.isDisplayed() && editButtonToolbar.isEnabled();
        if ( !tmp )
        {
            getLogger().info( "the edit button has wrong state" );
        }
        result &= tmp;
        return result;
    }

    public String getTitle()
    {
        return findElements( By.xpath( TITLE_SPAN_XPATH ) ).get( 0 ).getText();
    }
}
