package com.enonic.autotests.pages.form.liveedit;


import java.util.LinkedList;
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
    private final String SET_TINY_MCE_INNERHTML = "document.getElementById(arguments[0]).innerHTML=arguments[1];";

    public final String LAYOUT_COMPONENT = "//div[contains(@id,'LayoutComponentView')]";

    public final String FIGURE = "//figure[contains(@id,'ImageComponentView')]";

    public final String SHADER_PAGE = "//div[@class='xp-page-editor-shader xp-page-editor-page']";

    public static final String LIVE_FORM_PANEL = "//div[contains(@id,'LiveFormPanel')]";


    private final String IMAGE_COMPONENT_VIEW = "//*[contains(@id,'ImageComponentView')]";

    private final String TEXT_COMPONENT_VIEW = "//div[contains(@id,'TextComponentView')]";

    private LayoutComponentView layoutComponentView;

    private MceToolbar mceToolbar;

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

    public MceToolbar getMceToolbar()
    {
        if ( mceToolbar == null )
        {
            mceToolbar = new MceToolbar( getSession() );
        }
        return mceToolbar;
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
        String input = TEXT_COMPONENT_VIEW + "//div[@class='tiny-mce-here mce-content-body mce-edit-focus']";
        String id = getDisplayedElement( By.xpath( input ) ).getAttribute( "id" );
        setTextIntoArea( id, text );
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

    private void setTextIntoArea( String id, String text )
    {
        getJavaScriptExecutor().executeScript( SET_TINY_MCE_INNERHTML, id, text );
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

    public String getTextFromTextComponent()
    {
        if ( !isElementDisplayed( TEXT_COMPONENT_VIEW + "//section/p" ) )
        {
            saveScreenshot( "err_text_component" );
            throw new TestFrameworkException( "text in the component was not found!" );
        }
        return getDisplayedString( TEXT_COMPONENT_VIEW + "//section/p" );
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
}
