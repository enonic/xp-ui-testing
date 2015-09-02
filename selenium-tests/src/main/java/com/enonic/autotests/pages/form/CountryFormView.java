package com.enonic.autotests.pages.form;


import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.xp.data.PropertyTree;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class CountryFormView
    extends FormViewPanel
{
    public static final String DESCRIPTION = "description";

    public static final String POPULATION = "population";

    private final String DESCRIPTION_TEXTAREA = FORM_VIEW + "//textarea[contains(@name,'description')]";

    private final String POPULATION_INPUT = FORM_VIEW + "//input[contains(@name,'population')]";

    @FindBy(xpath = POPULATION_INPUT)
    protected WebElement populationInput;

    @FindBy(xpath = DESCRIPTION_TEXTAREA)
    protected WebElement descriptionTextArea;

    public CountryFormView( final TestSession session )
    {
        super( session );
    }

    @Override
    public FormViewPanel type( final PropertyTree data )
    {
        final String description = data.getString( DESCRIPTION );
        final String population = data.getString( POPULATION );
        clearAndType( descriptionTextArea, description );
        sleep( 700 );
        clearAndType( populationInput, population );
        sleep( 500 );
        return this;
    }
}
