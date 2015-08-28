package com.enonic.autotests.pages.form;


import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.xp.data.PropertyTree;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class ArticleFormView
    extends FormViewPanel
{
    public static final String TITLE = "title";

    public static final String BODY = "body";

    private final String TITLE_INPUT = FORM_VIEW + "//input[contains(@name,'title')]";

    private final String BODY_TEXTAREA = FORM_VIEW + "//textarea[contains(@name,'body')]";

    @FindBy(xpath = TITLE_INPUT)
    protected WebElement titleInput;

    @FindBy(xpath = BODY_TEXTAREA)
    protected WebElement bodyTextArea;

    public ArticleFormView( final TestSession session )
    {
        super( session );
    }

    @Override
    public FormViewPanel type( final PropertyTree data )
    {
        final String title = data.getString( TITLE );
        final String body = data.getString( BODY );
        clearAndType( titleInput, title );
        sleep( 700 );
        clearAndType( bodyTextArea, body );
        sleep( 500 );
        return this;
    }
}
