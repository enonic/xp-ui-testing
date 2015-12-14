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

    private final String IMAGE_COMPONENT_VIEW = "//*[contains(@id,'api.liveedit.image.ImageComponentView')]";

    public final String LAYOUT_COMPONENT = "//div[contains(@id,'api.liveedit.layout.LayoutComponentView')]";

    public final String FIGURE = "//figure[contains(@id,'api.liveedit.image.ImageComponentView')]";

    public final String SHADER_PAGE = "//div[@class='xp-page-editor-shader xp-page-editor-page']";

    private LayoutComponentView layoutComponentView;

    public LiveFormPanel( final TestSession session )
    {
        super( session );
    }

    public LayoutComponentView getLayoutComponentView()
    {
        if ( layoutComponentView == null )
        {
            layoutComponentView = new LayoutComponentView( getSession() );
        }
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
        return findElements( By.xpath( LAYOUT_COMPONENT + FIGURE ) ).size();
    }

    public long getNumberImageComponentsInLayout()
    {
        return findElements( By.xpath( LAYOUT_COMPONENT + IMAGE_COMPONENT_VIEW ) ).stream().filter( WebElement::isDisplayed ).count();
    }

    public boolean isImagePresentInLayout( String imageName )
    {
        String img = String.format( "//img[contains(@src,'%s')]", imageName );
        return isElementDisplayed( LAYOUT_COMPONENT + FIGURE + img );
    }

    public boolean isImagePresent( String imageName )
    {
        String img = String.format( "//img[contains(@src,'%s')]", imageName );
        return isElementDisplayed( FIGURE + img );
    }

    public int getNumberOfImagesByName( String imageName )
    {
        String img = String.format( "//img[contains(@src,'%s')]", imageName );
        return getDisplayedElements( By.xpath( LAYOUT_COMPONENT + FIGURE + img ) ).size();
    }

    public String getBackgroundColor()
    {
        WebElement element = getDisplayedElement( By.xpath( "//body" ) );
        return element.getCssValue( "background-color" );
    }
}
