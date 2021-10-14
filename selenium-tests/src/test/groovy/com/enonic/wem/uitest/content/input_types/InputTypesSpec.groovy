package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.form.CheckBoxFormViewPanel
import com.enonic.autotests.pages.form.DateFormViewPanel
import com.enonic.autotests.pages.form.DateTimeFormViewPanel
import com.enonic.autotests.pages.form.TimeFormViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
@Ignore
class InputTypesSpec
    extends Base_InputFields_Occurrences
{
    @Shared
    String TEST_DATE = "2015-02-28";

    @Shared
    String TEST_TIME = "19:01";

    @Shared
    String TEST_DATE_TIME = "2015-02-28 19:01";

    def "GIVEN 'Date' content is added WHEN the content is reopened THEN expected date should be displayed"()
    {
        given: "add a content with type 'Date'"
        Content dateContent = buildDate0_1_Content( TEST_DATE );
        selectSitePressNew( dateContent.getContentTypeName() ).typeData( dateContent ).save().closeBrowserTab().switchToBrowsePanelTab();
        contentBrowsePanel.doClearSelection();


        when: "the content is opened"
        findAndSelectContent( dateContent.getName() ).clickToolbarEditAndSwitchToWizardTab();
        DateFormViewPanel formViewPanel = new DateFormViewPanel( getSession() );

        then: "actual 'Date' and expected should be equals"
        formViewPanel.getDateValue().equals( TEST_DATE )
    }

    def "GIVEN 'DateTime' content was added WHEN the content is opened THEN actual 'Date Time' and expected should be equals"()
    {
        given: "'DateTime' content was added"
        Content dateTimeContent = buildDateTime0_1_Content( TEST_DATE_TIME );
        selectSitePressNew( dateTimeContent.getContentTypeName() ).typeData(
            dateTimeContent ).save().closeBrowserTab().switchToBrowsePanelTab();
        contentBrowsePanel.doClearSelection();

        when: "the content is opened"
        findAndSelectContent( dateTimeContent.getName() ).clickToolbarEditAndSwitchToWizardTab();
        DateTimeFormViewPanel dateTimeFormViewPanel = new DateTimeFormViewPanel( getSession() );

        then: "actual 'Date Time' and expected should be equals"
        dateTimeFormViewPanel.getDateTimeValue()== TEST_DATE_TIME ;
    }

    def "GIVEN 'Time' content was added WHEN the content is opened THEN actual and expected time should be equals"()
    {
        given: "'Time' content was added"
        Content timeContent = buildTime0_0_Content( TEST_TIME );
        selectSitePressNew( timeContent.getContentTypeName() ).typeData( timeContent ).save().closeBrowserTab().switchToBrowsePanelTab();
        contentBrowsePanel.doClearSelection();

        when: "the content is opened"
        findAndSelectContent( timeContent.getName() ).clickToolbarEditAndSwitchToWizardTab();
        TimeFormViewPanel timeFormViewPanel = new TimeFormViewPanel( getSession() );

        then: "actual and expected time should be equals"
        timeFormViewPanel.getTimeValue()== TEST_TIME ;
    }

    def "GIVEN 'checkbox' content with 'true' value is added WHEN the content is reopened THEN checkbox should be checked"()
    {
        given: "'checkbox' content with 'true' value was added"
        Content checkBoxContent = buildCheckBoxContent( true );
        selectSitePressNew( checkBoxContent.getContentTypeName() ).typeData(
            checkBoxContent ).save().closeBrowserTab().switchToBrowsePanelTab();
        contentBrowsePanel.doClearSelection();

        when: "the content is opened"
        findAndSelectContent( checkBoxContent.getName() ).clickToolbarEdit(  );
        CheckBoxFormViewPanel checkBoxFormViewPanel = new CheckBoxFormViewPanel( getSession() );

        then: "checkbox should be checked"
        checkBoxFormViewPanel.isChecked();
    }
}
