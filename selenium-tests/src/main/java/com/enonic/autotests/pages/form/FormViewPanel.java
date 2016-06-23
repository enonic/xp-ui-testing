package com.enonic.autotests.pages.form;


import org.openqa.selenium.By;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.utils.TestUtils;
import com.enonic.xp.data.PropertyTree;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public abstract class FormViewPanel
    extends Application
{
    protected static final String FORM_VIEW = "//div[contains(@id,'api.form.FormView')]";

    protected final String ADD_BUTTON_XPATH = FORM_VIEW + "//div[@class='bottom-button-row']//button[child::span[text()='Add']]";

    public FormViewPanel( final TestSession session )
    {
        super( session );
    }

    public abstract FormViewPanel type( final PropertyTree data );

    public boolean isAddButtonPresent()
    {
        return isElementDisplayed( ADD_BUTTON_XPATH );
    }

    public void clickOnAddButton()
    {
        if ( !isElementDisplayed( ADD_BUTTON_XPATH ) )
        {
            TestUtils.saveScreenshot( getSession(), "err_add_button" );
            throw new TestFrameworkException( "Add button not present in Form View Panel!" );
        }
        getDisplayedElement( By.xpath( ADD_BUTTON_XPATH ) ).click();
        sleep( 500 );
    }
}
