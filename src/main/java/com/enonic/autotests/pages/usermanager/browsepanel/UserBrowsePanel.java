package com.enonic.autotests.pages.usermanager.browsepanel;


import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.SaveOrUpdateException;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.BrowsePanel;
import com.enonic.autotests.pages.WizardPanel;
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowseFilterPanel;
import com.enonic.autotests.pages.usermanager.wizardpanel.GroupWizardPanel;
import com.enonic.autotests.pages.usermanager.wizardpanel.RoleWizardPanel;
import com.enonic.autotests.pages.usermanager.wizardpanel.UserWizardPanel;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class UserBrowsePanel
    extends BrowsePanel
{
    public final String ITEM_TYPE = "type";

    public enum BrowseItemType
    {
        USERS( "users" ), GROUPS( "groups" ), ROLES( "roles" ), SYSTEM( "system" );

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

    public final String OPEN_BUTTON_XPATH =
        "//div[contains(@id,'app.browse.UserBrowseToolbar')]/*[contains(@id, 'api.ui.button.ActionButton') and child::span[text()='Open']]";

    public final String MOVE_BUTTON_XPATH =
        "//div[contains(@id,'app.browse.UserBrowseToolbar')]/*[contains(@id, 'api.ui.button.ActionButton') and child::span[text()='Move']]";

    protected final String EDIT_BUTTON_XPATH =
        "//div[contains(@id,'app.browse.UserBrowseToolbar')]/*[contains(@id, 'api.ui.button.ActionButton') and child::span[text()='Edit']]";

    protected final String DELETE_BUTTON_XPATH =
        "//div[contains(@id,'app.browse.UserBrowseToolbar')]/*[contains(@id, 'api.ui.button.ActionButton') and child::span[text()='Delete']]";

    @FindBy(xpath = USER_MANAGER_BUTTON)
    private WebElement userManagerButton;

    @FindBy(xpath = NEW_BUTTON_XPATH)
    private WebElement newButton;

    @FindBy(xpath = EDIT_BUTTON_XPATH)
    private WebElement editButton;

    @FindBy(xpath = DUPLICATE_BUTTON_XPATH)
    private WebElement duplicateButton;

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
        String itemCheckBoxXpath = String.format( CHECKBOX_ROW_CHECKER, itemType.getValue() );

        getLogger().debug( "Xpath of checkbox  is :" + itemCheckBoxXpath );
        boolean isPresent = waitUntilVisibleNoException( By.xpath( itemCheckBoxXpath ), 3l );
        if ( !isPresent )
        {
            throw new SaveOrUpdateException( "checkbox for : " + itemType + "was not found" );
        }
        sleep( 700 );
        findElement( By.xpath( itemCheckBoxXpath ) ).click();
        getLogger().info( "check box was selected,  name is:" + itemType );
        getSession().put( ITEM_TYPE, itemType );
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
        BrowseItemType type = (BrowseItemType) getSession().get( ITEM_TYPE );
        switch ( type )
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

}
