package com.enonic.autotests.pages.form.liveedit;


import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel;
import com.enonic.autotests.pages.contentmanager.wizardpanel.LiveEditComponentContextMenu;
import com.enonic.autotests.services.NavigatorHelper;
import com.enonic.autotests.utils.NameHelper;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class LiveFormPanel
    extends Application
{

    public final String LAYOUT_COMPONENT = "//div[contains(@id,'LayoutComponentView')]";

    public final String FIGURE = "//figure[contains(@id,'ImageComponentView')]";

    public final String SHADER_PAGE = "//div[@class='xp-page-editor-shader xp-page-editor-page']";

    public static final String LIVE_FORM_PANEL = "//div[contains(@id,'LiveFormPanel')]";

    private final String IMAGE_COMPONENT_VIEW = "//*[contains(@id,'ImageComponentView')]";

    private final String TEXT_COMPONENT_VIEW = "//div[contains(@id,'TextComponentView')]";

    private final String FRAGMENT_COMPONENT_VIEW = "//div[contains(@id,'FragmentComponentView')]";

    private final String FRAGMENT_DISPLAY_NAMES = FRAGMENT_COMPONENT_VIEW + "//div[contains(@id,'FragmentComponentView')]";


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

    public ContentWizardPanel switchToContentWizardPanel()
    {
        NavigatorHelper.switchToBrowserTab( getSession(), "content-studio" );
        return new ContentWizardPanel( getSession() );
    }

    public LiveFormPanel typeTextInTextComponent( String text )
    {
        String input = "//div[contains(@id,'TextComponentView') and contains(@class,'editor-focused')]//div[contains(@id,'TextComponentView')]";
        String id = getDisplayedElement( By.xpath( input ) ).getAttribute( "id" );
        setTextInCke( id, text );
        sleep( 500 );
        return this;
    }

    public LiveEditComponentContextMenu clickOnTextComponentAndShowContextMenu()
    {
        WebElement textComponent = getDisplayedElement( By.xpath( TEXT_COMPONENT_VIEW + "/section" ) );
        Actions action = new Actions( getDriver() );
        action.contextClick( textComponent ).build().perform();
        sleep( 1000 );
        LiveEditComponentContextMenu contextMenu = new LiveEditComponentContextMenu( getSession() );
        return contextMenu;
    }

    private void setTextInCke( String id, String text )
    {
        getJavaScriptExecutor().executeScript( SCRIPT_SET_CKE, id, text );
    }

    /**
     * Waits until page loaded.
     *
     * @param timeout
     */
    public void waitUntilPageLoaded( long timeout )
    {
        boolean isPageLoaded = waitAndFind( By.xpath( LIVE_FORM_PANEL ), timeout );
        if ( !isPageLoaded )
        {
            saveScreenshot( NameHelper.uniqueName( "LiveFormPanel_bug" ) );
            throw new TestFrameworkException( "LIVE EDIT:  LiveFormPanel was not loaded!" );
        }
    }

    public boolean isTextComponentPresent()
    {
        return isElementDisplayed( TEXT_COMPONENT_VIEW );
    }

    public long getNumberOfFragments()
    {
        return getNumberOfElements( By.xpath( FRAGMENT_COMPONENT_VIEW ) );
    }

    public List<String> getFragmentDisplayNames()
    {
        return getDisplayedStrings( By.xpath( FRAGMENT_COMPONENT_VIEW ) );
    }

    public long getNumberOfColumnInFragment()
    {
        String columns = FRAGMENT_COMPONENT_VIEW + "//div[contains(@class,'col-sm')]";
        return getNumberOfElements( By.xpath( columns ) );
    }

    public String getTextFromTextComponent()
    {
        String textPath = "//section/p";
        if ( !isElementDisplayed( TEXT_COMPONENT_VIEW + textPath ) )
        {
            saveScreenshot( "err_text_component_not_found" );
            throw new TestFrameworkException( "text in the component was not found!" );
        }
        return getDisplayedString( TEXT_COMPONENT_VIEW + textPath );
    }

    public boolean isLayoutComponentPresent()
    {
        return findElements( By.xpath( LAYOUT_COMPONENT ) ).size() > 0;
    }

    public int getLayoutColumnNumber()
    {
        return findElements( By.xpath( LAYOUT_COMPONENT + "//div[contains(@id,'RegionView')]" ) ).size();
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
        return getNumberOfElements( By.xpath( LAYOUT_COMPONENT + IMAGE_COMPONENT_VIEW ) );
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

    public LinkedList<String> getImageNames()
    {
        LinkedList<String> names = getDisplayedElements( By.xpath( "//figure//img" ) ).stream().map( e -> e.getAttribute( "src" ) ).map(
            s -> getImageNameFromSRC( s ) ).collect( Collectors.toCollection( LinkedList::new ) );

        return names;
    }

    private String getImageNameFromSRC( String src )
    {
        String name = src.substring( src.lastIndexOf( "/" ) + 1 );
        return name;
    }

    public int getNumberOfImagesByName( String imageName )
    {
        String img = String.format( "//img[contains(@src,'%s')]", imageName );
        return getDisplayedElements( By.xpath( LAYOUT_COMPONENT + FIGURE + img ) ).size();
    }

    public String getBackgroundColor()
    {
        if ( !isElementDisplayed( "//body[@data-portal-component-type='page']" ) )
        {
            saveScreenshot( "err_background" );
            throw new TestFrameworkException( "component was not found" );
        }
        WebElement element = getDisplayedElement( By.xpath( "//body[@data-portal-component-type='page']" ) );
        return element.getCssValue( "background-color" );
    }

    public LiveFormPanel doubleClickOnTextComponent( String text )
    {
        String textComponentXpath = TEXT_COMPONENT_VIEW + String.format( "//p[contains(.,'%s')]", text );
        WebElement textComponent = getDisplayedElement( By.xpath( textComponentXpath ) );
        buildActions().click( textComponent ).build().perform();
        buildActions().doubleClick( textComponent ).build().perform();
        sleep( 2000 );
        return this;
    }
}
