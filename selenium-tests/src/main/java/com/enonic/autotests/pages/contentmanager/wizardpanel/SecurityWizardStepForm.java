package com.enonic.autotests.pages.contentmanager.wizardpanel;

import java.util.List;

import org.openqa.selenium.By;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.WizardStepForm;
import com.enonic.autotests.vo.contentmanager.security.ContentAclEntry;

public class SecurityWizardStepForm
    extends WizardStepForm
{
    private final String CONTAINER_XPATH = "//div[contains(@id,'SecurityWizardStepForm')]";

    public SecurityWizardStepForm( final TestSession session )
    {
        super( session );
        waitUntilVisible( By.xpath( CONTAINER_XPATH ) );
    }

    public SecurityWizardStepForm editPermissions( List<ContentAclEntry> aclEntries )
    {
        return this;
    }
}
