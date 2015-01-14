package com.enonic.autotests.pages.form.liveedit;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class LayoutComponentView
    extends UIComponent
{
    private final String COMPONENT_CONTAINER = "//div[contains(@id,'api.liveedit.layout.LayoutComponentView')]";

    private final String OPTION_FILTER = "//input[contains(@id,'selector.combobox.ComboBoxOptionFilterInput')]";

    @FindBy(xpath = COMPONENT_CONTAINER + OPTION_FILTER)
    private WebElement optionFilterInput;

    public LayoutComponentView( final TestSession session )
    {
        super( session );
    }

    public LiveFormPanel selectLayout( String layoutName )
    {
        optionFilterInput.sendKeys( layoutName );
        sleep( 200 );
        findElements( By.xpath( COMPONENT_CONTAINER + String.format(
            "//div[contains(@id,'api.app.NamesAndIconView')]//h6[@class='main-name' and text()='%s']", layoutName ) ) ).get( 0 ).click();
        return new LiveFormPanel( getSession() );
    }
}
