package com.enonic.autotests.pages.contentmanager.wizardpanel;


import org.openqa.selenium.By;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.WizardStepForm;
import com.enonic.autotests.pages.form.FormViewPanel;
import com.enonic.wem.api.data.PropertyTree;

public class ContentWizardStepForm
    extends WizardStepForm
{
    public ContentWizardStepForm( final TestSession session )
    {
        super( session );
        // XXX: Verify that ContentWizardStepForm is visible, if not throw exception
        waitUntilVisible( By.xpath( "//div[contains[@id,'ContentWizardStepForm']" ) );
    }

    public ContentWizardStepForm type( PropertyTree data )
    {
        new FormViewPanel( getSession() ).type( data );
        return this;
    }
}
