package com.enonic.autotests.pages.form;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.xp.data.PropertyTree;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class PdfFormViewPanel
    extends FormViewPanel

{
    public static final String ABSTRACT_TEXT = "abstract";

    private final String ABSTRACT_TEXT_INPUT = FORM_VIEW + "//textarea";

    @FindBy(xpath = ABSTRACT_TEXT_INPUT)
    private WebElement textExtractionArea;

    public PdfFormViewPanel( TestSession session )
    {
        super( session );
    }

    @Override
    public FormViewPanel type( final PropertyTree data )
    {
        String abstractText = data.getString( ABSTRACT_TEXT );
        clearAndType( textExtractionArea, abstractText );
        sleep( 2000 );

        return this;
    }

    public boolean isTextAreaPresent()
    {
        return textExtractionArea.isDisplayed();
    }

    public String getExtractionData()
    {
        return textExtractionArea.getAttribute( "value" );
    }

    public PdfFormViewPanel setExtractionData( String text )
    {
        clearAndType( textExtractionArea, text );
        return this;
    }

}
