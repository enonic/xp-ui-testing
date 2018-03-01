package com.enonic.autotests.pages.form;

import org.openqa.selenium.By;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.LoaderComboBox;
import com.enonic.xp.data.PropertyTree;

import static com.enonic.autotests.utils.SleepHelper.sleep;

/**
 * Created by on 5/19/2017.
 */
public class FreeFormViewPanel
    extends FormViewPanel
{
    private final String ITEM_SET = "//div[contains(@id,'FormItemSetView')]";

    private final String INPUT_RADIO_BUTTON = ITEM_SET +
        "//div[contains(@id,'FormOptionSetOptionView')]//span[contains(@id,'ui.RadioButton') and descendant::label[text()='Input']]";

    private final String BUTTON_RADIO_BUTTON = ITEM_SET +
        "//div[contains(@id,'FormOptionSetOptionView')]//span[contains(@id,'ui.RadioButton') and descendant::label[text()='Button']]";

    private final String SELECT_RADIO_BUTTON = ITEM_SET +
        "//div[contains(@id,'FormOptionSetOptionView')]//span[contains(@id,'ui.RadioButton') and descendant::label[text()='Select']]";

    private final String IMAGE_RADIO_BUTTON = ITEM_SET +
        "//div[contains(@id,'FormOptionSetOptionView')]//span[contains(@id,'ui.RadioButton') and descendant::label[text()='image']]";


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
}

