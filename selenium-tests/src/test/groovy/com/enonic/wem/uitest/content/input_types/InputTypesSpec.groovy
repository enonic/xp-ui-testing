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


    def "GIVEN existing 'Date' content WHEN content selected and 'Edit' button clicked THEN actual value and expected should be equals"()
    {
        given: "add a content with type 'Date'"
        Content dateContent = buildDate0_1_Content( TEST_DATE );
        selectSitePressNew( dateContent.getContentTypeName() ).typeData( dateContent ).save().closeBrowserTab().switchToBrowsePanelTab();


        when: "just created content selected and 'Edit' button clicked"
        findAndSelectContent( dateContent.getName() ).clickToolbarEditAndSwitchToWizardTab();
        DateFormViewPanel formViewPanel = new DateFormViewPanel( getSession() );

        then: "actual value and expected should be equals"
        formViewPanel.getDateValue().equals( TEST_DATE )
    }

    def "GIVEN existing 'DateTime' content WHEN content selected and 'Edit' button clicked THEN actual value and expected should be equals"()
    {
        given: "add a content with type 'Date Time'"
        Content dateTimeContent = buildDateTime0_1_Content( TEST_DATE_TIME );
        selectSitePressNew( dateTimeContent.getContentTypeName() ).typeData(
            dateTimeContent ).save().closeBrowserTab().switchToBrowsePanelTab();

        when: "just created content selected and 'Edit' button clicked"
        findAndSelectContent( dateTimeContent.getName() ).clickToolbarEditAndSwitchToWizardTab();
        DateTimeFormViewPanel dateTimeFormViewPanel = new DateTimeFormViewPanel( getSession() );

        then: "actual value and expected should be equals"
        dateTimeFormViewPanel.getDateTimeValue().equals( TEST_DATE_TIME );
    }

    def "GIVEN existing 'Time' content WHEN content selected and 'Edit' button clicked THEN actual and expected should be equals"()
    {
        given: "add a content with type 'Time'"
        Content timeContent = buildTime0_0_Content( TEST_TIME );
        selectSitePressNew( timeContent.getContentTypeName() ).typeData( timeContent ).save().closeBrowserTab().switchToBrowsePanelTab();

        when: "just created content selected and 'Edit' button clicked"
        findAndSelectContent( timeContent.getName() ).clickToolbarEditAndSwitchToWizardTab();
        TimeFormViewPanel timeFormViewPanel = new TimeFormViewPanel( getSession() );

        then: "actual and expected should be equals"
        timeFormViewPanel.getTimeValue().equals( TEST_TIME );
    }

    def "GIVEN existing 'checkbox' content WHEN content selected and 'Edit' button clicked THEN actual and expected should be equals"()
    {
        given: "add a content with type 'checkbox'"
        Content checkBoxContent = buildCheckBoxContent( true );
        selectSitePressNew( checkBoxContent.getContentTypeName() ).typeData(
            checkBoxContent ).save().closeBrowserTab().switchToBrowsePanelTab();

        when: "site expanded and just created content selected and 'Edit' button clicked"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( checkBoxContent )
        CheckBoxFormViewPanel checkBoxFormViewPanel = new CheckBoxFormViewPanel( getSession() );

        then: "actual and expected should be equals"
        checkBoxFormViewPanel.isChecked();
    }
}
