package com.enonic.autotests.pages.usermanager.wizardpanel;


import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.WizardPanel;
import com.enonic.autotests.utils.TestUtils;
import com.enonic.autotests.vo.usermanager.UserStore;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class UserStoreWizardPanel
    extends WizardPanel<UserStore>
{
    public static final String DIV_USER_STORE_WIZARD_PANEL =
        "//div[contains(@id,'app.wizard.UserStoreWizardPanel') and not(contains(@style,'display: none'))]";

    public static final String TOOLBAR_SAVE_BUTTON =
        "//div[contains(@id,'app.wizard.UserStoreWizardToolbar')]/*[contains(@id, 'api.ui.button.ActionButton') and child::span[text()='Save']]";

    public static final String TOOLBAR_CLOSE_WIZARD_BUTTON =
        "//div[contains(@id,'app.wizard.UserStoreWizardToolbar')]/*[contains(@id, 'api.ui.button.ActionButton') and child::span[text()='Close']]";

    private static final String TOOLBAR_DELETE_BUTTON =
        "//div[contains(@id,'app.wizard.UserStoreWizardToolbar')]/*[contains(@id, 'api.ui.button.ActionButton') and child::span[text()='Delete']]";

    @FindBy(xpath = TOOLBAR_SAVE_BUTTON)
    protected WebElement toolbarSaveButton;

    @FindBy(xpath = TOOLBAR_CLOSE_WIZARD_BUTTON)
    protected WebElement closeButton;

    @FindBy(xpath = TOOLBAR_DELETE_BUTTON)
    private WebElement toolbarDeleteButton;


    /**
     * The constructor.
     *
     * @param session
     */
    public UserStoreWizardPanel( TestSession session )
    {
        super( session );

    }

    @Override
    public String getWizardDivXpath()
    {
        return DIV_USER_STORE_WIZARD_PANEL;
    }

    @Override
    public WizardPanel<UserStore> save()
    {
        toolbarSaveButton.click();
        sleep( 1000 );
        return this;
    }

    @Override
    public WebElement getCloseButton()
    {
        return closeButton;
    }

    @Override
    public WizardPanel<UserStore> typeData( final UserStore userStore )
    {
        // 1. type a data: 'name' and 'Display Name'.
        waitElementClickable( By.name( "displayName" ), 2 );
        clearAndType( displayNameInput, userStore.getDisplayName() );
        sleep( 500 );
        if ( StringUtils.isNotEmpty( userStore.getName() ) )
        {
            waitElementClickable( By.name( "name" ), 2 );
            clearAndType( nameInput, userStore.getName() );
        }
        TestUtils.saveScreenshot( getSession(), userStore.getDisplayName() );
        return this;
    }

    public String getStoreNameInputValue()
    {
        return nameInput.getAttribute( "value" );

    }

    public UserStoreWizardPanel typeDisplayName( String displayName )
    {
        clearAndType( displayNameInput, displayName );
        return this;
    }

    @Override
    public boolean isEnabledSaveButton()
    {
        return waitUntilElementEnabledNoException( By.xpath( TOOLBAR_SAVE_BUTTON ), Application.IMPLICITLY_WAIT );
    }

    @Override
    public boolean isOpened()
    {
        return toolbarSaveButton.isDisplayed();

    }

    @Override
    public UserStoreWizardPanel waitUntilWizardOpened()
    {
        boolean result = waitUntilVisibleNoException( By.xpath( DIV_USER_STORE_WIZARD_PANEL ), Application.EXPLICIT_4 );
        findElements( By.xpath( DIV_USER_STORE_WIZARD_PANEL ) );
        if ( !result )
        {
            throw new TestFrameworkException( "UserStoreWizard was not showed!" );
        }
        return this;
    }


    public boolean isSaveButtonEnabled()
    {
        return toolbarSaveButton.isEnabled();
    }


}

