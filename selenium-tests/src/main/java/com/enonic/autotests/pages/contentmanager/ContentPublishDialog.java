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
    public static final String SHOW_DEPENDENCY_LINK_TEXT = "Show dependencies and child items to be published";

    public static final String HIDE_DEPENDENCY_LINK_TEXT = "Hide dependencies and child items to be published";

    public static final String DIALOG_TITLE = "Publishing Wizard";

    private final String DIALOG_CONTAINER = "//div[contains(@id,'app.publish.ContentPublishDialog')]";

    private final String TITLE_XPATH = DIALOG_CONTAINER + "//h2[@class='title']";

    private final String FILTER_INPUT = DIALOG_CONTAINER + "//input[contains(@id,'ComboBoxOptionFilterInput')]";

    private final String PUBLISH_NOW_BUTTON =
        DIALOG_CONTAINER + "//button[contains(@id,'api.ui.dialog.DialogButton') and child::span[contains(.,'Publish Now')]]";

    private final String CANCEL_BUTTON_TOP = DIALOG_CONTAINER + "//button[contains(@class,'cancel-button-top dialog-button')]";

    private final String CANCEL_BUTTON_BOTTOM = DIALOG_CONTAINER + "//button[contains(@class,'cancel-button-bottom')]";

    private final String INCLUDE_CHILD_CHECKBOX =
        DIALOG_CONTAINER + "//div[contains(@class,'include-child-check')]//input[@type='checkbox']";

    private final String INCLUDE_CHILD_CHECKBOX_LABEL = DIALOG_CONTAINER + "//div[contains(@class,'include-child-check')]//label";

    private final String DEPENDENCY_LINK =
        DIALOG_CONTAINER + "//div[contains(@id,'PublishDialogItemList')]//label[contains(@class,'dependencies-toggle-label')]";

    private final String DEPENDENCIES =
        DIALOG_CONTAINER + "//div[contains(@id,'PublishDialogDependantsItemList')]//span[@class='name-span']";

    private final String DISPLAY_NAMES_OF_CONTENTS_TO_PUBLISH = "//div[contains(@id,'PublishDialogItemList')]//h6[@class='main-name']";

    private final String NAMES_OF_CONTENTS_TO_PUBLISH = "//div[contains(@id,'PublishDialogItemList')]//p[@class='sub-name']";

    @FindBy(xpath = DEPENDENCY_LINK)
    private WebElement dependencyLink;

    @FindBy(xpath = PUBLISH_NOW_BUTTON)
    private WebElement publishButton;

    @FindBy(xpath = CANCEL_BUTTON_TOP)
    private WebElement cancelButtonTop;

    @FindBy(xpath = CANCEL_BUTTON_BOTTOM)
    private WebElement cancelButtonBottom;

    @FindBy(xpath = FILTER_INPUT)
    private WebElement optionFilterInput;

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

    public List<String> getContentNamesToPublish()
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
        publishButton.click();
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

    public boolean isDependencyLinkDisplayed()
    {
        return dependencyLink.isDisplayed();
    }

    public boolean isDependencyLinkExpanded()
    {
        return waitAndCheckAttrValue( dependencyLink, "class", "expanded", Application.EXPLICIT_NORMAL );
    }

    public String getDependencyLinkText()
    {
        return findElements( By.xpath( DEPENDENCY_LINK ) ).stream().filter( WebElement::isDisplayed ).findFirst().get().getText();
    }

    public ContentPublishDialog showDependency()
    {
        if ( getDependencyLinkText().equals( SHOW_DEPENDENCY_LINK_TEXT ) )
        {
            findElements( By.xpath( DEPENDENCY_LINK ) ).get( 0 ).click();
        }
        return this;

    }

    public ContentPublishDialog hideDependency()
    {
        if ( getDependencyLinkText().equals( HIDE_DEPENDENCY_LINK_TEXT ) )
        {
            findElements( By.xpath( DEPENDENCY_LINK ) ).get( 0 ).click();
        }
        return this;
    }

    public List<String> getDependencies()
    {
        List<String> list =
            findElements( By.xpath( DEPENDENCIES ) ).stream().filter( WebElement::isDisplayed ).map( WebElement::getText ).collect(
                Collectors.toList() );
        return list;
    }
}
