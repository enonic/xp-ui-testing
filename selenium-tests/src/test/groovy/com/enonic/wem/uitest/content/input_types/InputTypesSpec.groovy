package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.form.CheckBoxFormViewPanel
import com.enonic.autotests.pages.form.DateFormViewPanel
import com.enonic.autotests.pages.form.DateTimeFormViewPanel
import com.enonic.autotests.pages.form.TimeFormViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class InputTypesSpec
    extends Base_InputFields_Occurrences
{
    @Shared
    String TEST_DATE = "2015-02-28";

    @Shared
    String TEST_TIME = "19:01";

    @Shared
    String TEST_DATE_TIME = "2015-02-28 19:01";


    def "GIVEN content type with name 'Date' selected and wizard opened WHEN date typed and content saved THEN new content with correct date listed "()
    {
        given: "add a content with type 'Date'"
        Content dateContent = buildDate0_1_Content( TEST_DATE );
        selectSitePressNew( dateContent.getContentTypeName() ).waitUntilWizardOpened().typeData( dateContent ).save().close(
            dateContent.getDisplayName() );


        when: "site expanded and just created content selected and 'Edit' button clicked"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( dateContent );
        DateFormViewPanel formViewPanel = new DateFormViewPanel( getSession() );

        then: "actual value in the form view and expected should be equals"
        formViewPanel.getDateValue().equals( TEST_DATE )
    }

    def "GIVEN content type with name 'DateTime' selected and wizard opened WHEN date typed and content saved THEN new content with correct date listed "()
    {
        given: "add a content with type 'Date Time'"
        Content dateTimeContent = buildDateTime0_1_Content( TEST_DATE_TIME );
        selectSitePressNew( dateTimeContent.getContentTypeName() ).waitUntilWizardOpened().typeData( dateTimeContent ).save().close(
            dateTimeContent.getDisplayName() );

        when: "site expanded and just created content selected and 'Edit' button clicked"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( dateTimeContent )
        DateTimeFormViewPanel dateTimeFormViewPanel = new DateTimeFormViewPanel( getSession() );

        then: "actual value in the form view and expected should be equals"
        dateTimeFormViewPanel.getDateTimeValue().equals( TEST_DATE_TIME );
    }

    def "GIVEN content type with name 'Time' selected and wizard opened WHEN time typed and content saved THEN new content with correct date listed "()
    {
        given: "add a content with type 'Time'"
        Content timeContent = buildTime0_0_Content( TEST_TIME );
        selectSitePressNew( timeContent.getContentTypeName() ).waitUntilWizardOpened().typeData( timeContent ).save().close(
            timeContent.getDisplayName() ); ;

        when: "site expanded and just created content selected and 'Edit' button clicked"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( timeContent );
        TimeFormViewPanel timeFormViewPanel = new TimeFormViewPanel( getSession() );

        then: "actual value in the form view and expected should be equals"
        timeFormViewPanel.getTimeValue().equals( TEST_TIME );
    }

    def "GIVEN content type with name 'checkbox' selected and wizard opened WHEN  the checkbox selected and content saved THEN new content with correct boolean value listed "()
    {
        given: "add a content with type 'checkbox'"
        Content checkBoxContent = buildCheckBoxContent( true );
        selectSitePressNew( checkBoxContent.getContentTypeName() ).waitUntilWizardOpened().typeData( checkBoxContent ).save().close(
            checkBoxContent.getDisplayName() );

        when: "site expanded and just created content selected and 'Edit' button clicked"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( checkBoxContent )
        CheckBoxFormViewPanel checkBoxFormViewPanel = new CheckBoxFormViewPanel( getSession() );

        then: "actual value in the form view and expected should be equals"
        checkBoxFormViewPanel.isChecked();
    }
}
