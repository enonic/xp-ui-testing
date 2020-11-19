package com.enonic.autotests.pages.form;

import org.openqa.selenium.By;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.LoaderComboBox;
import com.enonic.xp.data.PropertyTree;

import static com.enonic.autotests.utils.SleepHelper.sleep;

/**
 * Created on 5/19/2017.
 */
public class FreeFormViewPanel
    extends FormViewPanel
{
    private final String ITEM_SET = "//div[contains(@id,'FormItemSetView')]";

    private final String INPUT_RADIO_BUTTON = ITEM_SET +
        "//div[contains(@id,'FormOptionSetOptionView')]//span[contains(@id,'RadioButton') and descendant::label[text()='Input']]//label";

    private final String BUTTON_RADIO_BUTTON = ITEM_SET +
        "//div[contains(@id,'FormOptionSetOptionView')]//span[contains(@id,'RadioButton') and descendant::label[text()='Button']]//label";

    private final String SELECT_RADIO_BUTTON = ITEM_SET +
        "//div[contains(@id,'FormOptionSetOptionView')]//span[contains(@id,'RadioButton') and descendant::label[text()='Select']]//label";

    private final String IMAGE_RADIO_BUTTON = ITEM_SET +
        "//div[contains(@id,'FormOptionSetOptionView')]//span[contains(@id,'RadioButton') and descendant::label[text()='image']]//label";

    private final String ITEM_SET_2 = ITEM_SET + "//div[contains(@id,'FormOccurrenceDraggableLabel') and contains(.,'Input')]";

    private final String ITEM_SET_3 = ITEM_SET + "//div[contains(@id,'FormOccurrenceDraggableLabel') and contains(.,'element type')]";


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

    public FreeFormViewPanel clickOnInputRadioButton()
    {
        getDisplayedElement( By.xpath( INPUT_RADIO_BUTTON ) ).click();
        sleep( 1000 );
        return this;
    }

    public FreeFormViewPanel clickOnButtonRadioButton()
    {
        buildActions().moveToElement( this.findElement( By.xpath( ITEM_SET_2 ) ) ).build().perform();
        getDisplayedElement( By.xpath( BUTTON_RADIO_BUTTON ) ).click();
        sleep( 1000 );
        return this;
    }

    public FreeFormViewPanel clickOnSelectRadioButton()
    {
        getDisplayedElement( By.xpath( SELECT_RADIO_BUTTON ) ).click();
        return this;
    }

    public FreeFormViewPanel clickOnImageRadioButton()
    {
        doScrollPanel( 250 );
        getDisplayedElement( By.xpath( IMAGE_RADIO_BUTTON ) ).click();
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

    public void expandItemSetRadio()
    {
        this.findElement( By.xpath( ITEM_SET_2 ) ).click();
        sleep( 400 );
        this.findElement( By.xpath( ITEM_SET_3 ) ).click();
    }

}

