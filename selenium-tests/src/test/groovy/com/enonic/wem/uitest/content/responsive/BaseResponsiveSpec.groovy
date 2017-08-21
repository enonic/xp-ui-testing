package com.enonic.wem.uitest.content.responsive

import com.enonic.autotests.pages.Application
import com.enonic.wem.uitest.content.BaseContentSpec
import org.openqa.selenium.Dimension

/**
 * Created on 5/29/2017.*/
class BaseResponsiveSpec
    extends BaseContentSpec
{
    protected setBrowserDimension( int width, int height )
    {
        browser.driver.manage().window().setSize( new Dimension( width, height ) );
        contentBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
    }
}
