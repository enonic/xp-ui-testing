package com.enonic.autotests.pages.contentmanager.wizardpanel.macro;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.RichComboBoxInput;
import com.enonic.autotests.utils.TestUtils;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class MacroModalDialog
    extends Application
{
    public static final String DIALOG_CONTAINER = "//div[contains(@id,'MacroModalDialog')]";

    public static String DIALOG_HEADER_TEXT = "Insert Macro";

    private final String DIALOG_HEADER = DIALOG_CONTAINER + "//div[contains(@id,'ModalDialogHeader')]/h2";

    private final String FILTER_INPUT = DIALOG_CONTAINER + COMBOBOX_OPTION_FILTER_INPUT;

    private final String CANCEL_BUTTON = DIALOG_CONTAINER + "//button[contains(@id,'DialogButton') and child::span[text()='Cancel']]";

    private final String INSERT_BUTTON = DIALOG_CONTAINER + "//button[contains(@id,'DialogButton') and child::span[text()='Insert']]";

    private final String MACRO_SELECTED_VIEW = DIALOG_CONTAINER + "//div[contains(@id,'MacroSelectedOptionsView')]";

    private final String TAB_BAR = DIALOG_CONTAINER + "//ul[contains(@id,'TabBar')]";

    private final String CONFIG_TAB_BAR_ITEM = TAB_BAR + "//li[contains(@id,'TabBarItem')]//span[@title='Configuration']";

    private final String PREVIEW_TAB_BAR_ITEM = TAB_BAR + "//li[contains(@id,'TabBarItem')]//span[@title='Preview']";


    private final String MACRO_SELECTED_DISPLAY_NAME = MACRO_SELECTED_VIEW + H6_DISPLAY_NAME;

    private final String REMOVE_SELECTED_MACRO = MACRO_SELECTED_VIEW + "//a[@class='remove']";

    private MacroConfigPanel macroConfigPanel;

    private MacroPreviewPanel macroPreviewPanel;

    @FindBy(xpath = FILTER_INPUT)
    private WebElement optionFilterInput;

    @FindBy(xpath = CANCEL_BUTTON)
    private WebElement cancelButton;

    @FindBy(xpath = INSERT_BUTTON)
    private WebElement insertButton;

    @FindBy(xpath = CONFIG_TAB_BAR_ITEM)
    private WebElement configurationTabLink;

    @FindBy(xpath = PREVIEW_TAB_BAR_ITEM)
    private WebElement previewTabLink;

    @FindBy(xpath = REMOVE_SELECTED_MACRO)
    private WebElement removeMacroButton;


    public MacroModalDialog( TestSession session )
    {
        super( session );
    }

    public String getHeader()
    {
        return getDisplayedString( DIALOG_HEADER );
    }

    public boolean isConfigurationTabLinkPresent()
    {
        return configurationTabLink.isDisplayed();
    }

    public boolean isPreviewTabLinkPresent()
    {
        return configurationTabLink.isDisplayed();
    }

    public boolean isRemoveMacroButtonPresent()
    {
        return removeMacroButton.isDisplayed();
    }

    public MacroModalDialog clickOnRemoveMacroButton()
    {
        if ( !isRemoveMacroButtonPresent() )
        {
            TestUtils.saveScreenshot( getSession(), "err_remove_macro" );
            throw new TestFrameworkException( "'remove macro' button was not found! " );
        }
        removeMacroButton.click();
        sleep( 300 );
        return this;
    }

    public MacroPreviewPanel clickOnPreviewTabLink()
    {
        previewTabLink.click();
        sleep( 200 );
        return new MacroPreviewPanel( getSession() );
    }

    public MacroConfigPanel clickOnConfigurationTabLink()
    {
        configurationTabLink.click();
        sleep( 200 );
        return macroConfigPanel;
    }

    public boolean isCancelButtonDisplayed()
    {
        return isElementDisplayed( CANCEL_BUTTON );
    }

    public boolean isInsertButtonDisplayed()
    {
        return isElementDisplayed( INSERT_BUTTON );
    }

    public boolean isOpened()
    {
        return isElementDisplayed( DIALOG_CONTAINER );
    }

    public boolean waitForClosed()
    {
        return waitsElementNotVisible( By.xpath( DIALOG_CONTAINER ), Application.EXPLICIT_QUICK );
    }

    public MacroModalDialog typeSearchText()
    {
        cancelButton.click();
        return this;
    }

    public String getSelectedMacroDisplayName()
    {
        return getDisplayedString( MACRO_SELECTED_DISPLAY_NAME );
    }

    public void clickOnCancel()
    {
        cancelButton.click();
    }

    public boolean isOptionFilterDisplayed()
    {
        return isElementDisplayed( COMBOBOX_OPTION_FILTER_INPUT );
    }

    public MacroModalDialog selectOption( MacroType macroName )
    {
        WebElement optionsInput = getDisplayedElement( By.xpath( DIALOG_CONTAINER + COMBOBOX_OPTION_FILTER_INPUT ) );
        clearAndType( optionsInput, macroName.getValue() );
        sleep( 500 );
        RichComboBoxInput richComboBoxInput = new RichComboBoxInput( getSession() );
        richComboBoxInput.selectOption( macroName.getValue() );
        switch ( macroName )
        {
            case TWITTER:
                macroConfigPanel = new TwitterConfigPanel( getSession() );
                break;
            case YOUTUBE:
                macroConfigPanel = new YoutubeConfigPanel( getSession() );
                break;
            default:
                macroConfigPanel = new TextAreaConfigPanel( getSession() );
        }
        return this;
    }

    public MacroConfigPanel getMacroConfigPanel()
    {
        return macroConfigPanel;
    }

    public void clickInsertButton()
    {
        getDisplayedElement( By.xpath( INSERT_BUTTON ) ).click();
        sleep( 400 );
    }

    public void waitForOpened()
    {
        if ( !waitUntilVisibleNoException( By.xpath( DIALOG_CONTAINER ), Application.EXPLICIT_NORMAL ) )
        {
            TestUtils.saveScreenshot( getSession(), "err_insert_link_dialog" );
            throw new TestFrameworkException( "InsertLink dialog was not opened!" );
        }
    }
}

