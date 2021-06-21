package com.enonic.autotests.pages.form.liveedit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.contentmanager.wizardpanel.InsertImageModalDialog;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class TextComponentView
    extends Application
{
    private final String COMPONENT_CONTAINER = "//div[contains(@id,'TextComponentView')]";

    private final String TOOLBOX = "//span[contains(@id,'cke_1_toolbox')]";

    private final String INSERT_IMAGE_BUTTON =
        COMPONENT_CONTAINER + TOOLBOX + "//a[contains(@class,'cke_button') and contains(@title,'Image')]";

    @FindBy(xpath = INSERT_IMAGE_BUTTON)
    private WebElement insertImageButton;

    public TextComponentView( final TestSession session )
    {
        super( session );
    }


    protected TextComponentView clickOnInsertImageButton()
    {
        if ( !waitUntilVisibleNoException( By.xpath( INSERT_IMAGE_BUTTON ), EXPLICIT_NORMAL ) )
        {
            saveScreenshot( "err_text_component_insert_image" );
            throw new TestFrameworkException( "Insert image button was not found!" );
        }
        insertImageButton.click();

        //InsertImageModalDialog insertImage = new InsertImageModalDialog( getSession() );
        //insertImage.waitForOpened();
        return this;
    }
}
