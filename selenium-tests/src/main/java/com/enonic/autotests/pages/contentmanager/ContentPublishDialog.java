package com.enonic.autotests.pages.contentmanager;


import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class ContentPublishDialog
    extends Application
{
    public static final String DEPENDENCIES_LIST_HEADER_TEXT = "Other items that will be published";

    public static final String DIALOG_SUBHEADER_READY_FOR_PUBLISH = "Your changes are ready for publishing";

    public static final String DIALOG_SUBHEADER_INVALID_CONTENT_PUBLISH = "Invalid content(s) prevent publish";

    public static final String WARNING_PUBLISH_MESSAGE = "Invalid content(s) prevent publish";

    public static final String DIALOG_TITLE = "Publishing Wizard";

    private final String DIALOG_CONTAINER = "//div[contains(@id,'app.publish.ContentPublishDialog')]";

    private final String TITLE_XPATH = DIALOG_CONTAINER + "//h2[@class='title']";


    private final String PUBLISH_NOW_BUTTON =
        DIALOG_CONTAINER + "//button[contains(@id,'DialogButton') and child::span[contains(.,'Publish')]]";

    private final String CANCEL_BUTTON_TOP = DIALOG_CONTAINER + "//button[contains(@class,'cancel-button-top dialog-button')]";

    private final String CANCEL_BUTTON_BOTTOM = DIALOG_CONTAINER + "//button[contains(@class,'cancel-button-bottom')]";

    private final String INCLUDE_CHILD_CHECKBOX =
        DIALOG_CONTAINER + "//div[contains(@class,'include-child-check')]//input[@type='checkbox']";

    private final String INCLUDE_CHILD_CHECKBOX_LABEL = DIALOG_CONTAINER + "//div[contains(@class,'include-child-check')]//label";

    private final String DEPENDENCIES_LIST = DIALOG_CONTAINER + "//div[contains(@id,'PublishDialogDependantsItemList')]";

    private final String DEPENDENCIES_STRINGS = DEPENDENCIES_LIST + "//span[@class='name-span']";

    private final String DEPENDENCIES_LIST_HEADER = DEPENDENCIES_LIST + "//h6[@class='dependencies-header']";

    private final String DISPLAY_NAMES_OF_CONTENTS_TO_PUBLISH = "//div[contains(@id,'PublishDialogItemList')]//h6[@class='main-name']";

    private final String NAMES_OF_CONTENTS_TO_PUBLISH = "//div[contains(@id,'PublishDialogItemList')]//p[@class='sub-name']";

    private final String DIALOG_SUBHEADER_XPATH = DIALOG_CONTAINER + "//h6[contains(@class,'publish-dialog-subheader')]";

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

    public String getDialogSubHeader()
    {
        return findElements( By.xpath( DIALOG_SUBHEADER_XPATH ) ).get( 0 ).getText();
    }

    public List<String> getNamesOfContentsToPublish()
    {
        List<String> names = findElements( By.xpath( NAMES_OF_CONTENTS_TO_PUBLISH ) ).stream().filter( WebElement::isDisplayed ).map(
            WebElement::getText ).collect( Collectors.toList() );
        return names;
    }

    public List<String> getContentDisplayNamesToPublish()
    {
        List<String> names =
            findElements( By.xpath( DISPLAY_NAMES_OF_CONTENTS_TO_PUBLISH ) ).stream().filter( WebElement::isDisplayed ).map(
                WebElement::getText ).collect( Collectors.toList() );
        return names;
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
        sleep( 500 );
        return this;
    }

    public boolean waitForDialogClosed()
    {
        return waitElementNotVisible( By.xpath( TITLE_XPATH ), Application.EXPLICIT_NORMAL );
    }

    public String getTitle()
    {
        if ( findElements( By.xpath( TITLE_XPATH ) ).size() == 0 )
        {
            throw new TestFrameworkException( "dialog's title was not found!" );
        }
        return findElements( By.xpath( DIALOG_CONTAINER + "//h2[@class='title']" ) ).get( 0 ).getText();
    }

    public boolean isOpened()
    {
        List<WebElement> elements = findElements( By.xpath( DIALOG_CONTAINER ) );
        return elements.stream().filter( WebElement::isDisplayed ).collect( Collectors.toList() ).size() > 0;
    }


    /**
     * Waits until 'ContentPublishDialog' is opened.
     *
     * @return {@link ContentPublishDialog} instance.
     */
    public ContentPublishDialog waitUntilDialogShowed( long timeout )
    {
        if ( !waitUntilVisibleNoException( By.xpath( DIALOG_CONTAINER ), timeout ) )
        {
            throw new TestFrameworkException( "Content publish dialog was not shown!" );
        }
        return this;
    }

    public boolean isPublishNowButtonEnabled()
    {
        return publishButton.isEnabled();
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
        return findElements( By.xpath( DEPENDENCIES_LIST_HEADER ) ).stream().filter( WebElement::isDisplayed ).findFirst().get().getText();
    }

    public boolean isDependenciesListHeaderDisplayed()
    {
        return findElements( By.xpath( DEPENDENCIES_LIST_HEADER ) ).stream().filter( WebElement::isDisplayed ).count() > 0;
    }

    public List<String> getDependencies()
    {
        List<String> list =
            findElements( By.xpath( DEPENDENCIES_STRINGS ) ).stream().filter( WebElement::isDisplayed ).map( WebElement::getText ).collect(
                Collectors.toList() );
        return list;
    }
}
