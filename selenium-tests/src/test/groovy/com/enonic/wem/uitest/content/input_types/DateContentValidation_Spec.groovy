package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.DateFormViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared

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


    def "GIVEN new wizard for Date content is opened WHEN date with wrong format has been typed THEN input's border becomes red"()
    {
        given: "date wizard is opened"
        Content dateContent = buildDate0_1_Content( BAD_FORMAT_DATE );
        ContentWizardPanel contentWizardPanel = selectSitePressNew( dateContent.getContentTypeName() );

        when: "date with wrong format typed"
        contentWizardPanel.typeData( dateContent );
        DateFormViewPanel dateFormViewPanel = new DateFormViewPanel( getSession() );
        saveScreenshot( "wrong_date_format" )

        then: "red border appears"
        dateFormViewPanel.isInvalidDate();
    }

    def "WHEN date with wrong day of month has been typed THEN input's border becomes red "()
    {
        given: "date wizard is opened"
        Content dateContent = buildDate0_1_Content( WRONG_DAY_DATE );
        ContentWizardPanel contentWizardPanel = selectSitePressNew( dateContent.getContentTypeName() );

        when: "wrong day of month typed"
        contentWizardPanel.typeData( dateContent );
        DateFormViewPanel dateFormViewPanel = new DateFormViewPanel( getSession() );
        saveScreenshot( "date_wrong_day" );

        then: "red border appears"
        dateFormViewPanel.isInvalidDate();
    }

    def "WHEN date with wrong month has been typed THEN input's border becomes red "()
    {
        given: "date wizard is opened"
        Content dateContent = buildDate0_1_Content( WRONG_MONTH_DATE );
        ContentWizardPanel contentWizardPanel = selectSitePressNew( dateContent.getContentTypeName() );

        when: "wrong month has been typed"
        contentWizardPanel.typeData( dateContent );
        DateFormViewPanel dateFormViewPanel = new DateFormViewPanel( getSession() );
        saveScreenshot( "date_wrong_month" );

        then: "red border appears"
        dateFormViewPanel.isInvalidDate();
    }

    def "WHEN data with correct format typed THEN input has a green border"()
    {
        given: "date wizard is opened"
        Content dateContent = buildDate0_1_Content( CORRECT_DATE );
        ContentWizardPanel contentWizardPanel = selectSitePressNew( dateContent.getContentTypeName() );

        when: "correct date has been typed"
        contentWizardPanel.typeData( dateContent );
        DateFormViewPanel dateFormViewPanel = new DateFormViewPanel( getSession() );

        then: "input has a green border"
        !dateFormViewPanel.isInvalidDate();
    }
}
