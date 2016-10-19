package com.enonic.autotests.pages.form;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.xp.data.PropertyTree;

import static com.enonic.autotests.utils.SleepHelper.sleep;

/**
 * Created on 18.10.2016.
 */
public class FormItemSetViewPanel
    extends FormViewPanel

{
    private final String FORM_ITEM_SET_VIEW = FORM_VIEW + "//div[contains(@id,'FormItemSetView')";

    private final String OCCURRENCE_VIEW = FORM_ITEM_SET_VIEW + "//div[contains(@id,'FormItemSetOccurrenceView')]";

    private final String TEXT_LINE = FORM_ITEM_SET_VIEW + "//input[contains(@id,'TextInput')]";

    protected final String HTML_AREA = FORM_ITEM_SET_VIEW + "//div[contains(@class,'mce-edit-area')]//iframe[contains(@id,'TextArea')]";


    @FindBy(xpath = TEXT_LINE)
    private WebElement textLineInput;


    public FormItemSetViewPanel( final TestSession session )
    {
        super( session );
    }

    @Override
    public FormViewPanel type( final PropertyTree data )
    {
        //typeTextLine( data.getString( TEXT_LINE_VALUE ) );
        // typeTextInHtmlArea( data.getString( HTML_AREA_VALUE ) );

        return this;
    }

    public FormItemSetViewPanel typeTextInHtmlArea( String text )
    {
        if ( text != null )
        {
            WebElement areaElement = findElement( By.xpath( HTML_AREA ) );
            Actions builder = new Actions( getDriver() );
            builder.click( findElement( By.xpath( HTML_AREA ) ) ).build().perform();
            setTextIntoArea( areaElement.getAttribute( "id" ), text );
            sleep( 300 );
        }
        return this;
    }
}
