package com.enonic.autotests.pages.form.liveedit;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class LayoutComponentView
    extends UIComponent
{
    private final String COMPONENT_CONTAINER = "//div[contains(@id,'api.liveedit.layout.LayoutComponentView')]";

    private final String OPTION_FILTER = "//input[contains(@id,'selector.combobox.ComboBoxOptionFilterInput')]";

    public static String REGION_XPATH = "//div[contains(@id,'api.liveedit.RegionView') and descendant::p[contains(.,'%s')]]";

    @FindBy(xpath = COMPONENT_CONTAINER + OPTION_FILTER)
    private WebElement optionFilterInput;

    public LayoutComponentView( final TestSession session )
    {
        super( session );
    }

    public LiveFormPanel selectLayout( String layoutName )
    {
        optionFilterInput.sendKeys( layoutName );
        sleep( 200 );
        if ( !isLayoutExists( layoutName ) )
        {
            throw new TestFrameworkException( "The layout with name: " + layoutName + "  was not found!" );
        }
        clickOnOptionsItem( layoutName );
        return new LiveFormPanel( getSession() );
    }

    private boolean isLayoutExists( String layoutName )
    {
        return findElements( By.xpath( COMPONENT_CONTAINER + String.format(
            "//div[contains(@id,'api.app.NamesAndIconView')]//h6[@class='main-name' and text()='%s']", layoutName ) ) ).size() > 0;
    }

    private void clickOnOptionsItem( String layoutName )
    {
        findElements( By.xpath( COMPONENT_CONTAINER + String.format(
            "//div[contains(@id,'api.app.NamesAndIconView')]//h6[@class='main-name' and text()='%s']", layoutName ) ) ).get( 0 ).click();
    }
}