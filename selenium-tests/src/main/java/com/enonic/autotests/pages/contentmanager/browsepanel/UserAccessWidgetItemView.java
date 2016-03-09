package com.enonic.autotests.pages.contentmanager.browsepanel;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.contentmanager.wizardpanel.EditPermissionsDialog;

public class UserAccessWidgetItemView
    extends Application

{
    private final String DIV_CONTAINER = "//div[contains(@id,'UserAccessWidgetItemView')]";

    private final String EDIT_PERM_LINK = DIV_CONTAINER + "//a[@class='edit-permissions-link']";

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
        editPermissionsLink.click();
        return new EditPermissionsDialog( getSession() );
    }

    public boolean isEditPermissionsLinkDisplayed()
    {
        return editPermissionsLink.isDisplayed();
    }

}
