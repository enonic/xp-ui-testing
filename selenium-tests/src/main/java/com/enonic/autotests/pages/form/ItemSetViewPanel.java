package com.enonic.autotests.pages.form;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.xp.data.PropertyTree;

import static com.enonic.autotests.utils.SleepHelper.sleep;

/**
 * Created on 18.10.2016.
 */
public class ItemSetViewPanel
    extends FormViewPanel

{
    public static String TEXT_LINE_VALUES = "item_set_text_line";

    public static String HTML_AREA_VALUES = "item_set_html_area";

    private final String FORM_ITEM_SET_VIEW = FORM_VIEW + "//div[contains(@id,'FormItemSetView')]";

    private final String OCCURRENCE_VIEW = FORM_ITEM_SET_VIEW + "//div[contains(@id,'FormItemSetOccurrenceView')]";

    private final String DRAG_HANDLER = OCCURRENCE_VIEW + "//div[@class='drag-control']";

    private final String TEXT_LINE_INPUTS = FORM_ITEM_SET_VIEW + "//input[contains(@id,'TextInput')]";

    protected final String HTML_AREA_INPUTS = FORM_ITEM_SET_VIEW + "//textarea[contains(@id,'TextArea')]";

    protected final String ADD_ITEM_SET_BUTTON = FORM_VIEW + "//button/span[text()='Add ItemSet']";

    protected final String REMOVE_ITEM_SET_BUTTON = OCCURRENCE_VIEW + "/a[@class='remove-button']";

    @FindBy(xpath = ADD_ITEM_SET_BUTTON)
    private WebElement addItemSetButton;

    public ItemSetViewPanel( final TestSession session )
    {
        super( session );
    }

    @Override
    public FormViewPanel type( final PropertyTree data )
    {
        Iterable<String> textLineStrings = data.getStrings( TEXT_LINE_VALUES );
        Iterable<String> htmlAreaStrings = data.getStrings( HTML_AREA_VALUES );
        typeTextInTextLines( textLineStrings );
        typeTextInHtmlAreas( htmlAreaStrings );
        return this;
    }

    /**
     * Swaps two sets
     */
    public void doSwapItems()
    {
        WebElement source = findElements( By.xpath( DRAG_HANDLER ) ).get( 0 );
        WebElement target = findElements( By.xpath( DRAG_HANDLER ) ).get( 1 );
        Actions builder = new Actions( getDriver() );
        builder.dragAndDrop( source, target ).build().perform();
        sleep( 1000 );
    }

    public List<String> getTextFromTextLines()
    {
        List<WebElement> textInputs = findElements( By.xpath( TEXT_LINE_INPUTS ) );
        return textInputs.stream().map( input -> input.getAttribute( "value" ) ).collect( Collectors.toList() );
    }

    public ItemSetViewPanel typeTextInHtmlAreas( Iterable<String> strings )
    {
        List<WebElement> frames = findElements( By.xpath( HTML_AREA_INPUTS ) );
        if ( frames.size() == 0 )
        {
            throw new TestFrameworkException( "Html areas were not found on the page" );
        }
        Iterator<String> it = strings.iterator();
        int i = 0;
        while ( it.hasNext() )
        {
            typeTextInHtmlArea( frames.get( i ), it.next() );
            i++;
        }
        return this;
    }

    public List<String> getInnerTextFromHtmlAreas()
    {
        List<WebElement> frames = findElements( By.xpath( HTML_AREA_INPUTS ) );
        //List<WebElement> editors =
        // findElements( By.xpath( "//div[contains(@id,'api.form.FormView')]//textarea[contains(@id,'api.ui.text.TextArea')]" ) );
        return frames.stream().map( e -> getCKEData( e.getAttribute( "id" ) ) ).collect( Collectors.toList() );
    }

    public ItemSetViewPanel typeTextInTextLines( final Iterable<String> stringsForTextLines )
    {
        List<WebElement> textLines = findElements( By.xpath( TEXT_LINE_INPUTS ) );
        Iterator<String> it = stringsForTextLines.iterator();
        int i = 0;
        while ( it.hasNext() )
        {
            clearAndType( textLines.get( i ), it.next() );
            i++;
        }
        return this;
    }

    public void removeOneItem()
    {
        if ( !isElementDisplayed( REMOVE_ITEM_SET_BUTTON ) )
        {
            saveScreenshot( "err_remove_set_button" );
            throw new TestFrameworkException( "Button remove item set was not found!" );
        }
        findElement( By.xpath( REMOVE_ITEM_SET_BUTTON ) ).click();
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
        boolean isClickable = waitUntilClickableNoException( By.xpath( ADD_ITEM_SET_BUTTON ), Application.EXPLICIT_NORMAL );
        if ( !isClickable )
        {
            saveScreenshot( "err_item_set_add_button" );
            throw new TestFrameworkException( "Add set button is not clickable!" );
        }
        addItemSetButton.click();
        sleep( 500 );
    }

    /**
     * Types a string  the first htmlArea.
     */
    protected ItemSetViewPanel typeTextInHtmlArea( WebElement areaElement, String text )
    {
        setTextInCKE( areaElement.getAttribute( "id" ), text );
        sleep( 500 );
        return this;
    }

    protected ItemSetViewPanel typeTextInHtmlArea( String text )
    {
        List<WebElement> frames = findElements( By.xpath( HTML_AREA_INPUTS ) );
        if ( frames.size() == 0 )
        {
            throw new TestFrameworkException( "Html areas were not found on the page" );
        }
        setTextInCKE( frames.get( 0 ).getAttribute( "id" ), text );
        sleep( 500 );
        return this;
    }

//

    public long getNumberOfSets()
    {
        return getNumberOfElements( By.xpath( OCCURRENCE_VIEW ) );
    }
}
