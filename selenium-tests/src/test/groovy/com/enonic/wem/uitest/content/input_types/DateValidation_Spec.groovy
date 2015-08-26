package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.DateTimeFormViewPanel
import com.enonic.autotests.pages.form.TimeFormViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared

class DateValidation_Spec
    extends Base_InputFields_Occurrences
{

    @Shared
    String WRONG_TIME = "191:01";

    @Shared
    String TEST_TIME = "16:10";

    @Shared
    String TEST_DATE_TIME1 = "2015-02-28 19:01";

    @Shared
    String WRONG_DATE_TIME = "015-02-28 19:01";

    def "GIVEN saving of content with type 'Time 0:0' WHEN value of time is wrong THEN 'Publish' button disabled"()
    {
        given: "start to add a content with type 'Time 0:0'"
        Content timeContent = buildTime0_0_Content( WRONG_TIME );
        ContentWizardPanel wizard = selectSiteOpenWizard( timeContent.getContentTypeName() );
        TimeFormViewPanel formViewPanel = new TimeFormViewPanel( getSession() );

        when: "time with wrong format typed "
        wizard.typeData( timeContent );

        then: "'Publish' button disabled"
        !wizard.isPublishButtonEnabled();

        and: "time input is red"
        formViewPanel.isTimeInvalid();
    }

    def "GIVEN saving of content with type 'DateTime 1:1' and value of datetime is wrong WHEN datetime typed and content published THEN warning message appears"()
    {
        given: "add a content with type 'DateTime 1:1'"
        Content dateTimeContent = buildDateTime1_1_Content( WRONG_DATE_TIME );
        ContentWizardPanel wizard = selectSiteOpenWizard( dateTimeContent.getContentTypeName() );
        DateTimeFormViewPanel formViewPanel = new DateTimeFormViewPanel( getSession() );

        when: "date time with wrong format typed "
        wizard.typeData( dateTimeContent );

        then: "'Publish' button disabled"
        !wizard.isPublishButtonEnabled();

        and: "time input is red"
        formViewPanel.isDateTimeInvalid();
    }

    def "GIVEN wizard for adding a required DateTime(1:1) opened WHEN name typed and dateTime not typed AND 'Publish' button pressed THEN validation message and warning message appears"()
    {
        given: "start to add a content with type 'DateTime(1:1)'"
        Content dateTimeContent = buildDateTime1_1_Content( TEST_DATE_TIME1 );
        ContentWizardPanel wizard = selectSiteOpenWizard( dateTimeContent.getContentTypeName() );

        when: "only the name typed and dateTime not typed and 'Publish' button clicked"
        wizard.typeDisplayName( dateTimeContent.getDisplayName() );

        then: "'Publish' button disabled"
        !wizard.isPublishButtonEnabled();
    }

    def "GIVEN wizard for adding a required DateTime(2:4) opened WHEN name typed and dateTime not typed AND 'Publish' button pressed THEN validation message and warning message appears"()
    {
        given: "start to add a content with type 'DateTime(2:4)'"
        Content dateTimeContent = buildDateTime2_4_Content( null );
        ContentWizardPanel wizard = selectSiteOpenWizard( dateTimeContent.getContentTypeName() );

        when: "only the 'name' typed and datetime not typed"
        wizard.typeDisplayName( dateTimeContent.getDisplayName() );

        then: "'Publish' button disabled"
        !wizard.isPublishButtonEnabled();
    }

    def "GIVEN wizard for adding a required Time(2:4) opened WHEN name typed and time not typed AND 'Publish' button pressed THEN validation message and warning message appears"()
    {
        given: "start to add a content with type 'Time(2:4)'"
        Content timeContent = buildTime2_4_Content( null );
        ContentWizardPanel wizard = selectSiteOpenWizard( timeContent.getContentTypeName() );

        when: "only the 'name' typed and time not typed"
        wizard.typeDisplayName( timeContent.getDisplayName() );

        then: "'Publish' button disabled"
        !wizard.isPublishButtonEnabled();
    }
}
