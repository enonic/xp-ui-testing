package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.ContentUtils
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.SingleSelectorRadioFormView
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath
import com.enonic.xp.data.PropertyTree
import spock.lang.Ignore
import spock.lang.Shared

class Occurrences_SingleSelector_Radio_1_1_Spec
    extends Base_InputFields_Occurrences

{

    @Shared
    Content content_wit_opt;


    def "WHEN wizard for adding a 'Single Selector Radio-content' opened THEN radio buttons present on page and no any options selected"()
    {
        when: "start to add a content with type 'Single Selector Radio 1:1'"
        String option = null;
        Content radioContent = buildSingleSelectorRadio1_1_Content( option );
        contentBrowsePanel.clickCheckboxAndSelectRow( SITE_NAME ).clickToolbarNew().selectContentType( radioContent.getContentTypeName() )
        SingleSelectorRadioFormView formViewPanel = new SingleSelectorRadioFormView( getSession() );

        then: "radio buttons present on page and no any options selected"
        formViewPanel.getSelectedOption().isEmpty();
    }


    def "GIVEN saving of not required 'Single Selector Radio-content' without selected option WHEN content opened for edit THEN no one selected options present in form view"()
    {
        given: "new content with type 'Single Selector Radio'"
        String option = null;
        Content radioContent = buildSingleSelectorRadio1_1_Content( option );
        contentBrowsePanel.clickCheckboxAndSelectRow( SITE_NAME ).clickToolbarNew().selectContentType(
            radioContent.getContentTypeName() ).typeData( radioContent ).save().close( radioContent.getDisplayName() );

        when: "content opened for edit"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( radioContent );
        SingleSelectorRadioFormView formViewPanel = new SingleSelectorRadioFormView( getSession() );

        then:
        formViewPanel.getSelectedOption().isEmpty();
    }

    def "WHEN saving of required 'Single Selector Radio content' without selected option  THEN new invalid content listed"()
    {
        given: "new content with type 'Single Selector Radio'"
        String option = null;
        Content radioContent = buildSingleSelectorRadio1_1_Content( option );
        contentBrowsePanel.clickCheckboxAndSelectRow( SITE_NAME ).clickToolbarNew().selectContentType(
            radioContent.getContentTypeName() ).typeData( radioContent ).save().close( radioContent.getDisplayName() );

        when: "content opened for edit"
        filterPanel.typeSearchText( radioContent.getName() );

        then:
        contentBrowsePanel.isInvalidContent( radioContent.getPath().toString() );

    }

    def "GIVEN saving of not required 'Single Selector Radio-content' with  selected option WHEN content opened for edit THEN correct selected option  present in form view"()
    {
        given: "new content with type 'Single Selector Radio'"
        String option = "option A";
        content_wit_opt = buildSingleSelectorRadio1_1_Content( option );
        contentBrowsePanel.clickCheckboxAndSelectRow( SITE_NAME ).clickToolbarNew().selectContentType(
            content_wit_opt.getContentTypeName() ).typeData( content_wit_opt ).save().close( content_wit_opt.getDisplayName() );

        when: "content opened for edit"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( content_wit_opt );
        SingleSelectorRadioFormView formViewPanel = new SingleSelectorRadioFormView( getSession() );

        then:
        formViewPanel.getSelectedOption() == option;
    }

    def "GIVEN a not required 'Single Selector Radio-content' with selected option WHEN content opened and option changed THEN new option displayed"()
    {
        given: "a content with type 'Single Selector Radio' opened for edit"
        String newOption = "option B";
        ContentWizardPanel contentWizardPanel = contentBrowsePanel.selectAndOpenContentFromToolbarMenu( content_wit_opt );


        when: "new option selected"
        SingleSelectorRadioFormView formViewPanel = new SingleSelectorRadioFormView( getSession() );
        formViewPanel.selectOption( newOption );
        contentWizardPanel.save().close( content_wit_opt.getDisplayName() );
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( content_wit_opt );

        then: "new selected option displayed"
        formViewPanel.getSelectedOption() == newOption;

    }
    //TODO remove it when sources will be updated on teamcity
    @Ignore
    def "GIVEN creating new Single Selector Radio-content (1:1) on root WHEN required text input is empty and button 'Publish' pressed THEN validation message appears"()
    {
        given: "start to add a content with type 'single selector Radio-content (1:1)'"
        Content content = buildSingleSelectorRadio1_1_Content( null )
        ContentWizardPanel contentWizardPanel = contentBrowsePanel.clickCheckboxAndSelectRow(
            SITE_NAME ).clickToolbarNew().selectContentType( content.getContentTypeName() );

        when:
        contentWizardPanel.clickOnPublishButton();
        TestUtils.saveScreenshot( getSession(), "ss_radio1_1_publish" )
        SingleSelectorRadioFormView formViewPanel = new SingleSelectorRadioFormView( getSession() );

        then: "new content listed in the grid and can be opened for edit"
        formViewPanel.isValidationMessagePresent();
        and:
        formViewPanel.getValidationMessage() == SingleSelectorRadioFormView.VALIDATION_MESSAGE_1_1;
    }


    private Content buildSingleSelectorRadio1_1_Content( String option )
    {
        PropertyTree data = ContentUtils.buildSingleSelectionData( option );
        Content textLineContent = Content.builder().
            name( NameHelper.uniqueName( "ss_radio1_1_" ) ).
            displayName( "ss_radio1_1 content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_MODULE_NAME + ":ss_radio1_1" ).data( data ).
            build();
        return textLineContent;
    }
}