package com.enonic.autotests.pages.modules;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.vo.application.ApplicationInfo;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class ApplicationItemStatisticsPanel
    extends Application
{
    private final String STATISTIC_PANEL_CONTAINER = "//div[contains(@id,'ApplicationItemStatisticsPanel')]";

    private final String DATA_CONTAINER = "//div[@class='application-data-container']";

    private final String BUILD_DATE = DATA_CONTAINER + "//li[text()='Build date']/following-sibling::li";

    private final String VERSION = DATA_CONTAINER + "//li[text()='Version']/following-sibling::li";

    private final String KEY = DATA_CONTAINER + "//li[text()='Key']/following-sibling::li";

    private final String SYSTEM_REQUIRED = DATA_CONTAINER + "//li[text()='System Required']/following-sibling::li";

    @FindBy(xpath = STATISTIC_PANEL_CONTAINER + "//div[contains(@class,'drop-down-button')]")
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

    public boolean isBuildDateVisible()
    {
        return findElements( By.xpath( BUILD_DATE ) ).stream().filter( WebElement::isDisplayed ).count() > 0;
    }

    public boolean isVersionVisible()
    {
        return findElements( By.xpath( VERSION ) ).stream().filter( WebElement::isDisplayed ).count() > 0;
    }

    public boolean isKeyVisible()
    {
        return findElements( By.xpath( KEY ) ).stream().filter( WebElement::isDisplayed ).count() > 0;
    }

    public boolean isSystemRequiredVisible()
    {
        return findElements( By.xpath( SYSTEM_REQUIRED ) ).stream().filter( WebElement::isDisplayed ).count() > 0;
    }

    public String getBuildDate()
    {
        return findElements( By.xpath( BUILD_DATE ) ).stream().filter( WebElement::isDisplayed ).findFirst().get().getText();

    }

    public String getVersion()
    {
        return findElements( By.xpath( VERSION ) ).stream().filter( WebElement::isDisplayed ).findFirst().get().getText();

    }

    public String getKey()
    {
        return findElements( By.xpath( KEY ) ).stream().filter( WebElement::isDisplayed ).findFirst().get().getText();

    }

    public String getSystemRequired()
    {
        return findElements( By.xpath( SYSTEM_REQUIRED ) ).stream().filter( WebElement::isDisplayed ).findFirst().get().getText();

    }

    public ApplicationInfo getApplicationInfo()
    {
        return ApplicationInfo.builder().buildDate( getBuildDate() ).version( getVersion() ).key( getKey() ).build();
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
