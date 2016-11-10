package com.enonic.autotests.pages.form;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.xp.data.PropertyTree;

import static com.enonic.autotests.utils.SleepHelper.sleep;

/**
 * Created on 18.10.2016.
 */
public class ItemSetViewPanel
    extends FormViewPanel

{
    public static String TEXT_LINE_VALUE = "item_set_text_line";

    public static String HTML_AREA_VALUE = "item_set_html_area";

    private final String FORM_ITEM_SET_VIEW = FORM_VIEW + "//div[contains(@id,'FormItemSetView')]";

    private final String OCCURRENCE_VIEW = FORM_ITEM_SET_VIEW + "//div[contains(@id,'FormItemSetOccurrenceView')]";

    private final String TEXT_LINE = FORM_ITEM_SET_VIEW + "//input[contains(@id,'TextInput')]";

    protected final String HTML_AREA = FORM_ITEM_SET_VIEW + "//div[contains(@class,'mce-edit-area')]//iframe[contains(@id,'TextArea')]";

    protected final String ADD_ITEM_SET_BUTTON = FORM_VIEW + "//button/span[text()='Add ItemSet']";

    protected final String REMOVE_ITEM_SET_BUTTON = OCCURRENCE_VIEW + "/a[@class='remove-button']";

    @FindBy(xpath = TEXT_LINE)
    private WebElement textLineInput;

    @FindBy(xpath = ADD_ITEM_SET_BUTTON)
    private WebElement addItemSetButton;

    public ItemSetViewPanel( final TestSession session )
    {
        super( session );
    }

    @Override
    public FormViewPanel type( final PropertyTree data )
    {
        typeTextInTextLine( data.getString( TEXT_LINE_VALUE ) );
        typeTextInHtmlArea( data.getString( HTML_AREA_VALUE ) );

        return this;
    }

    public ItemSetViewPanel typeTextInTextLine( final String text )
    {

        return this;
    }

    public void removeOneItem()
    {
        if ( !isElementDisplayed( REMOVE_ITEM_SET_BUTTON ) )
        {
            saveScreenshot( "err_remove_set_button" );
            throw new TestFrameworkException( "Button remove item set was not found!" );
        }
        findElements( By.xpath( REMOVE_ITEM_SET_BUTTON ) ).get( 0 ).click();
    }

    public boolean isFormItemSetDisplayed()
    {
        return isElementDisplayed( FORM_ITEM_SET_VIEW );
    }

    @Override
    public boolean isAddButtonPresent()
    {
        return addItemSetButton.isDisplayed();
    }

    @Override
    public void clickOnAddButton()
    {
        addItemSetButton.click();
        sleep( 500 );
//        boolean isLoaded = waitUntilVisibleNoException( By.xpath( FORM_ITEM_SET_VIEW ), EXPLICIT_NORMAL );
//        if ( !isLoaded )
//        {
//            saveScreenshot( "err_load_item_set" );
//            throw new TestFrameworkException( "ItemSet form was not loaded!" );
//        }
    }

    public ItemSetViewPanel typeTextInHtmlArea( String text )
    {
        if ( text != null )
        {
            WebElement areaElement = findElement( By.xpath( HTML_AREA ) );
            Actions builder = buildActions();
            builder.click( findElement( By.xpath( HTML_AREA ) ) ).build().perform();
            setTextIntoArea( areaElement.getAttribute( "id" ), text );
            sleep( 300 );
        }
        return this;
    }

    public long getNumberOfSets()
    {
        return getNumberOfElements( By.xpath( OCCURRENCE_VIEW ) );
    }
}
