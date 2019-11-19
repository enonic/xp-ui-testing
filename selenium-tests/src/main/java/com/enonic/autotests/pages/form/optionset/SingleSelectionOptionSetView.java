package com.enonic.autotests.pages.form.optionset;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.Application;

import static com.enonic.autotests.utils.SleepHelper.sleep;

/**
 * Created on 2/10/2017.
 */
public class SingleSelectionOptionSetView
    extends Application
{
    private final String CONTAINER =
        "//div[contains(@id,'api.form.FormView')]//div[contains(@id,'FormOptionSetView') and descendant::div[text()='Single selection']]";

    private final String RADIO_1 = CONTAINER + "//span[contains(@id,'RadioButton') and child::label[text()='Option 1']]";

    private final String RADIO_2 = CONTAINER + "//span[contains(@id,'RadioButton') and child::label[text()='Option 2']]";

    private final String OPTION_SET_NAME_INPUT =
        CONTAINER + "//div[contains(@id,'InputView') and descendant::div[text()='Name']]" + TEXT_INPUT;

    @FindBy(xpath = RADIO_1)
    protected WebElement radio1;

    @FindBy(xpath = RADIO_2)
    protected WebElement radio2;

    @FindBy(xpath = OPTION_SET_NAME_INPUT)
    protected WebElement setNameInput;


    public SingleSelectionOptionSetView( final TestSession session )
    {
        super( session );
    }

    public boolean isOpened()
    {
        return isElementDisplayed( CONTAINER );
    }

    public SingleSelectionOptionSetView clickOnFirstRadio()
    {
        radio1.click();
        sleep( 200 );
        return this;
    }

    public SingleSelectionOptionSetView clickOnSecondRadio()
    {
        radio2.click();
        sleep( 200 );
        return this;
    }

    public SingleSelectionOptionSetView typeSetName( String name )
    {
        clearAndType( setNameInput, name );
        return this;
    }
}
