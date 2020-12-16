package com.enonic.autotests.pages.form.optionset;

import org.openqa.selenium.By;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.LoaderComboBox;

import static com.enonic.autotests.utils.SleepHelper.sleep;

/**
 * Created on 5/12/2017.
 */
public class MultiSelectionOptionSetView
    extends Application
{
    private final String CONTAINER =
        "//div[contains(@id,'FormView')]//div[contains(@id,'FormOptionSetView') and descendant::p[text()='Multi selection']]";

    private final String OPTION_CHECKBOX = CONTAINER + CHECKBOX_ELEMENT + "/label";

    public MultiSelectionOptionSetView( final TestSession session )
    {
        super( session );
    }

    public boolean isOpened()
    {
        return isElementDisplayed( CONTAINER );
    }

    public MultiSelectionOptionSetView clickOnCheckbox( int number )
    {
        if ( getDisplayedElements( By.xpath( OPTION_CHECKBOX ) ).size() >= number )
        {
            getDisplayedElements( By.xpath( OPTION_CHECKBOX ) ).get( number ).click();
            sleep( 1000 );
        }
        else
        {
            throw new TestFrameworkException( "OptionSet error when clicking the checkbox in multi selection form" );
        }
        return this;
    }

    public MultiSelectionOptionSetView selectImage( String imageName )
    {
        LoaderComboBox loaderComboBox = new LoaderComboBox( getSession() );
        clearAndType( getDisplayedElement( By.xpath( COMBOBOX_OPTION_FILTER_INPUT ) ), imageName );
        sleep( 700 );
        loaderComboBox.selectOption( imageName );
        sleep( 300 );
        return this;
    }
}
