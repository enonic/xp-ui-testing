package com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.contentmanager.wizardpanel.EditPermissionsDialog;
import com.enonic.autotests.utils.NameHelper;

public class UserAccessWidgetItemView
    extends Application

{
    public static final String EVERYONE_CAN_READ = "Everyone can read this item";

    private final String DIV_CONTAINER = "//div[contains(@id,'UserAccessWidgetItemView')]";

    private String ACCESS_LINE_NAMES =
        "//div[contains(@id,'UserAccessListItemView') and child::span[text()='%s']]//span[contains(@class,'user-icon')]";

    private final String EDIT_PERM_LINK = DIV_CONTAINER + "//a[@class='edit-permissions-link']";

    private final String EVERYONE_HEADER = "//div[@class='user-access-widget-header']//span[@class='header-string']";

    @FindBy(xpath = EDIT_PERM_LINK)
    private WebElement editPermissionsLink;

    public UserAccessWidgetItemView( final TestSession session )
    {
        super( session );
    }

    public boolean isDisplayed()
    {
        return isElementDisplayed( DIV_CONTAINER );
    }

    public EditPermissionsDialog clickOnEditPermissionsLink()
    {
        boolean isClickable = waitUntilClickableNoException( By.xpath( EDIT_PERM_LINK ), Application.EXPLICIT_NORMAL );
        if ( !isClickable )
        {
            saveScreenshot( NameHelper.uniqueName( "err_perm_link" ) );
            throw new TestFrameworkException( "edit permissions link is not displayed" );
        }
        editPermissionsLink.click();
        return new EditPermissionsDialog( getSession() );
    }

    public boolean isEditPermissionsLinkDisplayed()
    {
        return editPermissionsLink.isDisplayed();
    }

    public boolean isEveryoneHeaderPresent()
    {
        return isElementDisplayed( EVERYONE_HEADER );
    }

    public String getEveryoneText()
    {
        return getDisplayedString( EVERYONE_HEADER );
    }

    public List<String> getNamesFromAccessLine( String permissionOperation )
    {
        String elements = String.format( ACCESS_LINE_NAMES, permissionOperation );
        return getDisplayedStrings( By.xpath( elements ) );
    }
}
