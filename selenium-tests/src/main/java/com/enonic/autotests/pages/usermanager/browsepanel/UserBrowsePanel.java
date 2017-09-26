package com.enonic.autotests.pages.usermanager.browsepanel;


import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.BrowsePanel;
import com.enonic.autotests.pages.WizardPanel;
import com.enonic.autotests.pages.usermanager.wizardpanel.GroupWizardPanel;
import com.enonic.autotests.pages.usermanager.wizardpanel.RoleWizardPanel;
import com.enonic.autotests.pages.usermanager.wizardpanel.UserStoreWizardPanel;
import com.enonic.autotests.pages.usermanager.wizardpanel.UserWizardPanel;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class UserBrowsePanel
    extends BrowsePanel
{
    public static final String USER_ITEM_TYPE = "user_item_type";

    private final String USER_BROWSE_TOOLBAR = "//div[contains(@id,'UserBrowseToolbar')]";

    private final String USER_ITEMS_GRID = "//div[contains(@id,'UserItemsTreeGrid')]";

    public final String USERS_BUTTON = "//div[contains(@id,'AppIcon') and child::span[text()='Users']]";

    public final String NEW_BUTTON_XPATH = USER_BROWSE_TOOLBAR + "/*[contains(@id, 'ActionButton') and child::span[contains(.,'New')]]";

    public final String DUPLICATE_BUTTON_XPATH =
        USER_BROWSE_TOOLBAR + "/*[contains(@id, 'ActionButton') and child::span[text()='Duplicate']]";

    protected final String EDIT_BUTTON_XPATH = USER_BROWSE_TOOLBAR + "/*[contains(@id, 'ActionButton') and child::span[text()='Edit']]";

    protected final String DELETE_BUTTON_XPATH = USER_BROWSE_TOOLBAR + "/*[contains(@id, 'ActionButton') and child::span[text()='Delete']]";

    protected final String SYNC_BUTTON_XPATH = USER_BROWSE_TOOLBAR + "/*[contains(@id, 'ActionButton') and child::span[text()='Sync']]";

    @FindBy(xpath = USERS_BUTTON)
    private WebElement usersButton;

    @FindBy(xpath = NEW_BUTTON_XPATH)
    private WebElement newButton;

    @FindBy(xpath = EDIT_BUTTON_XPATH)
    private WebElement editButton;

    @FindBy(xpath = DELETE_BUTTON_XPATH)
    private WebElement deleteButton;

    @FindBy(xpath = DUPLICATE_BUTTON_XPATH)
    private WebElement duplicateButton;

    @FindBy(xpath = SYNC_BUTTON_XPATH)
    private WebElement syncButton;

    private UserBrowseFilterPanel userBrowseFilterPanel;

    public UserBrowseFilterPanel getUserBrowseFilterPanel()
    {
        if ( userBrowseFilterPanel == null )
        {
            userBrowseFilterPanel = new UserBrowseFilterPanel( getSession() );
        }
        return userBrowseFilterPanel;
    }

    /**
     * The Constructor
     *
     * @param session
     */
    public UserBrowsePanel( final TestSession session )
    {
        super( session );
    }

    public UserBrowseFilterPanel getFilterPanel()
    {
        if ( userBrowseFilterPanel == null )
        {
            userBrowseFilterPanel = new UserBrowseFilterPanel( getSession() );
        }
        return userBrowseFilterPanel;
    }


    @Override
    public UserBrowsePanel pressAppHomeButton()
    {
        usersButton.click();
        sleep( 1000 );
        waitUntilPageLoaded( Application.EXPLICIT_NORMAL );
        return this;
    }

    public UserBrowsePanel expandStoreAndSelectUsers( String storeName )
    {
        clickOnExpander( storeName );
        sleep( 700 );
        clickOnRowByName( "users" );
        sleep( 500 );
        getSession().put( USER_ITEM_TYPE, UserItemName.USERS_FOLDER );
        return this;
    }

    public UserBrowsePanel expandStoreAndSelectGroups( String storeName )
    {
        clickOnExpander( storeName );
        sleep( 700 );
        // pressKeyOnRow( storeName, Keys.ARROW_RIGHT );
        clickOnRowByName( "groups" );
        sleep( 500 );
        getSession().put( USER_ITEM_TYPE, UserItemName.GROUPS_FOLDER );
        return this;
    }

    public UserBrowsePanel expandUsersFolder( String storeName )
    {
        clickOnExpander( storeName );
        clickOnExpander( "users" );
        return this;
    }

    @Override
    public DeleteUserItemDialog clickToolbarDelete()
    {
        boolean isEnabledDeleteButton = waitUntilElementEnabledNoException( By.xpath( DELETE_BUTTON_XPATH ), 2l );
        if ( !isEnabledDeleteButton )
        {
            saveScreenshot( "err_deleting_users" );
            throw new TestFrameworkException( "Impossible to delete a user store, because the 'Delete' button is disabled!" );
        }
        deleteButton.click();
        DeleteUserItemDialog dialog = new DeleteUserItemDialog( getSession() );
        dialog.waitForOpened();
        return dialog;
    }

    public List<String> getItemsNameFromGrid()
    {
        return getDisplayedStrings( By.xpath( USER_ITEMS_GRID + P_NAME ) );
    }

    public UserBrowsePanel clickCheckboxAndSelectFolder( UserItemName itemType )
    {
        getSession().put( USER_ITEM_TYPE, itemType );
        return clickCheckboxAndSelectRow( itemType.getValue() );
    }

    public UserBrowsePanel clickCheckboxAndSelectUser( String userAppItemName )
    {
        getSession().put( USER_ITEM_TYPE, UserItemName.USER );
        return clickCheckboxAndSelectRow( userAppItemName );
    }

    public UserBrowsePanel clickCheckboxAndSelectUserStore( String userAppItemName )
    {
        getSession().put( USER_ITEM_TYPE, UserItemName.USER_STORE );
        return clickCheckboxAndSelectRow( userAppItemName );
    }

    public UserBrowsePanel clickCheckboxAndSelectGroup( String groupName )
    {
        getSession().put( USER_ITEM_TYPE, UserItemName.GROUP );
        return clickCheckboxAndSelectRow( groupName );
    }

    public UserBrowsePanel clickCheckboxAndSelectRole( String roleName )
    {
        getSession().put( USER_ITEM_TYPE, UserItemName.ROLE );
        return clickCheckboxAndSelectRow( roleName );
    }

    public UserBrowsePanel selectGroupsFolderInUserStore( String userStoreName )
    {
        if ( isRowExpanded( userStoreName ) )
        {
            clickOnExpander( userStoreName );
        }
        getSession().put( USER_ITEM_TYPE, UserItemName.GROUPS_FOLDER );
        return clickOnRowAndSelectGroupInUserStore( userStoreName );
    }

    public boolean exists( String name )
    {
        return exists( name, false );
    }

    public UserBrowsePanel clickOnRowAndSelectGroupInUserStore( String userStoreName )
    {
        String GROUP_ROW = String.format( NAMES_VIEW_BY_NAME, userStoreName ) + "/ancestor::div[contains(@class,'slick-row')] " +
            "//following-sibling::div//" +
            String.format( "div[contains(@id,'api.app.NamesView') and child::p[contains(@title,'%s')]]", "groups" );
        Actions builder = new Actions( getDriver() );
        builder.click( findElement( By.xpath( GROUP_ROW ) ) ).build().perform();
        sleep( 500 );
        return this;
    }

    /**
     * @param session
     * @return true if 'Users app' opened, otherwise false.
     */
    public boolean isOpened( TestSession session )
    {
        return isElementDisplayed( USERS_BUTTON );
    }

    public NewPrincipalDialog clickToolbarNew()
    {
        newButton.click();
        sleep( 500 );
        NewPrincipalDialog newPrincipalDialog = new NewPrincipalDialog( getSession() );
        newPrincipalDialog.waitForLoaded( Application.EXPLICIT_NORMAL );
        return newPrincipalDialog;
    }

    /**
     * Clicks on 'New...' button and opens a user, group or role wizard
     *
     * @return {@link WizardPanel} instance.
     */
    public Application clickOnToolbarNew( UserItemName selectedItem )
    {
        newButton.click();
        sleep( 500 );
        NewPrincipalDialog newPrincipalDialog = new NewPrincipalDialog( getSession() );
        //UserItemName selectedItem = (UserItemName) getSession().get( USER_ITEM_TYPE );
        if ( selectedItem == null )
        {
            return newPrincipalDialog.waitForLoaded( Application.EXPLICIT_NORMAL );
        }
        switch ( selectedItem )
        {
            case ROLES_FOLDER:
            {
                RoleWizardPanel roleWizardPanel = new RoleWizardPanel( getSession() );
                return roleWizardPanel.waitUntilWizardOpened();
            }

            case GROUPS_FOLDER:
            {
                GroupWizardPanel groupWizardPanel = new GroupWizardPanel( getSession() );
                return groupWizardPanel.waitUntilWizardOpened();
            }

            case USERS_FOLDER:
            {
                UserWizardPanel userWizardPanel = new UserWizardPanel( getSession() );
                return userWizardPanel.waitUntilWizardOpened();
            }

            default:
                throw new TestFrameworkException( "unknown type of principal!" );
        }
    }

    public UserStoreWizardPanel openUserStoreWizard()
    {
        sleep( 500 );
        newButton.click();
        NewPrincipalDialog newPrincipalDialog = new NewPrincipalDialog( getSession() );
        newPrincipalDialog.waitForLoaded( Application.EXPLICIT_NORMAL );
        newPrincipalDialog.selectItemOpenWizard( NewPrincipalDialog.ItemsToCreate.USER_STORE, null );
        sleep( 500 );
        UserStoreWizardPanel wizard = new UserStoreWizardPanel( getSession() );
        wizard.waitUntilWizardOpened();
        return wizard;
    }

    /**
     * @return true if 'Delete' button enabled, otherwise false.
     */
    public boolean isDeleteButtonEnabled()
    {
        return deleteButton.isEnabled();
    }

    /**
     * @return true if 'New' button enabled, otherwise false.
     */
    public boolean isNewButtonEnabled()
    {
        return newButton.isEnabled();
    }

    public boolean isEditButtonEnabled()
    {
        return editButton.isEnabled();
    }

    public boolean isDuplicateEnabled()
    {
        return duplicateButton.isEnabled();
    }

    public boolean isSyncEnabled()
    {
        return syncButton.isEnabled();
    }

    @Override
    public WizardPanel clickToolbarEdit()
    {

        WizardPanel wizard = null;
        editButton.click();
        UserItemName selectedItem = (UserItemName) getSession().get( USER_ITEM_TYPE );
        if ( selectedItem == null )
        {
            wizard = new UserStoreWizardPanel( getSession() );
            wizard.waitUntilWizardOpened();
            return wizard;
        }
        switch ( selectedItem )
        {
            case ROLE:
                wizard = new RoleWizardPanel( getSession() );
                break;
            case GROUP:
                wizard = new GroupWizardPanel( getSession() );
                break;
            case USER:
                wizard = new UserWizardPanel( getSession() );
                break;
            case USER_STORE:
                wizard = new UserStoreWizardPanel( getSession() );
                break;
            default:
                throw new TestFrameworkException( "unknown type of item!" );
        }
        wizard.waitUntilWizardOpened();
        wizard.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
        return wizard;
    }
}
