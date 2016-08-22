package com.enonic.autotests.pages.contentmanager;


import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.utils.TestUtils;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class ContentPublishDialog
    extends Application
{
    public static final String OTHER_ITEMS_WILL_BE_PUBLISHED = "Other items that will be published";

    public static final String DIALOG_SUBHEADER_READY_FOR_PUBLISH = "Your changes are ready for publishing";

    public static final String DIALOG_SUBHEADER_INVALID_CONTENT_PUBLISH = "Invalid content(s) prevent publish";

    public static final String DIALOG_TITLE = "Publishing Wizard";

    private final String DIALOG_CONTAINER = "//div[contains(@id,'ContentPublishDialog')]";

    private final String TITLE_XPATH = DIALOG_CONTAINER + "//h2[@class='title']";

    private final String PUBLISH_NOW_BUTTON =
        DIALOG_CONTAINER + "//button[contains(@id,'DialogButton') and child::span[contains(.,'Publish')]]";

    private final String CANCEL_BUTTON_TOP = DIALOG_CONTAINER + "//div[contains(@class,'cancel-button-top')]";

    private final String CANCEL_BUTTON_BOTTOM = DIALOG_CONTAINER + "//button[contains(@class,'cancel-button-bottom')]";

    private final String INCLUDE_CHILD_CHECKBOX =
        DIALOG_CONTAINER + "//div[contains(@class,'include-child-check')]//input[@type='checkbox']";

    private final String INCLUDE_CHILD_CHECKBOX_LABEL = DIALOG_CONTAINER + "//div[contains(@class,'include-child-check')]//label";

    private final String DEPENDANT_LIST = DIALOG_CONTAINER + "//ul[contains(@id,'PublishDialogDependantList')]";

    private final String DEPENDANT_NAMES = DEPENDANT_LIST + "//div[contains(@id,'DependantItemViewer')]//h6[@class='main-name']";

    private final String DEPENDENCIES_LIST_HEADER = DIALOG_CONTAINER + "//h6[@class='dependants-header']";

    private final String ITEM_LIST = "//ul[contains(@id,'DialogItemList')]";

    private final String DISPLAY_NAMES_OF_CONTENTS_TO_PUBLISH = DIALOG_CONTAINER + ITEM_LIST + H6_DISPLAY_NAME;

    private final String NAMES_OF_CONTENTS_TO_PUBLISH = DIALOG_CONTAINER + ITEM_LIST + P_NAME;

    private String ITEM_ROW_TO_PUBLISH_BY_DISPLAY_NAME =
        ITEM_LIST + "//div[contains(@id,'StatusSelectionItem') and descendant::h6[@class='main-name' and contains(.,'%s')]]";

    private String STATUS_OF_ITEM_TO_PUBLISH = ITEM_ROW_TO_PUBLISH_BY_DISPLAY_NAME + "//div[contains(@class,'status')]";

    private final String DIALOG_INVALID_SUB_HEADER_XPATH =
        DIALOG_CONTAINER + "//div[contains(@id,'ModalDialogHeader')]//h6[@class='sub-title']";

    private String STATUS_OF_CONTENT = ITEM_LIST +
        "//div[contains(@id,'StatusSelectionItem') and descendant::h6[@class='main-name' and contains(.,'%s')]]/div[contains(@class,'status')][2]";

    @FindBy(xpath = PUBLISH_NOW_BUTTON)
    private WebElement publishButton;

    @FindBy(xpath = CANCEL_BUTTON_TOP)
    private WebElement cancelButtonTop;

    @FindBy(xpath = CANCEL_BUTTON_BOTTOM)
    private WebElement cancelButtonBottom;

    @FindBy(xpath = INCLUDE_CHILD_CHECKBOX)
    private WebElement includeChildCheckbox;

    public ContentPublishDialog( final TestSession session )
    {
        super( session );
    }

    public void clickOnCancelTopButton()
    {
        cancelButtonTop.click();
        sleep( 200 );
    }

    public String getContentStatus( String displayName )
    {
        String status = String.format( STATUS_OF_CONTENT, displayName );
        if ( !isElementDisplayed( status ) )
        {
            TestUtils.saveScreenshot( getSession(), "err_publish_dlg_content_status" );
            throw new TestFrameworkException( "status of content was not found!" );
        }
        return getDisplayedString( status );
    }

    public String getDialogSubHeader()
    {
        if ( !isElementDisplayed( DIALOG_INVALID_SUB_HEADER_XPATH ) )
        {
            TestUtils.saveScreenshot( getSession(), "err_subheader_not_displayed" );
            throw new TestFrameworkException( "Publish dialog: error-subheader not shown!" );
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

    public ContentPublishDialog setIncludeChildCheckbox( boolean value )
    {
        boolean isChecked = includeChildCheckbox.isSelected();
        if ( !isChecked && value || isChecked && !value )
        {
            findElements( By.xpath( INCLUDE_CHILD_CHECKBOX_LABEL ) ).get( 0 ).click();
        }
        sleep( 700 );
        return this;
    }

    public void clickOnCancelBottomButton()
    {
        cancelButtonBottom.click();
        sleep( 200 );
    }

    public ContentPublishDialog clickOnPublishNowButton()
    {
        sleep( 500 );
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
            TestUtils.saveScreenshot( getSession(), "err_publish_dialog_opening" );
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
        waitUntilElementEnabled( By.xpath( PUBLISH_NOW_BUTTON ), timeout );
    }

    public boolean isCancelButtonBottomEnabled()
    {
        return cancelButtonBottom.isEnabled();
    }

    public boolean isCancelButtonTopEnabled()
    {
        return cancelButtonTop.isEnabled();
    }

    public boolean isIncludeChildCheckboxSelected()
    {
        return includeChildCheckbox.isSelected();
    }

    public boolean isIncludeChildCheckboxDisplayed()
    {
        return includeChildCheckbox.isDisplayed();
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
