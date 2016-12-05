package com.enonic.autotests.pages.usermanager.wizardpanel;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.SaveOrUpdateException;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.WizardPanel;
import com.enonic.autotests.pages.contentmanager.wizardpanel.ConfirmationDialog;
import com.enonic.autotests.utils.NameHelper;
import com.enonic.autotests.vo.usermanager.Role;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class RoleWizardPanel
    extends WizardPanel<Role>
{
    private final String WIZARD_PANEL = "//div[contains(@id,'RoleWizardPanel')]";

    private final String MEMBERS_FORM = WIZARD_PANEL + "//div[contains(@id,'RoleMembersWizardStepForm')]";

    private final String TOOLBAR = "//div[contains(@id,'PrincipalWizardToolbar')]";

    public final String TOOLBAR_SAVE_BUTTON = WIZARD_PANEL + TOOLBAR + "/*[contains(@id,'ActionButton') and child::span[text()='Save']]";

    private final String TOOLBAR_DELETE_BUTTON =
        WIZARD_PANEL + TOOLBAR + "/*[contains(@id,'ActionButton') and child::span[text()='Delete']]";

    private final String DESCRIPTION_INPUT = WIZARD_PANEL + "//div[@class='form-view']//input[contains(@id,'TextInput')]";

    private final String ROLE_OPTIONS_FILTER_INPUT =
        "//div[contains(@id,'FormItem') and child::label[text()='Members']]" + COMBOBOX_OPTION_FILTER_INPUT;

    @FindBy(xpath = TOOLBAR_SAVE_BUTTON)
    private WebElement toolbarSaveButton;

    @FindBy(xpath = TOOLBAR_DELETE_BUTTON)
    private WebElement toolbarDeleteButton;

    @FindBy(xpath = DESCRIPTION_INPUT)
    private WebElement descriptionInput;

    @FindBy(xpath = WIZARD_PANEL + ROLE_OPTIONS_FILTER_INPUT)
    protected WebElement roleOptionsFilter;

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
            saveScreenshot( NameHelper.uniqueName( "err_save_button" ) );
            throw new SaveOrUpdateException( "'Save' is disabled!" );
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
        boolean isClickable = waitUntilClickableNoException( By.name( "displayName" ), 2 );
        if ( !isClickable )
        {
            saveScreenshot( "err_role_wizard" );
            throw new TestFrameworkException( "input for display name was not found" );
        }
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
        if ( role.getMemberDisplayNames() != null )
        {
            addMembers( role.getMemberDisplayNames() );
        }
        return this;
    }

    public RoleWizardPanel addMembers( List<String> displayNames )
    {
        displayNames.stream().forEach( memberDisplayName -> addMember( memberDisplayName ) );
        return this;
    }

    public RoleWizardPanel removeMember( String displayName )
    {
        String removeButton = String.format( MEMBERS_FORM +
                                                 "//div[contains(@class,'principal-selected-options-view') and descendant::h6[contains(@class,'main-name') and text()='%s']]//a[@class='icon-close']",
                                             displayName );

        if ( !isElementDisplayed( removeButton ) )
        {
            saveScreenshot( NameHelper.uniqueName( "err_member" + displayName ) );
            throw new TestFrameworkException( "member was not found! " + displayName );
        }
        getDisplayedElement( By.xpath( removeButton ) ).click();
        sleep( 300 );
        return this;
    }

    public void addMember( String memberDisplayName )
    {
        clearAndType( roleOptionsFilter, memberDisplayName );
        sleep( 1000 );
        String rowCheckboxXpath = String.format( SLICK_ROW_BY_DISPLAY_NAME + "//label[child::input[@type='checkbox']]", memberDisplayName );
        if ( findElements( By.xpath( rowCheckboxXpath ) ).size() == 0 )
        {
            saveScreenshot( "err_" + memberDisplayName );
            throw new TestFrameworkException( "Role was not found!" );
        }
        if ( !isMemberAlreadyAdded( memberDisplayName ) )
        {
            findElement( By.xpath( rowCheckboxXpath ) ).click();
            roleOptionsFilter.sendKeys( Keys.ENTER );
            sleep( 300 );
        }
    }

    private boolean isMemberAlreadyAdded( String memberDisplayName )
    {
        String checkboxPath = String.format( SLICK_ROW_BY_DISPLAY_NAME + "//input[@type='checkbox']", memberDisplayName );
        if ( findElements( By.xpath( checkboxPath ) ).size() == 0 )
        {
            saveScreenshot( NameHelper.uniqueName( "err_role_member" ) );
            throw new TestFrameworkException( "checkbox for option was not found: " + memberDisplayName );
        }
        return findElement( By.xpath( checkboxPath ) ).getAttribute( "checked" ) != null;
    }

    @Override
    public RoleWizardPanel waitUntilWizardOpened()
    {
        boolean result = waitUntilVisibleNoException( By.xpath( WIZARD_PANEL ), Application.EXPLICIT_NORMAL );
        if ( !result )
        {
            saveScreenshot( NameHelper.uniqueName( "err_role" ) );
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

    @Override
    public ConfirmationDialog clickToolbarDelete()
    {
        toolbarDeleteButton.click();
        sleep( 1000 );
        ConfirmationDialog confirmationDialog = new ConfirmationDialog( getSession() );
        return confirmationDialog;
    }

    public String getDescription()
    {
        return descriptionInput.getAttribute( "value" );
    }
}
