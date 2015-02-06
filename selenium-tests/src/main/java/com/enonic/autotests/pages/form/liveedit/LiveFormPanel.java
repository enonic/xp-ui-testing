package com.enonic.autotests.pages.form.liveedit;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.utils.NameHelper;
import com.enonic.autotests.utils.TestUtils;

public class LiveFormPanel
    extends Application
{
    private final String PANEL_DIV = "//div[contains(@id,'app.wizard.page.LiveFormPanel')]";

    public final String LAYOUT_COMPONENT = "//div[contains(@id,'api.liveedit.layout.LayoutComponentView')]";

    public final String IMAGE_COMPONENT_VIEW = "//div[contains(@id,'api.liveedit.image.ImageComponentView')]";

    public final String SHADER_PAGE = "//div[@class='shader page']";

    private LayoutComponentView layoutComponentView;

    public LiveFormPanel( final TestSession session )
    {
        super( session );
    }

    public LayoutComponentView getLayoutComponentView()
    {
        return layoutComponentView;
    }

    public void setLayoutComponentView( LayoutComponentView layoutComponentView )
    {
        this.layoutComponentView = layoutComponentView;
    }

    /**
     * Waits until page loaded.
     *
     * @param timeout
     */
    public void waitUntilPageLoaded( long timeout )
    {
        boolean isPageLoaded = waitAndFind( By.xpath( PANEL_DIV ), timeout );
        if ( !isPageLoaded )
        {
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "LiveFormPanel_bug" ) );
            throw new TestFrameworkException( "LIVE EDIT:  LiveFormPanel was not loaded!" );
        }
    }

    public boolean isLayoutComponentPresent()
    {
        return findElements( By.xpath( LAYOUT_COMPONENT ) ).size() > 0;
    }

    public int getLayoutColumnNumber()
    {
        return findElements( By.xpath( LAYOUT_COMPONENT + "//div[contains(@id,'liveedit.RegionView')]" ) ).size();

    }

    public boolean isShaderDisplayed()
    {
        if ( findElements( By.xpath( SHADER_PAGE ) ).size() == 0 )
        {
            return false;
        }
        WebElement elem = findElements( By.xpath( SHADER_PAGE ) ).get( 0 );
        String style = elem.getAttribute( "style" );
        return !style.contains( "display: none" );
    }

    public int getNumberImagesInLayout()
    {
        return findElements( By.xpath( LAYOUT_COMPONENT + IMAGE_COMPONENT_VIEW ) ).size();
    }

}
