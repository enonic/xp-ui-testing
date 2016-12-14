package com.enonic.autotests.pages.form.liveedit;


import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.contentmanager.SourceCodeMceWindow;

/**
 * MCE toolbar on the LiveFormPanel
 * Created on 19.10.2016.
 */
public class MceToolbar
    extends Application
{
    private final String MCE_TOOLBAR_CONTAINER = "//div[contains(@class,'mce-toolbar-container')]";

    @FindBy(xpath = MCE_TOOLBAR_CONTAINER + "//button/span[text()='Formats']")
    private WebElement formatsButtonMenu;

    @FindBy(xpath = MCE_TOOLBAR_CONTAINER + "//button/i[contains(@class,'alignleft')]")
    private WebElement alignLeftButton;

    @FindBy(xpath = MCE_TOOLBAR_CONTAINER + "//button/i[contains(@class,'aligncenter')]")
    private WebElement alignCenterButton;

    @FindBy(xpath = MCE_TOOLBAR_CONTAINER + "//button/i[contains(@class,'alignright')]")
    private WebElement alignRightButton;

    @FindBy(xpath = MCE_TOOLBAR_CONTAINER + "//button/i[contains(@class,'alignjustify')]")
    private WebElement alignJustifyButton;

    @FindBy(xpath = MCE_TOOLBAR_CONTAINER + "//button/i[contains(@class,'bullist')]")
    private WebElement bulListButton;

    @FindBy(xpath = MCE_TOOLBAR_CONTAINER + "//button/i[contains(@class,'numlist')]")
    private WebElement numListButton;

    @FindBy(xpath = MCE_TOOLBAR_CONTAINER + "//button/i[contains(@class,'outdent')]")
    private WebElement outdentButton;

    @FindBy(xpath = MCE_TOOLBAR_CONTAINER + "//button/i[contains(@class,'indent')]")
    private WebElement indentButton;

    @FindBy(xpath = MCE_TOOLBAR_CONTAINER + "//button/i[contains(@class,'charmap')]")
    private WebElement specialCharacterButton;

    @FindBy(xpath = MCE_TOOLBAR_CONTAINER + "//button/i[contains(@class,'anchor')]")
    private WebElement anchorButton;

    @FindBy(xpath = MCE_TOOLBAR_CONTAINER + "//button/i[contains(@class,'image')]")
    private WebElement insertImageButton;

    @FindBy(xpath = MCE_TOOLBAR_CONTAINER + "//button/i[contains(@class,'media')]")
    private WebElement insertMacroButton;

    @FindBy(xpath = MCE_TOOLBAR_CONTAINER + "//button/i[contains(@class,'link')]")
    private WebElement insertLinkButton;

    @FindBy(xpath = MCE_TOOLBAR_CONTAINER + "//button/i[contains(@class,'unlink')]")
    private WebElement removeLinkButton;

    @FindBy(xpath = MCE_TOOLBAR_CONTAINER + "//button/i[contains(@class,'table')]")
    private WebElement insertTableButton;

    @FindBy(xpath = MCE_TOOLBAR_CONTAINER + "//button/i[contains(@class,'code')]")
    private WebElement sourceCodeButton;


    public MceToolbar( final TestSession session )
    {
        super( session );
    }

    public boolean isDisplayed()
    {
        return isElementDisplayed( MCE_TOOLBAR_CONTAINER );
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
        return isElementDisplayed( MCE_TOOLBAR_CONTAINER + "//button/i[contains(@class,'code')]" );
    }

    public SourceCodeMceWindow clickOnSourceCodeButton()
    {
        sourceCodeButton.click();
        SourceCodeMceWindow window = new SourceCodeMceWindow( getSession() );
        window.waitForOpened();
        return window;
    }
}
