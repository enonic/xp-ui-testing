package com.enonic.autotests.pages.form.liveedit;


import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.services.NavigatorHelper;
import com.enonic.autotests.utils.NameHelper;
import com.enonic.autotests.utils.TestUtils;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class ContextWindow
    extends Application
{
    private final String DIV_CONTEXT_WINDOW = "//div[contains(@id,'app.wizard.page.contextwindow.ContextWindow')]";

    private final String DIV_DROP = "//div[@class='live-edit-drop-target-placeholder']";

    private final String INSERTABLES_GRID = "//div[contains(@id,'InsertablesGrid')]";

    private final String GRID_ITEM =
        INSERTABLES_GRID + "//div[contains(@class,'grid-row') and descendant::div[@data-live-edit-type ='%s']]";


    @FindBy(xpath = "//li[contains(@class,'tab-bar-item') and @title= 'Insert']")
    private WebElement insertToolbarItem;

    public ContextWindow( final TestSession session )
    {
        super( session );
    }

    /**
     * Waits until page loaded.
     *
     * @param timeout
     */
    public void waitUntilWindowLoaded( long timeout )
    {
        boolean isPageLoaded = waitAndFind( By.xpath( DIV_CONTEXT_WINDOW ), timeout );
        if ( !isPageLoaded )
        {
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "ContextWindow_bug" ) );
            throw new TestFrameworkException( "LIVE EDIT:  ContextWindow was not loaded!" );
        }
    }

    public boolean isContextWindowPresent()
    {
        return findElements( By.xpath( DIV_CONTEXT_WINDOW ) ).size() > 0;
    }

    public ContextWindow clickOnInsertLink()
    {
        String insertButtonXpath = DIV_CONTEXT_WINDOW + "//li[contains(@id,'TabBarItem') and @title='Insert']";
        findElements( By.xpath( insertButtonXpath ) ).get( 0 ).click();
        sleep( 3000 );
        return this;
    }

    /**
     * Waits until page loaded.
     *
     * @param componentName layout or image or text
     * @param region        image or text will be inserted to this region
     */

    public LiveFormPanel addComponentByDragAndDrop( String componentName, String region )
    {
        String gridItem = String.format( GRID_ITEM, componentName );
        WebElement from = findElements( By.xpath( gridItem ) ).get( 0 );
        Actions builder = new Actions( getDriver() );

        builder.clickAndHold( from ).build().perform();
        WebElement frameTarget = getDriver().findElement( By.xpath( Application.LIVE_EDIT_FRAME ) );
        int offsetX = frameTarget.getLocation().x;

        NavigatorHelper.switchToLiveEditFrame( getSession() );
        WebElement target = getDriver().findElement( By.xpath( "//div[@id='main' and @data-live-edit-id='2']" ) );
        builder.moveToElement( target ).release( target ).build().perform();
        sleep( 500 );
        try
        {
            Robot robot = new Robot();
            robot.setAutoDelay( 500 );
            robot.mouseMove( offsetX - 100, target.getLocation().getY() );
            robot.mouseMove( offsetX + 300, target.getLocation().getY() + 220 );

            robot.mousePress( InputEvent.BUTTON1_MASK );
            robot.mouseRelease( InputEvent.BUTTON1_MASK );
        }
        catch ( AWTException e )
        {
            getLogger().error( "Robot mouseMove failed" );
        }

        sleep( 7000 );
        LiveFormPanel liveFormPanel = new LiveFormPanel( getSession() );
        if ( componentName.equalsIgnoreCase( "layout" ) )
        {

            liveFormPanel.setLayoutComponentView( new LayoutComponentView( getSession() ) );
        }
        return liveFormPanel;
    }
}
