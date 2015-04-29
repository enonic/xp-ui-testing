package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.DateFormViewPanel
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath
import com.enonic.xp.data.PropertyTree
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class DateContentValidation_Spec
    extends Base_InputFields_Occurrences

{

    @Shared
    String CORRECT_DATE = "2015-02-28";

    @Shared
    String BAD_FORMAT_DATE = "02-28-2015";

    @Shared
    String WRONG_MONTH_DATE = "2015-15-28";

    @Shared
    String WRONG_DAY_DATE = "2015-15-32";


    def "WHEN data with wrong format typed THEN input's border becomes red "()
    {
        given: "date with wrong format"
        Content dateContent = buildDateContent( BAD_FORMAT_DATE );
        ContentWizardPanel contentWizardPanel = contentBrowsePanel.clickCheckboxAndSelectRow(
            SITE_NAME ).clickToolbarNew().selectContentType( dateContent.getContentTypeName() );


        when: "date typed"
        contentWizardPanel.typeData( dateContent );
        DateFormViewPanel dateFormViewPanel = new DateFormViewPanel( getSession() );

        then: "red border appears"
        dateFormViewPanel.isInvalidDate();
    }

    def "WHEN data with wrong day of month typed THEN input's border becomes red "()
    {
        given: "date with wrong format"
        Content dateContent = buildDateContent( WRONG_DAY_DATE );
        ContentWizardPanel contentWizardPanel = contentBrowsePanel.clickCheckboxAndSelectRow(
            SITE_NAME ).clickToolbarNew().selectContentType( dateContent.getContentTypeName() );


        when: "date typed"
        contentWizardPanel.typeData( dateContent );
        DateFormViewPanel dateFormViewPanel = new DateFormViewPanel( getSession() );

        then: "red border appears"
        dateFormViewPanel.isInvalidDate();
    }

    def "WHEN data with wrong month typed THEN input's border becomes red "()
    {
        given: "date with wrong format"
        Content dateContent = buildDateContent( WRONG_MONTH_DATE );
        ContentWizardPanel contentWizardPanel = contentBrowsePanel.clickCheckboxAndSelectRow(
            SITE_NAME ).clickToolbarNew().selectContentType( dateContent.getContentTypeName() );


        when: "date typed"
        contentWizardPanel.typeData( dateContent );
        DateFormViewPanel dateFormViewPanel = new DateFormViewPanel( getSession() );

        then: "red border appears"
        dateFormViewPanel.isInvalidDate();
    }

    def "WHEN data with correct format typed THEN input has a green border"()
    {
        given: "date with correct date format"
        Content dateContent = buildDateContent( CORRECT_DATE );
        ContentWizardPanel contentWizardPanel = contentBrowsePanel.clickCheckboxAndSelectRow(
            SITE_NAME ).clickToolbarNew().selectContentType( dateContent.getContentTypeName() );


        when: "date typed"
        contentWizardPanel.typeData( dateContent );
        DateFormViewPanel dateFormViewPanel = new DateFormViewPanel( getSession() );

        then: "red border appears"
        !dateFormViewPanel.isInvalidDate();
    }

    private Content buildDateContent( String date )
    {
        String name = "date";

        PropertyTree data = new PropertyTree();
        data.addStrings( DateFormViewPanel.DATE_PROPERTY, date );


        Content dateContent = Content.builder().
            name( NameHelper.uniqueName( name ) ).
            displayName( "date content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_MODULE_NAME + ":date" ).data( data ).
            build();
        return dateContent;
    }
}
