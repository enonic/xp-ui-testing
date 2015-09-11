package com.enonic.autotests.pages.form;


import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.xp.data.PropertyTree;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class CityFormView
    extends FormViewPanel
{
    public static final String LOCATION = "location";

    public static final String POPULATION = "population";

    private final String LOCATION_INPUT = FORM_VIEW + "//input[contains(@placeholder,'latitude,longitude')]";

    private final String POPULATION_INPUT = FORM_VIEW + "//input[contains(@name,'population')]";

    @FindBy(xpath = POPULATION_INPUT)
    protected WebElement populationInput;

    @FindBy(xpath = LOCATION_INPUT)
    protected WebElement locationInput;

    public CityFormView( final TestSession session )
    {
        super( session );
    }

    @Override
    public FormViewPanel type( final PropertyTree data )
    {
        final String location = data.getString( LOCATION );
        final String population = data.getString( POPULATION );
        clearAndType( locationInput, location );
        sleep( 700 );
        clearAndType( populationInput, population );
        sleep( 500 );
        return this;
    }
}
