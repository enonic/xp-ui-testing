package com.enonic.autotests.pages.contentmanager.wizardpanel;


import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.vo.contentmanager.security.ContentAclEntry;
import com.enonic.autotests.vo.contentmanager.security.PermissionSuite;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class EditPermissionsDialog
    extends Application
{
    private final String CONTAINER_XPATH = "//div[contains(@id,'app.wizard.EditPermissionsDialog')]";

    private final String INHERIT_PERMISSIONS_CHECKBOX = CONTAINER_XPATH + "//div[contains(@id,'api.ui.Checkbox')]/label";
//input[@type='checkbox']";

    private final String OPTIONS_FILTER_INPUT = CONTAINER_XPATH + "//input[contains(@id,'combobox.ComboBoxOptionFilterInput')]";

    private String PRINCIPAL_PATH = "//div[contains(@id,'AccessControlEntryViewer')]//p[contains(@title,'%s')]";

    private final String APPLY_BUTTON_XPATH = "//button[contains(@id,'dialog.DialogButton') and child::span[text()='Apply']]";

    private String ACL_ENTRY_ROW =
        "//div[contains(@class,'access-control-entry') and descendant::p[@class='sub-name' and contains(@title,'%s')]]";

    @FindBy(xpath = INHERIT_PERMISSIONS_CHECKBOX)
    WebElement inheritPermissionsCheckbox;

    public EditPermissionsDialog( TestSession session )
    {
        super( session );
    }

    public EditPermissionsDialog waitForOpened()
    {
        if ( !waitUntilVisibleNoException( By.xpath( CONTAINER_XPATH ), EXPLICIT_2 ) )
        {
            throw new TestFrameworkException( "Edit Permissions Dialog was not opened!" );
        }
        return this;
    }

    public EditPermissionsDialog clickOnInheritCheckbox()
    {
        inheritPermissionsCheckbox.click();
        if ( !waitUntilVisibleNoException( By.xpath( OPTIONS_FILTER_INPUT ), Application.EXPLICIT_2 ) )
        {
            throw new TestFrameworkException( "options filter input not found!" );
        }
        return this;
    }

    public EditPermissionsDialog updatePermissions( List<ContentAclEntry> entries )
    {
        entries.stream().forEach( e -> updatePermission( e ) );
        return this;
    }

    public EditPermissionsDialog updatePermission( ContentAclEntry entry )
    {
        selectPrincipal( entry.getPrincipalName() );
        selectOperations( entry.getPrincipalName(), entry.getPermissionSuite() );
        clickOnApply();
        return this;
    }

    public EditPermissionsDialog clickOnApply()
    {
        if ( findElements( By.xpath( APPLY_BUTTON_XPATH ) ).size() == 0 )
        {
            throw new TestFrameworkException( "Apply button was not found!" );
        }
        findElements( By.xpath( APPLY_BUTTON_XPATH ) ).get( 0 ).click();
        return this;
    }

    private void selectPrincipal( String principalName )
    {
        findElement( By.xpath( OPTIONS_FILTER_INPUT ) ).sendKeys( principalName );
        By principalBy = By.xpath( String.format( PRINCIPAL_PATH, principalName ) );
        if ( !waitUntilVisibleNoException( principalBy, EXPLICIT_2 ) )
        {
            throw new TestFrameworkException( "principal was not found! : " + principalName );
        }
        findElement( principalBy ).click();
    }

    private void selectOperations( String principalName, PermissionSuite suite )
    {
        findElements(
            By.xpath( CONTAINER_XPATH + String.format( ACL_ENTRY_ROW, principalName ) + "//div[contains(@class,'tab-menu-button')]" ) ).get(
            0 ).click();
        By tabMenuItemBy = By.xpath( CONTAINER_XPATH + String.format( ACL_ENTRY_ROW, principalName ) +
                                         String.format( "//li[contains(@id,'TabMenuItem')]/span[@title='%s']", suite.getValue() ) );
        if ( !waitUntilVisibleNoException( tabMenuItemBy, EXPLICIT_2 ) )
        {
            throw new TestFrameworkException( suite.getValue() + "  was not found in the tab menu!" );
        }
        findElement( tabMenuItemBy ).click();
        sleep( 500 );
    }
}
