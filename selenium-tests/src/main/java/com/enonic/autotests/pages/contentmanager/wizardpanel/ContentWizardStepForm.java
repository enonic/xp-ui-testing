package com.enonic.autotests.pages.contentmanager.wizardpanel;


import org.openqa.selenium.By;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.WizardStepForm;
import com.enonic.autotests.pages.form.DateFormViewPanel;
import com.enonic.autotests.pages.form.DateTimeFormViewPanel;
import com.enonic.autotests.pages.form.DoubleFormViewPanel;
import com.enonic.autotests.pages.form.FormViewPanel;
import com.enonic.autotests.pages.form.ModuleContentType;
import com.enonic.autotests.pages.form.PageTemplateFormViewPanel;
import com.enonic.autotests.pages.form.SiteFormViewPanel;
import com.enonic.autotests.pages.form.TimeFormViewPanel;
import com.enonic.xp.data.PropertyTree;
import com.enonic.xp.schema.content.ContentTypeName;

public class ContentWizardStepForm
    extends WizardStepForm
{
    public ContentWizardStepForm( final TestSession session )
    {
        super( session );
        waitUntilVisible( By.xpath( "//div[contains(@id,'ContentWizardStepForm')]" ) );
    }

    public ContentWizardStepForm type( PropertyTree data, String contentTypeName )
    {
        FormViewPanel formViewPanel = null;
        if ( contentTypeName.equals( ContentTypeName.site().toString() ) )
        {
            formViewPanel = new SiteFormViewPanel( getSession() );
        }
        else if ( contentTypeName.contains( ModuleContentType.DATE_TIME.getName() ) )
        {
            formViewPanel = new DateTimeFormViewPanel( getSession() );
        }
        else if ( contentTypeName.contains( ModuleContentType.TIME.getName() ) )
        {
            formViewPanel = new TimeFormViewPanel( getSession() );
        }
        else if ( contentTypeName.contains( ModuleContentType.DOUBLE.getName() ) )
        {
            formViewPanel = new DoubleFormViewPanel( getSession() );
        }
        else if ( contentTypeName.contains( ModuleContentType.DATE.getName() ) )
        {
            formViewPanel = new DateFormViewPanel( getSession() );
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
