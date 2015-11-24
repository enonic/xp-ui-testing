package com.enonic.autotests.pages.contentmanager.wizardpanel;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.Application;

public class ItemViewPanelPage
    extends Application
{
    public static String VERIFY_TITLE_SPAN_XPATH = "//span[@class='label' and @title='%s']";

    public static String TITLE_SPAN_XPATH = "//span[@class='label' and @title]";

    private static final String TOOLBAR_EDIT_BUTTON_XPATH =
        "//div[contains(@id,'app.view.ContentItemViewToolbar')]//button[child::span[text()='Edit']]";

    private static final String TOOLBAR_DELETE_BUTTON_XPATH =
        "//div[contains(@id,'app.view.ContentItemViewToolbar')]//button[child::span[text()='Delete']]";

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
     */
    public void waitUntilOpened( String displayName )
    {
        String titleXpath = String.format( VERIFY_TITLE_SPAN_XPATH, displayName );
        waitUntilVisible( By.xpath( titleXpath ) );
    }

    public void close()
    {
        closeButton.click();
    }

    /**
     * Press the 'Edit' button and opens a  ContentWizardPanel.
     *
     * @return {@link ContentWizardPanel} instance
     */
    public ContentWizardPanel clickToolbarEdit()
    {
        editButtonToolbar.click();
        ContentWizardPanel wizard = new ContentWizardPanel( getSession() );
        wizard.waitUntilWizardOpened();
        return wizard;
    }

    /**
     * @return title of this page.
     */
    public String getTitle()
    {
        return findElements( By.xpath( TITLE_SPAN_XPATH ) ).get( 0 ).getText();
    }
}
