package com.enonic.autotests.pages.usermanager.wizardpanel;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.SaveOrUpdateException;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.WizardPanel;
import com.enonic.autotests.pages.contentmanager.wizardpanel.ConfirmationDialog;
import com.enonic.autotests.utils.NameHelper;
import com.enonic.autotests.utils.TestUtils;
import com.enonic.autotests.vo.usermanager.Role;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class RoleWizardPanel
    extends WizardPanel<Role>
{
    private final String WIZARD_PANEL = "//div[contains(@id,'app.wizard.RoleWizardPanel')]";

    private final String TOOLBAR = "//div[contains(@id,'PrincipalWizardToolbar')]";

    public final String TOOLBAR_SAVE_BUTTON = WIZARD_PANEL + TOOLBAR +
        "/*[contains(@id, 'api.ui.button.ActionButton') and child::span[text()='Save']]";

    private final String TOOLBAR_DELETE_BUTTON = WIZARD_PANEL + TOOLBAR +
        "/*[contains(@id, 'api.ui.button.ActionButton') and child::span[text()='Delete']]";

    private final String DESCRIPTION_INPUT = WIZARD_PANEL + "//div[@class='form-view']//input[contains(@id,'TextInput')]";

    @FindBy(xpath = TOOLBAR_SAVE_BUTTON)
    private WebElement toolbarSaveButton;

    @FindBy(xpath = TOOLBAR_DELETE_BUTTON)
    private WebElement toolbarDeleteButton;

    @FindBy(xpath = DESCRIPTION_INPUT)
    private WebElement descriptionInput;

    /**
     * The constructor.
     *
     * @param session
     */
    public RoleWizardPanel( TestSession session )
    {
        super( session );
    }

    @Override
    public String getWizardDivXpath()
    {
        return WIZARD_PANEL;
    }

    @Override
    public WizardPanel<Role> save()
    {
        boolean isSaveButtonEnabled = waitUntilElementEnabledNoException( By.xpath( TOOLBAR_SAVE_BUTTON ), 2l );
        if ( !isSaveButtonEnabled )
        {
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "err_save_button" ) );
            throw new SaveOrUpdateException( "Impossible to save, button 'Save' is not available!!" );
        }
        toolbarSaveButton.click();
        sleep( 700 );
        return this;
    }

    @Override
    public boolean isOpened()
    {
        return isElementDisplayed( WIZARD_PANEL );
    }

    @Override
    public boolean isSaveButtonEnabled()
    {
        return toolbarSaveButton.isEnabled();
    }

    @Override
    public WizardPanel<Role> typeData( final Role role )
    {
        waitElementClickable( By.name( "displayName" ), 2 );
        getLogger().info( "types displayName: " + role.getDisplayName() );
        sleep( 500 );
        if ( StringUtils.isNotEmpty( role.getDisplayName() ) )
        {
            getLogger().info( "types display name: " + role.getDisplayName() );
            clearAndType( displayNameInput, role.getDisplayName() );
        }
        if ( StringUtils.isNotEmpty( role.getName() ) )
        {
            getLogger().info( "types name: " + role.getName() );
            clearAndType( nameInput, role.getName().trim() );
        }
        if ( StringUtils.isNotEmpty( role.getDescription() ) )
        {
            getLogger().info( "types the description: " + role.getDescription() );
            clearAndType( descriptionInput, role.getDescription() );
        }
        TestUtils.saveScreenshot( getSession(), role.getDisplayName() );
        return this;
    }

    @Override
    public RoleWizardPanel waitUntilWizardOpened()
    {
        boolean result = waitUntilVisibleNoException( By.xpath( WIZARD_PANEL ), Application.EXPLICIT_NORMAL );
        if ( !result )
        {
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "err_role" ) );
            throw new TestFrameworkException( "RoleWizard was not opened!" );
        }
        return this;
    }

    @Override
    public RoleWizardPanel typeDisplayName( String displayName )
    {
        clearAndType( displayNameInput, displayName );
        return this;
    }

    public RoleWizardPanel typeName( String name )
    {
        clearAndType( nameInput, name );
        return this;
    }

    @Override
    public boolean isDeleteButtonEnabled()
    {
        return toolbarDeleteButton.isEnabled();
    }

    public ConfirmationDialog clickToolbarDelete()
    {
        toolbarDeleteButton.click();
        sleep( 1000 );
        ConfirmationDialog confirmationDialog = new ConfirmationDialog( getSession() );
        return confirmationDialog;
    }
}
