package com.enonic.autotests.pages.contentmanager.wizardpanel;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.utils.NameHelper;
import com.enonic.autotests.utils.TestUtils;
import com.enonic.autotests.utils.WaitHelper;
import com.enonic.autotests.vo.contentmanager.security.ContentAclEntry;
import com.enonic.autotests.vo.contentmanager.security.PermissionSuite;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class EditPermissionsDialog
    extends Application
{
    private final String CONTAINER_XPATH = "//div[contains(@id,'app.wizard.EditPermissionsDialog')]";

    private final String INHERIT_PERMISSIONS_CHECKBOX = CONTAINER_XPATH + "//div[contains(@id,'api.ui.Checkbox')]";

    private final String INHERIT_PERMISSIONS_CHECKBOX_LABEL = CONTAINER_XPATH + "//div[contains(@id,'api.ui.Checkbox')]/label";


    private final String OPTIONS_FILTER_INPUT = CONTAINER_XPATH + "//input[contains(@id,'combobox.ComboBoxOptionFilterInput')]";

    private String PRINCIPAL_PATH = "//div[contains(@id,'AccessControlEntryViewer')]//p[contains(.,'%s')]";

    private final String APPLY_BUTTON_XPATH = "//button[contains(@id,'dialog.DialogButton') and child::span[text()='Apply']]";

    private String ACL_ENTRY_ROW =
        "//div[contains(@class,'access-control-entry') and descendant::p[@class='sub-name' and contains(.,'%s')]]";

    @FindBy(xpath = INHERIT_PERMISSIONS_CHECKBOX_LABEL)
    WebElement inheritPermissionsCheckbox;

    public EditPermissionsDialog( TestSession session )
    {
        super( session );
    }

    public EditPermissionsDialog waitForOpened()
    {
        if ( !waitUntilVisibleNoException( By.xpath( CONTAINER_XPATH ), EXPLICIT_LONG ) )
        {
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "err-perm-dialog" ) );
            throw new TestFrameworkException( "Edit Permissions Dialog was not opened!" );
        }
        return this;
    }

    public boolean isOpened()
    {
        return findElements( By.xpath( CONTAINER_XPATH ) ).size() > 0;
    }

    public EditPermissionsDialog uncheckInheritCheckbox()
    {
        inheritPermissionsCheckbox.click();
        if ( !waitUntilVisibleNoException( By.xpath( OPTIONS_FILTER_INPUT ), Application.EXPLICIT_NORMAL ) )
        {
            throw new TestFrameworkException( "options filter input not found!" );
        }
        return this;
    }

    public boolean isOptionsFilterDisplayed()
    {

        return findElements( By.xpath( OPTIONS_FILTER_INPUT ) ).size() > 0;
    }

    public EditPermissionsDialog updatePermissions( List<ContentAclEntry> entries )
    {
        entries.stream().forEach( e -> addPermission( e ) );
        return this;
    }

    public EditPermissionsDialog addPermission( ContentAclEntry entry )
    {
        selectPrincipal( entry.getPrincipalName() );
        if ( entry.getPermissionSuite() != null )
        {
            selectOperations( entry.getPrincipalName(), entry.getPermissionSuite() );
        }//when operations not specified, CAN_READ will be applied by default
        sleep( 500 );
        return this;
    }

    public EditPermissionsDialog removeAclEntry( String principalName )
    {
        if ( findElements(
            By.xpath( CONTAINER_XPATH + String.format( ACL_ENTRY_ROW, principalName ) + "//a[@class='icon-close']" ) ).size() == 0 )
        {
            throw new TestFrameworkException( "Principal with name :" + principalName + "  was not found!" );
        }
        findElements( By.xpath( CONTAINER_XPATH + String.format( ACL_ENTRY_ROW, principalName ) + "//a[@class='icon-close']" ) ).get(
            0 ).click();

        return this;
    }

    public EditPermissionsDialog clickOnApply()
    {
        if ( findElements( By.xpath( APPLY_BUTTON_XPATH ) ).size() == 0 )
        {
            throw new TestFrameworkException( "Apply button was not found!" );
        }
        WaitHelper.waitUntilElementEnabled( getSession(), By.xpath( APPLY_BUTTON_XPATH ) );
        findElements( By.xpath( APPLY_BUTTON_XPATH ) ).get( 0 ).click();
        return this;
    }

    private void selectPrincipal( String principalName )
    {
        findElement( By.xpath( OPTIONS_FILTER_INPUT ) ).sendKeys( principalName );
        sleep( 1000 );
        By principalBy = By.xpath( String.format( PRINCIPAL_PATH, principalName ) );
        if ( !waitUntilVisibleNoException( principalBy, EXPLICIT_QUICK ) )
        {
            throw new TestFrameworkException( "principal was not found! : " + principalName );
        }
        findElements( principalBy ).get( 0 ).click();
    }

    private void selectOperations( String principalName, PermissionSuite suite )
    {
        findElements(
            By.xpath( CONTAINER_XPATH + String.format( ACL_ENTRY_ROW, principalName ) + "//div[contains(@class,'tab-menu-button')]" ) ).get(
            0 ).click();
        By tabMenuItemBy = By.xpath( CONTAINER_XPATH + String.format( ACL_ENTRY_ROW, principalName ) +
                                         String.format( "//li[contains(@id,'TabMenuItem')]/span[@title='%s']", suite.getValue() ) );
        if ( !waitUntilVisibleNoException( tabMenuItemBy, EXPLICIT_QUICK ) )
        {
            throw new TestFrameworkException( suite.getValue() + "  was not found in the tab menu!" );
        }
        findElement( tabMenuItemBy ).click();
        sleep( 500 );
    }

    public boolean isInheritPermissionsCheckboxDisplayed()
    {
        return inheritPermissionsCheckbox.isDisplayed();
    }

    public boolean isInheritCheckBoxChecked()
    {
        WebElement checkbox = findElements( By.xpath( CONTAINER_XPATH + "//div[contains(@id,'api.ui.Checkbox')]" ) ).get( 0 );
        return isCheckBoxChecked( checkbox.getAttribute( "id" ) );
    }

    public EditPermissionsDialog setCheckedForInheritCheckbox( boolean value )
    {
        boolean isChecked = isInheritCheckBoxChecked();
        if ( ( !isChecked && value ) || ( isChecked && !value ) )
        {
            inheritPermissionsCheckbox.click();
        }
        //String id = findElements( By.xpath( INHERIT_PERMISSIONS_CHECKBOX ) ).get( 0 ).getAttribute( "id" );
        //setChecked( id, value );
        return this;
    }

    public List<String> getPrincipalNames()
    {
        List<WebElement> elements =
            findElements( By.xpath( CONTAINER_XPATH + "//div[contains(@id,'api.app.NamesView')]/p[@class='sub-name']" ) );
        return elements.stream().map( WebElement::getText ).collect( Collectors.toList() );
    }

    public List<ContentAclEntry> getAclEntries()
    {
        ContentAclEntry.Builder builder;
        List<ContentAclEntry> entries = new ArrayList<>();
        List<WebElement> principals = findElements( By.xpath(
            CONTAINER_XPATH + "//div[@class='access-control-entry']//div[contains(@id,'api.app.NamesView')]/p[@class='sub-name']" ) );

        List<String> principalsStrings = principals.stream().map( WebElement::getText ).collect( Collectors.toList() );

        List<WebElement> suites = findElements( By.xpath(
            CONTAINER_XPATH + "//div[@class='access-control-entry']//div[contains(@id,'TabMenuButton')]//span[@class='label']" ) );
        List<String> suitesStrings =
            suites.stream().filter( WebElement::isDisplayed ).map( WebElement::getText ).collect( Collectors.toList() );
        for ( int i = 0; i < principalsStrings.size(); i++ )
        {
            builder = ContentAclEntry.builder();
            builder.principalName( principalsStrings.get( i ) );
            builder.suite( getSuite( suitesStrings.get( i ) ) );
            entries.add( builder.build() );
        }
        return entries;
    }

    private PermissionSuite getSuite( String suiteString )
    {
        switch ( suiteString )
        {
            case "Full Access":
                return PermissionSuite.FULL_ACCESS;
            case "Can Read":
                return PermissionSuite.CAN_READ;
            default:
                return PermissionSuite.CUSTOM;
        }
    }
}
