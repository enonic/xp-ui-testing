package com.enonic.autotests.pages.form.liveedit;


import java.awt.AWTException;
import java.awt.Robot;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel;
import com.enonic.autotests.pages.contentmanager.wizardpanel.context_window.PageInspectionPanel;
import com.enonic.autotests.utils.NameHelper;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class ContextWindow
    extends Application
{
    private final String DIV_CONTEXT_WINDOW = "//div[contains(@id,'ContextWindow') and not(contains(@class,'hidden'))]";

    private final String EMULATOR_TAB_LINK = DIV_CONTEXT_WINDOW + String.format( TAB_BAR_ITEM, "Emulator" );

    private final String INSERT_TAB_LINK = DIV_CONTEXT_WINDOW + String.format( TAB_BAR_ITEM, "Insert" );

    private final String INSPECT_TAB_LINK = DIV_CONTEXT_WINDOW + String.format( TAB_BAR_ITEM, "Inspect" );

    private final String INSERTABLES_GRID = "//div[contains(@id,'InsertablesGrid')]";

    private final String GRID_ITEM =
        INSERTABLES_GRID + "//div[contains(@class,'grid-row') and descendant::div[@data-portal-component-type ='%s']]";

    private PageInspectionPanel inspectionPanel;

    ContentWizardPanel contentWizardPanel;

    private final String TOOLBAR_DIV = "//div[contains(@id,'ContentWizardToolbar')]";

    @FindBy(xpath = "//li[contains(@class,'tab-bar-item') and @title= 'Insert']")
    private WebElement insertToolbarItem;

    public ContextWindow( final TestSession session )
    {
        super( session );
        contentWizardPanel = new ContentWizardPanel( session );
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
            saveScreenshot( NameHelper.uniqueName( "err_context_wind_load" ) );
            throw new TestFrameworkException( "LIVE EDIT:  ContextWindow was not loaded!" );
        }
    }

    public boolean isContextWindowPresent()
    {
        return waitUntilVisibleNoException( By.xpath( DIV_CONTEXT_WINDOW ), Application.EXPLICIT_NORMAL );
    }

    public ContextWindow clickOnInsertLink()
    {
        if ( !waitUntilVisibleNoException( By.xpath( INSERT_TAB_LINK ), Application.EXPLICIT_NORMAL ) )
        {
            saveScreenshot( "err_insert_link" );
            throw new TestFrameworkException( "Insert link was not found on the ContextWindow!" );
        }
        getDisplayedElement( By.xpath( INSERT_TAB_LINK ) ).click();
        sleep( 500 );
        return this;
    }

    public ContextWindow clickOnEmulatorLink()
    {
        if ( !waitUntilVisibleNoException( By.xpath( EMULATOR_TAB_LINK ), Application.EXPLICIT_NORMAL ) )
        {
            saveScreenshot( "err_emulator_link" );
            throw new TestFrameworkException( "Emulator link was not found on the ContextWindow!" );
        }
        getDisplayedElement( By.xpath( EMULATOR_TAB_LINK ) ).click();
        sleep( 500 );
        return this;
    }

    public ContextWindow clickOnInspectLink()
    {
        if ( !waitUntilVisibleNoException( By.xpath( INSPECT_TAB_LINK ), Application.EXPLICIT_NORMAL ) )
        {
            saveScreenshot( "err_inspect_link" );
            throw new TestFrameworkException( "'inspect' link was not found on the ContextWindow!" );
        }
        getDisplayedElement( By.xpath( INSPECT_TAB_LINK ) ).click();
        sleep( 500 );
        return this;
    }

    /**
     * Drags 'layout' item from the ContextWindow and drop it on the 'Live Edit' frame
     *
     * @param headers
     * @return {@link LayoutComponentView} instance where layout can be selected by name from a options menu
     */

    public UIComponent addLayoutByDragAndDrop( String... headers )
    {
        sleep( 1000 );
        String gridItem = String.format( GRID_ITEM, "layout" );
        WebElement componentForDrag = findElements( By.xpath( gridItem ) ).get( 0 );

        Actions builder = new Actions( getDriver() );
        builder.clickAndHold( componentForDrag ).build().perform();

        WebElement liveEditFrame = getDriver().findElement( By.xpath( Application.LIVE_EDIT_FRAME ) );
        int liveEditFrameX = liveEditFrame.getLocation().x;
        int liveEditFrameY = liveEditFrame.getLocation().y;
        int toolbarHeight = findElements( By.xpath( TOOLBAR_DIV ) ).get( 0 ).getSize().getHeight();

        WebElement regionDiv = findElement( By.xpath( "//div[contains(@id,'api.liveedit.RegionPlaceholder')]" ) );

        showDragHelper( regionDiv, liveEditFrameX, liveEditFrameY, toolbarHeight, headers );
        sleep( 1000 );
        builder.release( regionDiv );
        sleep( 1000 );
        builder.build().perform();
        return new LayoutComponentView( getSession() );
    }

    public UIComponent insertPartByDragAndDrop( String targetDivId, String... headers )
    {
        sleep( 1000 );
        String gridItem = String.format( GRID_ITEM, "part" );
        WebElement componentForDrag = findElement( By.xpath( gridItem ) );

        Actions builder = new Actions( getDriver() );
        builder.clickAndHold( componentForDrag ).build().perform();

        WebElement liveEditFrame = findElement( By.xpath( Application.LIVE_EDIT_FRAME ) );
        int liveEditFrameX = liveEditFrame.getLocation().x;
        int liveEditFrameY = liveEditFrame.getLocation().y;
        int toolbarHeight = findElements( By.xpath( TOOLBAR_DIV ) ).get( 0 ).getSize().getHeight();

        contentWizardPanel.switchToLiveEditFrame();
        String divXpath = String.format( "//div[contains(@id,'%s')]", targetDivId );
        WebElement regionDiv = findElements( By.xpath( divXpath ) ).get( 0 );

        showDragHelper( regionDiv, liveEditFrameX, liveEditFrameY, toolbarHeight, headers );
        sleep( 1000 );
        builder.release( regionDiv );
        sleep( 1000 );
        builder.build().perform();
        return new PartComponentView( getSession() );
    }

    /**
     * Drags 'image' item from the ContextWindow and drop it on the layout on the 'Live Edit' frame
     *
     * @param regionName 'left', 'center' or 'right'
     * @param headers
     * @return {@link ImageComponentView} instance where image can be selected by name from a options menu
     */
    public UIComponent insertImageByDragAndDrop( String regionName, String... headers )
    {
        sleep( 1000 );
        String gridItem = String.format( GRID_ITEM, "image" );
        WebElement componentForDrag = findElements( By.xpath( gridItem ) ).get( 0 );

        Actions builder = new Actions( getDriver() );
        builder.clickAndHold( componentForDrag ).build().perform();

        WebElement liveEditFrame = findElement( By.xpath( Application.LIVE_EDIT_FRAME ) );
        int liveEditFrameX = liveEditFrame.getLocation().x;
        int liveEditFrameY = liveEditFrame.getLocation().y;
        int toolbarHeight = findElements( By.xpath( TOOLBAR_DIV ) ).get( 0 ).getSize().getHeight();

        contentWizardPanel.switchToLiveEditFrame();
        String regionXpath = String.format( "//div[contains(@id,'api.liveedit.RegionPlaceholder')]/p[contains(.,'%s')]", regionName );
        if ( findElements( By.xpath( regionXpath ) ).size() == 0 )
        {
            throw new TestFrameworkException( String.format( "the region: %s was not found!", regionName ) );
        }
        WebElement regionPlaceHolderDiv = findElement( By.xpath( regionXpath ) );

        showDragHelper( regionPlaceHolderDiv, liveEditFrameX, liveEditFrameY, toolbarHeight, headers );
        builder.release( regionPlaceHolderDiv );
        builder.build().perform();
        return new ImageComponentView( getSession() );
    }

    /**
     * Moves mouse over the page and shows a green 'drag helper'
     *
     * @param targetElement
     * @param liveEditFrameX
     * @param liveEditFrameY
     * @param toolbarHeight
     * @param headers
     */
    private void showDragHelper( WebElement targetElement, int liveEditFrameX, int liveEditFrameY, int toolbarHeight, String... headers )
    {
        int mainDivY = targetElement.getLocation().y;
        int mainDivX = targetElement.getLocation().x;
        Robot robot = getRobot();
        robot.setAutoWaitForIdle( true );
        int xOffset = calculateOffsetX( liveEditFrameX );
        robot.mouseMove( mainDivX + xOffset, mainDivY + 30 );
        sleep( 1000 );
        int yOffset = calculateOffsetY( toolbarHeight, liveEditFrameY, headers );
        robot.mouseMove( mainDivX + xOffset, mainDivY + yOffset );
        sleep( 1000 );
    }

    /**
     * click and hold a drug helper and move it to target and release.
     *
     * @param targetElement this is a IMAGE_DROPZONE or LAYOUT_DROPZONE
     * @param dropZone
     */
    private void moveDragHelperToTargetAndReleaseOnDropZone( WebElement targetElement, String dropZone )
    {
        List<WebElement> elements = findElements( By.xpath( "//div[@id='drag-helper' and not(contains(@style, 'display: none'))]" ) );
        WebElement dragHelper = elements.get( 0 );
        Actions builder2 = new Actions( getDriver() );
        builder2.clickAndHold( dragHelper ).build().perform();

        builder2.moveToElement( targetElement ).build().perform();
        sleep( 500 );
        WebElement dropZoneLayout = findElement( By.xpath( dropZone ) );
        builder2.release( dropZoneLayout ).build().perform();
    }

    private int calculateOffsetY( int toolbarHeight, int liveEditFrameY, String... elements )
    {
        WebElement mainDiv = findElements( By.xpath( "//div[contains(@id,'RegionPlaceholder')]" ) ).get( 0 );
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
        WebElement mainDiv = findElements( By.xpath( "//div[contains(@id,'RegionPlaceholder')]" ) ).get( 0 );
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

    public PageInspectionPanel getInspectionPanel()
    {
        if ( inspectionPanel == null )
        {
            return new PageInspectionPanel( getSession() );
        }
        return inspectionPanel;
    }
}