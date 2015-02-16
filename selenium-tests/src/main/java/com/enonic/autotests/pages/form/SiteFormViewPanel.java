package com.enonic.autotests.pages.form;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.xp.data.PropertyTree;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class SiteFormViewPanel
    extends FormViewPanel
{
    @FindBy(xpath = "//div[contains(@id,'api.form.FormView')]//textarea[contains(@name,'description')]")
    private WebElement descriptionInput;

    @FindBy(xpath = "//div[contains(@id,'ModuleConfiguratorComboBox' ) and contains(@class,'form-input composite-input rich-combobox')]//div[@class='dropdown-handle']")
    private WebElement moduleSelectorComboBox;


    public SiteFormViewPanel( final TestSession session )
    {
        super( session );
    }

    @Override
    public FormViewPanel type( final PropertyTree data )
    {
        String description = data.getString( "description" );
        // type a description
        descriptionInput.sendKeys( description );

        Actions builder = new Actions( getDriver() );
        builder.click( moduleSelectorComboBox ).build().perform();
        sleep( 500 );
        //select a module  and click on it
        String moduleName = data.getString( "moduleKey" );
        String moduleGridItem = String.format( "//div[contains(@id,'api.app.NamesView')]/h6[@title='%s']", moduleName );
        if ( getDriver().findElements( By.xpath( moduleGridItem ) ).size() == 0 )
        {
            throw new TestFrameworkException( "module with name: " + moduleName + "  was not found!" );
        }
        getDriver().findElements( By.xpath( moduleGridItem ) ).get( 0 ).click();
        sleep( 500 );
        return this;
    }
}
