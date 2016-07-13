package com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.utils.TestUtils;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class DependenciesWidgetItemView
    extends Application
{
    private final String DIV_CONTAINER = "//div[contains(@id,'DependenciesWidgetItemView')]";

    private final String OUTBOUND = DIV_CONTAINER + "//div[contains(@class,'dependencies-container outbound')]";

    private final String SHOW_OUTBOUND_BUTTON = DIV_CONTAINER + "//button/span[contains(.,'Show Outbound')]";

    private final String SHOW_INBOUND_BUTTON = DIV_CONTAINER + "//button/span[contains(.,'Show Inbound')]";

    public DependenciesWidgetItemView( final TestSession session )
    {
        super( session );
    }

    @FindBy(xpath = SHOW_OUTBOUND_BUTTON)
    private WebElement showOutboundButton;

    @FindBy(xpath = SHOW_INBOUND_BUTTON)
    private WebElement showInboundButton;

    public boolean isShowOutBoundButtonDisplayed()
    {
        return showOutboundButton.isDisplayed();
    }

    public DependenciesWidgetItemView clickOnShowOutBoundButton()
    {
        showOutboundButton.click();
        sleep( 400 );
        return this;
    }

    public DependenciesWidgetItemView clickOnShowInBoundButton()
    {
        showInboundButton.click();
        sleep( 400 );
        return this;
    }

    public boolean isDisplayed()
    {
        return isElementDisplayed( DIV_CONTAINER );
    }

    public DependenciesWidgetItemView waitUntilPanelDisplayed( long timeout )
    {
        if ( !waitUntilVisibleNoException( By.xpath( DIV_CONTAINER ), timeout ) )
        {
            TestUtils.saveScreenshot( getSession(), "err_dependencies_widget_open" );
            throw new TestFrameworkException( "Dependencies Widget was not shown!" );
        }
        return this;
    }
}
