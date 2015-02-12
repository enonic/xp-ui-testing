package com.enonic.autotests.pages.form.liveedit;


import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.Application;

public abstract class UIComponent
    extends Application
{

    public static String NAMES_ICON_VIEW = "//div[contains(@id,'api.app.NamesAndIconView')]//h6[@class='main-name' and text()='%s']";
    public UIComponent( final TestSession session )
    {
        super( session );
    }
}
