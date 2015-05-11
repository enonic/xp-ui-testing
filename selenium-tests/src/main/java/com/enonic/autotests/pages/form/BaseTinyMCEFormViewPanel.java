package com.enonic.autotests.pages.form;


import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.xp.data.PropertyTree;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public abstract class BaseTinyMCEFormViewPanel
    extends FormViewPanel
{
    public static final String NUMBER_OF_EDITORS = "number-of-editors";

    public static String STRING_PROPERTY = "string";

    public static String STRINGS_PROPERTY = "strings";

    protected final String STEP_XPATH = "//li[contains(@id,'api.ui.tab.TabBarItem')]//span[@title='Tiny MCE editor']";

    protected final String TINY_MCE = FORM_VIEW + "//div[contains(@class,'mce-edit-area')]//iframe[contains(@id,'api.ui.text.TextArea')]";

    public BaseTinyMCEFormViewPanel( final TestSession session )
    {
        super( session );
    }

    public int getNumberOfMCE()
    {
        return findElements( By.xpath( TINY_MCE ) ).size();
    }

    @Override
    public FormViewPanel type( final PropertyTree data )
    {
        List<WebElement> textArea = findElements( By.xpath( TINY_MCE ) );
        if ( textArea.size() == 0 )
        {
            throw new TestFrameworkException( "no one text input was not found" );
        }

        int i = 0;
        long number = data.getLong( NUMBER_OF_EDITORS );
        for ( final String sourceString : data.getStrings( STRINGS_PROPERTY ) )
        {
            if ( i >= number )
            {
                throw new TestFrameworkException( "number of text area can not be more than expected" );
            }
            if ( i >= textArea.size() )
            {
                break;
            }
            textArea.get( i ).sendKeys( sourceString );
            sleep( 300 );
            i++;
        }
        sleep( 300 );
        return this;
    }
}
