package com.enonic.autotests.pages.contentmanager.wizardpanel;


import org.openqa.selenium.By;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.WizardStepForm;
import com.enonic.autotests.pages.form.CheckBoxFormViewPanel;
import com.enonic.autotests.pages.form.DateFormViewPanel;
import com.enonic.autotests.pages.form.DateTimeFormViewPanel;
import com.enonic.autotests.pages.form.DoubleFormViewPanel;
import com.enonic.autotests.pages.form.FormViewPanel;
import com.enonic.autotests.pages.form.GeoPointFormViewPanel;
import com.enonic.autotests.pages.form.LongFormViewPanel;
import com.enonic.autotests.pages.form.ModuleContentType;
import com.enonic.autotests.pages.form.PageTemplateFormViewPanel;
import com.enonic.autotests.pages.form.SiteFormViewPanel;
import com.enonic.autotests.pages.form.TextLine0_1_FormViewPanel;
import com.enonic.autotests.pages.form.TextLine1_0_FormViewPanel;
import com.enonic.autotests.pages.form.TextLine1_1_FormViewPanel;
import com.enonic.autotests.pages.form.TextLine2_5_FormViewPanel;
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

        else if ( contentTypeName.contains( ModuleContentType.GEO_POINT.getName() ) )
        {
            formViewPanel = new GeoPointFormViewPanel( getSession() );
        }
        else if ( contentTypeName.contains( ModuleContentType.CHECKBOX.getName() ) )
        {
            formViewPanel = new CheckBoxFormViewPanel( getSession() );
        }

        else if ( contentTypeName.equals( ContentTypeName.pageTemplate().toString() ) )
        {
            formViewPanel = new PageTemplateFormViewPanel( getSession() );
        }
        else if ( contentTypeName.contains( ModuleContentType.LONG.getName() ) )
        {
            formViewPanel = new LongFormViewPanel( getSession() );
        }
        else if ( contentTypeName.contains( ModuleContentType.TEXTLINE1_0.getName() ) )
        {
            formViewPanel = new TextLine1_0_FormViewPanel( getSession() );
        }
        else if ( contentTypeName.contains( ModuleContentType.TEXTLINE1_1.getName() ) )
        {
            formViewPanel = new TextLine1_1_FormViewPanel( getSession() );
        }
        else if ( contentTypeName.contains( ModuleContentType.TEXTLINE0_1.getName() ) )
        {
            formViewPanel = new TextLine0_1_FormViewPanel( getSession() );
        }
        else if ( contentTypeName.contains( ModuleContentType.TEXTLINE2_5.getName() ) )
        {
            formViewPanel = new TextLine2_5_FormViewPanel( getSession() );
        }
        else
        {
            throw new TestFrameworkException( "ContentWizardStepForm: type() not implemented for " + contentTypeName );
        }
        formViewPanel.type( data );

        return this;
    }
}
