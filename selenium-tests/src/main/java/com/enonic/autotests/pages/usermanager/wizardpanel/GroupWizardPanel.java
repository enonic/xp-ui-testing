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
import com.enonic.autotests.vo.usermanager.Group;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class GroupWizardPanel
    extends WizardPanel<Group>
{
    private final String CONTAINER = "//div[contains(@id,'app.wizard.GroupWizardPanel')]";

    private final String TOOLBAR = "//div[contains(@id,'PrincipalWizardToolbar')]";

    public static final String DIV_ROLE_WIZARD_PANEL =
        "//div[contains(@id,'app.wizard.GroupWizardPanel') and not(contains(@style,'display: none'))]";

    public final String TOOLBAR_SAVE_BUTTON = CONTAINER + TOOLBAR +
        "//*[contains(@id, 'api.ui.button.ActionButton') and child::span[text()='Save']]";

    private final String TOOLBAR_DELETE_BUTTON = CONTAINER + TOOLBAR + "/*[contains(@id, 'ActionButton') and child::span[text()='Delete']]";

    private final String DESCRIPTION_INPUT = CONTAINER + "//div[@class='form-view']//input[contains(@id,'TextInput')]";

    @FindBy(xpath = TOOLBAR_DELETE_BUTTON)
    private WebElement toolbarDeleteButton;

    @FindBy(xpath = TOOLBAR_SAVE_BUTTON)
    private WebElement toolbarSaveButton;

    @FindBy(xpath = DESCRIPTION_INPUT)
    private WebElement descriptionInput;

    /**
     * The constructor.
     *
     * @param session
     */
    public GroupWizardPanel( TestSession session )
    {
        super( session );
    }

    @Override
    public String getWizardDivXpath()
    {
        return DIV_ROLE_WIZARD_PANEL;
    }

    @Override
    public WizardPanel<Group> save()
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

    public ConfirmationDialog clickToolbarDelete()
    {
        toolbarDeleteButton.click();
        sleep( 1000 );
        ConfirmationDialog confirmationDialog = new ConfirmationDialog( getSession() );
        return confirmationDialog;
    }

    @Override
    public boolean isOpened()
    {
        return isElementDisplayed( CONTAINER );
    }

    @Override
    public boolean isSaveButtonEnabled()
    {
        return getDisplayedElement( By.xpath( TOOLBAR_SAVE_BUTTON ) ).isEnabled();
    }

    @Override
    public WizardPanel<Group> typeData( final Group group )
    {
        waitElementClickable( By.name( "displayName" ), 2 );
        getLogger().info( "types displayName: " + group.getDisplayName() );
        sleep( 500 );
        if ( StringUtils.isNotEmpty( group.getName() ) )
        {
            getLogger().info( "types name: " + group.getName() );
            clearAndType( nameInput, group.getName().trim() );
        }
        if ( StringUtils.isNotEmpty( group.getDisplayName() ) )
        {
            getLogger().info( "types display name: " + group.getDisplayName() );
            clearAndType( displayNameInput, group.getDisplayName() );
        }
        if ( StringUtils.isNotEmpty( group.getDescription() ) )
        {
            getLogger().info( "types the description: " + group.getDescription() );
            clearAndType( descriptionInput, group.getDescription() );
        }
        TestUtils.saveScreenshot( getSession(), group.getDisplayName() );
        return this;
    }

    public GroupWizardPanel typeDisplayName( String displayName )
    {
        clearAndType( displayNameInput, displayName );
        return this;
    }

    @Override
    public WizardPanel<Group> waitUntilWizardOpened()
    {
        boolean result = waitUntilVisibleNoException( By.xpath( DIV_ROLE_WIZARD_PANEL ), Application.EXPLICIT_NORMAL );
        findElements( By.xpath( DIV_ROLE_WIZARD_PANEL ) );
        if ( !result )
        {
            throw new TestFrameworkException( "UserWizard was not showed!" );
        }
        return this;
    }

    @Override
    public boolean isDeleteButtonEnabled()
    {
        return toolbarDeleteButton.isEnabled();
    }
}