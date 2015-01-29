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
        USERS( "users" ), GROUPS( "groups" ), ROLES( "roles" ), SYSTEM( "system" ), USER_STORE( "user_store" );

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

    public static final String USER_MANAGER_BUTTON = "//button[@id='api.app.bar.HomeButton' and text()='Content Manager']";

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

    /**
     * The Constructor
     *
     * @param session
     */
    public UserBrowsePanel( final TestSession session )
    {
        super( session );
    }

    @Override
    public UserBrowsePanel goToAppHome()
    {
        userManagerButton.click();
        sleep( 1000 );
        waitUntilPageLoaded( Application.IMPLICITLY_WAIT );
        return this;
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

    public UserBrowsePanel clickCheckboxAndSelectRow( BrowseItemType itemType )
    {

        getSession().put( ITEM_TYPE, itemType );
        return clickCheckboxAndSelectRow( itemType.getValue() );

    }

    public UserBrowsePanel selectGroupsFolderInUserStore( String userStoreName )
    {
        //1.expand a userStore:
        if ( isRowExpanded( userStoreName ) )
        {
            clickOnExpander( userStoreName );
        }

        getSession().put( ITEM_TYPE, BrowseItemType.GROUPS );
        return clickOnRowAndSelectGroupInUserStore( userStoreName );

    }

    public UserBrowsePanel clickOnRowAndSelectGroupInUserStore( String userStoreName )
    {
        //"//div[contains(@id,'api.app.NamesView') and child::p[contains(@title,'%s')]]"
        String GROUP_ROW = String.format( DIV_NAMES_VIEW, userStoreName ) + "/ancestor::div[contains(@class,'slick-row')] " +
            "//following-sibling::div//" +
            String.format( "div[contains(@id,'api.app.NamesView') and child::p[contains(@title,'%s')]]", "groups" );
        ////div[contains(@id,'api.app.NamesView') and child::p[contains(@title,'enonic')]]/ancestor::div[contains(@class,'slick-row')]//following-sibling::div//div[contains(@id,'api.app.NamesView') and child::p[contains(@title,'groups')]]
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
        // WizardPanel<T> result;
        newButton.click();
        sleep( 500 );

        BrowseItemType selectedItem = (BrowseItemType) getSession().get( ITEM_TYPE );
        if ( selectedItem == null )
        {
            return new UserStoreWizardPanel( getSession() );
        }

        switch ( selectedItem )
        {
            case ROLES:
                return new RoleWizardPanel( getSession() );
            case GROUPS:
                return new GroupWizardPanel( getSession() );
            case USERS:
                return new UserWizardPanel( getSession() );
            default:
                throw new TestFrameworkException( "unknown type of principal!" );
        }
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


}
