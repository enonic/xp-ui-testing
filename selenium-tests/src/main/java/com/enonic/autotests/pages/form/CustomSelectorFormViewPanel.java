package com.enonic.autotests.pages.form;

import java.util.Collections;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.RichComboBoxInput;
import com.enonic.xp.data.PropertyTree;

import static com.enonic.autotests.utils.SleepHelper.sleep;

/**
 * Created  on 06.09.2016.
 */
public class CustomSelectorFormViewPanel
    extends FormViewPanel
{
    protected final String CONTAINER_DIV = FORM_VIEW + "//div[contains(@id,'CustomSelector')]";

    protected final String OPTION_FILTER_INPUT = CONTAINER_DIV + COMBOBOX_OPTION_FILTER_INPUT;

    private final String SELECTED_OPTIONS = "//div[contains(@id,'CustomSelectorSelectedOptionView')]" + H6_DISPLAY_NAME;

    @FindBy(xpath = OPTION_FILTER_INPUT)
    protected WebElement optionFilterInput;

    public static final String CUSTOM_SELECTOR_OPTIONS = "images";

    public CustomSelectorFormViewPanel( final TestSession session )
    {
        super( session );
    }

    @Override
    public FormViewPanel type( final PropertyTree data )
    {
        RichComboBoxInput richComboBoxInput = new RichComboBoxInput( getSession() );
        for ( final String optionName : data.getStrings( CUSTOM_SELECTOR_OPTIONS ) )
        {
//            clearAndType( optionFilterInput, optionName );
//            sleep( 400 );
//            richComboBoxInput.selectOption( optionName );
//            sleep( 300 );
            selectOption( optionName );
        }
        return this;
    }

    public void selectOption( String optionName )
    {
        RichComboBoxInput richComboBoxInput = new RichComboBoxInput( getSession() );
        clearAndType( optionFilterInput, optionName );
        sleep( 400 );
        richComboBoxInput.selectOption( optionName );
        sleep( 300 );
    }

    public boolean isOptionFilterIsDisplayed()
    {
        return optionFilterInput.isDisplayed();
    }

    public List<String> getSelectedOptions()
    {
        if ( !isElementDisplayed( SELECTED_OPTIONS ) )
        {
            return Collections.emptyList();
        }
        return getDisplayedStrings( By.xpath( SELECTED_OPTIONS ) );
    }
}
