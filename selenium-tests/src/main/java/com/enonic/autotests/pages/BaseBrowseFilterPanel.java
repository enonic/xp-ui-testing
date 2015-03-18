package com.enonic.autotests.pages;


import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;

public class BaseBrowseFilterPanel
    extends Application
{
    public final String CLEAR_FILTER_LINK = "Clear filter";

    public static final String SEARCH_INPUT_XPATH =
        "//input[contains(@id,'api.app.browse.filter.TextSearchField') and contains(@class,'text-search-field')]";

    @FindBy(xpath = SEARCH_INPUT_XPATH)
    protected WebElement searchInput;

    public BaseBrowseFilterPanel( final TestSession session )
    {
        super( session );
    }
}
