package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.optionset.OptionSetFormView
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath
import spock.lang.Shared
import spock.lang.Stepwise

/**
 * Created  on 5/12/2017.
 *
 * Tasks:
 * xp-ui-testing#7 Add Selenium tests for OptionSet
 * */
@Stepwise
class OptionSet_Spec
    extends Base_InputFields_Occurrences
{
    @Shared
    Content OPTION_SET;

    def "GIVEN wizard for OptionSet content is opened WHEN 'radio1' button in the 'single selection' clicked AND 'Save' button pressed THEN red circle should be present on the wizard page"()
    {
        given: "'HtmlArea content has been added and 'div' is wrapper element"
        OPTION_SET = build_OptionSet_Content();
        ContentWizardPanel wizard = selectSitePressNew( OPTION_SET.getContentTypeName() );
        OptionSetFormView optionSetFormView = new OptionSetFormView( getSession() );
        wizard.typeDisplayName( OPTION_SET.getName() );


        when: "first radio button in the 'single selection' is clicked"
        optionSetFormView.getSingleSelectionOptionSet().clickOnFirstRadio();

        and: "'Save' button has been pressed"
        wizard.save();
        saveScreenshot( "opt_set_radio1_empty" );

        then: "red circle should be present on the wizard page, because required inputs are empty"
        wizard.isContentInvalid();
    }

    def "GIVEN existing 'Option Set' content WHEN required fields are empty THEN the content should be displayed as invalid"()
    {
        when:
        findAndSelectContent( OPTION_SET.getName() );

        then: "the content should be displayed as invalid"
        contentBrowsePanel.isContentInvalid( OPTION_SET.getName() );
    }

    private Content build_OptionSet_Content()
    {
        Content tinyMceContent = Content.builder().
            name( NameHelper.uniqueName( "optionset" ) ).
            displayName( "option set content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_APP_NAME + ":optionset" ).
            build();
        return tinyMceContent;
    }
}
