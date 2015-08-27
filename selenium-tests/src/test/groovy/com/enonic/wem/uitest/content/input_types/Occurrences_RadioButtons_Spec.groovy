package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.SingleSelectorRadioFormView
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared

class Occurrences_RadioButtons_Spec
    extends Base_InputFields_Occurrences

{

    @Shared
    Content content_wit_opt;


    def "WHEN wizard for adding a 'Radio Buttons' opened THEN radio buttons present on page and no any options selected"()
    {
        when: "start to add a content with type 'Radio Buttons'"
        String option = null;
        Content radioButtonsContent = buildRadioButtonsContent( option );
        selectSiteOpenWizard( radioButtonsContent.getContentTypeName() );
        SingleSelectorRadioFormView formViewPanel = new SingleSelectorRadioFormView( getSession() );

        then: "radio buttons present on page and no any options selected"
        formViewPanel.getSelectedOption().isEmpty();
    }

    def "GIVEN saving of not required 'Radio Buttons' without selected option WHEN content opened for edit THEN no one selected options present in form view"()
    {
        given: "new content with type 'Radio Buttons'"
        String option = null;
        Content radioButtonsContent = buildRadioButtonsContent( option );
        selectSiteOpenWizard( radioButtonsContent.getContentTypeName() ).typeData( radioButtonsContent ).save().close(
            radioButtonsContent.getDisplayName() );

        when: "content opened for edit"
        TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "close-radio" ) )
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( radioButtonsContent );
        SingleSelectorRadioFormView formViewPanel = new SingleSelectorRadioFormView( getSession() );

        then:
        formViewPanel.getSelectedOption().isEmpty();
    }

    def "GIVEN saving of not required 'Radio Buttons content' without selected option WHEN 'Publish' button pressed THEN content with 'Online' status listed"()
    {
        given: "new content with type 'Radio Buttons'"
        String option = null;
        Content radioButtonsContent = buildRadioButtonsContent( option );
        ContentWizardPanel wizard = selectSiteOpenWizard( radioButtonsContent.getContentTypeName() ).typeData( radioButtonsContent )
        String publishedMessage = wizard.save().clickOnWizardPublishButton().clickOnPublishNowButton().waitPublishNotificationMessage(
            Application.EXPLICIT_NORMAL );
        wizard.close( radioButtonsContent.getDisplayName() );

        when: "content opened for edit"
        filterPanel.typeSearchText( radioButtonsContent.getName() );

        then:
        contentBrowsePanel.getContentStatus( radioButtonsContent.getName() ).equals( ContentStatus.ONLINE.getValue() );
        and:
        !contentBrowsePanel.isContentInvalid( radioButtonsContent.getName() );
        and: "correct publish message was present"
        publishedMessage == String.format( Application.CONTENT_PUBLISHED_NOTIFICATION_MESSAGE, radioButtonsContent.getDisplayName() );
    }

    def "GIVEN saving of not required 'Single Selector Radio-content' with  selected option WHEN content opened for edit THEN correct selected option  present in form view"()
    {
        given: "new content with type 'Single Selector Radio'"
        String option = "option A";
        content_wit_opt = buildRadioButtonsContent( option );
        selectSiteOpenWizard( content_wit_opt.getContentTypeName() ).typeData( content_wit_opt ).save().close(
            content_wit_opt.getDisplayName() );

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
}