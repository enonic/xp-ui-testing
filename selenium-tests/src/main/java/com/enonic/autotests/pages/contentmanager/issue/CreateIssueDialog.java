package com.enonic.autotests.pages.contentmanager.issue;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;

/**
 * Created on 7/5/2017.
 */
public class CreateIssueDialog
    extends Application
{
    private final String DIALOG_CONTAINER = "//div[contains(@id,'CreateIssueDialog')]";

    private final String CANCEL_BUTTON_TOP = DIALOG_CONTAINER + "//div[contains(@class,'cancel-button-top')]";

    private final String CANCEL_BUTTON_BOTTOM = DIALOG_CONTAINER + "//button[contains(@class,'button-bottom')]";

    private final String CREATE_ISSUE_BUTTON =
        DIALOG_CONTAINER + "//button[contains(@class,'dialog-button') and child:span[text()='Create Issue']";

    private final String TITLE_INPUT = "//div[contains(@id,'FormItem') and child::label[text()='Title']]//input[@type='text']";

    private final String DESCRIPTION_TEXT_AREA = DIALOG_CONTAINER + TEXT_AREA_INPUT;

    @FindBy(xpath = CANCEL_BUTTON_TOP)
    private WebElement cancelButtonTop;

    @FindBy(xpath = CANCEL_BUTTON_BOTTOM)
    private WebElement cancelButtonBottom;

    @FindBy(xpath = CREATE_ISSUE_BUTTON)
    private WebElement descriptionTextArea;

    @FindBy(xpath = DESCRIPTION_TEXT_AREA)
    private WebElement createIssueButton;

    @FindBy(xpath = TITLE_INPUT)
    private WebElement titleTextInput;

    public CreateIssueDialog( final TestSession session )
    {
        super( session );
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

    public void waitForOpened()
    {
        if ( !waitUntilVisibleNoException( By.xpath( DIALOG_CONTAINER ), Application.EXPLICIT_NORMAL ) )
        {
            saveScreenshot( "err_create_issue_dialog" );
            throw new TestFrameworkException( "'Create Issue' dialog was not opened!" );
        }
    }
}
