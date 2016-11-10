package com.enonic.autotests.pages.contentmanager.wizardpanel;


import org.openqa.selenium.By;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.WizardStepForm;
import com.enonic.autotests.pages.form.ArticleFormView;
import com.enonic.autotests.pages.form.CheckBoxFormViewPanel;
import com.enonic.autotests.pages.form.CityFormView;
import com.enonic.autotests.pages.form.ComboBoxFormViewPanel;
import com.enonic.autotests.pages.form.CountryFormView;
import com.enonic.autotests.pages.form.CustomSelectorFormViewPanel;
import com.enonic.autotests.pages.form.DateFormViewPanel;
import com.enonic.autotests.pages.form.DateTimeFormViewPanel;
import com.enonic.autotests.pages.form.DoubleFormViewPanel;
import com.enonic.autotests.pages.form.FieldSetFormViewPanel;
import com.enonic.autotests.pages.form.FormViewPanel;
import com.enonic.autotests.pages.form.GeoPointFormViewPanel;
import com.enonic.autotests.pages.form.HtmlArea0_0_FormViewPanel;
import com.enonic.autotests.pages.form.HtmlArea0_1_FormViewPanel;
import com.enonic.autotests.pages.form.ImageSelectorFormViewPanel;
import com.enonic.autotests.pages.form.InputsFormViewPanel;
import com.enonic.autotests.pages.form.ItemSetViewPanel;
import com.enonic.autotests.pages.form.LongFormViewPanel;
import com.enonic.autotests.pages.form.PageTemplateFormViewPanel;
import com.enonic.autotests.pages.form.RelationshipFormView;
import com.enonic.autotests.pages.form.ShortcutFormViewPanel;
import com.enonic.autotests.pages.form.SingleSelectorRadioFormView;
import com.enonic.autotests.pages.form.SiteFormViewPanel;
import com.enonic.autotests.pages.form.TagFormViewPanel;
import com.enonic.autotests.pages.form.TestAppContentType;
import com.enonic.autotests.pages.form.TextAreaFormViewPanel;
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
        else if ( contentTypeName.contains( TestAppContentType.DATE_TIME0_1.getName() ) ||
            contentTypeName.contains( TestAppContentType.DATE_TIME1_1.getName() ) )
        {
            formViewPanel = new DateTimeFormViewPanel( getSession() );
        }
        else if ( contentTypeName.contains( TestAppContentType.TIME0_0.getName() ) )
        {
            formViewPanel = new TimeFormViewPanel( getSession() );
        }
        else if ( contentTypeName.contains( TestAppContentType.DOUBLE.getName() ) )
        {
            formViewPanel = new DoubleFormViewPanel( getSession() );
        }
        else if ( contentTypeName.contains( TestAppContentType.DATE0_1.getName() ) ||
            contentTypeName.contains( TestAppContentType.DATE1_1.getName() ) )
        {
            formViewPanel = new DateFormViewPanel( getSession() );
        }

        else if ( contentTypeName.contains( TestAppContentType.GEO_POINT0_0.getName() ) ||
            contentTypeName.contains( TestAppContentType.GEO_POINT1_1.getName() ) )
        {
            formViewPanel = new GeoPointFormViewPanel( getSession() );
        }
        else if ( contentTypeName.contains( TestAppContentType.CHECKBOX.getName() ) )
        {
            formViewPanel = new CheckBoxFormViewPanel( getSession() );
        }

        else if ( contentTypeName.equals( ContentTypeName.pageTemplate().toString() ) )
        {
            formViewPanel = new PageTemplateFormViewPanel( getSession() );
        }
        else if ( contentTypeName.contains( TestAppContentType.LONG.getName() ) )
        {
            formViewPanel = new LongFormViewPanel( getSession() );
        }
        else if ( contentTypeName.contains( TestAppContentType.TEXTLINE1_0.getName() ) )
        {
            formViewPanel = new TextLine1_0_FormViewPanel( getSession() );
        }
        else if ( contentTypeName.contains( TestAppContentType.TEXTLINE1_1.getName() ) )
        {
            formViewPanel = new TextLine1_1_FormViewPanel( getSession() );
        }
        else if ( contentTypeName.contains( TestAppContentType.TEXTLINE0_1.getName() ) )
        {
            formViewPanel = new TextLine0_1_FormViewPanel( getSession() );
        }
        else if ( contentTypeName.contains( TestAppContentType.TEXTLINE2_5.getName() ) )
        {
            formViewPanel = new TextLine2_5_FormViewPanel( getSession() );
        }
        else if ( contentTypeName.contains( TestAppContentType.TAG0_5.getName() ) ||
            contentTypeName.contains( TestAppContentType.TAG2_5.getName() ) ||
            contentTypeName.contains( TestAppContentType.TAG_UNLIM.getName() ) )
        {
            formViewPanel = new TagFormViewPanel( getSession() );
        }
        else if ( contentTypeName.contains( TestAppContentType.COMBOBOX0_0.getName() ) ||
            contentTypeName.contains( TestAppContentType.COMBOBOX0_1.getName() ) ||
            contentTypeName.contains( TestAppContentType.COMBOBOX1_1.getName() ) ||
            contentTypeName.contains( TestAppContentType.COMBOBOX2_4.getName() ) )
        {
            formViewPanel = new ComboBoxFormViewPanel( getSession() );
        } //
        else if ( contentTypeName.contains( TestAppContentType.RADIO_BUTTONS.getName() ) )
        {
            formViewPanel = new SingleSelectorRadioFormView( getSession() );
        }
        else if ( contentTypeName.contains( TestAppContentType.TEXT_AREA.getName() ) )
        {
            formViewPanel = new TextAreaFormViewPanel( getSession() );
        }
        else if ( contentTypeName.contains( TestAppContentType.FIELD_SET.getName() ) )
        {
            formViewPanel = new FieldSetFormViewPanel( getSession() );
        }
        else if ( contentTypeName.contains( TestAppContentType.HTMLAREA0_1.getName() ) )
        {
            formViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );
        }
        else if ( contentTypeName.contains( TestAppContentType.HTMLAREA0_0.getName() ) ||
            contentTypeName.contains( TestAppContentType.HTMLAREA0_2.getName() ) )
        {
            formViewPanel = new HtmlArea0_0_FormViewPanel( getSession() );
        }
        else if ( contentTypeName.contains( TestAppContentType.IMAGE_SELCTOR0_0.getName() ) ||
            contentTypeName.contains( TestAppContentType.IMAGE_SELCTOR0_1.getName() ) ||
            contentTypeName.contains( TestAppContentType.IMAGE_SELCTOR1_1.getName() ) ||
            contentTypeName.contains( TestAppContentType.IMAGE_SELCTOR2_4.getName() ) )
        {
            formViewPanel = new ImageSelectorFormViewPanel( getSession() );
        }
        else if ( contentTypeName.contains( TestAppContentType.DEFAULT_RELATION0_1.getName() ) ||
            contentTypeName.contains( TestAppContentType.DEFAULT_RELATION2_4.getName() ) ||
            contentTypeName.contains( TestAppContentType.CUSTOM_RELATION0_1.getName() ) )
        {
            formViewPanel = new RelationshipFormView( getSession() );
        }

        else if ( contentTypeName.contains( TestAppContentType.CUSTOM_SELECTOR0_2.getName() ) ||
            contentTypeName.contains( TestAppContentType.CUSTOM_SELECTOR1_1.getName() ) )
        {
            formViewPanel = new CustomSelectorFormViewPanel( getSession() );
        }

        else if ( contentTypeName.contains( "base:shortcut" ) )
        {
            formViewPanel = new ShortcutFormViewPanel( getSession() );
        }
        else if ( contentTypeName.contains( TestAppContentType.ARTICLE.getName() ) )
        {
            formViewPanel = new ArticleFormView( getSession() );
        }
        else if ( contentTypeName.contains( TestAppContentType.COUNTRY.getName() ) )
        {
            formViewPanel = new CountryFormView( getSession() );
        }
        else if ( contentTypeName.contains( TestAppContentType.CITY.getName() ) )
        {
            formViewPanel = new CityFormView( getSession() );
        }
        else if ( contentTypeName.contains( TestAppContentType.ALL_INPUTS.getName() ) )
        {
            formViewPanel = new InputsFormViewPanel( getSession() );
        }

        else if ( contentTypeName.contains( TestAppContentType.ITEM_SET.getName() ) )
        {
            formViewPanel = new ItemSetViewPanel( getSession() );
        }

        else
        {
            throw new TestFrameworkException( "ContentWizardStepForm: type() not implemented for " + contentTypeName );
        }
        formViewPanel.type( data );

        return this;
    }
}
