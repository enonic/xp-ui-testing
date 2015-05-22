package com.enonic.autotests.pages.form;


import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.utils.NameHelper;
import com.enonic.autotests.utils.TestUtils;
import com.enonic.xp.data.PropertyTree;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class TagFormViewPanel
    extends FormViewPanel
{
    protected String VALIDATION_VIEWER = FORM_VIEW + "//div[contains(@id, 'ValidationRecordingViewer')]";

    private final String TAGS_INPUT_XPATH = FORM_VIEW + "//input[@type='text']";

    private final String LI_TAG_XPATH = FORM_VIEW + "//ul/li[contains(@id,'api.ui.tags.Tag')]";


    public TagFormViewPanel( final TestSession session )
    {
        super( session );
    }

    @FindBy(xpath = TAGS_INPUT_XPATH)
    private WebElement tagsInput;

    public boolean isValidationMessagePresent()
    {
        List<WebElement> result = findElements( By.xpath( VALIDATION_VIEWER ) );
        if ( result.size() == 0 )
        {
            return false;
        }
        else
        {
            return result.get( 0 ).isDisplayed();
        }
    }

    @Override
    public FormViewPanel type( final PropertyTree data )
    {
        List<WebElement> inputs = findElements( By.xpath( TAGS_INPUT_XPATH ) );
        if ( inputs.size() == 0 )
        {
            throw new TestFrameworkException( "tag input was not found" );
        }
        long min = data.getLong( "min" );
        long max = data.getLong( "max" );
        long i = 0;
        for ( final String tagText : data.getStrings( "tags" ) )
        {
            if ( i > max )
            {
                throw new TestFrameworkException( "number of text inputs can not be more than 5" );
            }

            tagsInput.sendKeys( tagText );
            sleep( 300 );
            tagsInput.sendKeys( Keys.ENTER );
            sleep( 300 );
            i++;
        }

        return this;
    }

    public boolean isTagsInputDisplayed()
    {
        return tagsInput.isDisplayed();
    }

    public int getNumberOfTags()
    {
        return findElements( By.xpath( LI_TAG_XPATH ) ).size();
    }

    public List<String> getTagsText()
    {
        sleep( 500 );
        TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "tags-text" ) );
        List<WebElement> spans = findElements( By.xpath( LI_TAG_XPATH + "/span" ) );
        return spans.stream().filter( WebElement::isDisplayed ).map( WebElement::getText ).collect( Collectors.toList() );
    }

    public TagFormViewPanel removeLastTag()
    {
        List<WebElement> spans = findElements( By.xpath( LI_TAG_XPATH + "/a" ) );
        if ( spans.size() != 0 )
        {
            spans.get( spans.size() - 1 ).click();
        }
        else
        {
            throw new TestFrameworkException( "no one tag was found!" );
        }
        return this;
    }

}
