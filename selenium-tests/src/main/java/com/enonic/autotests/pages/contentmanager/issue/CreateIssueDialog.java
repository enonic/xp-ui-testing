package com.enonic.autotests.pages.contentmanager.issue;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.LoaderComboBox;
import com.enonic.autotests.vo.contentmanager.Issue;

import static com.enonic.autotests.utils.SleepHelper.sleep;

/**
 * Created on 7/5/2017.
 */
public class CreateIssueDialog
    extends Application
{
    private final String DIALOG_CONTAINER = "//div[contains(@id,'CreateIssueDialog')]";

    protected final String ASSIGNEES_OPTION_FILTER_INPUT =
        DIALOG_CONTAINER + "//div[contains(@id,'LoaderComboBox') and @name='principalSelector']" + COMBOBOX_OPTION_FILTER_INPUT;

    protected final String ITEMS_OPTION_FILTER_INPUT =
        DIALOG_CONTAINER + "//div[contains(@id,'LoaderComboBox') and @name='contentSelector']" + COMBOBOX_OPTION_FILTER_INPUT;


    private final String CANCEL_BUTTON_TOP = DIALOG_CONTAINER + APP_CANCEL_BUTTON_TOP;

    private final String CANCEL_BUTTON_BOTTOM = DIALOG_CONTAINER + "//button[contains(@class,'button-bottom')]";

    private final String CREATE_TASK_BUTTON =
        DIALOG_CONTAINER + "//button[contains(@class,'dialog-button') and child::span[text()='Create Task']]";

    private final String TITLE_INPUT = "//div[contains(@id,'FormItem') and child::label[text()='Title']]//input[@type='text']";

    private final String TITLE_INPUT_ERROR_MESSAGE =
        "//div[contains(@id,'FormItem') and child::label[text()='Title']]" + VALIDATION_RECORDING_VIEWER;

    private final String ASSIGNEES_INPUT_ERROR_MESSAGE =
        DIALOG_CONTAINER + "//div[contains(@id,'FormItem') and child::label[text()='Assignees']]" + VALIDATION_RECORDING_VIEWER;

    private final String DESCRIPTION_TEXT_AREA = DIALOG_CONTAINER + TEXT_AREA_INPUT;

    private final String DISPLAY_NAMES_ITEMS_LIST = DIALOG_CONTAINER + "//ul[contains(@id,'PublishDialogItemList')]" + H6_DISPLAY_NAME;

    private final String INCLUDE_CHILDREN_TOGGLER = DIALOG_CONTAINER + "//div[contains(@id,'IncludeChildrenToggler')]";

    @FindBy(xpath = CANCEL_BUTTON_TOP)
    private WebElement cancelButtonTop;

    @FindBy(xpath = CANCEL_BUTTON_BOTTOM)
    private WebElement cancelButtonBottom;

    @FindBy(xpath = DESCRIPTION_TEXT_AREA)
    private WebElement descriptionTextArea;

    @FindBy(xpath = CREATE_TASK_BUTTON)
    private WebElement createTaskButton;

    @FindBy(xpath = TITLE_INPUT)
    private WebElement titleTextInput;

    @FindBy(xpath = ASSIGNEES_OPTION_FILTER_INPUT)
    protected WebElement assigneesOptionFilterInput;

    @FindBy(xpath = ITEMS_OPTION_FILTER_INPUT)
    protected WebElement itemsOptionFilterInput;


    public CreateIssueDialog( final TestSession session )
    {
        super( session );
    }

    public List<String> getDisplayNameOfSelectedItems()
    {
        return getDisplayedStrings( By.xpath( DISPLAY_NAMES_ITEMS_LIST ) );
    }

    public CreateIssueDialog typeData( Issue issue )
    {
        if ( StringUtils.isNotEmpty( issue.getTitle() ) )
        {
            typeTitle( issue.getTitle() );
        }
        if ( StringUtils.isNotEmpty( issue.getDescription() ) )
        {
            typeDescription( issue.getDescription() );
        }
        if ( issue.getAssignees() != null )
        {
            selectAssignees( issue.getAssignees() );
        }
        if ( issue.getItemsToPublish() != null )
        {
            selectItemsToPublish( issue.getItemsToPublish() );
        }
        return this;
    }

    public CreateIssueDialog selectAssignee( String itemDisplayName )
    {
        typeInAssigneesFilter( itemDisplayName );
        LoaderComboBox loaderComboBox = new LoaderComboBox( getSession() );
        loaderComboBox.selectOption( itemDisplayName );
        sleep( 400 );
        return this;
    }

    public CreateIssueDialog selectAssignees( List<String> userDisplayNames )
    {
        userDisplayNames.stream().forEach( userDisplayName -> selectAssignee( userDisplayName ) );
        return this;
    }

    public CreateIssueDialog selectItemsToPublish( List<String> itemNames )
    {
        itemNames.stream().forEach( itemDisplayName -> selectItem( itemDisplayName ) );
        return this;
    }

    public CreateIssueDialog selectItem( String itemDisplayName )
    {
        typeInItemsFilter( itemDisplayName );
        sleep( 300 );
        LoaderComboBox loaderComboBox = new LoaderComboBox( getSession() );
        loaderComboBox.selectOption( itemDisplayName, DIALOG_CONTAINER );
        sleep( 300 );
        return this;
    }

    public void typeInAssigneesFilter( String itemDisplayName )
    {
        clearAndType( assigneesOptionFilterInput, itemDisplayName );
        sleep( 300 );
    }

    public void typeInItemsFilter( String itemName )
    {
        clearAndType( itemsOptionFilterInput, itemName );
        sleep( 500 );
    }

    public CreateIssueDialog typeDescription( String text )
    {
        clearAndType( descriptionTextArea, text );
        return this;
    }

    public CreateIssueDialog typeTitle( String title )
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

    public void waitForOpened()
    {
        if ( !waitUntilVisibleNoException( By.xpath( DIALOG_CONTAINER ), Application.EXPLICIT_NORMAL ) )
        {
            saveScreenshot( "err_create_issue_dialog" );
            throw new TestFrameworkException( "'Create Issue' dialog was not opened!" );
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

    public boolean isAssigneesOptionFilterDisplayed()
    {
        return assigneesOptionFilterInput.isDisplayed();
    }

    public boolean isItemsOptionFilterDisplayed()
    {
        return itemsOptionFilterInput.isDisplayed();
    }

    public boolean isCreateTaskButtonDisplayed()
    {
        return createTaskButton.isDisplayed();
    }

    public void clickOnCreateTaskButton()
    {
        createTaskButton.click();
        sleep( 700 );
    }

    public CreateIssueDialog clickOnIncludeChildrenToggler()
    {
        getDisplayedElement( By.xpath( INCLUDE_CHILDREN_TOGGLER ) ).click();
        sleep( 300 );
        return this;
    }

    public boolean isCancelButtonBottomDisplayed()
    {
        return cancelButtonBottom.isDisplayed();
    }

    public boolean isTitleInputDisplayed()
    {
        return titleTextInput.isDisplayed();
    }

    public boolean isDescriptionTextAreaDisplayed()
    {
        return descriptionTextArea.isDisplayed();
    }

    public boolean isCancelTopButtonDisplayed()
    {
        return cancelButtonTop.isDisplayed();
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
