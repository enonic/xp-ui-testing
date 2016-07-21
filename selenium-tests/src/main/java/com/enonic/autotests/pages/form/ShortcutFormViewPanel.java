package com.enonic.autotests.pages.form;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.RichComboBoxInput;
import com.enonic.autotests.utils.TestUtils;
import com.enonic.xp.data.PropertyTree;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class ShortcutFormViewPanel
    extends FormViewPanel
{
    public static final String SHORTCUT_PROPERTY = "shortcut";

    protected final String CONTENT_SELECTOR = FORM_VIEW + "//div[contains(@id,'ContentSelector')]";

    protected final String TARGET_OPTION_FILTER_INPUT = CONTENT_SELECTOR + COMBOBOX_OPTION_FILTER_INPUT;

    private String REMOVE_TARGET_BUTTON = CONTENT_SELECTOR + "//div[contains(@id,'ContentSelectedOptionView')]//a[@class='remove']";

    private String TARGET_DISPLAY_NAME = CONTENT_SELECTOR + H6_DISPLAY_NAME;

    @FindBy(xpath = TARGET_OPTION_FILTER_INPUT)
    protected WebElement optionFilterInput;

    public ShortcutFormViewPanel( TestSession session )
    {
        super( session );
    }

    @Override
    public FormViewPanel type( final PropertyTree data )
    {
        final String targetName = data.getString( SHORTCUT_PROPERTY );
        selectTarget( targetName );
        return this;
    }

    public ShortcutFormViewPanel selectTarget( String targetName )
    {
        clearAndType( optionFilterInput, targetName );
        sleep( 300 );
        RichComboBoxInput richComboBoxInput = new RichComboBoxInput( getSession() );
        richComboBoxInput.selectOption( targetName );
        return this;
    }

    public ShortcutFormViewPanel removeTarget()
    {
        if ( !isElementDisplayed( REMOVE_TARGET_BUTTON ) )
        {
            TestUtils.saveScreenshot( getSession(), "err_remove_target" );
            throw new TestFrameworkException( "remove target button was not found!" );
        }
        getDisplayedElement( By.xpath( REMOVE_TARGET_BUTTON ) ).click();
        return this;
    }

    public String getTargetDisplayName()
    {
        if ( !isElementDisplayed( TARGET_DISPLAY_NAME ) )
        {
            return null;
        }
        else
        {
            return getDisplayedString( TARGET_DISPLAY_NAME );
        }
    }
}
