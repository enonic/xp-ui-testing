package com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel;

import org.openqa.selenium.By;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.utils.TestUtils;

public class DependenciesWidgetItemView
    extends Application
{
    private final String DIV_CONTAINER = "//div[contains(@id,'DependenciesWidgetItemView')]";

    public DependenciesWidgetItemView( final TestSession session )
    {
        super( session );
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
