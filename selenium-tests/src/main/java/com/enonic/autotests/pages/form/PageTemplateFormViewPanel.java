package com.enonic.autotests.pages.form;


import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel;
import com.enonic.autotests.utils.NameHelper;
import com.enonic.xp.data.PropertyTree;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class PageTemplateFormViewPanel
    extends FormViewPanel
{
    public static final String SUPPORTS = "supports";

    public static final String PAGE_CONTROLLER = "pageController";

    private ContentWizardPanel contentWizardPanel;

    private final String SUPPORT_OPTION_FILTER_INPUT =
        FORM_VIEW + "//div[contains(@id,'ContentTypeFilter')]//input[contains(@class,'option-filter-input')]";

    private String PAGE_DESCRIPTOR_DROP_DOWN_FILTER_INPUT = "//div[contains(@id,'PageDescriptorDropdown')]" + DROPDOWN_OPTION_FILTER_INPUT;

    private String SELECTED_OPTION_VIEW_BY_DISPLAY_NAME =
        "//div[contains(@id,'ContentTypeSelectedOptionView') and descendant::h6[text()='%s']]";

    private String REMOVE_BUTTON = SELECTED_OPTION_VIEW_BY_DISPLAY_NAME + "//a[@class='remove']";

    @FindBy(xpath = SUPPORT_OPTION_FILTER_INPUT)
    private WebElement optionFilterInput;

    public PageTemplateFormViewPanel( final TestSession session )
    {
        super( session );
        this.contentWizardPanel = new ContentWizardPanel( session );
    }

    @Override
    public FormViewPanel type( final PropertyTree data )
    {
        String supports = data.getString( SUPPORTS );
        if ( StringUtils.isNotEmpty( supports ) )
        {
            selectSupportOption( supports );
        }
        sleep( 2000 );
        selectPageController( data.getString( PAGE_CONTROLLER ) );
        return this;
    }

    public boolean isSupportOptionFilterDisplayed()
    {
        return optionFilterInput.isDisplayed();
    }

    public void selectSupportOption( String supports )
    {
        optionFilterInput.sendKeys( supports );
        sleep( 500 );
        String siteContentTypeGridItem = String.format( "//div[contains(@id,'NamesView')]/p[contains(.,'%s')]", supports );
        if ( !isElementDisplayed( siteContentTypeGridItem ) )
        {
            saveScreenshot( "err_" + supports );
            throw new TestFrameworkException( "content type with name: " + supports + "  was not found!" );
        }
        //select supports: portal:site
        getDisplayedElement( By.xpath( siteContentTypeGridItem ) ).click();
        sleep( 500 );
    }

    public PageTemplateFormViewPanel removeSupportOption( String optionName )
    {
        String xpath = String.format( REMOVE_BUTTON, optionName );
        getDisplayedElement( By.xpath( xpath ) ).click();
        sleep( 300 );
        return this;
    }

    public void selectPageController( String pageName )
    {
        contentWizardPanel.switchToLiveEditFrame();
        if ( !isElementDisplayed( PAGE_DESCRIPTOR_DROP_DOWN_FILTER_INPUT ) )
        {
            saveScreenshot( "err_page_controller" );
            throw new TestFrameworkException( "page controller: DropdownOptionFilterInput was not found" );
        }
        getDisplayedElement( By.xpath( PAGE_DESCRIPTOR_DROP_DOWN_FILTER_INPUT ) ).sendKeys( pageName );
        sleep( 1000 );
        //select a 'page name'
        String pageItemXpath = String.format( "//div[contains(@id,'PageDescriptorDropdown')]//h6[text()='%s']", pageName );
        boolean isClickable = waitUntilClickableNoException( By.xpath( pageItemXpath ), Application.EXPLICIT_NORMAL );
        if ( !isElementDisplayed( pageItemXpath ) || !isClickable )
        {
            saveScreenshot( "err_" + pageName );
            throw new TestFrameworkException( "page controller was not found or not clickable ! " + pageName );
        }
        saveScreenshot( NameHelper.uniqueName( pageName ) );
        getDisplayedElement( By.xpath( pageItemXpath ) ).click();
        waitUntilVisibleNoException( By.xpath( "//body[@data-portal-component-type='page']" ), Application.EXPLICIT_NORMAL );

    }
}
