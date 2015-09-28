package com.enonic.autotests.pages.modules;

import java.util.List;
import java.util.stream.Collectors;

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

    private final String LIST_ITEMS = "/following-sibling::li";

    private final String CONTENT_TYPES_HEADER = DATA_CONTAINER + "//div[contains(@class,'schemas')]//li[text()='Content Types']";

    private final String CONTENT_TYPES = CONTENT_TYPES_HEADER + LIST_ITEMS;

    private final String MIXINS_HEADER = DATA_CONTAINER + "//div[contains(@class,'schemas')]//li[text()='Mixins']";

    private final String MIXINS = MIXINS_HEADER + LIST_ITEMS;

    private final String RELATIONSHIP_TYPES_HEADER = DATA_CONTAINER + "//div[contains(@class,'schemas')]//li[text()='RelationshipTypes']";

    private final String PAGE_HEADER = DATA_CONTAINER + "//div[contains(@class,'descriptors')]//li[text()='Page']";

    private final String PAGES = PAGE_HEADER + LIST_ITEMS;

    private final String PART_HEADER = DATA_CONTAINER + "//div[contains(@class,'descriptors')]//li[text()='Part']";

    private final String PARTS = PART_HEADER + LIST_ITEMS;

    private final String LAYOUT_HEADER = DATA_CONTAINER + "//div[contains(@class,'descriptors')]//li[text()='Layout']";

    private final String LAYOUTS = LAYOUT_HEADER + LIST_ITEMS;


    private final String RELATIONSHIP_TYPES = RELATIONSHIP_TYPES_HEADER + LIST_ITEMS;

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

    public boolean isLayoutHeaderPresent()
    {
        return findElements( By.xpath( LAYOUT_HEADER ) ).stream().filter( WebElement::isDisplayed ).count() > 0;
    }

    public List<String> getLayouts()
    {
        return findElements( By.xpath( LAYOUTS ) ).stream().filter( WebElement::isDisplayed ).map( WebElement::getText ).collect(
            Collectors.toList() );
    }

    public boolean isPartHeaderPresent()
    {
        return findElements( By.xpath( PART_HEADER ) ).stream().filter( WebElement::isDisplayed ).count() > 0;
    }

    public List<String> getParts()
    {
        return findElements( By.xpath( PARTS ) ).stream().filter( WebElement::isDisplayed ).map( WebElement::getText ).collect(
            Collectors.toList() );
    }

    public boolean isPageHeaderPresent()
    {
        return findElements( By.xpath( PAGE_HEADER ) ).stream().filter( WebElement::isDisplayed ).count() > 0;
    }

    public List<String> getPages()
    {
        return findElements( By.xpath( PAGES ) ).stream().filter( WebElement::isDisplayed ).map( WebElement::getText ).collect(
            Collectors.toList() );
    }

    public boolean isContentTypesHeaderPresent()
    {
        return findElements( By.xpath( CONTENT_TYPES_HEADER ) ).stream().filter( WebElement::isDisplayed ).count() > 0;
    }

    public boolean isRelationShipTypesHeaderPresent()
    {
        return findElements( By.xpath( RELATIONSHIP_TYPES_HEADER ) ).stream().filter( WebElement::isDisplayed ).count() > 0;
    }

    public List<String> getRelationShipTypes()
    {
        return findElements( By.xpath( RELATIONSHIP_TYPES ) ).stream().map( WebElement::getText ).collect( Collectors.toList() );
    }

    public List<String> getContentTypes()
    {
        return findElements( By.xpath( CONTENT_TYPES ) ).stream().map( WebElement::getText ).collect( Collectors.toList() );
    }

    public boolean isMixinsHeaderPresent()
    {
        return findElements( By.xpath( MIXINS_HEADER ) ).stream().filter( WebElement::isDisplayed ).count() > 0;
    }

    public List<String> getMixins()
    {
        return findElements( By.xpath( MIXINS ) ).stream().map( WebElement::getText ).collect( Collectors.toList() );
    }

    public boolean isBuildDatePresent()
    {
        return findElements( By.xpath( BUILD_DATE ) ).stream().filter( WebElement::isDisplayed ).count() > 0;
    }

    public boolean isVersionPresent()
    {
        return findElements( By.xpath( VERSION ) ).stream().filter( WebElement::isDisplayed ).count() > 0;
    }

    public boolean isKeyPresent()
    {
        return findElements( By.xpath( KEY ) ).stream().filter( WebElement::isDisplayed ).count() > 0;
    }

    public boolean isSystemRequiredPresent()
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
