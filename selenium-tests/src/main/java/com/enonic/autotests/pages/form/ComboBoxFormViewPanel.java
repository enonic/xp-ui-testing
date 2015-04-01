package com.enonic.autotests.pages.form;


import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.xp.data.PropertyTree;

public class ComboBoxFormViewPanel
    extends FormViewPanel

{

    protected final String COMBO_BOX = "//div[contains(@id,'ComboBox')]";

    protected final String COMBO_BOX_OPTIONS_INPUT_XPATH = FORM_VIEW + COMBO_BOX + "//input[contains(@id,'ComboBoxOptionFilterInput')]";

    @FindBy(xpath = COMBO_BOX_OPTIONS_INPUT_XPATH)
    protected WebElement optionFilterInput;


    public ComboBoxFormViewPanel( final TestSession session )
    {
        super( session );
    }

    @Override
    public FormViewPanel type( final PropertyTree data )
    {  // data.getProperty(  )
        optionFilterInput.sendKeys();
//        if ( inputs.size() == 0 )
//        {
//            throw new TestFrameworkException( "text input was not found" );
//        }
//        String text = data.getString( OPTION_STRING );
//        if ( text != null )
//        {
//            optionFilterInput.sendKeys( text );
//        }
//        else
//        {
//            getLogger().info( "TextLine1:1 - there are no ane text data for typing in the TexTline" );
//        }

        return this;
    }

    public boolean isOptionFilterInputEnabled()
    {
        return optionFilterInput.isEnabled();
    }


}
