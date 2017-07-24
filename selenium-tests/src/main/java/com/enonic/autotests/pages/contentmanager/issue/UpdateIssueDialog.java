package com.enonic.autotests.pages.contentmanager.issue;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;

import static com.enonic.autotests.utils.SleepHelper.sleep;

/**
 * Created on 7/19/2017.
 */
public class UpdateIssueDialog
    extends Application
{
    private final String DIALOG_CONTAINER = "//div[contains(@id,'UpdateIssueDialog')]";

    protected final String ASSIGNEES_OPTION_FILTER_INPUT =
        DIALOG_CONTAINER + "//div[contains(@id,'LoaderComboBox') and @name='principalSelector']" + COMBOBOX_OPTION_FILTER_INPUT;

    protected final String ITEMS_OPTION_FILTER_INPUT =
        DIALOG_CONTAINER + "//div[contains(@id,'LoaderComboBox') and @name='contentSelector']" + COMBOBOX_OPTION_FILTER_INPUT;


    private final String CANCEL_BUTTON_TOP = DIALOG_CONTAINER + "//div[contains(@class,'cancel-button-top')]";

    private final String CANCEL_BUTTON_BOTTOM = DIALOG_CONTAINER + "//button[contains(@class,'button-bottom')]";

    private final String SAVE_ISSUE_BUTTON = DIALOG_CONTAINER + "//button[contains(@class,'dialog-button') and child::span[text()='Save']]";

    private final String TITLE_INPUT =
        DIALOG_CONTAINER + "//div[contains(@id,'FormItem') and child::label[text()='Title']]//input[@type='text']";

    private final String TITLE_INPUT_ERROR_MESSAGE =
        "//div[contains(@id,'FormItem') and child::label[text()='Title']]" + VALIDATION_RECORDING_VIEWER;

    private final String ASSIGNEES_INPUT_ERROR_MESSAGE =
        DIALOG_CONTAINER + "//div[contains(@id,'FormItem') and child::label[text()='Assignees']]" + VALIDATION_RECORDING_VIEWER;

    private final String SELECTED_ASSIGNEES = DIALOG_CONTAINER + PRINCIPAL_SELECTED_OPTION + H6_DISPLAY_NAME;

    private final String ITEM_NAMES_LIST = DIALOG_CONTAINER + "//ul[contains(@id,'PublishDialogItemList')]" + P_NAME;

    private final String DESCRIPTION_TEXT_AREA = DIALOG_CONTAINER + TEXT_AREA_INPUT;

    private final String DISPLAY_NAMES_ITEMS_LIST = DIALOG_CONTAINER + "//ul[contains(@id,'PublishDialogItemList')]" + H6_DISPLAY_NAME;

    @FindBy(xpath = CANCEL_BUTTON_TOP)
    private WebElement cancelButtonTop;

    @FindBy(xpath = CANCEL_BUTTON_BOTTOM)
    private WebElement cancelButtonBottom;

    @FindBy(xpath = DESCRIPTION_TEXT_AREA)
    private WebElement descriptionTextArea;

    @FindBy(xpath = SAVE_ISSUE_BUTTON)
    private WebElement saveIssueButton;

    @FindBy(xpath = TITLE_INPUT)
    private WebElement titleTextInput;

    @FindBy(xpath = ASSIGNEES_OPTION_FILTER_INPUT)
    protected WebElement assigneesOptionFilterInput;

    @FindBy(xpath = ITEMS_OPTION_FILTER_INPUT)
    protected WebElement itemsOptionFilterInput;


    public UpdateIssueDialog( final TestSession session )
    {
        super( session );
    }

    public List<String> getDisplayNameOfSelectedItems()
    {
        return getDisplayedStrings( By.xpath( DISPLAY_NAMES_ITEMS_LIST ) );
    }

    public String getTitle()
    {
        return titleTextInput.getAttribute( "value" );
    }

    public String getDescription()
    {
        return descriptionTextArea.getAttribute( "value" );
    }

    public UpdateIssueDialog removeAssignee( String userDisplayName )
    {

        return this;
    }

    public List<String> getAssignees()
    {
        return getDisplayedStrings( By.xpath( SELECTED_ASSIGNEES ) );
    }

    public List<String> getItemNames()
    {
        return getDisplayedStrings( By.xpath( ITEM_NAMES_LIST ) );
    }

    public UpdateIssueDialog typeTitle( String title )
    {
        clearAndType( titleTextInput, title );
        return this;
    }

    public String getValidationMessageForTitleInput()
    {
        if ( isElementDisplayed( TITLE_INPUT_ERROR_MESSAGE ) )
        {
            return getDisplayedString( TITLE_INPUT_ERROR_MESSAGE );
        }
        return "";
    }

    public String getValidationMessageForAssigneesInput()
    {
        if ( isElementDisplayed( ASSIGNEES_INPUT_ERROR_MESSAGE ) )
        {
            return getDisplayedString( ASSIGNEES_INPUT_ERROR_MESSAGE );
        }
        return "";
    }

    public void waitForOpened()
    {
        if ( !waitUntilVisibleNoException( By.xpath( DIALOG_CONTAINER ), Application.EXPLICIT_NORMAL ) )
        {
            saveScreenshot( "err_update_issue_dialog" );
            throw new TestFrameworkException( "'Update Issue' dialog was not opened!" );
        }
    }

    public boolean waitForClosed()
    {
        boolean result = waitsElementNotVisible( By.xpath( DIALOG_CONTAINER ), Application.EXPLICIT_NORMAL );
        if ( !result )
        {
            saveScreenshot( "create_issue_not_closed" );
        }
        return result;
    }

    public boolean isSaveButtonDisplayed()
    {
        return saveIssueButton.isDisplayed();
    }

    public void clickOnSaveIssueButton()
    {
        saveIssueButton.click();
        sleep( 500 );
    }

    public void clickOnCancelTopButton()
    {
        cancelButtonTop.click();
        sleep( 500 );
    }

    public void clickOnCancelBottomButton()
    {
        cancelButtonBottom.click();
        sleep( 500 );
    }

}
