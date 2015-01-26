package com.enonic.autotests.pages.contentmanager.wizardpanel;


import org.openqa.selenium.By;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.WizardStepForm;
import com.enonic.autotests.pages.form.FormViewPanel;
import com.enonic.autotests.pages.form.PageTemplateFormViewPanel;
import com.enonic.autotests.pages.form.SiteFormViewPanel;
import com.enonic.wem.api.data.PropertyTree;
import com.enonic.wem.api.schema.content.ContentTypeName;

public class ContentWizardStepForm
    extends WizardStepForm
{
    public ContentWizardStepForm( final TestSession session )
    {
        super( session );
        // XXX: Verify that ContentWizardStepForm is visible, if not throw exception
        waitUntilVisible( By.xpath( "//div[contains(@id,'ContentWizardStepForm')]" ) );
    }

    public ContentWizardStepForm type( PropertyTree data, String contentTypeName )
    {
        FormViewPanel formViewPanel = null;
        if ( contentTypeName.equals( ContentTypeName.site().toString() ) )
        {
            formViewPanel = new SiteFormViewPanel( getSession() );
        }
        // we can fill in any other form   (not only system)
        else if ( contentTypeName.equals( "demo:geopoint" ) )
        {
            throw new TestFrameworkException( "ContentWizardStepForm:  not implemented for demo:geopoint" );
        }
        else if ( contentTypeName.equals( ContentTypeName.pageTemplate().toString() ) )
        {
            formViewPanel = new PageTemplateFormViewPanel( getSession() );
        }
        else
        {
            throw new TestFrameworkException( "ContentWizardStepForm: type() not implemented for " + contentTypeName );
        }
        formViewPanel.type( data );

        return this;
    }
}
