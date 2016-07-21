package com.enonic.autotests.pages.form.liveedit;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.utils.TestUtils;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class LayoutComponentView
    extends UIComponent
{
    private final String COMPONENT_CONTAINER = "//div[contains(@id,'LayoutComponentView')]";

    public static String REGION_XPATH = "//div[contains(@id,'RegionView') and descendant::p[contains(.,'%s')]]";

    @FindBy(xpath = COMPONENT_CONTAINER + COMBOBOX_OPTION_FILTER_INPUT)
    private WebElement optionFilterInput;

    public LayoutComponentView( final TestSession session )
    {
        super( session );
    }

    public LiveFormPanel selectLayout( String layoutName )
    {
        optionFilterInput.sendKeys( layoutName );
        sleep( 900 );
        TestUtils.saveScreenshot( getSession(), "try_find" + layoutName );
        if ( !isLayoutExists( layoutName ) )
        {
            TestUtils.saveScreenshot( getSession(), "err_" + layoutName );
            throw new TestFrameworkException( "The layout with name: " + layoutName + "  was not found!" );
        }
        clickOnOptionsItem( layoutName );
        return new LiveFormPanel( getSession() );
    }

    private boolean isLayoutExists( String layoutName )
    {
        return isElementDisplayed( COMPONENT_CONTAINER + String.format( NAMES_ICON_VIEW, layoutName ) );
    }

    private void clickOnOptionsItem( String layoutName )
    {
        getDisplayedElement( By.xpath( COMPONENT_CONTAINER + String.format( NAMES_ICON_VIEW, layoutName ) ) ).click();
    }
}