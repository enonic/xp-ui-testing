package com.enonic.autotests.pages.usermanager.browsepanel;


import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.SaveOrUpdateException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.BrowsePanel;
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowseFilterPanel;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class UserBrowsePanel
    extends BrowsePanel
{
    public static final String USER_MANAGER_BUTTON = "//button[@id='api.app.bar.HomeButton' and text()='Content Manager']";

    @FindBy(xpath = USER_MANAGER_BUTTON)
    private WebElement userManagerButton;

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

    public UserBrowsePanel clickCheckboxAndSelectRow( String itemName )
    {
        String itemCheckBoxXpath = String.format( CHECKBOX_ROW_CHECKER, itemName );

        getLogger().debug( "Xpath of checkbox  is :" + itemCheckBoxXpath );
        boolean isPresent = waitUntilVisibleNoException( By.xpath( itemCheckBoxXpath ), 3l );
        if ( !isPresent )
        {
            throw new SaveOrUpdateException( "checkbox for : " + itemName + "was not found" );
        }
        sleep( 700 );
        findElement( By.xpath( itemCheckBoxXpath ) ).click();
        getLogger().info( "check box was selected,  name is:" + itemName );

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

}
