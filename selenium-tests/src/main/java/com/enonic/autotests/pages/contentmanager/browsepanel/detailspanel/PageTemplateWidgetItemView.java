package com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel;

import org.openqa.selenium.By;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.Application;

/**
 * Created by on 8/3/2017.
 */
public class PageTemplateWidgetItemView
    extends Application
{
    private final static String TEMPLATE_NOT_USED_MESSAGE = "Page Template is not used";

    private final String DIV_CONTAINER = "//div[contains(@id,'PageTemplateWidgetItemView')]";

    private final String TEMPLATE_NOT_USED_XPATH = DIV_CONTAINER + "//p[@class='no-template']";

    private final String PAGE_TEMPLATE_VIEWER = "//div[@class='page-template-viewer']";

    private final String CONTROLLER_NAME = DIV_CONTAINER + P_NAME + "//span";

    private final String CONTROLLER_LINK = DIV_CONTAINER + P_NAME + "//a";

    private final String TEMPLATE_TYPE = DIV_CONTAINER + H6_DISPLAY_NAME;

    public PageTemplateWidgetItemView( final TestSession session )
    {
        super( session );
    }

    public String getControllerName()
    {
        waitUntilVisibleNoException( By.xpath(CONTROLLER_NAME),Application.EXPLICIT_NORMAL );
        return getDisplayedString( CONTROLLER_NAME );
    }

    public String getControllerTextLink()
    {
        return getDisplayedString( CONTROLLER_LINK );
    }

    public String getTemplateType()
    {
        return getDisplayedString( TEMPLATE_TYPE );
    }

    public boolean isDisplayed()
    {
        return isElementDisplayed( DIV_CONTAINER );
    }

    public boolean isTemplateNotUsed()
    {
        return isElementDisplayed( TEMPLATE_NOT_USED_XPATH );
    }

    public String getTemplateNotUsedMessage()
    {
        return getDisplayedString( TEMPLATE_NOT_USED_XPATH );
    }
}
