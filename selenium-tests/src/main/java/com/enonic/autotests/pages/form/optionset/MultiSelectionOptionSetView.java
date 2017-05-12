package com.enonic.autotests.pages.form.optionset;

import org.openqa.selenium.By;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.Application;

/**
 * Created on 5/12/2017.
 */
public class MultiSelectionOptionSetView
    extends Application
{
    private String CSS_CONTAINER = "div[id^='api.form.FormView'] div[id^='FormOptionSetOptionView']:has(div:contains('Multi selection'))";

    public MultiSelectionOptionSetView( final TestSession session )
    {
        super( session );
    }

    public boolean isOpened()
    {
        return findElements( By.cssSelector( CSS_CONTAINER ) ).size() > 0;
    }

}