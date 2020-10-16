package com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel;


import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.utils.NameHelper;
import com.enonic.autotests.utils.WaitHelper;
import com.enonic.autotests.vo.contentmanager.ContentVersion;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class AllContentVersionsView
    extends Application
{
    private final String CONTAINER_WIDGET = "//div[contains(@id,'VersionHistoryView')]";

    private final String VERSIONS_VIEW_UL = "//ul[contains(@id,'VersionHistoryList')]";

    protected final String TAB_MENU_BUTTON = "//div[contains(@id,'TabMenuButton') and child::span[text()='Version History']]";

    private final String VERSION_ITEM = "//li[contains(@class,'version-list-item') and child::div[not(contains(@class,'publish-action'))]]";

    @FindBy(xpath = TAB_MENU_BUTTON)
    WebElement tabMenuButton;

    public AllContentVersionsView( final TestSession session )
    {
        super( session );
    }


    public LinkedList<ContentVersion> getAllVersions()
    {
        waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
        List<WebElement> liElements = null;
        try
        {
            liElements = getDisplayedElements( By.xpath( VERSIONS_VIEW_UL + VERSION_ITEM ) );
        }
        catch ( StaleElementReferenceException e )
        {
            sleep( 1000 );
            liElements = getDisplayedElements( By.xpath( VERSIONS_VIEW_UL + VERSION_ITEM ) );
        }

        return liElements.stream().map( e -> buildContentVersion( e ) ).collect( Collectors.toCollection( LinkedList::new ) );
    }

    private ContentVersion buildContentVersion( WebElement li )
    {
        String statusInElement = ".//div[contains(@class,'status')]";
        String result = null;
        String modifierName = null;
        try
        {
            result = li.findElements( By.xpath( "." + P_SUB_NAME ) ).get( 0 ).getText();
            modifierName = result.substring( result.indexOf( "by" ) + 2 ).trim();
        }
        catch ( StaleElementReferenceException e )
        {
            sleep( 1000 );
            result = li.findElements( By.xpath( "." + P_SUB_NAME ) ).get( 0 ).getText();
            modifierName = result.substring( result.indexOf( "by" + 2 ) ).trim();
        }

        String status = null;
        if ( li.findElements( By.xpath( statusInElement ) ).size() > 0 )
        {
            status = li.findElements( By.xpath( statusInElement ) ).get( 0 ).getText();
        }

        String whenModified = li.findElements( By.xpath( "." + H6_MAIN_NAME ) ).get( 0 ).getText();
        return ContentVersion.builder().modifier( modifierName ).status( status ).modified( whenModified ).build();
    }

    public ContentVersion getActiveVersion()
    {
        if ( !isElementDisplayed( VERSIONS_VIEW_UL + "/li[contains(@class,'content-version-item active')]" ) )
        {
            saveScreenshot( "err_active_version" );
            throw new TestFrameworkException( "active version was not found in the version history panel! " );
        }
        WebElement element = getDisplayedElement( By.xpath( VERSIONS_VIEW_UL + VERSION_ITEM ) );
        return buildContentVersion( element );
    }

    public boolean isVersionInfoExpanded( int index )
    {
        List<WebElement> liElements = getDisplayedElements( By.xpath( VERSIONS_VIEW_UL + VERSION_ITEM ) );
        WebElement version = liElements.get( index );
        String attrClass = version.getAttribute( "class" );
        return attrClass.contains( ( "expanded" ) ) || attrClass.contains( "active" );
    }

    public boolean isVersionActive( int index )
    {
        List<WebElement> liElements = getDisplayedElements( By.xpath( VERSIONS_VIEW_UL + VERSION_ITEM ) );
        WebElement version = liElements.get( index );
        String attrClass = version.getAttribute( "class" );
        return attrClass.contains( "active" );
    }

    public ContentVersionInfoView clickOnVersionAndExpand( int index )
    {
        WebElement versionItem = clickOnVersionItem( index );
        waitAndCheckAttrValue( versionItem, "class", "expanded", EXPLICIT_1 );
        return new ContentVersionInfoView( getSession() );
    }

    private WebElement clickOnVersionItem( int index )
    {
        List<WebElement> liElements = getDisplayedElements( By.xpath( VERSIONS_VIEW_UL + VERSION_ITEM ) );
        if ( liElements.size() == 0 || index >= liElements.size() )
        {
            saveScreenshot( NameHelper.uniqueName( "err_expand_version" ) );
            throw new TestFrameworkException( "required version does not exist!" );
        }

        WebElement versionItem = liElements.get( index );
        Actions builder = new Actions( getDriver() );
        //builder.moveToElement( version ).click().build().perform();
        builder.click( versionItem ).build().perform();
        return versionItem;
    }

    public void clickOnVersionAndCloseView( int index )
    {
        List<WebElement> liElements = getDisplayedElements( By.xpath( VERSIONS_VIEW_UL + VERSION_ITEM ) );
        if ( liElements.size() == 0 || index >= liElements.size() )
        {
            saveScreenshot( NameHelper.uniqueName( "err_expand_version" ) );
            throw new TestFrameworkException( "required version does not exist!" );
        }

        WebElement versionItem = liElements.get( index );
        waitAndCheckAttrValue( versionItem, "class", "expanded", EXPLICIT_1 );
        clickOnVersionItem( index );
    }

    public AllContentVersionsView waitUntilLoaded()
    {
        boolean result = waitUntilVisibleNoException( By.xpath( CONTAINER_WIDGET ), EXPLICIT_LONG );
        if ( !result )
        {
            saveScreenshot( "err_load_version_panel" );
            throw new TestFrameworkException( "Error when Versions panel is loading" );
        }
        return this;
    }

    public boolean waitUntilPanelNotVisible()
    {
        return WaitHelper.waitsElementNotVisible( getDriver(), By.xpath( CONTAINER_WIDGET ), Application.EXPLICIT_NORMAL );
    }
}
