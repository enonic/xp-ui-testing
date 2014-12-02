package com.enonic.autotests.pages.contentmanager.wizardpanel;


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
        // TODO: Verify that ContentWizardStepForm is visible, if not throw exception
    }

    public ContentWizardStepForm type( PropertyTree data )
    {
        new FormViewPanel( getSession() ).type( data );
        return this;
    }
}
