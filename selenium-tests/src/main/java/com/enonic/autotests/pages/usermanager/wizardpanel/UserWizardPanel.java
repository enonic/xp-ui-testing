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
import com.enonic.autotests.utils.TestUtils;
import com.enonic.autotests.vo.usermanager.User;

import static com.enonic.autotests.utils.SleepHelper.sleep;


public class UserWizardPanel
    extends WizardPanel<User>
{
    public static final String PASSWORD_ERROR_MESSAGE = "Password can not be empty.";

    private final String TOOLBAR = "//div[contains(@id,'app.wizard.PrincipalWizardToolbar')]";

    public static String GRID_ROW =
        "//div[@class='slick-viewport']//div[contains(@class,'slick-row') and descendant::p[@class='sub-name' and contains(.,'%s')]]";

    public static final String DIV_USER_WIZARD_PANEL =
        "//div[contains(@id,'app.wizard.UserWizardPanel') and not(contains(@style,'display: none'))]";

    public final String TOOLBAR_SAVE_BUTTON = TOOLBAR + "/*[contains(@id, 'api.ui.button.ActionButton') and child::span[text()='Save']]";

    public final String TOOLBAR_CLOSE_WIZARD_BUTTON =
        TOOLBAR + "/*[contains(@id, 'api.ui.button.ActionButton') and child::span[text()='Close']]";

    private final String TOOLBAR_DELETE_BUTTON =
        TOOLBAR + "/*[contains(@id, 'api.ui.button.ActionButton') and child::span[text()='Delete']]";

    private final String ROLE_OPTIONS_FILTER_INPUT =
        "//div[contains(@id,'FormItem') and child::label[text()='Roles']]//input[contains(@id,'ComboBoxOptionFilterInput')]";

    private final String GROUP_OPTIONS_FILTER_INPUT =
        "//div[contains(@id,'FormItem') and child::label[text()='Groups']]//input[contains(@id,'ComboBoxOptionFilterInput')]";

    @FindBy(xpath = DIV_USER_WIZARD_PANEL + ROLE_OPTIONS_FILTER_INPUT)
    protected WebElement roleOptionsFilter;

    @FindBy(xpath = DIV_USER_WIZARD_PANEL + GROUP_OPTIONS_FILTER_INPUT)
    protected WebElement groupOptionsFilter;

    @FindBy(xpath = "//input[@type = 'email']")
    protected WebElement emailInput;

    @FindBy(xpath = "//input[@type = 'password']")
    protected WebElement passwordInput;

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
    public UserWizardPanel( TestSession session )
    {
        super( session );
    }

    @Override
    public String getWizardDivXpath()
    {
        return DIV_USER_WIZARD_PANEL;
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
        waitElementClickable( By.name( "displayName" ), 2 );
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
        TestUtils.saveScreenshot( getSession(), user.getDisplayName() );
        return this;
    }

    private void addRoles( List<String> names )
    {
        names.stream().forEach( roleName -> addRole( roleName ) );
    }

    private void addGroups( List<String> groupNames )
    {
        groupNames.stream().forEach( name -> addGroup( name ) );
    }

    private void addGroup( String groupName )
    {
        groupOptionsFilter.sendKeys( groupName );
        sleep( 1000 );
        String rowCheckboxXpath = String.format( GRID_ROW + "//label[child::input[@type='checkbox']]", groupName );
        if ( findElements( By.xpath( rowCheckboxXpath ) ).size() == 0 )
        {
            throw new TestFrameworkException( "Group was not found!" );
        }
        if ( isRoleSelected( groupName ) )
        {
            getDisplayedElement( By.xpath( rowCheckboxXpath ) ).click();
            groupOptionsFilter.sendKeys( Keys.ENTER );
            sleep( 300 );
        }
    }

    private void addRole( String roleName )
    {
        roleOptionsFilter.sendKeys( roleName );
        sleep( 1000 );
        String rowCheckboxXpath = String.format( GRID_ROW + "//label[child::input[@type='checkbox']]", roleName );
        if ( findElements( By.xpath( rowCheckboxXpath ) ).size() == 0 )
        {
            throw new TestFrameworkException( "Role was not found!" );
        }
        if ( isRoleSelected( roleName ) )
        {
            findElements( By.xpath( rowCheckboxXpath ) ).get( 0 ).click();
            roleOptionsFilter.sendKeys( Keys.ENTER );
            sleep( 300 );
        }
    }

    private boolean isRoleSelected( String roleName )
    {
        return findElements( By.xpath( String.format( GRID_ROW + "//input[@type='checkbox']", roleName ) ) ).get( 0 ).getAttribute(
            "value" ) != "on";
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
        boolean result = waitUntilVisibleNoException( By.xpath( DIV_USER_WIZARD_PANEL ), Application.EXPLICIT_NORMAL );
        findElements( By.xpath( DIV_USER_WIZARD_PANEL ) );
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
