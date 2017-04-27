package com.enonic.autotests.pages.contentmanager;


import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.utils.NameHelper;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class ContentPublishDialog
    extends Application
{
    public static final String OTHER_ITEMS_WILL_BE_PUBLISHED = "Other items that will be published";

    public static final String DIALOG_SUBHEADER_READY_FOR_PUBLISH = "Your changes are ready for publishing";

    public static final String DIALOG_SUBHEADER_INVALID_CONTENT_PUBLISH = "Invalid item(s) prevent publish";

    public static final String DIALOG_TITLE = "Publishing Wizard";

    private final String DIALOG_CONTAINER = "//div[contains(@id,'ContentPublishDialog')]";

    private final String TITLE_XPATH = DIALOG_CONTAINER + "//h2[@class='title']";

    private final String PUBLISH_BUTTON =
        DIALOG_CONTAINER + "//button[contains(@id,'DialogButton') and child::span[contains(.,'Publish')]]";

    private final String CANCEL_BUTTON_TOP = DIALOG_CONTAINER + "//div[contains(@class,'cancel-button-top')]";

    private final String CANCEL_BUTTON_BOTTOM = DIALOG_CONTAINER + "//button[contains(@class,'cancel-button-bottom')]";

    private final String INCLUDE_CHILD_TOGGLER = DIALOG_CONTAINER + "//div[contains(@id,'IncludeChildrenToggler')]";

    private final String DEPENDANT_LIST = DIALOG_CONTAINER + "//ul[contains(@id,'PublishDialogDependantList')]";

    private final String DEPENDANT_NAMES = DEPENDANT_LIST + "//div[contains(@id,'DependantItemViewer')]" + H6_MAIN_NAME;

    private final String DEPENDENCIES_LIST_HEADER = DIALOG_CONTAINER + "//h6[@class='dependants-header']";

    private final String ITEM_LIST = "//ul[contains(@id,'PublishDialogItemList')]";

    private final String DISPLAY_NAMES_OF_CONTENTS_TO_PUBLISH = DIALOG_CONTAINER + ITEM_LIST + H6_DISPLAY_NAME;

    private final String NAMES_OF_CONTENTS_TO_PUBLISH = DIALOG_CONTAINER + ITEM_LIST + P_NAME;

    private String ITEM_ROW_TO_PUBLISH_BY_DISPLAY_NAME =
        ITEM_LIST + "//div[contains(@id,'StatusSelectionItem') and descendant::h6[contains(@class,'main-name') and contains(.,'%s')]]";

    private String DEPENDANT_ROW_TO_PUBLISH_BY_DISPLAY_NAME =
        DEPENDANT_LIST + "//div[contains(@id,'StatusSelectionItem') and descendant::h6[contains(@class,'main-name') and contains(.,'%s')]]";

    private String REMOVE_DEPENDANT_ICON = DEPENDANT_ROW_TO_PUBLISH_BY_DISPLAY_NAME + "//div[contains(@class,'icon remove')]";

    private String STATUS_OF_ITEM_TO_PUBLISH = ITEM_ROW_TO_PUBLISH_BY_DISPLAY_NAME + "//div[contains(@class,'status')]";

    private final String DIALOG_INVALID_SUB_HEADER_XPATH =
        DIALOG_CONTAINER + "//div[contains(@id,'ModalDialogHeader')]//h6[@class='sub-title']";

    private String STATUS_OF_CONTENT = ITEM_LIST +
        "//div[contains(@id,'StatusSelectionItem') and descendant::h6[contains(@class,'main-name') and contains(.,'%s')]]/div[contains(@class,'status')][2]";

    private final String SHOW_SCHEDULE_DIALOG_BUTTON = DIALOG_CONTAINER + "//button[contains(@class,'show-schedule')]";

    @FindBy(xpath = SHOW_SCHEDULE_DIALOG_BUTTON)
    private WebElement showScheduleButton;

    @FindBy(xpath = PUBLISH_BUTTON)
    private WebElement publishButton;

    @FindBy(xpath = CANCEL_BUTTON_TOP)
    private WebElement cancelButtonTop;

    @FindBy(xpath = CANCEL_BUTTON_BOTTOM)
    private WebElement cancelButtonBottom;

    //@FindBy(xpath = INCLUDE_OFFLINE_ITEMS)
    // private WebElement includeOfflineItemsCheckbox;

    public ContentPublishDialog( final TestSession session )
    {
        super( session );
    }

    public void clickOnCancelTopButton()
    {
        cancelButtonTop.click();
        sleep( 200 );
    }

    //TODO checkbox is temporarily removed
//    public void clickOnIncludeOfflineItemsCheckbox()
//    {
//        includeOfflineItemsCheckbox.click();
//        sleep( 200 );
//    }

//    public void isIncludeOfflineItemsCheckboxDisplayed()
//    {
//        includeOfflineItemsCheckbox.isDisplayed();
//        sleep( 200 );
//    }

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
        sleep( 700 );
        return this;
    }

    public boolean isDependantsDisplayed()
    {
        return isElementDisplayed( DEPENDENCIES_LIST_HEADER );
    }

    public void clickOnCancelBottomButton()
    {
        cancelButtonBottom.click();
        sleep( 200 );
    }

    public SchedulePublishDialog clickOnShowScheduleButton()
    {
        showScheduleButton.click();
        sleep( 200 );
        SchedulePublishDialog dialog = new SchedulePublishDialog( getSession() );
        dialog.waitUntilDialogShown( Application.EXPLICIT_NORMAL );
        return dialog;

    }

    public ContentPublishDialog clickOnPublishNowButton()
    {
        sleep( 500 );
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
        sleep( 1000 );
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
            throw new TestFrameworkException( "Content publish dialog was not shown!" );
        }
        return this;
    }

    public boolean isPublishNowButtonEnabled()
    {
        return publishButton.isEnabled();
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

    public String getDependenciesListHeader()
    {
        return getDisplayedElement( By.xpath( DEPENDENCIES_LIST_HEADER ) ).getText();
    }

    public boolean isDependenciesListHeaderDisplayed()
    {
        return isElementDisplayed( DEPENDENCIES_LIST_HEADER );
    }

    public List<String> getDependantList()
    {
        return getDisplayedStrings( By.xpath( DEPENDANT_NAMES ) );
    }
}
