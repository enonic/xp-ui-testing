package com.enonic.autotests.pages.form;


import org.openqa.selenium.By;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.xp.data.PropertyTree;

public abstract class FormViewPanel
    extends Application
{
    protected static final String FORM_VIEW = "//div[contains(@id,'api.form.FormView')]";

    public static String VALIDATION_MESSAGE_1_1 = "This field is required";

    public static String VALIDATION_MESSAGE = "Min %s occurrences required";

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
        return waitUntilVisibleNoException( By.xpath( VALIDATION_VIEWER ), Application.EXPLICIT_NORMAL );

    }

    public String getValidationMessage()
    {
        if ( isValidationMessagePresent() )
        {
            return findElements( By.xpath( VALIDATION_VIEWER + "//li" ) ).get( 0 ).getText();
        }
        else
        {
            throw new TestFrameworkException( "validation message was not found!" );
        }
    }

}
