package com.enonic.autotests.pages.form.optionset;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.Application;

/**
 * Created on 2/10/2017.
 */
public class SingleSelectionOptionSetView
    extends Application
{
    private String CSS_CONTAINER = "div[id^='api.form.FormView'] div[id^='FormOptionSetView']:has(div:contains('Single selection'))";

    private final String CONTAINER =
        "//div[contains(@id,'api.form.FormView')]//div[contains(@id,'FormOptionSetView') and descendant::div[text()='Single selection']]";

    private final String RADIO_1 = CONTAINER + "//span[contains(@id,'api.ui.RadioButton') and child::label[text()='Option 1']]";

    private final String RADIO_2 = CONTAINER + "//span[contains(@id,'api.ui.RadioButton') and child::label[text()='Option 2']]";

    private String ITEMS_CONTAINER = CONTAINER + "//div[contains()]";


    @FindBy(xpath = RADIO_1)
    protected WebElement radio1;

    @FindBy(xpath = RADIO_2)
    protected WebElement radio2;


    public SingleSelectionOptionSetView( final TestSession session )
    {
        super( session );
    }

    public boolean isOpened()
    {
        return findElements( By.cssSelector( CSS_CONTAINER ) ).size() > 0;
    }

    public SingleSelectionOptionSetView clickOnFirstRadio()
    {
        radio1.click();
        return this;
    }

    public SingleSelectionOptionSetView clickOnSecondRadio()
    {
        return this;
    }
}
