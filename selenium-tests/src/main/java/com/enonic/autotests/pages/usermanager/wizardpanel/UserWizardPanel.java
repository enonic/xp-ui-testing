package com.enonic.autotests.pages.usermanager.wizardpanel;


import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.google.common.base.Strings;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.WizardPanel;
import com.enonic.autotests.pages.contentmanager.wizardpanel.ConfirmationDialog;
import com.enonic.autotests.utils.NameHelper;
import com.enonic.autotests.vo.usermanager.User;

import static com.enonic.autotests.utils.SleepHelper.sleep;


public class UserWizardPanel
    extends WizardPanel<User>
{
    public static final String PASSWORD_ERROR_MESSAGE = "Password can not be empty.";

    private final String TOOLBAR = "//div[contains(@id,'Toolbar')]";

    public final String USER_WIZARD_PANEL = "//div[contains(@id,'UserWizardPanel') and not(contains(@style,'display: none'))]";

    public final String TOOLBAR_SAVE_BUTTON = TOOLBAR + "/*[contains(@id, 'ActionButton') and child::span[text()='Save']]";

    private final String TOOLBAR_DELETE_BUTTON = TOOLBAR + "/*[contains(@id, 'ActionButton') and child::span[text()='Delete']]";

    private final String ROLE_OPTIONS_FILTER_INPUT =
        "//div[contains(@id,'FormItem') and child::label[text()='Roles']]" + COMBOBOX_OPTION_FILTER_INPUT;

    private final String GROUP_OPTIONS_FILTER_INPUT =
        "//div[contains(@id,'FormItem') and child::label[text()='Groups']]" + COMBOBOX_OPTION_FILTER_INPUT;

    private final String ROLE_COMBOBOX = "//div[contains(@id,'PrincipalComboBox')]";

    private String REMOVE_ROLE_BUTTON = ROLE_COMBOBOX +
        "//div[contains(@class,'principal-selected-option-view') and descendant::p[contains(.,'%s')]]//a[@class='icon-close']";

    private final String ALL_ROLES = USER_WIZARD_PANEL + "//div[contains(@id,'PrincipalSelectedOptionsView')]" + P_NAME;

    @FindBy(xpath = USER_WIZARD_PANEL + ROLE_OPTIONS_FILTER_INPUT)
    protected WebElement roleOptionsFilter;

    @FindBy(xpath = USER_WIZARD_PANEL + GROUP_OPTIONS_FILTER_INPUT)
    protected WebElement groupOptionsFilter;

    @FindBy(xpath = "//input[@type = 'email']")
    protected WebElement emailInput;

    @FindBy(xpath = "//input[@type = 'password']")
    protected WebElement passwordInput;

    @FindBy(xpath = TOOLBAR_SAVE_BUTTON)
    protected WebElement toolbarSaveButton;

    @FindBy(xpath = TOOLBAR_DELETE_BUTTON)
    private WebElement toolbarDeleteButton;

    /**
     * The constructor.
     *
     * @param session
     */
    public UserWizardPanel( TestSession session )
    {
        super( session );
    }

    @Override
    public String getWizardDivXpath()
    {
        return USER_WIZARD_PANEL;
    }

    @Override
    public WizardPanel<User> save()
    {
        toolbarSaveButton.click();
        sleep( 1000 );
        return this;
    }

    @Override
    public WizardPanel<User> typeData( final User user )
    {
        boolean isClickable = waitUntilClickableNoException( By.name( "displayName" ), 2 );
        if ( !isClickable )
        {
            saveScreenshot( "err_user_wizard" );
            throw new TestFrameworkException( "input for display name was not found" );
        }
        getLogger().info( "types displayName: " + user.getDisplayName() );
        clearAndType( displayNameInput, user.getDisplayName() );
        sleep( 500 );
        clearAndType( emailInput, user.getEmail() );
        sleep( 300 );
        if ( !Strings.isNullOrEmpty( user.getName() ) )
        {
            clearAndType( nameInput, user.getName() );
        }
        if ( !Strings.isNullOrEmpty( user.getPassword() ) )
        {
            clearAndType( passwordInput, user.getPassword() );
        }
        sleep( 500 );
        if ( user.getRoles() != null )
        {
            addRoles( user.getRoles() );
        }
        if ( user.getGroups() != null )
        {
            addGroups( user.getGroups() );
        }
        return this;
    }

    private void addRoles( List<String> names )
    {
        names.stream().forEach( roleName -> addRole( roleName ) );
    }

    public UserWizardPanel removeRoleByName( String roleName )
    {

        String removeButtonXpath = String.format( REMOVE_ROLE_BUTTON, roleName );
        if ( !isElementDisplayed( removeButtonXpath ) )
        {
            saveScreenshot( "err_" + roleName );
            throw new TestFrameworkException( "role was not found in membership-step-form:  " + roleName );
        }
        getDisplayedElement( By.xpath( removeButtonXpath ) ).click();
        return this;
    }

    private void addGroups( List<String> groupNames )
    {
        groupNames.stream().forEach( name -> addGroup( name ) );
    }

    private void addGroup( String groupName )
    {
        clearAndType( groupOptionsFilter, groupName );
        sleep( 1000 );
        String rowCheckboxXpath = String.format( SLICK_ROW_BY_NAME + "//label[child::input[@type='checkbox']]", groupName );
        if ( findElements( By.xpath( rowCheckboxXpath ) ).size() == 0 )
        {
            saveScreenshot( "err_group_not_found" );
            throw new TestFrameworkException( "Group was not found!" );
        }
        if ( !isRoleOrGroupAlreadySelected( groupName ) )
        {
            getDisplayedElement( By.xpath( rowCheckboxXpath ) ).click();
            groupOptionsFilter.sendKeys( Keys.ENTER );
            sleep( 300 );
        }
    }

    public void addRole( String roleName )
    {
        clearAndType( roleOptionsFilter, roleName );
        sleep( 1000 );
        String rowCheckboxXpath = String.format( SLICK_ROW_BY_NAME + "//label[child::input[@type='checkbox']]", roleName );
        if ( findElements( By.xpath( rowCheckboxXpath ) ).size() == 0 )
        {
            saveScreenshot( NameHelper.uniqueName( "err_role" ) );
            throw new TestFrameworkException( "Role was not found!" );
        }
        if ( !isRoleOrGroupAlreadySelected( roleName ) )
        {
            findElement( By.xpath( rowCheckboxXpath ) ).click();
            roleOptionsFilter.sendKeys( Keys.ENTER );
            sleep( 300 );
        }
    }

    private boolean isRoleOrGroupAlreadySelected( String name )
    {
        String rowXpath = String.format( SLICK_ROW_BY_NAME + "//input[@type='checkbox']", name );
        if ( findElements( By.xpath( rowXpath ) ).size() == 0 )
        {
            saveScreenshot( NameHelper.uniqueName( "err_" ) );
            throw new TestFrameworkException( "checkbox for role or group was not found: " + name );
        }
        return findElement( By.xpath( rowXpath ) ).getAttribute( "checked" ) != null;
    }

    public UserWizardPanel typeDisplayName( String displayName )
    {
        clearAndType( displayNameInput, displayName );
        return this;
    }

    public UserWizardPanel typePassword( String password )
    {
        clearAndType( passwordInput, password );
        return this;
    }

    public ChangeUserPasswordDialog clickOnChangePassword()
    {
        findElements( By.xpath( "//button[contains(@class,'change-password-button')]" ) ).get( 0 ).click();
        return new ChangeUserPasswordDialog( getSession() );
    }

    @Override
    public boolean isSaveButtonEnabled()
    {
        return waitUntilElementEnabledNoException( By.xpath( TOOLBAR_SAVE_BUTTON ), Application.EXPLICIT_NORMAL );
    }

    @Override
    public boolean isOpened()
    {
        return findElements( By.xpath( TOOLBAR_SAVE_BUTTON ) ).stream().filter( WebElement::isDisplayed ).count() > 0;
    }

    @Override
    public UserWizardPanel waitUntilWizardOpened()
    {
        boolean result = waitUntilVisibleNoException( By.xpath( USER_WIZARD_PANEL ), Application.EXPLICIT_NORMAL );
        if ( !result )
        {
            saveScreenshot( "err_user_wizard" );
            throw new TestFrameworkException( "UserWizard was not shown!" );
        }
        return this;
    }

    @Override
    public boolean isDeleteButtonEnabled()
    {
        return getDisplayedElement( By.xpath( TOOLBAR_DELETE_BUTTON ) ).isEnabled();
    }

    @Override
    public ConfirmationDialog clickToolbarDelete()
    {
        getDisplayedElement( By.xpath( TOOLBAR_DELETE_BUTTON ) ).click();
        sleep( 500 );
        ConfirmationDialog confirmationDialog = new ConfirmationDialog( getSession() );
        return confirmationDialog;
    }

    public List<String> getRoleNames()
    {
        return getDisplayedStrings( By.xpath( ALL_ROLES ) );
    }
}
