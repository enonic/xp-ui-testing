package com.enonic.autotests.pages.form.liveedit;


import java.awt.AWTException;
import java.awt.Robot;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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
    private final String DIV_CONTEXT_WINDOW =
        "//div[contains(@id,'app.wizard.page.contextwindow.ContextWindow') and not(contains(@class,'hidden'))]";

    private final String DIV_DROP = "//div[@class='live-edit-drop-target-placeholder']";

    private final String DIV_MOVE = "//div[contains(@id,'api.liveedit.RegionPlaceholder')]//p[text()='Drop components here']";


    private final String INSERTABLES_GRID = "//div[contains(@id,'InsertablesGrid')]";

    private final String GRID_ITEM =
        INSERTABLES_GRID + "//div[contains(@class,'grid-row') and descendant::div[@data-live-edit-type ='%s']]";

    private final String LAYOUT_DROPZONE = "//div[contains(@id,'api.liveedit.RegionDropzone') and @class='region-dropzone layout']";

    private final String TOOLBAR_DIV = "//div[contains(@id,'app.wizard.ContentWizardToolbar')]";


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
        String insertButtonXpath = DIV_CONTEXT_WINDOW + "//li[contains(@id,'TabBarItem')]/span[ @title='Insert']";
        if ( findElements( By.xpath( insertButtonXpath ) ).size() == 0 )
        {
            throw new TestFrameworkException( "Insert link was not found on the ContextWindow!" );
        }
        findElements( By.xpath( insertButtonXpath ) ).get( 0 ).click();
        sleep( 1000 );
        return this;
    }


    public UIComponent addComponentByDragAndDrop( String componentName, String regionXpath, String... headers )
    {
        sleep( 3000 );
        String gridItem = String.format( GRID_ITEM, componentName );
        WebElement componentForDrag = findElements( By.xpath( gridItem ) ).get( 0 );

        Actions builder = new Actions( getDriver() );
        builder.clickAndHold( componentForDrag ).build().perform();

        WebElement liveEditFrame = getDriver().findElement( By.xpath( Application.LIVE_EDIT_FRAME ) );
        int liveEditFrameX = liveEditFrame.getLocation().x;
        int liveEditFrameY = liveEditFrame.getLocation().y;
        int toolbarHeight = findElements( By.xpath( TOOLBAR_DIV ) ).get( 0 ).getSize().getHeight();

        NavigatorHelper.switchToLiveEditFrame( getSession() );
        WebElement dropComponentDiv = findElement( By.xpath( "//div[contains(@id,'api.liveedit.RegionPlaceholder')]" ) );
        if ( findElements( By.xpath( "//div[contains(@id,'api.liveedit.RegionView')]" ) ).size() == 0 )
        {
            throw new TestFrameworkException( "the div element was not found!" );
        }
        //WebElement dropComponentDiv = findElements( By.xpath( "//div[contains(@id,'api.liveedit.RegionView')]" ) ).get( 0 );

        int mainDivY = dropComponentDiv.getLocation().y;
        int mainDivX = dropComponentDiv.getLocation().x;

        Robot robot = getRobot();
        robot.setAutoWaitForIdle( true );
        int xOffset = calculateOffsetX( liveEditFrameX );
        robot.mouseMove( mainDivX + xOffset, mainDivY - 10 );
        int yOffset = calculateOffsetY( toolbarHeight, liveEditFrameY, headers );
        robot.mouseMove( mainDivX + xOffset, mainDivY + yOffset );
        sleep( 1000 );
        //RELEASE
        builder.moveToElement( dropComponentDiv ).release( dropComponentDiv );
        sleep( 1000 );
        builder.build().perform();

        WebElement dragHelper = findElements( By.xpath( "//div[@id='drag-helper']" ) ).get( 0 );
        Actions builder2 = new Actions( getDriver() );
        builder2.clickAndHold( dragHelper ).build().perform();

        builder2.moveToElement( dropComponentDiv ).build().perform();
        TestUtils.saveScreenshot( getSession(), "layout_dropzone" );
        WebElement dropZoneLayout = getDriver().findElement( By.xpath( LAYOUT_DROPZONE ) );
        builder2.release( dropZoneLayout ).build().perform();

        if ( componentName.equalsIgnoreCase( "layout" ) )
        {
            return new LayoutComponentView( getSession() );
        }
        if ( componentName.equalsIgnoreCase( "image" ) )
        {

            return new ImageComponentView( getSession() );
        }
        return null;
    }

    private void hoverTo( WebElement webElement )
    {
        String javaScript = "var evObj = document.createEvent('MouseEvents');" +
            "evObj.initMouseEvent(\"mouseover\",true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);" +
            "arguments[0].dispatchEvent(evObj);";

        ( (JavascriptExecutor) getDriver() ).executeScript( javaScript, webElement );
    }


    private int calculateOffsetY( int toolbarHeight, int liveEditFrameY, String... elements )
    {
        WebElement mainDiv = findElement( By.xpath( "//div[contains(@id,'api.liveedit.RegionPlaceholder')]" ) );
        int mainOffsetY = mainDiv.getSize().getHeight() / 2;
        int height = 0;
        for ( int i = 0; i < elements.length; i++ )
        {
            height += findElements( By.xpath( elements[i] ) ).get( 0 ).getSize().getHeight();
        }

        int yOffset = toolbarHeight + height + liveEditFrameY + mainOffsetY;

        return yOffset;
    }

    private int calculateOffsetX( int liveEditFrameX )
    {
        WebElement mainDiv = findElement( By.xpath( "//div[contains(@id,'api.liveedit.RegionPlaceholder')]" ) );
        int mainOffsetX = mainDiv.getSize().getWidth() / 2;
        int xOffset = liveEditFrameX + mainOffsetX;

        return xOffset;
    }

    private Robot getRobot()
    {
        Robot robot = null;
        try
        {
            robot = new Robot();
        }
        catch ( AWTException e )
        {
            getLogger().error( "Robot mouseMove failed" );
        }
        return robot;
    }
}