package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.DateFormViewPanel
import com.enonic.autotests.vo.contentmanager.Content
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


    def "WHEN date with wrong format typed THEN input's border becomes red "()
    {
        given: "date with wrong format"
        Content dateContent = buildDate0_1_Content( BAD_FORMAT_DATE );
        ContentWizardPanel contentWizardPanel = selectSiteOpenWizard( dateContent.getContentTypeName() );

        when: "date typed"
        contentWizardPanel.typeData( dateContent );
        DateFormViewPanel dateFormViewPanel = new DateFormViewPanel( getSession() );

        then: "red border appears"
        dateFormViewPanel.isInvalidDate();
    }

    def "WHEN data with wrong day of month typed THEN input's border becomes red "()
    {
        given: "date with wrong format"
        Content dateContent = buildDate0_1_Content( WRONG_DAY_DATE );
        ContentWizardPanel contentWizardPanel = selectSiteOpenWizard( dateContent.getContentTypeName() );

        when: "date typed"
        contentWizardPanel.typeData( dateContent );
        DateFormViewPanel dateFormViewPanel = new DateFormViewPanel( getSession() );

        then: "red border appears"
        dateFormViewPanel.isInvalidDate();
    }

    def "WHEN data with wrong month typed THEN input's border becomes red "()
    {
        given: "date with wrong format"
        Content dateContent = buildDate0_1_Content( WRONG_MONTH_DATE );
        ContentWizardPanel contentWizardPanel = selectSiteOpenWizard( dateContent.getContentTypeName() );

        when: "date typed"
        contentWizardPanel.typeData( dateContent );
        DateFormViewPanel dateFormViewPanel = new DateFormViewPanel( getSession() );

        then: "red border appears"
        dateFormViewPanel.isInvalidDate();
    }

    def "WHEN data with correct format typed THEN input has a green border"()
    {
        given: "date with correct date format"
        Content dateContent = buildDate0_1_Content( CORRECT_DATE );
        ContentWizardPanel contentWizardPanel = selectSiteOpenWizard( dateContent.getContentTypeName() );

        when: "date typed"
        contentWizardPanel.typeData( dateContent );
        DateFormViewPanel dateFormViewPanel = new DateFormViewPanel( getSession() );

        then: "red border appears"
        !dateFormViewPanel.isInvalidDate();
    }
}
