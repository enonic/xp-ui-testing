package com.enonic.autotests.pages.modules;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class ApplicationItemStatisticsPanel
    extends Application
{
    private final String STATISTIC_PANEL = "//div[contains(@id,'ModuleItemStatisticsPanel')]";

    @FindBy(xpath = STATISTIC_PANEL + "//div[contains(@class,'drop-down-button')]")
    private WebElement actionMenuButton;

    public ApplicationItemStatisticsPanel( TestSession session )
    {
        super( session );
    }

    public ApplicationItemStatisticsPanel showActionMenu()
    {
        actionMenuButton.click();
        sleep( 200 );
        return this;
    }

    public ApplicationItemStatisticsPanel selectMenuItem( String itemName )
    {
        if ( findElements(
            By.xpath( String.format( "//li[contains(@id,'api.ui.menu.ActionMenuItem') and text()='%s']", itemName ) ) ).size() == 0 )
        {
            throw new TestFrameworkException( "menu item was not found!" + itemName );
        }
        WebElement item =
            findElement( By.xpath( String.format( "//li[contains(@id,'api.ui.menu.ActionMenuItem') and text()='%s']", itemName ) ) );
        if ( !item.isDisplayed() )
        {
            throw new TestFrameworkException( "menu item is not visible! " + itemName );
        }
        item.click();
        sleep( 500 );

        return this;
    }
}
