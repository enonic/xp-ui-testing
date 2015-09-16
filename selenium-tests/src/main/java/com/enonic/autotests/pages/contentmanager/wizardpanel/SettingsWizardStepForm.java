package com.enonic.autotests.pages.contentmanager.wizardpanel;


import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.WizardStepForm;
import com.enonic.autotests.vo.contentmanager.ContentSettings;

public class SettingsWizardStepForm
    extends WizardStepForm
{
    public SettingsWizardStepForm( final TestSession session )
    {
        super( session );
    }

    public SettingsWizardStepForm typeSettings( ContentSettings settings )
    {
        return this;
    }
}
