package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.ContentUtils
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.SingleSelectorDropDownFormView
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath
import com.enonic.xp.data.PropertyTree
import spock.lang.Shared

class Occurrences_SingleSelector_Dropdown_0_1_Spec
    extends Base_InputFields_Occurrences

{

    @Shared
    Content content_wit_opt;


    def "WHEN wizard for adding a 'Single Selector DropDown-content 0:1' opened THEN option filter input is present "()
    {
        when: "start to add a content with type 'Single Selector DropDown 0:1'"
        String option = null;
        Content dropDownContent = buildSingleSelectorDropDown0_1_Content( option );
        selectSiteOpenWizard( dropDownContent.getContentTypeName() );
        SingleSelectorDropDownFormView formViewPanel = new SingleSelectorDropDownFormView( getSession() );

        then: "option filter input is present and enabled"
        formViewPanel.isOptionFilterInputDisplayed();
    }

    def "GIVEN saving of not required 'Single Selector DropDown -content' without options WHEN content opened for edit THEN no one selected options present in form view"()
    {
        given: "new content with type 'Single Selector DropDown'"
        String option = null;
        Content dropDownContent = buildSingleSelectorDropDown0_1_Content( option );
        selectSiteOpenWizard( dropDownContent.getContentTypeName() ).typeData( dropDownContent ).save().close(
            dropDownContent.getDisplayName() );

        when: "content opened for edit"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( dropDownContent );
        SingleSelectorDropDownFormView formViewPanel = new SingleSelectorDropDownFormView( getSession() );

        then:
        formViewPanel.getSelectedOption().isEmpty();

    }

    def "GIVEN saving of not required 'Single Selector DropDown content' without options WHEN 'Publish' button pressed THEN content with 'Online' status listed"()
    {
        given: "new content with type 'Single Selector DropDown'"
        String option = null;
        Content dropDownContent = buildSingleSelectorDropDown0_1_Content( option );
        selectSiteOpenWizard( dropDownContent.getContentTypeName() ).typeData( dropDownContent ).save().clickOnPublishButton().close(
            dropDownContent.getDisplayName() );

        when: "content opened for edit"
        filterPanel.typeSearchText( dropDownContent.getName() );

        then:
        contentBrowsePanel.getContentStatus( dropDownContent.getName() ).equals( ContentStatus.ONLINE.getValue() );
        and:
        !contentBrowsePanel.isContentInvalid( dropDownContent.getName().toString() );
    }

    def "GIVEN saving of 'Single Selector DropDown -content' with  selected option WHEN content opened for edit THEN correct selected option and button 'Remove' present in form view"()
    {
        given: "new content with type 'Single Selector ComboBox'"
        String option = "option A";
        content_wit_opt = buildSingleSelectorDropDown0_1_Content( option );
        filterPanel.typeSearchText( SITE_NAME );
        selectSiteOpenWizard( content_wit_opt.getContentTypeName() ).typeData( content_wit_opt ).save().close(
            content_wit_opt.getDisplayName() );

        when: "content opened for edit"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( content_wit_opt );
        SingleSelectorDropDownFormView formViewPanel = new SingleSelectorDropDownFormView( getSession() );

        then:
        formViewPanel.getSelectedOption() == option;
        and: "options filter not displayed"
        !formViewPanel.isOptionFilterInputDisplayed();
    }

    def "GIVEN a not required 'Single Selector DropDown -content' with selected option WHEN content opened and option changed THEN new option displayed"()
    {
        given: "a content with type Single Selector ComboBox' opened for edit"
        String newOption = "option B";
        PropertyTree newData = ContentUtils.buildSingleSelectionData( newOption );
        ContentWizardPanel contentWizardPanel = contentBrowsePanel.selectAndOpenContentFromToolbarMenu( content_wit_opt );


        when: "new option selected"
        SingleSelectorDropDownFormView formViewPanel = new SingleSelectorDropDownFormView( getSession() );
        formViewPanel.clickOnChangeOptionButton().type( newData );
        contentWizardPanel.save().close( content_wit_opt.getDisplayName() );
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( content_wit_opt );

        then: "new selected option displayed"
        formViewPanel.getSelectedOption() == newOption;

    }

    private Content buildSingleSelectorDropDown0_1_Content( String option )
    {
        PropertyTree data = ContentUtils.buildSingleSelectionData( option );
        Content dropDownContent = Content.builder().
            name( NameHelper.uniqueName( "ss_dropdown0_1_" ) ).
            displayName( "ss_dropdown0_1 content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_MODULE_NAME + ":ss_dropdown0_1" ).data( data ).
            build();
        return dropDownContent;
    }
}

