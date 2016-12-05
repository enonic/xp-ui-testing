package com.enonic.autotests.pages.usermanager.browsepanel;


import java.util.ArrayList;
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
    public final String USER_ITEM_TYPE = "user_item_type";

    public enum BrowseItemType
    {
        USERS_FOLDER( "users" ), USER( "user" ), GROUPS_FOLDER( "groups" ), GROUP( "group" ), ROLES_FOLDER( "roles" ), ROLE(
        "role" ), SYSTEM( "system" ), USER_STORE( "user_store" );

        private BrowseItemType( String type )
        {
            this.value = type;
        }

        private String value;

        public String getValue()
        {
            return this.value;
        }
    }

    private final String USER_ITEMS_GRID = "//div[contains(@id,'UserItemsTreeGrid')]";

    public final String USERS_BUTTON = "//button[contains(@id,'api.app.bar.HomeButton') and child::span[text()='Users']]";

    public final String NEW_BUTTON_XPATH =
        "//div[contains(@id,'UserBrowseToolbar')]/*[contains(@id, 'ActionButton') and child::span[text()='New']]";

    public final String DUPLICATE_BUTTON_XPATH =
        "//div[contains(@id,'UserBrowseToolbar')]/*[contains(@id, 'ActionButton') and child::span[text()='Duplicate']]";

    protected final String EDIT_BUTTON_XPATH =
        "//div[contains(@id,'UserBrowseToolbar')]/*[contains(@id, 'ActionButton') and child::span[text()='Edit']]";

    protected final String DELETE_BUTTON_XPATH =
        "//div[contains(@id,'UserBrowseToolbar')]/*[contains(@id, 'ActionButton') and child::span[text()='Delete']]";

    protected final String SYNC_BUTTON_XPATH =
        "//div[contains(@id,'UserBrowseToolbar')]/*[contains(@id, 'ActionButton') and child::span[text()='Sync']]";

    @FindBy(xpath = USERS_BUTTON)
    private WebElement userManagerButton;

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
        userManagerButton.click();
        sleep( 1000 );
        waitUntilPageLoaded( Application.EXPLICIT_NORMAL );
        return this;
    }

    public UserBrowsePanel expandStoreAndSelectUsers( String storeName )
    {
        clickOnExpander( storeName );
        sleep( 700 );
        selectRowByName( "users" );
        sleep( 500 );
        getSession().put( USER_ITEM_TYPE, BrowseItemType.USERS_FOLDER );
        return this;
    }

    public UserBrowsePanel expandStoreAndSelectGroups( String storeName )
    {
        clickOnExpander( storeName );
        sleep( 700 );
        // pressKeyOnRow( storeName, Keys.ARROW_RIGHT );
        selectRowByName( "groups" );
        sleep( 500 );
        getSession().put( USER_ITEM_TYPE, BrowseItemType.GROUPS_FOLDER );
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
            throw new TestFrameworkException( "Impossible to delete a user store, because the 'Delete' button is disabled!" );
        }
        deleteButton.click();
        DeleteUserItemDialog dialog = new DeleteUserItemDialog( getSession() );
        dialog.waitForOpened();
        return dialog;
    }

    public List<String> getItemsNameFromGrid()
    {
        List<String> allNames = new ArrayList<>();
        List<WebElement> rows = findElements( By.xpath( USER_ITEMS_GRID + P_NAME ) );
        for ( WebElement row : rows )
        {
            allNames.add( row.getAttribute( "title" ) );
        }
        return allNames;
    }

    public UserBrowsePanel clickCheckboxAndSelectFolder( BrowseItemType itemType )
    {
        getSession().put( USER_ITEM_TYPE, itemType );
        return clickCheckboxAndSelectRow( itemType.getValue() );
    }

    public UserBrowsePanel clickCheckboxAndSelectUser( String userAppItemName )
    {
        getSession().put( USER_ITEM_TYPE, BrowseItemType.USER );
        return clickCheckboxAndSelectRow( userAppItemName );
    }

    public UserBrowsePanel clickCheckboxAndSelectUserStore( String userAppItemName )
    {
        getSession().put( USER_ITEM_TYPE, BrowseItemType.USER_STORE );
        return clickCheckboxAndSelectRow( userAppItemName );
    }

    public UserBrowsePanel clickCheckboxAndSelectGroup( String groupName )
    {
        getSession().put( USER_ITEM_TYPE, BrowseItemType.GROUP );
        return clickCheckboxAndSelectRow( groupName );
    }

    public UserBrowsePanel clickCheckboxAndSelectRole( String roleName )
    {
        getSession().put( USER_ITEM_TYPE, BrowseItemType.ROLE );
        return clickCheckboxAndSelectRow( roleName );
    }

    public UserBrowsePanel selectGroupsFolderInUserStore( String userStoreName )
    {
        if ( isRowExpanded( userStoreName ) )
        {
            clickOnExpander( userStoreName );
        }
        getSession().put( USER_ITEM_TYPE, BrowseItemType.GROUPS_FOLDER );
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

    /**
     * Clicks on 'New' button and opens a user, group or role wizard
     *
     * @return {@link WizardPanel} instance.
     */
    public WizardPanel clickToolbarNew()
    {
        newButton.click();
        sleep( 500 );

        BrowseItemType selectedItem = (BrowseItemType) getSession().get( USER_ITEM_TYPE );
        if ( selectedItem == null )
        {
            return new UserStoreWizardPanel( getSession() );
        }
        switch ( selectedItem )
        {
            case ROLES_FOLDER:
                return new RoleWizardPanel( getSession() );
            case GROUPS_FOLDER:
                return new GroupWizardPanel( getSession() );
            case USERS_FOLDER:
                return new UserWizardPanel( getSession() );
            default:
                throw new TestFrameworkException( "unknown type of principal!" );
        }
    }

    public UserStoreWizardPanel openUserStoreWizard()
    {
        sleep( 500 );
        newButton.click();
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
        BrowseItemType selectedItem = (BrowseItemType) getSession().get( USER_ITEM_TYPE );
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
        return wizard;
    }
}
