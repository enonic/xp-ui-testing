package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.SingleSelectorRadioFormView
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared

class Occurrences_RadioButtons_Spec
    extends Base_InputFields_Occurrences

{

    @Shared
    Content content_wit_opt;


    def "WHEN wizard for  'Radio Buttons'-content is opened THEN radio buttons should be present on the page AND options should not be selected"()
    {
        when: "start to add a content with type 'Radio Buttons'"
        String option = null;
        Content radioButtonsContent = buildRadioButtonsContent( option );
        selectSitePressNew( radioButtonsContent.getContentTypeName() );
        SingleSelectorRadioFormView formViewPanel = new SingleSelectorRadioFormView( getSession() );

        then: " radio buttons are present on page and options are not selected"
        formViewPanel.getSelectedOption().isEmpty();
    }

    def "GIVEN saving of content without selected option WHEN content opened for edit THEN no one selected options is present in form view"()
    {
        given: "new content with type 'Radio Buttons'"
        String option = null;
        Content radioButtonsContent = buildRadioButtonsContent( option );
        selectSitePressNew( radioButtonsContent.getContentTypeName() ).typeData(
            radioButtonsContent ).save().closeBrowserTab().switchToBrowsePanelTab();

        when: "content opened for edit"
        saveScreenshot( NameHelper.uniqueName( "radio-button" ) )
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( radioButtonsContent );
        SingleSelectorRadioFormView formViewPanel = new SingleSelectorRadioFormView( getSession() );

        then: "no one selected options is present in form view"
        formViewPanel.getSelectedOption().isEmpty();
    }

    def "GIVEN saving of not required 'Radio Buttons content' without selected option WHEN 'Publish' button pressed THEN content with 'Online' status listed"()
    {
        given: "new content with type 'Radio Buttons'"
        String option = null;
        Content radioButtonsContent = buildRadioButtonsContent( option );
        ContentWizardPanel wizard = selectSitePressNew( radioButtonsContent.getContentTypeName() ).typeData( radioButtonsContent )
        wizard.save().clickOnWizardPublishButton().clickOnPublishNowButton();
        String publishedMessage = contentBrowsePanel.waitPublishNotificationMessage( Application.EXPLICIT_NORMAL );
        wizard.close( radioButtonsContent.getDisplayName() );

        when: "content opened for edit"
        filterPanel.typeSearchText( radioButtonsContent.getName() );

        then:
        contentBrowsePanel.getContentStatus( radioButtonsContent.getName() ).equals( ContentStatus.PUBLISHED.getValue() );
        and:
        !contentBrowsePanel.isContentInvalid( radioButtonsContent.getName() );
        and: "correct publish message was present"
        publishedMessage == String.format( Application.ONE_CONTENT_PUBLISHED_NOTIFICATION_MESSAGE_TMP,  radioButtonsContent.getName() );
    }

    def "GIVEN saving of not required 'Single Selector Radio-content' with  selected option WHEN content is opened THEN correct selected option  present in form view"()
    {
        given: "new content with type 'Single Selector Radio' is saved"
        String option = "option A";
        content_wit_opt = buildRadioButtonsContent( option );
        selectSitePressNew( content_wit_opt.getContentTypeName() ).typeData(
            content_wit_opt ).save().closeBrowserTab().switchToBrowsePanelTab();

        when: "the content is opened"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( content_wit_opt );
        SingleSelectorRadioFormView formViewPanel = new SingleSelectorRadioFormView( getSession() );

        then:
        formViewPanel.getSelectedOption() == option;
    }

    def "GIVEN a not required 'Single Selector Radio-content' with selected option WHEN content opened and option changed THEN new option should be selected"()
    {
        given: "content with type 'Single Selector Radio' is opened"
        String newOption = "option B";
        ContentWizardPanel contentWizardPanel = contentBrowsePanel.selectAndOpenContentFromToolbarMenu( content_wit_opt );

        when: "new option has been selected"
        SingleSelectorRadioFormView formViewPanel = new SingleSelectorRadioFormView( getSession() );
        formViewPanel.selectOption( newOption );
        contentWizardPanel.save().closeBrowserTab().switchToBrowsePanelTab();
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( content_wit_opt );

        then: "new selected option should be displayed"
        formViewPanel.getSelectedOption() == newOption;
    }
}