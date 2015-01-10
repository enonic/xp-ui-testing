package com.enonic.autotests.pages.form;


import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.wem.api.data.PropertyTree;
import com.enonic.wem.api.schema.content.ContentTypeName;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class PageTemplateFormViewPanel
    extends FormViewPanel
{
    private String MENU_ITEM_CHECKBOX =
        "//div[contains(@id,'api.form.InputView-1') and descendant::div[@title='Menu item']]//div[contains(@id,'api.ui.Checkbox')]/label";

    private String PAGE_DESCRIPTOR_DROP_DOWN_FILTER_INPUT =
        "//div[@id='api.content.page.PageDescriptorDropdown']//input[contains(@id,'api.ui.selector.dropdown.DropdownOptionFilterInput')]";

    @FindBy(xpath = "//div[contains(@id,'api.form.FormView')]//input[contains(@class,'option-filter-input')]")
    private WebElement optionFilterInput;

    @FindBy(xpath = "//div[contains(@id,'api.form.FormView')]//input[contains(@name,'menuName')]")
    private WebElement menuNameInput;

    public PageTemplateFormViewPanel( final TestSession session )
    {
        super( session );
    }

    @Override
    public FormViewPanel type( final PropertyTree data )
    {
        optionFilterInput.sendKeys( "Site" );
        String siteContentTypeGridItem = String.format( "//div[contains(@id,'api.app.NamesView')]/p[@title='%s']", ContentTypeName.site() );
        if ( getDriver().findElements( By.xpath( siteContentTypeGridItem ) ).size() == 0 )
        {
            throw new TestFrameworkException( "content type with name: " + ContentTypeName.site().toString() + "  was not found!" );
        }
        //select supports: system:site
        getDriver().findElements( By.xpath( siteContentTypeGridItem ) ).get( 0 ).click();
        sleep( 500 );
        String nameInMenu = data.getString( "nameInMenu" );
        if ( nameInMenu != null )
        {
            typeMenuTab( nameInMenu );
        }
        selectPageController( data.getString( "pageController" ) );
        return this;
    }

    private void selectPageController( String pageName )
    {
        switchToLiveEditFrame();
        //do filter options:
        findElements( By.xpath( "//input[@id='api.ui.selector.dropdown.DropdownOptionFilterInput']" ) ).get( 0 ).sendKeys( pageName );
        //select a 'Main page'
        findElements( By.xpath( "//div[@id='api.content.page.PageDescriptorDropdown']//h6[@title='Main page']" ) ).get( 0 ).click();

        switchToContentManagerFrame();

    }

    private void switchToLiveEditFrame()
    {
        getDriver().switchTo().window( getDriver().getWindowHandle() );
        //switch from content manager frame to 'main' frame
        getDriver().switchTo().frame( 0 );
        List<WebElement> liveEditFrames = findElements( By.xpath( Application.LIVE_EDIT_FRAME ) );
        if ( liveEditFrames.size() == 0 )
        {
            throw new TestFrameworkException( "Unable to switch to the iframe " );
        }
        //switch to 'live edit' frame
        getDriver().switchTo().frame( liveEditFrames.get( 0 ) );
    }

    private void switchToContentManagerFrame()
    {
        getDriver().switchTo().window( getDriver().getWindowHandle() );
        List<WebElement> cm = findElements( By.xpath( Application.CONTENT_MANAGER_FRAME_XPATH ) );
        getDriver().switchTo().frame( cm.get( 0 ) );
    }

    private void typeMenuTab( String nameInMenu )
    {
        if ( getDriver().findElements( By.xpath( MENU_ITEM_CHECKBOX ) ).size() == 0 )
        {
            throw new TestFrameworkException( "MENU TAB: menu item checkbox   was not found!" );
        }
        //select supports: system:site
        getDriver().findElements( By.xpath( MENU_ITEM_CHECKBOX ) ).get( 0 ).click();
        menuNameInput.sendKeys( nameInMenu );
    }
}
