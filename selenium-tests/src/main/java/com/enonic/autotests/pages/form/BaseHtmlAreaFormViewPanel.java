package com.enonic.autotests.pages.form;


import org.openqa.selenium.By;

import com.enonic.autotests.TestSession;

public abstract class BaseHtmlAreaFormViewPanel
    extends FormViewPanel
{
    public static final String EMPTY_TEXT_AREA_CONTENT = "<p><br data-mce-bogus=\"1\"></p>";

    public static final String NUMBER_OF_EDITORS = "number-of-editors";

    public static String STRING_PROPERTY = "string";

    public static String STRINGS_PROPERTY = "strings";

    protected final String STEP_XPATH = "//li[contains(@id,'api.ui.tab.TabBarItem')]//span[text()='Html Area']";

    protected final String TEXT_IN_AREA_SCRIPT = "return document.getElementById('tinymce').innerHTML";

    protected final String TINY_MCE = FORM_VIEW + "//div[contains(@class,'mce-edit-area')]//iframe[contains(@id,'api.ui.text.TextArea')]";

    protected final String TEXT_AREA = "//iframe[contains(@id,'api.ui.text.TextArea')]";

    public BaseHtmlAreaFormViewPanel( final TestSession session )
    {
        super( session );
    }

    public int getNumberOfAreas()
    {
        return findElements( By.xpath( TINY_MCE ) ).size();
    }

}
