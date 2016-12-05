package com.enonic.autotests.pages.form.liveedit;


import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel;

public abstract class UIComponent
    extends Application
{

    public static String NAMES_ICON_VIEW = "//div[contains(@id,'NamesAndIconView')]//h6[contains(@class,'main-name') and text()='%s']";

    private ContentWizardPanel contentWizardPanel;

    public UIComponent( final TestSession session )
    {
        super( session );
        contentWizardPanel = new ContentWizardPanel( session );
    }

    ContentWizardPanel getContentWizardPanel()
    {
        return contentWizardPanel;
    }
}
