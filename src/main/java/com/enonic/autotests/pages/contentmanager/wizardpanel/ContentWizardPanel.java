package com.enonic.autotests.pages.contentmanager.wizardpanel;

import static com.enonic.autotests.utils.SleepHelper.sleep;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.WizardPanel;
import com.enonic.autotests.utils.TestUtils;
import com.enonic.autotests.vo.contentmanager.BaseAbstractContent;

/**
 * 'Content Manager' application, Add new Content Wizard page.
 */
public class ContentWizardPanel
    extends WizardPanel
{
    public static String START_WIZARD_TITLE = "New %s";

    private static final String TOOLBAR_PUBLISH_BUTTON_XPATH =
        "//div[@class='panel wizard-panel']/div[@class='toolbar']//button[text()='Publish']";

    private static final String TOOLBAR_DELETE_BUTTON_XPATH =
        "//div[@class='panel wizard-panel']/div[@class='toolbar']//button[text()='Delete']";

    public static final String TOOLBAR_DUPLICATE_BUTTON_XPATH =
        "//div[@class='panel wizard-panel']/div[@class='toolbar']//button[text()='Duplicate']";

    @FindBy(xpath = TOOLBAR_PUBLISH_BUTTON_XPATH)
    private WebElement toolbarPublishButton;

    @FindBy(xpath = TOOLBAR_DELETE_BUTTON_XPATH)
    private WebElement toolbarDeleteButton;

    @FindBy(xpath = TOOLBAR_DUPLICATE_BUTTON_XPATH)
    private WebElement toolbarDuplicateButton;  

    /**
     * The constructor.
     *
     * @param session
     */
    public ContentWizardPanel( TestSession session )
    {
        super( session );
    }

    /**
     * @return
     */
    public String getTitle()
    {
        List<WebElement> elems = getDriver().findElements( By.xpath( "//div[child::span[@class='tabcount']]/span[@class='label']" ) );
        if ( elems.size() > 0 )
        {
            return elems.get( 0 ).getText();
        }
        else
        {
            return null;
        }
    }

    /**
     * Types data and press the "Save" button from the toolbar.
     *
     * @param content
     */
    public ContentWizardPanel typeData( BaseAbstractContent content )
    {
        sleep( 500 );
        // 1. type a data: 'name' and 'Display Name'.
        waitElementClickable( By.name( "displayName" ), 2 );
        getLogger().info( "types displayName: " + content.getDisplayName() );
        clearAndType( displayNameInput, content.getDisplayName() );
        sleep( 500 );
        if ( StringUtils.isNotEmpty( content.getName() ) )
        {
            waitElementClickable( By.name( "name" ), 2 );
            getLogger().info( "types name: " + content.getName() );
            clearAndType( nameInput, content.getName() );
        }

        TestUtils.saveScreenshot( getSession() );
        // 2. populate main tab
        return this;
    }
}
