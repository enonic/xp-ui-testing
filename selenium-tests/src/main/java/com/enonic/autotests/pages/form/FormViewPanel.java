package com.enonic.autotests.pages.form;


import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.Application;
import com.enonic.xp.data.PropertyTree;

public abstract class FormViewPanel
    extends Application
{
    protected static final String FORM_VIEW = "//div[contains(@id,'api.form.FormView')]";

    protected String VALIDATION_VIEWER = FORM_VIEW + "//div[contains(@id, 'ValidationRecordingViewer')]";

    protected static String VALUE_INPUT =
        "//div[contains(@id,'api.form.InputView') and descendant::div[@title='%s']]//input[contains(@id,'TextInput')]";

    public FormViewPanel( final TestSession session )
    {
        super( session );
    }

    public abstract FormViewPanel type( final PropertyTree data );

    public boolean isValidationMessagePresent()
    {
        List<WebElement> result = findElements( By.xpath( VALIDATION_VIEWER ) );
        if ( result.size() == 0 )
        {
            return false;
        }
        else
        {
            return result.get( 0 ).isDisplayed();
        }
    }

}
