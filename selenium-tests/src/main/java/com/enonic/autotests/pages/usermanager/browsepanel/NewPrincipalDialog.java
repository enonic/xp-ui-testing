package com.enonic.autotests.pages.usermanager.browsepanel;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.WizardPanel;
import com.enonic.autotests.pages.usermanager.wizardpanel.GroupWizardPanel;
import com.enonic.autotests.pages.usermanager.wizardpanel.RoleWizardPanel;
import com.enonic.autotests.pages.usermanager.wizardpanel.UserWizardPanel;

import static com.enonic.autotests.utils.SleepHelper.sleep;

/**
 * Created on 31.08.2017.
 */
public class NewPrincipalDialog
    extends Application
{
    private final String DIALOG_CONTAINER = "//div[contains(@id,'NewPrincipalDialog')]";

    private final String HEADER = DIALOG_CONTAINER + "//h2[@class='title']";

    private final String PATH = DIALOG_CONTAINER + "//p[@class='path']";

    public static String HEADER_TEXT = "Create New";

    public final String USER_ITEM_NAMES = DIALOG_CONTAINER + "//div[contains(@id,'UserTypesTreeGridItemViewer')]" + H6_DISPLAY_NAME;

    private final String CANCEL_BUTTON = DIALOG_CONTAINER + APP_CANCEL_BUTTON_TOP;

    private final String USER_ITEMS_GRID = DIALOG_CONTAINER + "//div[contains(@id,'UserItemTypesTreeGrid')]";

    private final String USER_ITEM = USER_ITEMS_GRID + NAMES_VIEW_BY_DISPLAY_NAME;

    private String USER_ITEM_EXPANDER = DIALOG_CONTAINER +
        NAMES_VIEW_BY_NAME + "/ancestor::div[contains(@class,'slick-cell')]/span[contains(@class,'collapse') or contains(@class,'expand')]";

    public enum ItemsToCreate
    {
        USER( "User" ), USER_GROUP( "User Group" ), USER_STORE( "User Store" ), ROLE( "Role" );

        private ItemsToCreate( String type )
        {
            this.value = type;
        }

        private String value;

        public String getValue()
        {
            return this.value;
        }
    }


    @FindBy(xpath = CANCEL_BUTTON)
    private WebElement cancelButton;

    /**
     * The constructor
     *
     * @param session
     */
    public NewPrincipalDialog( TestSession session )
    {
        super( session );
    }

    public NewPrincipalDialog waitForLoaded( long timeout )
    {
        if ( !waitUntilVisibleNoException( By.xpath( DIALOG_CONTAINER ), timeout ) )
        {
            saveScreenshot( "err_new_principal_dlg" );
            throw new TestFrameworkException( "NewPrincipalDialog was not loaded!" );
        }
        return this;
    }

    /**
     * @param item
     * @param userStoreName
     * @return
     */
    public WizardPanel selectItemOpenWizard( ItemsToCreate item, String userStoreName )
    {
        WizardPanel wizard = null;
        switch ( item )
        {
            case ROLE:
            {
                getDisplayedElement( By.xpath( String.format( USER_ITEM, ItemsToCreate.ROLE.getValue() ) ) ).click();
                wizard = new RoleWizardPanel( getSession() );
                break;
            }

            case USER_GROUP:
            {
                if ( userStoreName != null )
                {
                    //expand the UserGroup item
                    clickOnExpander( ItemsToCreate.USER_GROUP.getValue() );
                    //click on the User Store
                    getDisplayedElement( By.xpath( String.format( USER_ITEM, userStoreName ) ) ).click();
                }
                else
                {
                    getDisplayedElement( By.xpath( String.format( USER_ITEM, ItemsToCreate.USER_GROUP.getValue() ) ) ).click();
                }

                wizard = new GroupWizardPanel( getSession() );
                break;
            }
            case USER:
            {
                //expand the item
                //click on the expander
                getDisplayedElement( By.xpath( String.format( USER_ITEM, ItemsToCreate.USER.getValue() ) ) ).click();
                wizard = new UserWizardPanel( getSession() );
                break;
            }
            default:
                throw new TestFrameworkException( "unknown type of item!" );
        }
        wizard.waitUntilWizardOpened();
        wizard.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
        return wizard;
    }

    public boolean isExpanderPresent( String itemName )
    {
        String expanderElement = String.format( USER_ITEM_EXPANDER, itemName );
        return isDynamicElementPresent( By.xpath( expanderElement ), 2 );
    }

    public NewPrincipalDialog clickOnExpander( String itemName )
    {
        boolean isExpanderPresent = isExpanderPresent( itemName );
        if ( !isExpanderPresent )
        {
            throw new TestFrameworkException( "expander for the Item:" + itemName + " was not found!" );
        }
        String expanderIcon = String.format( USER_ITEM_EXPANDER, itemName );
        getDisplayedElement( By.xpath( expanderIcon ) ).click();
        sleep( 500 );
        return this;
    }

    public String getHeader()
    {
        return getDisplayedString( HEADER );
    }

    public String getPath()
    {
        return getDisplayedString( PATH );
    }

    public boolean isCancelButtonTopDisplayed()
    {
        return isElementDisplayed( By.xpath( CANCEL_BUTTON ) );
    }

    public List<String> getItemNames()
    {
        return getDisplayedStrings( By.xpath( USER_ITEM_NAMES ) );
    }
}
