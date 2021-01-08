package com.enonic.autotests.pages.form;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.LoaderComboBox;
import com.enonic.autotests.utils.NameHelper;
import com.enonic.xp.data.PropertyTree;

import static com.enonic.autotests.utils.SleepHelper.sleep;

/**
 * Created on 5/19/2017.
 */
public class FreeFormViewPanel
    extends FormViewPanel
{
    private final String ITEM_SET = "//div[contains(@id,'FormItemSetView')]";

    private final String ELEMENT_TYPE_OPTION_FILTER_INPUT =
        ITEM_SET + "//div[contains(@id,'FormOptionSetView') and descendant::h5[text()='element type']]" + DROPDOWN_OPTION_FILTER_INPUT;

    private final String INPUT_TYPE_OPTION_FILTER_INPUT =
        ITEM_SET + "//div[contains(@id,'FormOptionSetOptionView') and descendant::h5[text()='input type']]" + DROPDOWN_OPTION_FILTER_INPUT;

    private final String INPUT_TYPE_DROPDOWN =
        "//div[contains(@id,'FormOptionSetView') and descendant::h5[text()='input type']]//div[contains(@id,'Dropdown')]";

    private final String ELEMENT_TYPE_DROPDOWN =
        "//div[contains(@id,'FormOptionSetView') and descendant::h5[text()='element type']]//div[contains(@id,'Dropdown')]";

    private final String ITEM_SET_2 = ITEM_SET + "//div[contains(@id,'FormOccurrenceDraggableLabel') and contains(.,'Input')]";

    private final String ITEM_SET_3 = ITEM_SET + "//div[contains(@id,'FormOccurrenceDraggableLabel') and contains(.,'element type')]";

    @FindBy(xpath = ELEMENT_TYPE_OPTION_FILTER_INPUT)
    private WebElement elementTypeOptionFilterInput;

    @FindBy(xpath = INPUT_TYPE_OPTION_FILTER_INPUT)
    private WebElement inputTypeOptionFilterInput;

    /**
     * The constructor.
     *
     * @param session
     */
    public FreeFormViewPanel( final TestSession session )
    {
        super( session );
    }

    @Override
    public FormViewPanel type( final PropertyTree data )
    {
        return this;
    }


    public FreeFormViewPanel selectElementType( String option )
    {
        findElement( By.xpath( ITEM_SET + ELEMENT_TYPE_DROPDOWN ) ).click();
        sleep( 200 );
        clearAndType( elementTypeOptionFilterInput, option );
        sleep( 400 );
        String appGridItem = "//div[contains(@id,'FormOptionSetView') and descendant::h5[text()='element type']]" +
            String.format( NAMES_VIEW_BY_DISPLAY_NAME, option );
        if ( !isElementDisplayed( appGridItem ) )
        {
            saveScreenshot( NameHelper.uniqueName( "err_freeform_" ) );
            throw new TestFrameworkException( "Option: " + option + "  was not found!" );
        }
        buildActions().moveToElement( findElement( By.xpath( appGridItem ) ) );
        findElement( By.xpath( appGridItem ) ).click();
        sleep( 700 );
        return this;
    }

    public FreeFormViewPanel selectInputType( String option )
    {
        findElement( By.xpath( ITEM_SET + INPUT_TYPE_DROPDOWN ) ).click();
        sleep( 200 );
        clearAndType( inputTypeOptionFilterInput, option );
        sleep( 1000 );
        String appGridItem = "//div[contains(@id,'FormOptionSetView') and descendant::h5[text()='input type']]" +
            String.format( NAMES_VIEW_BY_DISPLAY_NAME, option );
        if ( !isElementDisplayed( appGridItem ) )
        {
            saveScreenshot( NameHelper.uniqueName( "err_freeform_" ) );
            throw new TestFrameworkException( "Option: " + option + "  was not found!" );
        }
        buildActions().moveToElement( findElement( By.xpath( appGridItem ) ) );
        findElement( By.xpath( appGridItem ) ).click();
        sleep( 700 );
        return this;
    }


    public FreeFormViewPanel selectImage( String imageName )
    {
        LoaderComboBox loaderComboBox = new LoaderComboBox( getSession() );
        clearAndType( getDisplayedElement( By.xpath( COMBOBOX_OPTION_FILTER_INPUT ) ), imageName );
        sleep( 700 );
        loaderComboBox.selectOption( imageName );
        sleep( 400 );
        return this;
    }

    //Expand/collapse the form:
    public void clickOnForm()
    {
        this.findElement( By.xpath( ITEM_SET ) ).click();
        sleep( 400 );
    }

}

