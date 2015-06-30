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

    public static String DIALOG_TITLE = "Publishing Wizard";

    private final String DIALOG_CONTAINER = "//div[contains(@id,'app.publish.ContentPublishDialog')]";

    private final String TITLE_XPATH = DIALOG_CONTAINER + "//h2[@class='title']";

    private final String FILTER_INPUT = DIALOG_CONTAINER + "//input[contains(@id,'ComboBoxOptionFilterInput')]";

    private final String PUBLISH_NOW_BUTTON =
        DIALOG_CONTAINER + "//button[contains(@id,'api.ui.dialog.DialogButton') and child::span[contains(.,'Publish Now')]]";

    private final String CANCEL_BUTTON_TOP = DIALOG_CONTAINER + "//button[contains(@class,'cancel-button-top dialog-button')]";

    private final String CANCEL_BUTTON_BOTTOM = DIALOG_CONTAINER + "//button[contains(@class,'cancel-button-bottom')]";

    @FindBy(xpath = PUBLISH_NOW_BUTTON)
    private WebElement publishButton;

    @FindBy(xpath = CANCEL_BUTTON_TOP)
    private WebElement cancelButtonTop;

    @FindBy(xpath = CANCEL_BUTTON_BOTTOM)
    private WebElement cancelButtonBottom;


    @FindBy(xpath = FILTER_INPUT)
    private WebElement optionFilterInput;

    public ContentPublishDialog( final TestSession session )
    {
        super( session );
    }

    public void clickOnCancelTopButton()
    {
        cancelButtonTop.click();
        sleep( 200 );
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


}
