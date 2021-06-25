package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.SingleSelectorRadioFormView
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class Occurrences_RadioButtons_Spec
    extends Base_InputFields_Occurrences

{
    @Shared
    Content content_wit_opt;

    def "GIVEN new 'Single Selector Radio-content' is created (option was selected) WHEN the content is reopened THEN expected option should be selected"()
    {
        given: "new content with type 'Single Selector Radio' is saved"
        String option = "option A";
        content_wit_opt = buildRadioButtonsContent( option );
        selectSitePressNew( content_wit_opt.getContentTypeName() ).typeData(
            content_wit_opt ).save().closeBrowserTab().switchToBrowsePanelTab();

        when: "the content has been reopened"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( content_wit_opt );
        SingleSelectorRadioFormView formViewPanel = new SingleSelectorRadioFormView( getSession() );

        then:"expected option should be selected"
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

    def "WHEN wizard for 'Radio Buttons'-content is opened THEN radio buttons should be present in the page AND options should not be selected"()
    {
        when: "wizard for 'Radio Buttons'-content is opened:"
        String option = null;
        Content radioButtonsContent = buildRadioButtonsContent( option );
        selectSitePressNew( radioButtonsContent.getContentTypeName() );
        SingleSelectorRadioFormView formViewPanel = new SingleSelectorRadioFormView( getSession() );

        then: "radio buttons should be present in the page and options are not selected"
        formViewPanel.getSelectedOption().isEmpty();
    }

    def "GIVEN new content is saved (option was not selected) WHEN content has been reopened THEN options should not be selected in the form view"()
    {
        given: "new content with type 'Radio Buttons'"
        String option = null;
        Content radioButtonsContent = buildRadioButtonsContent( option );
        selectSitePressNew( radioButtonsContent.getContentTypeName() ).typeData(
            radioButtonsContent ).save().closeBrowserTab().switchToBrowsePanelTab();

        when: "content has been reopened"
        saveScreenshot( NameHelper.uniqueName( "radio-button" ) )
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( radioButtonsContent );
        SingleSelectorRadioFormView formViewPanel = new SingleSelectorRadioFormView( getSession() );

        then: "no one selected options is present in form view"
        formViewPanel.getSelectedOption().isEmpty();
    }

    def "GIVEN content with not required 'Radio Buttons' is created WHEN 'Publish' button has been pressed THEN the content gets 'Published'"()
    {
        given: "new 'Radio Buttons' content is added"
        String option = null;
        Content radioButtonsContent = buildRadioButtonsContent( option );
        ContentWizardPanel wizard = selectSitePressNew( radioButtonsContent.getContentTypeName() ).typeData( radioButtonsContent )
        wizard.clickOnMarkAsReadyAndDoPublish();
        String publishedMessage = contentBrowsePanel.waitPublishNotificationMessage( Application.EXPLICIT_NORMAL );
        wizard.close( radioButtonsContent.getDisplayName() );

        when: "content has been selected in the grid"
        filterPanel.typeSearchText( radioButtonsContent.getName() );

        then:
        contentBrowsePanel.getContentStatus( radioButtonsContent.getName() ) == ContentStatus.PUBLISHED.getValue();
        and:
        !contentBrowsePanel.isContentInvalid( radioButtonsContent.getName() );
        and: "expected notification message should appear"
        publishedMessage == String.format( Application.ITEM_IS_PUBLISHED_NOTIFICATION_MESSAGE, radioButtonsContent.getName() );
    }
}
