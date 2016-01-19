package com.enonic.autotests.pages.form;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel;
import com.enonic.autotests.utils.NameHelper;
import com.enonic.autotests.utils.TestUtils;
import com.enonic.xp.data.PropertyTree;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class PageTemplateFormViewPanel
    extends FormViewPanel
{
    public static final String SUPPORTS = "supports";

    public static final String PAGE_CONTROLLER = "pageController";

    ContentWizardPanel contentWizardPanel;

    private String PAGE_DESCRIPTOR_DROP_DOWN_FILTER_INPUT =
        "//div[contains(@id,'PageDescriptorDropdown')]//input[contains(@id,'api.ui.selector.dropdown.DropdownOptionFilterInput')]";

    @FindBy(xpath = "//div[contains(@id,'api.form.FormView')]//input[contains(@class,'option-filter-input')]")
    private WebElement optionFilterInput;

    @FindBy(xpath = "//div[contains(@id,'api.form.FormView')]//input[contains(@name,'menuName')]")
    private WebElement menuNameInput;

    public PageTemplateFormViewPanel( final TestSession session )
    {
        super( session );
        this.contentWizardPanel = new ContentWizardPanel( session );
    }

    @Override
    public FormViewPanel type( final PropertyTree data )
    {
        String supports = data.getString( SUPPORTS );
        selectSupportOption( supports );
        sleep( 2000 );
        selectPageController( data.getString( PAGE_CONTROLLER ) );
        return this;
    }

    private void selectSupportOption( String supports )
    {
        optionFilterInput.sendKeys( supports );
        sleep( 500 );
        String siteContentTypeGridItem = String.format( "//div[contains(@id,'NamesView')]/p[contains(.,'%s')]", supports );
        if ( !isElementDisplayed( siteContentTypeGridItem ) )
        {
            TestUtils.saveScreenshot( getSession(), "err_" + supports );
            throw new TestFrameworkException( "content type with name: " + supports + "  was not found!" );
        }
        //select supports: portal:site
        getDisplayedElement( By.xpath( siteContentTypeGridItem ) ).click();
        sleep( 500 );
    }

    private void selectPageController( String pageName )
    {
        contentWizardPanel.switchToLiveEditFrame();
        if ( !isElementDisplayed( PAGE_DESCRIPTOR_DROP_DOWN_FILTER_INPUT ) )
        {
            TestUtils.saveScreenshot( getSession(), "err_page_controller" );
            throw new TestFrameworkException( "page controller: DropdownOptionFilterInput was not found" );
        }
        getDisplayedElement( By.xpath( PAGE_DESCRIPTOR_DROP_DOWN_FILTER_INPUT ) ).sendKeys( pageName );
        sleep( 1000 );
        //select a 'page name'
        String pageItemXpath = String.format( "//div[contains(@id,'PageDescriptorDropdown')]//h6[text()='%s']", pageName );
        boolean isClickable = waitUntilClickableNoException( By.xpath( pageItemXpath ), Application.EXPLICIT_NORMAL );
        if ( !isElementDisplayed( pageItemXpath ) || !isClickable )
        {
            TestUtils.saveScreenshot( getSession(), "err_" + NameHelper.uniqueName( pageName ) );
            throw new TestFrameworkException( "page controller was not found or not clickable ! " + pageName );
        }
        TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( pageName ) );
        getDisplayedElement( By.xpath( pageItemXpath ) ).click();
    }
}
