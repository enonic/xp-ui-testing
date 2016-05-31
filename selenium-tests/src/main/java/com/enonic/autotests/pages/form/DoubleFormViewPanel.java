package com.enonic.autotests.pages.form;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.google.common.collect.Iterators;

import com.enonic.autotests.TestSession;
import com.enonic.xp.data.PropertyTree;

import static com.enonic.autotests.utils.SleepHelper.sleep;


public class DoubleFormViewPanel
    extends FormViewPanel
{
    public static String DOUBLE_VALUES = "double_values";

    private final String DOUBLE_INPUT = FORM_VIEW + "//input[contains(@id,'TextInput') and contains(@name,'double')]";

    public DoubleFormViewPanel( final TestSession session )
    {
        super( session );
    }

    @Override
    public FormViewPanel type( final PropertyTree data )
    {

        Iterable<String> doubleValues = data.getStrings( DOUBLE_VALUES );
        int requiredNumber = Iterators.size( doubleValues.iterator() );
        if ( requiredNumber == 0 )
        {
            return this;
        }
        List<WebElement> actualInputs = getDisplayedElements( By.xpath( DOUBLE_INPUT ) );

        addDoubleInputs( requiredNumber - actualInputs.size() );

        List<WebElement> inputsActual = getDisplayedElements( By.xpath( DOUBLE_INPUT ) );
        inputsActual.stream().forEach( e -> typeDoubleValue( e, doubleValues.iterator().next() ) );
        return this;
    }

    public List<String> getInputsValues()
    {
        List<WebElement> actualInputs = getDisplayedElements( By.xpath( DOUBLE_INPUT ) );
        return actualInputs.stream().map( e -> getDoubleInputValue( e ) ).collect( Collectors.toList() );
    }

    private String getDoubleInputValue( WebElement input )
    {
        return input.getAttribute( "value" );
    }

    public DoubleFormViewPanel typeDoubleValue( WebElement doubleInput, String value )
    {
        clearAndType( doubleInput, value );
        sleep( 300 );
        return this;
    }

    private void addDoubleInputs( int numberToAdd )
    {
        for ( int i = 0; i < numberToAdd; i++ )
        {
            clickOnAddButton();
        }
    }
}