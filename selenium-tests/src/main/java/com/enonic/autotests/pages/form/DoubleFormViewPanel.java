package com.enonic.autotests.pages.form;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.google.common.collect.Iterators;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.Application;
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

    public static void main( String args[] )
    {
        System.out.print( Double.MAX_VALUE );
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
        Iterator<String> iterator = doubleValues.iterator();
        actualInputs.stream().forEach( e -> typeDoubleValue( e, iterator.next() ) );
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

    private String getFirstDoubleInputValue()
    {
        List<WebElement> inputs = getDisplayedElements( By.xpath( DOUBLE_INPUT ) );
        return inputs.get( 0 ).getAttribute( "value" );
    }

    public DoubleFormViewPanel typeDoubleValue( WebElement doubleInput, String value )
    {
        clearAndType( doubleInput, value );
        sleep( 300 );
        return this;
    }

    public DoubleFormViewPanel typeDoubleValue( String value )
    {
        List<WebElement> actualInputs = getDisplayedElements( By.xpath( DOUBLE_INPUT ) );
        typeDoubleValue( actualInputs.get( 0 ), value );
        return this;
    }

    private void addDoubleInputs( int numberToAdd )
    {
        for ( int i = 0; i < numberToAdd; i++ )
        {
            clickOnAddButton();
        }
    }

    public boolean isValueInInputValid( int index )
    {
        List<WebElement> actualInputs = getDisplayedElements( By.xpath( DOUBLE_INPUT ) );
        return !waitAndCheckAttrValue( actualInputs.get( index ), "class", "invalid", Application.EXPLICIT_NORMAL );
    }
}