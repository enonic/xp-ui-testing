package com.enonic.autotests.pages.form;

import java.util.LinkedList;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.contentmanager.wizardpanel.SiteConfiguratorDialog;
import com.enonic.autotests.utils.NameHelper;
import com.enonic.autotests.utils.TestUtils;
import com.enonic.xp.data.PropertyTree;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class SiteFormViewPanel
    extends FormViewPanel
{
    public static final String APP_KEY = "applicationKey";

    public static final String DESCRIPTION_KEY = "description";

    private String SITE_CONFIGURATOR_OPTION_BY_DISPLAY_NAME =
        "//div[contains(@id,'SiteConfiguratorSelectedOptionView') and descendant::h6[@class='main-name' and text()='%s']]";

    private String SITE_CONFIGURATOR_OPTIONS = "//div[contains(@id,'SiteConfiguratorSelectedOptionView')]//h6[@class='main-name']";

    @FindBy(xpath = FORM_VIEW + "//textarea[contains(@name,'description')]")
    private WebElement descriptionInput;

    @FindBy(xpath = FORM_VIEW + COMBOBOX_OPTION_FILTER_INPUT)
    private WebElement optionFilterInput;

    @FindBy(
        xpath = "//div[contains(@id,'SiteConfiguratorComboBox' ) and contains(@class,'form-input composite-input rich-combobox')]//div[@class='dropdown-handle']")
    private WebElement moduleSelectorComboBox;


    public SiteFormViewPanel( final TestSession session )
    {
        super( session );
    }

    @Override
    public FormViewPanel type( final PropertyTree data )
    {
        String description = data.getString( DESCRIPTION_KEY );
        descriptionInput.sendKeys( description );
        sleep( 500 );
        Iterable<String> appNames = data.getStrings( APP_KEY );
        appNames.forEach( name -> selectApp( name ) );
        return this;
    }

    public FormViewPanel selectCheckBoxAndApply( String appName )
    {
        clearAndType( optionFilterInput, appName );
        sleep( 700 );
        String checkboxXpath = FORM_VIEW + String.format( SLICK_ROW_BY_DISPLAY_NAME, appName ) + "//div[contains(@class,'checkboxsel')]";
        if ( !isElementDisplayed( checkboxXpath ) )
        {
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "err_app_checkbox" ) );
            throw new TestFrameworkException( "checkbox for application with name: " + appName + "  was not found!" );
        }
        findElement( By.xpath( checkboxXpath ) ).click();
        String applyButtonXpath = FORM_VIEW + "//div[contains(@class,'combobox')]//button[contains(@class,'apply-button')]";
        boolean isVisible = waitUntilVisibleNoException( By.xpath( applyButtonXpath ), Application.EXPLICIT_NORMAL );
        if ( !isVisible )
        {
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "err_app_apply" ) );
            throw new TestFrameworkException( "'apply' button for application with name: " + appName + "  was not found!" );
        }
        findElement( By.xpath( applyButtonXpath ) ).click();
        return this;
    }

    public FormViewPanel removeApp( String appName )
    {
        String removeButtonXpath =
            FORM_VIEW + String.format( SITE_CONFIGURATOR_OPTION_BY_DISPLAY_NAME, appName ) + "//a[contains(@class,'remove-button')]";
        if ( !isElementDisplayed( removeButtonXpath ) )
        {
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "err_app_remove_btn" ) );
            throw new TestFrameworkException( "remove button for application with name: " + appName + "  was not found!" );
        }
        findElement( By.xpath( removeButtonXpath ) ).click();
        return this;
    }

    private void selectApp( String appName )
    {
        clearAndType( optionFilterInput, appName );
        String moduleGridItem = String.format( NAMES_VIEW_BY_DISPLAY_NAME, appName );
        if ( !isElementDisplayed( moduleGridItem ) )
        {
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "err_app_" ) );
            throw new TestFrameworkException( "application with name: " + appName + "  was not found!" );
        }
        //else select application from options.
        findElement( By.xpath( moduleGridItem ) ).click();
        sleep( 500 );
    }

    public SiteConfiguratorDialog openSiteConfiguration( String appName )
    {
        SiteConfiguratorDialog dialog = null;
        String editButton = String.format(
            "//div[contains(@id,'SiteConfiguratorSelectedOptionView') and descendant::h6[@class='main-name' and text()='%s']]//a[@class='edit-button']",
            appName );
        if ( findElements( By.xpath( editButton ) ).size() == 0 )
        {
            return null;
        }
        else
        {
            getDisplayedElement( By.xpath( editButton ) ).click();
            dialog = new SiteConfiguratorDialog( getSession() );
            dialog.waitForOpened();
            sleep( 300 );
        }
        return dialog;
    }

    public SiteFormViewPanel swapApplications( String sourceApp, String targetApp )
    {
        String sourceItem = String.format( SITE_CONFIGURATOR_OPTION_BY_DISPLAY_NAME, sourceApp );
        String targetItem = String.format( SITE_CONFIGURATOR_OPTION_BY_DISPLAY_NAME, targetApp );
        if ( findElements( By.xpath( sourceItem ) ).size() == 0 || findElements( By.xpath( targetItem ) ).size() == 0 )
        {
            TestUtils.saveScreenshot( getSession(), "err_swap_app" );
            throw new TestFrameworkException(
                "SiteFormViewPanel : drag and drop failed. items were not found: " + sourceApp + " " + targetApp );
        }
        WebElement source = findElements( By.xpath( sourceItem ) ).get( 0 );
        WebElement target = findElements( By.xpath( targetItem ) ).get( 0 );

        Actions builder = new Actions( getDriver() );
        builder.clickAndHold( source ).build().perform();
        builder.moveToElement( target ).build().perform();
        builder.release( target ).build().perform();
        sleep( 3000 );
        return this;
    }

    public LinkedList<String> getAppDisplayNames()
    {
        LinkedList<String> appDisplayNames =
            findElements( By.xpath( SITE_CONFIGURATOR_OPTIONS ) ).stream().filter( WebElement::isDisplayed ).map(
                WebElement::getText ).collect( Collectors.toCollection( LinkedList::new ) );
        return appDisplayNames;
    }
}
