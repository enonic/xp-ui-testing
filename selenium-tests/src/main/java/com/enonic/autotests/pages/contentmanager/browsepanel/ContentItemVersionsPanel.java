package com.enonic.autotests.pages.contentmanager.browsepanel;


import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.utils.TestUtils;
import com.enonic.autotests.utils.WaitHelper;
import com.enonic.autotests.vo.contentmanager.ContentVersion;

public class ContentItemVersionsPanel
    extends Application
{
    private final String CONTAINER_WIDGET = "//div[contains(@id,'VersionsWidgetItemView')]";

    private final String ALL_CONTENT_VERSION_UL = "//ul[contains(@id,'AllContentVersionsView')]";

    protected final String TAB_MENU_BUTTON = "//div[contains(@id,'TabMenuButton') and child::span[text()='Version History']]";

    private final String CONTENT_VERSION_VIEWER = "//div[contains(@id,'api.content.ContentVersionViewer')]";

    @FindBy(xpath = TAB_MENU_BUTTON)
    WebElement tabMenuButton;

    public ContentItemVersionsPanel( final TestSession session )
    {
        super( session );
    }


    public LinkedList<ContentVersion> getAllVersions()
    {
        List<WebElement> liElements =
            getDisplayedElements( By.xpath( ALL_CONTENT_VERSION_UL + "/li[contains(@class,'content-version-item')]" ) );
        return liElements.stream().map( e -> buildContentVersion( e ) ).collect( Collectors.toCollection( LinkedList::new ) );
    }

    private ContentVersion buildContentVersion( WebElement li )
    {
        String statusInElement = ".//div[contains(@class,'status')]";
        String modifierName = li.findElements( By.xpath( "." + H6_DISPLAY_NAME ) ).get( 0 ).getText();
        String status = null;
        if ( li.findElements( By.xpath( statusInElement ) ).size() > 0 )
        {
            status = li.findElements( By.xpath( statusInElement ) ).get( 0 ).getText();
        }

        String whenModified = li.findElements( By.xpath( "." + P_NAME ) ).get( 0 ).getText();
        return ContentVersion.builder().modifier( modifierName ).status( status ).modified( whenModified ).build();
    }

    ///
    public ContentVersion getActiveVersion()
    {
        if ( !isElementDisplayed( ALL_CONTENT_VERSION_UL + "/li[contains(@class,'content-version-item active')]" ) )
        {
            TestUtils.saveScreenshot( getSession(), "err_active_version" );
            throw new TestFrameworkException( "active version was not found in the version history panel! " );
        }
        WebElement element =
            getDisplayedElement( By.xpath( ALL_CONTENT_VERSION_UL + "/li[contains(@class,'content-version-item active')]" ) );
        return buildContentVersion( element );
    }

    public ContentItemVersionsPanel isLoaded()
    {
        if ( !isElementDisplayed( CONTAINER_WIDGET ) )
        {
            TestUtils.saveScreenshot( getSession(), "err_version_panel" );
            throw new TestFrameworkException( "ContentItemVersionsPanel was not loaded!" );
        }
        return this;
    }

    public boolean waitUntilPanelNotVisible()
    {
        return WaitHelper.waitsElementNotVisible( getDriver(), By.xpath( CONTAINER_WIDGET ), Application.EXPLICIT_NORMAL );
    }
}
