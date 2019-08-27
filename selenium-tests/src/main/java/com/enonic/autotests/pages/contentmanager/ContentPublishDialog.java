package com.enonic.autotests.pages.contentmanager;


import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.contentmanager.issue.CreateIssueDialog;
import com.enonic.autotests.utils.NameHelper;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class ContentPublishDialog
    extends Application
{

    public static final String DIALOG_SUBHEADER_INVALID_CONTENT_PUBLISH = "Invalid item(s) prevent publishing";

    public static final String DIALOG_TITLE = "Publishing Wizard";

    private final String DIALOG_CONTAINER = "//div[contains(@id,'ContentPublishDialog')]";

    private final String TITLE_XPATH = DIALOG_CONTAINER + "//h2[@class='title']";

    private final String PUBLISH_BUTTON =
        DIALOG_CONTAINER + "//button[contains(@id,'DialogButton') and child::span[contains(.,'Publish Now')]]";

    private final String CANCEL_BUTTON_TOP = DIALOG_CONTAINER + APP_CANCEL_BUTTON_TOP;

    private final String CANCEL_BUTTON_BOTTOM = DIALOG_CONTAINER + "//button[contains(@class,'cancel-button-bottom')]";

    private final String INCLUDE_CHILD_TOGGLER = DIALOG_CONTAINER + "//div[contains(@id,'IncludeChildrenToggler')]";

    private final String DEPENDANT_LIST = DIALOG_CONTAINER + "//ul[contains(@id,'PublishDialogDependantList')]";

    private final String DEPENDANT_NAMES = DEPENDANT_LIST + "//div[contains(@id,'DependantItemViewer')]" + H6_MAIN_NAME;

    private final String OTHER_ITEMS_WILL_BE_PUBLISHED = DIALOG_CONTAINER + "//p[@class='dependants-desc']";

    private final String ITEM_LIST = "//ul[contains(@id,'PublishDialogItemList')]";

    private final String DISPLAY_NAMES_OF_CONTENTS_TO_PUBLISH = DIALOG_CONTAINER + ITEM_LIST + H6_DISPLAY_NAME;

    private final String NAMES_OF_CONTENTS_TO_PUBLISH = DIALOG_CONTAINER + ITEM_LIST + P_NAME;

    private final String SHOW_DEPENDANT_ITEMS_LINK = DIALOG_CONTAINER + "//div[@class='dependants']/h6[contains(.,'Show dependent items')]";

    private String ITEM_ROW_TO_PUBLISH_BY_DISPLAY_NAME =
        ITEM_LIST + "//div[contains(@id,'StatusSelectionItem') and descendant::h6[contains(@class,'main-name') and contains(.,'%s')]]";

    private String DEPENDANT_ROW_TO_PUBLISH_BY_DISPLAY_NAME =
        DEPENDANT_LIST + "//div[contains(@id,'StatusSelectionItem') and descendant::h6[contains(@class,'main-name') and contains(.,'%s')]]";

    private String REMOVE_DEPENDANT_ICON = DEPENDANT_ROW_TO_PUBLISH_BY_DISPLAY_NAME + "//div[contains(@class,'icon remove')]";

    private String STATUS_OF_ITEM_TO_PUBLISH = ITEM_ROW_TO_PUBLISH_BY_DISPLAY_NAME + "//div[contains(@class,'status')]";

    private final String DIALOG_INVALID_SUB_HEADER_XPATH =
        DIALOG_CONTAINER + "//div[contains(@id,'ModalDialogHeader')]//h6[@class='sub-title']";

    private final String STATUS_OF_CONTENT = ITEM_LIST +
        "//div[contains(@id,'StatusSelectionItem') and descendant::h6[contains(@class,'main-name') and contains(.,'%s')]]/div[contains(@class,'status')][2]";

    private final String CREATE_ISSUE_MENU_ITEM = DIALOG_CONTAINER + "//ul[contains(@id,'Menu')]//li[contains(.,'Create Issue...')]";

    private final String SCHEDULE_MENU_ITEM = DIALOG_CONTAINER + "//ul[contains(@id,'Menu')]//li[contains(.,'Schedule...')]";

    private final String PUBLISH_MENU_DROPDOWN_HANDLER = DIALOG_CONTAINER + "//div[contains(@id,'MenuButton')]" + DROP_DOWN_HANDLE_BUTTON;

    private final String ADD_SCHEDULE_BUTTON = DIALOG_CONTAINER + "//button[contains(@id,'ButtonEl') and contains(@class,'icon-calendar')]";

    private final String ONLINE_FROM_INPUT = DIALOG_CONTAINER +
        "//div[contains(@id,'DateTimePicker') and preceding-sibling::label[text()='Online from']]//input[contains(@id,'TextInput')]";

    @FindBy(xpath = PUBLISH_MENU_DROPDOWN_HANDLER)
    private WebElement publishMenuDropDownHandler;

    @FindBy(xpath = PUBLISH_BUTTON)
    private WebElement publishButton;

    @FindBy(xpath = ADD_SCHEDULE_BUTTON)
    private WebElement addScheduleButton;

    @FindBy(xpath = CANCEL_BUTTON_TOP)
    private WebElement cancelButtonTop;

    @FindBy(xpath = CANCEL_BUTTON_BOTTOM)
    private WebElement cancelButtonBottom;

    @FindBy(xpath = SHOW_DEPENDANT_ITEMS_LINK)
    private WebElement showDependantItemsLink;

    @FindBy(xpath = ONLINE_FROM_INPUT)
    private WebElement onlineFromInput;

    public ContentPublishDialog( final TestSession session )
    {
        super( session );
    }

    public void clickOnCancelTopButton()
    {
        cancelButtonTop.click();
        sleep( 200 );
    }

    public void clickOnShowDependentItemsLink()
    {
        boolean result = waitUntilVisibleNoException( By.xpath( SHOW_DEPENDANT_ITEMS_LINK ), Application.EXPLICIT_NORMAL );
        if ( !result )
        {
            throw new TestFrameworkException( "Content Publish Dialog - Show Dependent Items Link is not visible!" );
        }
        showDependantItemsLink.click();
        sleep( 400 );
    }

    /**
     * Clicks on the drop down handler and shows :'Create Issue' and 'Schedule' menu items
     */
    public ContentPublishDialog showPublishMenu()
    {
        publishMenuDropDownHandler.click();
        sleep( 400 );
        return this;
    }

    public SchedulePublishDialog clickOnScheduleMenuItem()
    {
        if ( !isScheduleMenuItemEnabled() )
        {
            saveScreenshot( "err_schedule_menu_item" );
            throw new TestFrameworkException( "menu item was not found!" );
        }
        getDisplayedElement( By.xpath( SCHEDULE_MENU_ITEM ) ).click();
        SchedulePublishDialog dialog = new SchedulePublishDialog( getSession() );
        dialog.waitUntilDialogShown( Application.EXPLICIT_NORMAL );
        return dialog;
    }

    public CreateIssueDialog clickOnCreateIssueMenuItem()
    {
        if ( !isCreateIssueMenuItemDisplayed() )
        {
            saveScreenshot( "err_create_issue_menu_item" );
            throw new TestFrameworkException( "'create issue' menu item was not found!" );
        }
        getDisplayedElement( By.xpath( CREATE_ISSUE_MENU_ITEM ) ).click();
        CreateIssueDialog dialog = new CreateIssueDialog( getSession() );
        dialog.waitForOpened();
        return dialog;
    }

    public boolean isCreateIssueMenuItemDisplayed()
    {
        return isElementDisplayed( CREATE_ISSUE_MENU_ITEM );
    }

    public boolean isScheduleMenuItemDisplayed()
    {
        return isElementDisplayed( SCHEDULE_MENU_ITEM );
    }

    public boolean isScheduleMenuItemEnabled()
    {
        findElements( By.xpath( SCHEDULE_MENU_ITEM ) );
        if ( !isElementDisplayed( SCHEDULE_MENU_ITEM ) )
        {
            saveScreenshot( "err_schedule_menu_item_not_visible " );
            throw new TestFrameworkException( "'schedule' menu item is not visible!" );
        }
        return !getAttribute( getDisplayedElement( By.xpath( SCHEDULE_MENU_ITEM ) ), "class", Application.EXPLICIT_NORMAL ).contains(
            "disabled" );
    }

    public boolean isPublishItemRemovable( String itemDisplayName )
    {
        String xpath = String.format( ITEM_ROW_TO_PUBLISH_BY_DISPLAY_NAME, itemDisplayName );
        if ( !isElementDisplayed( xpath ) )
        {
            throw new TestFrameworkException( "publish item was not found!" + itemDisplayName );
        }
        return findElement( By.xpath( xpath ) ).getAttribute( "class" ).contains( "removable" );
    }

    public boolean isDependantItemRemovable( String itemDisplayName )
    {
        String xpath = String.format( DEPENDANT_ROW_TO_PUBLISH_BY_DISPLAY_NAME, itemDisplayName );
        if ( !isElementDisplayed( xpath ) )
        {
            throw new TestFrameworkException( "publish item was not found!" + itemDisplayName );
        }
        return findElement( By.xpath( xpath ) ).getAttribute( "class" ).contains( "removable" );
    }

    public ContentPublishDialog removeDependant( String name )
    {
        String removeButton = String.format( REMOVE_DEPENDANT_ICON, name );
        if ( !isElementDisplayed( removeButton ) )
        {
            saveScreenshot( "err_remove_dependant" );
            throw new TestFrameworkException( "remove icon was not found on the publish dialog!" );
        }
        findElement( By.xpath( removeButton ) ).click();
        sleep( 300 );
        return this;
    }

    public ContentPublishDialog removeItem( String itemDisplayName )
    {
        String xpath = String.format( ITEM_ROW_TO_PUBLISH_BY_DISPLAY_NAME, itemDisplayName );
        if ( !isElementDisplayed( xpath ) )
        {
            throw new TestFrameworkException( "publish item was not found!" + itemDisplayName );
        }

        String removeButton = xpath + "//div[@class='icon remove']";
        if ( !isElementDisplayed( removeButton ) )
        {
            saveScreenshot( "err_remove_item" );
            throw new TestFrameworkException( "remove icon was not found on the publish dialog!" );
        }
        findElement( By.xpath( removeButton ) ).click();
        sleep( 300 );
        return this;
    }

    public String getContentStatus( String displayName )
    {
        String status = String.format( STATUS_OF_CONTENT, displayName );
        if ( !isElementDisplayed( status ) )
        {
            saveScreenshot( "err_publish_dlg_content_status" );
            throw new TestFrameworkException( "status of content was not found!" );
        }
        return getDisplayedString( status );
    }

    public String getDialogSubHeader()
    {
        if ( !isElementDisplayed( DIALOG_INVALID_SUB_HEADER_XPATH ) )
        {
            saveScreenshot( "err_publish_dlg_subheader" );
            throw new TestFrameworkException( "Publish dialog: subheader was not found!" );
        }
        return getDisplayedString( DIALOG_INVALID_SUB_HEADER_XPATH );
    }

    public List<String> getNamesOfContentsToPublish()
    {
        List<String> names = getDisplayedStrings( By.xpath( NAMES_OF_CONTENTS_TO_PUBLISH ) );
        return names;
    }

    public List<String> getContentDisplayNamesToPublish()
    {
        List<String> displayNames = getDisplayedStrings( By.xpath( DISPLAY_NAMES_OF_CONTENTS_TO_PUBLISH ) );
        return displayNames;
    }

    public boolean isIncludeChildTogglerDisplayed()
    {
        return isElementDisplayed( INCLUDE_CHILD_TOGGLER );
    }

    public ContentPublishDialog includeChildren( boolean value )
    {
        boolean isIncluded = isDependantsDisplayed();
        if ( !isIncluded && value || isIncluded && !value )
        {
            if ( !isIncludeChildTogglerDisplayed() )
            {
                saveScreenshot( "err_include_children_toggler" );
                throw new TestFrameworkException( "'include children toggler' is not displayed" );
            }
            getDisplayedElement( By.xpath( INCLUDE_CHILD_TOGGLER ) ).click();
        }
        sleep( 1200 );
        return this;
    }

    public boolean isDependantsDisplayed()
    {
        return isElementDisplayed( OTHER_ITEMS_WILL_BE_PUBLISHED );
    }

    public void clickOnCancelBottomButton()
    {
        cancelButtonBottom.click();
        sleep( 200 );
    }

    public ContentPublishDialog clickOnPublishButton()
    {
        waitUntilVisibleNoException( By.xpath( PUBLISH_BUTTON ), Application.EXPLICIT_NORMAL );
        if ( !isElementDisplayed( PUBLISH_BUTTON ) )
        {
            saveScreenshot( NameHelper.uniqueName( "err_publish_button" ) );
            throw new TestFrameworkException( "publish button was not found!" );
        }
        if ( !publishButton.isEnabled() )
        {
            saveScreenshot( NameHelper.uniqueName( "err_publish_button_disabled" ) );
            throw new TestFrameworkException( "publish button is disabled!" );
        }
        publishButton.click();
        sleep( 1400 );
        return this;
    }

    public boolean waitForDialogClosed()
    {
        return waitsElementNotVisible( By.xpath( TITLE_XPATH ), Application.EXPLICIT_NORMAL );
    }

    public String getTitle()
    {
        if ( findElements( By.xpath( TITLE_XPATH ) ).size() == 0 )
        {
            throw new TestFrameworkException( "dialog's title was not found!" );
        }
        return findElement( By.xpath( DIALOG_CONTAINER + "//h2[@class='title']" ) ).getText();
    }

    public boolean isOpened()
    {
        return isElementDisplayed( DIALOG_CONTAINER );
    }

    /**
     * Waits until 'ContentPublishDialog' is opened.
     *
     * @return {@link ContentPublishDialog} instance.
     */
    public ContentPublishDialog waitUntilDialogShown( long timeout )
    {
        if ( !waitUntilVisibleNoException( By.xpath( DIALOG_CONTAINER ), timeout ) )
        {
            saveScreenshot( "err_publish_dialog_opening" );
            throw new TestFrameworkException( "Content publish dialog was not loaded!" );
        }
        return this;
    }

    public boolean isPublishButtonEnabled()
    {
        //button is disabled( has opacity: 0.5 and pointer-events: none) when Dialog is locked
        return !getDisplayedElement( By.xpath( DIALOG_CONTAINER ) ).getAttribute( "class" ).contains( "locked" );
    }

    public void waitUntilPublishButtonEnabled( long timeout )
    {
        waitUntilElementEnabled( By.xpath( PUBLISH_BUTTON ), timeout );
    }

    public boolean isCancelButtonBottomEnabled()
    {
        return cancelButtonBottom.isEnabled();
    }

    public boolean isCancelButtonTopEnabled()
    {
        return cancelButtonTop.isEnabled();
    }

    public String getDependenciesListMessage()
    {
        return getDisplayedElement( By.xpath( OTHER_ITEMS_WILL_BE_PUBLISHED ) ).getText();
    }

    public boolean isDependenciesListHeaderDisplayed()
    {
        return isElementDisplayed( OTHER_ITEMS_WILL_BE_PUBLISHED );
    }

    public List<String> getDependantList()
    {
        return getDisplayedStrings( By.xpath( DEPENDANT_NAMES ) );
    }

    ContentPublishDialog typeOnlineFrom( String dateTime )
    {
        clearAndType( onlineFromInput, dateTime );
        return this;
    }

    public ContentPublishDialog clickOnAddScheduleButton()
    {
        addScheduleButton.click();
        sleep( 500 );
        return this;
    }

    public ContentPublishDialog clickOnScheduleButton()
    {
        try
        {
            waitUntilElementEnabled( By.xpath( DIALOG_CONTAINER + "//button[contains(@id,'DialogButton')]/span[text()='Schedule']" ),
                                     EXPLICIT_NORMAL );
            getDisplayedElement( By.xpath( DIALOG_CONTAINER + "//button[contains(@id,'DialogButton')]/span[text()='Schedule']" ) ).click();
            return this;
        }
        catch ( Exception e )
        {
            throw new TestFrameworkException( "Content Publish Dialog - Error when clicking on Schedule button" + e.getMessage() );
        }
    }
}
