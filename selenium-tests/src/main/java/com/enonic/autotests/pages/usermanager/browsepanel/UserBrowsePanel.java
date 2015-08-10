package com.enonic.autotests.pages.usermanager.browsepanel;


import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.BrowsePanel;
import com.enonic.autotests.pages.WizardPanel;
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowseFilterPanel;
import com.enonic.autotests.pages.usermanager.wizardpanel.GroupWizardPanel;
import com.enonic.autotests.pages.usermanager.wizardpanel.RoleWizardPanel;
import com.enonic.autotests.pages.usermanager.wizardpanel.UserStoreWizardPanel;
import com.enonic.autotests.pages.usermanager.wizardpanel.UserWizardPanel;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class UserBrowsePanel
    extends BrowsePanel
{
    public final String ITEM_TYPE = "type";

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

    public static final String USER_MANAGER_BUTTON = "//button[@id='api.app.bar.HomeButton' and text()='User Manager']";

    public final String NEW_BUTTON_XPATH =
        "//div[contains(@id,'app.browse.UserBrowseToolbar')]/*[contains(@id, 'api.ui.button.ActionButton') and child::span[text()='New']]";

    public final String DUPLICATE_BUTTON_XPATH =
        "//div[contains(@id,'app.browse.UserBrowseToolbar')]/*[contains(@id, 'api.ui.button.ActionButton') and child::span[text()='Duplicate']]";

    protected final String EDIT_BUTTON_XPATH =
        "//div[contains(@id,'app.browse.UserBrowseToolbar')]/*[contains(@id, 'api.ui.button.ActionButton') and child::span[text()='Edit']]";

    protected final String DELETE_BUTTON_XPATH =
        "//div[contains(@id,'app.browse.UserBrowseToolbar')]/*[contains(@id, 'api.ui.button.ActionButton') and child::span[text()='Delete']]";

    protected final String SYNCH_BUTTON_XPATH =
        "//div[contains(@id,'app.browse.UserBrowseToolbar')]/*[contains(@id, 'api.ui.button.ActionButton') and child::span[text()='Synch']]";

    @FindBy(xpath = USER_MANAGER_BUTTON)
    private WebElement userManagerButton;

    @FindBy(xpath = NEW_BUTTON_XPATH)
    private WebElement newButton;

    @FindBy(xpath = EDIT_BUTTON_XPATH)
    private WebElement editButton;

    @FindBy(xpath = DELETE_BUTTON_XPATH)
    private WebElement deleteButton;

    @FindBy(xpath = DUPLICATE_BUTTON_XPATH)
    private WebElement duplicateButton;

    @FindBy(xpath = SYNCH_BUTTON_XPATH)
    private WebElement synchButton;

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
    public UserBrowsePanel goToAppHome()
    {
        userManagerButton.click();
        sleep( 1000 );
        waitUntilPageLoaded( Application.EXPLICIT_NORMAL );
        return this;
    }

    public UserBrowsePanel expandStoreAndSelectUsers( String storeName )
    {
        clickAndSelectRow( storeName );
        sleep( 700 );
        pressKeyOnRow( storeName, Keys.ARROW_RIGHT );
        sleep( 700 );
        clickAndSelectRow( "users" );
        getSession().put( ITEM_TYPE, BrowseItemType.USERS_FOLDER );
        return this;
    }

    public UserBrowsePanel expandStoreAndSelectGroups( String storeName )
    {
        clickAndSelectRow( storeName );
        sleep( 700 );
        pressKeyOnRow( storeName, Keys.ARROW_RIGHT );
        clickAndSelectRow( "groups" );
        getSession().put( ITEM_TYPE, BrowseItemType.GROUPS_FOLDER );
        return this;
    }

    public UserBrowsePanel expandUsersFolder( String storeName )
    {
        expandStoreAndSelectUsers( storeName );
        pressKeyOnRow( storeName, Keys.ARROW_RIGHT );
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

    public List<String> getNamesFromBrowsePanel()
    {
        List<String> allNames = new ArrayList<>();
        List<WebElement> rows = getDriver().findElements( By.xpath( ALL_NAMES_FROM_BROWSE_PANEL_XPATH ) );
        for ( WebElement row : rows )
        {
            allNames.add( row.getAttribute( "title" ) );
        }
        return allNames;
    }

    public UserBrowsePanel clickCheckboxAndSelectFolder( BrowseItemType itemType )
    {
        getSession().put( ITEM_TYPE, itemType );
        return clickCheckboxAndSelectRow( itemType.getValue() );

    }

    public UserBrowsePanel clickCheckboxAndSelectUser( String userAppItemName )
    {
        getSession().put( ITEM_TYPE, BrowseItemType.USER );
        return clickCheckboxAndSelectRow( userAppItemName );

    }

    public UserBrowsePanel selectGroupsFolderInUserStore( String userStoreName )
    {
        //1.expand a userStore:
        if ( isRowExpanded( userStoreName ) )
        {
            clickOnExpander( userStoreName );
        }

        getSession().put( ITEM_TYPE, BrowseItemType.GROUPS_FOLDER );
        return clickOnRowAndSelectGroupInUserStore( userStoreName );

    }

    public UserBrowsePanel clickOnRowAndSelectGroupInUserStore( String userStoreName )
    {
        String GROUP_ROW = String.format( DIV_NAMES_VIEW, userStoreName ) + "/ancestor::div[contains(@class,'slick-row')] " +
            "//following-sibling::div//" +
            String.format( "div[contains(@id,'api.app.NamesView') and child::p[contains(@title,'%s')]]", "groups" );
        Actions builder = new Actions( getDriver() );
        builder.click( findElement( By.xpath( GROUP_ROW ) ) ).build().perform();
        sleep( 500 );
        return this;
    }


    /**
     * @param session
     * @return true if 'Content Manager' opened and CMSpacesPage showed, otherwise false.
     */
    public static boolean isOpened( TestSession session )
    {
        List<WebElement> searchInput = session.getDriver().findElements( By.xpath( ContentBrowseFilterPanel.SEARCH_INPUT_XPATH ) );
        if ( searchInput.size() > 0 && searchInput.get( 0 ).isDisplayed() )
        {
            return true;
        }
        else
        {
            return false;
        }
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

        BrowseItemType selectedItem = (BrowseItemType) getSession().get( ITEM_TYPE );
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
        newButton.click();
        sleep( 1000 );
        return new UserStoreWizardPanel( getSession() );
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

    public boolean isSynchEnabled()
    {
        return synchButton.isEnabled();
    }

    @Override
    public WizardPanel clickToolbarEdit()
    {

        editButton.click();
        BrowseItemType selectedItem = (BrowseItemType) getSession().get( ITEM_TYPE );
        if ( selectedItem == null )
        {
            return new UserStoreWizardPanel( getSession() );
        }

        switch ( selectedItem )
        {
            case ROLE:
                return new RoleWizardPanel( getSession() );
            case GROUP:
                return new GroupWizardPanel( getSession() );
            case USER:
                return new UserWizardPanel( getSession() );
            default:
                throw new TestFrameworkException( "unknown type of item!" );
        }
    }
}
