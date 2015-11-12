package com.enonic.autotests.pages.form;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.utils.NameHelper;
import com.enonic.autotests.utils.TestUtils;
import com.enonic.xp.data.PropertyTree;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class SiteFormViewPanel
    extends FormViewPanel
{
    public static final String APP_KEY = "applicationKey";

    public static final String DESCRIPTION_KEY = "description";
    @FindBy(xpath = "//div[contains(@id,'api.form.FormView')]//textarea[contains(@name,'description')]")
    private WebElement descriptionInput;

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
        //expand the combobox
        Actions builder = new Actions( getDriver() );
        builder.click( moduleSelectorComboBox ).build().perform();
        sleep( 500 );
        Iterable<String> appNames = data.getStrings( APP_KEY );
        appNames.forEach( name -> selectApp( name ) );
        return this;
    }

    private void selectApp( String appName )
    {
        String moduleGridItem = String.format( "//div[contains(@id,'api.app.NamesView')]/h6[text()='%s']", appName );
        if ( getDriver().findElements( By.xpath( moduleGridItem ) ).size() == 0 )
        {
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "err_app_" ) );
            throw new TestFrameworkException( "application with name: " + appName + "  was not found!" );
        }
        //else select application from the options.
        findElement( By.xpath( moduleGridItem ) ).click();
        sleep( 500 );
    }
}
