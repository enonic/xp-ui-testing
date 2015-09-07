package com.enonic.autotests.pages.form.liveedit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;

import static com.enonic.autotests.utils.SleepHelper.sleep;


public class PartComponentView
    extends UIComponent
{
    private final String COMPONENT_CONTAINER = "//div[contains(@id,'api.liveedit.part.PartComponentView')]";

    private final String OPTION_FILTER = "//input[contains(@id,'selector.combobox.ComboBoxOptionFilterInput')]";

    public static String PART_XPATH = "//div[contains(@id,'PartDescriptorViewer') and descendant::p[contains(.,'%s')]]";

    @FindBy(xpath = COMPONENT_CONTAINER + OPTION_FILTER)
    private WebElement optionFilterInput;

    public PartComponentView( final TestSession session )
    {
        super( session );
    }

    public LiveFormPanel selectItem( String partName )
    {
        optionFilterInput.sendKeys( partName );
        sleep( 200 );
        if ( !isPartExists( partName ) )
        {
            throw new TestFrameworkException( "The part with name: " + partName + "  was not found!" );
        }
        clickOnOptionsItem( partName );
        return new LiveFormPanel( getSession() );
    }

    private boolean isPartExists( String partName )
    {
        return findElements( By.xpath( COMPONENT_CONTAINER + String.format( NAMES_ICON_VIEW, partName ) ) ).stream().filter(
            WebElement::isDisplayed ).count() > 0;
    }

    private void clickOnOptionsItem( String partName )
    {
        findElements( By.xpath( COMPONENT_CONTAINER + String.format( NAMES_ICON_VIEW, partName ) ) ).get( 0 ).click();
    }
}
