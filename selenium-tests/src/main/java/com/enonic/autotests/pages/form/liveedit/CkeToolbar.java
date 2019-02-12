package com.enonic.autotests.pages.form.liveedit;


import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.contentmanager.SourceCodeDialog;

/**
 * MCE toolbar on the LiveFormPanel
 * Created on 19.10.2016.
 */
public class CkeToolbar
    extends Application
{
    private final String TOOLBAR_CONTAINER = "//div[contains(@class,'mce-toolbar-container')]";
    private final String SOURCE_CODE_BUTTON= "//a[contains(@class,'cke_button__sourcedialog') and contains(@href,'Source')]";

    @FindBy(xpath = "//button/span[text()='Formats']")
    private WebElement formatsButtonMenu;

    @FindBy(xpath = "//button/i[contains(@class,'alignleft')]")
    private WebElement alignLeftButton;

    @FindBy(xpath = "//button/i[contains(@class,'aligncenter')]")
    private WebElement alignCenterButton;

    @FindBy(xpath = TOOLBAR_CONTAINER + "//button/i[contains(@class,'alignright')]")
    private WebElement alignRightButton;

    @FindBy(xpath = TOOLBAR_CONTAINER + "//button/i[contains(@class,'alignjustify')]")
    private WebElement alignJustifyButton;

    @FindBy(xpath = TOOLBAR_CONTAINER + "//button/i[contains(@class,'bullist')]")
    private WebElement bulListButton;

    @FindBy(xpath = TOOLBAR_CONTAINER + "//button/i[contains(@class,'numlist')]")
    private WebElement numListButton;

    @FindBy(xpath = TOOLBAR_CONTAINER + "//button/i[contains(@class,'outdent')]")
    private WebElement outdentButton;

    @FindBy(xpath = TOOLBAR_CONTAINER + "//button/i[contains(@class,'indent')]")
    private WebElement indentButton;

    @FindBy(xpath = TOOLBAR_CONTAINER + "//button/i[contains(@class,'charmap')]")
    private WebElement specialCharacterButton;

    @FindBy(xpath = TOOLBAR_CONTAINER + "//button/i[contains(@class,'anchor')]")
    private WebElement anchorButton;

    @FindBy(xpath = TOOLBAR_CONTAINER + "//button/i[contains(@class,'image')]")
    private WebElement insertImageButton;

    @FindBy(xpath = TOOLBAR_CONTAINER + "//button/i[contains(@class,'media')]")
    private WebElement insertMacroButton;

    @FindBy(xpath = TOOLBAR_CONTAINER + "//button/i[contains(@class,'link')]")
    private WebElement insertLinkButton;

    @FindBy(xpath = TOOLBAR_CONTAINER + "//button/i[contains(@class,'unlink')]")
    private WebElement removeLinkButton;

    @FindBy(xpath = TOOLBAR_CONTAINER + "//button/i[contains(@class,'table')]")
    private WebElement insertTableButton;

    @FindBy(xpath = SOURCE_CODE_BUTTON)
    //fullScreen: `//a[contains(@class,'cke_button__fullscreen_)  and contains(@href,'Fullscreen')]\``,)
    private WebElement sourceCodeButton;


    public CkeToolbar( final TestSession session )
    {
        super( session );
    }

    public boolean isDisplayed()
    {
        return isElementDisplayed( "//a[contains(@class,'cke_button')]" );
    }

    public boolean isFormatsMenuDisplayed()
    {
        return formatsButtonMenu.isDisplayed();
    }

    public boolean isAlignLeftButtonDisplayed()
    {
        return alignLeftButton.isDisplayed();
    }

    public boolean isAlignRightButtonDisplayed()
    {
        return alignRightButton.isDisplayed();
    }

    public boolean isAlignCenterButtonDisplayed()
    {
        return alignCenterButton.isDisplayed();
    }

    public boolean isAlignJustifyButtonDisplayed()
    {
        return alignJustifyButton.isDisplayed();
    }

    public boolean isBulListButtonDisplayed()
    {
        return bulListButton.isDisplayed();
    }

    public boolean isNumListButtonDisplayed()
    {
        return numListButton.isDisplayed();
    }

    public boolean isIndentButtonDisplayed()
    {
        return indentButton.isDisplayed();
    }

    public boolean isOutdentButtonDisplayed()
    {
        return outdentButton.isDisplayed();
    }

    public boolean isAnchorButtonDisplayed()
    {
        return anchorButton.isDisplayed();
    }

    public boolean isSpecialCharButtonDisplayed()
    {
        return specialCharacterButton.isDisplayed();
    }

    public boolean isInsertImageButtonDisplayed()
    {
        return insertImageButton.isDisplayed();
    }

    public boolean isInsertMacroButtonDisplayed()
    {
        return insertMacroButton.isDisplayed();
    }

    public boolean isInsertLinkButtonDisplayed()
    {
        return insertLinkButton.isDisplayed();
    }

    public boolean isRemoveLinkButtonDisplayed()
    {
        return removeLinkButton.isDisplayed();
    }

    public boolean isInsertTableButtonDisplayed()
    {
        return insertTableButton.isDisplayed();
    }

    public boolean isSourceCodeButtonDisplayed()
    {
        return isElementDisplayed( SOURCE_CODE_BUTTON );
    }

    public SourceCodeDialog clickOnSourceCodeButton()
    {
        sourceCodeButton.click();
        //switchToDefaultWindow();
        SourceCodeDialog window = new SourceCodeDialog( getSession() );
        window.waitForOpened();
        return window;
    }

    public void switchToDefaultWindow()
    {
        getDriver().switchTo().defaultContent();
        getSession().setInLiveEditFrame( false );
    }
}
