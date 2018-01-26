package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.FreeFormViewPanel
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Stepwise

/**
 * Created on 5/25/2017.
 *
 *  Task:
 * xp-ui-testing#39 Add Selenium tests to verify #4813
 *
 * Verifies:
 * OptionSet -Error appears when 'upload' button in the ImageSelector was pressed #4813
 *
 * */
@Stepwise
class FreeFormNestedSet_Spec
    extends Base_InputFields_Occurrences
{
    @Shared
    Content SET_IN_SET_CONTENT

    def "GIVEN wizard for FreeForm is opened WHEN 'Save' button has been pressed THEN red icon should be present on the wizard page"()
    {
        given: "wizard for FreeFor is opened"
        SET_IN_SET_CONTENT = buildFreeFormOptionSet_Content();
        ContentWizardPanel wizard = selectSitePressNew( SET_IN_SET_CONTENT.getContentTypeName() );

        when: "the name has been typed and Save button pressed"
        wizard.typeData( SET_IN_SET_CONTENT ).save();
        saveScreenshot( "set_in_set_empty" );

        then: "red icon should be present on the wizard page, because required inputs are not filled"
        wizard.isContentInvalid()
    }
    //TODO remove it when bug  https://github.com/enonic/xp/issues/4830 will be fixed
    @Ignore
    def "GIVEN existing not valid 'FreeForm' content WHEN the content has been selected THEN the content should be displayed as invalid(in the grid)"()
    {
        when: "'FreeForm' content is selected"
        findAndSelectContent( SET_IN_SET_CONTENT.getName() );

        then: "the content should be displayed as invalid, because required inputs are not filled"
        contentBrowsePanel.isContentInvalid()
    }

    def "GIVEN existing not valid 'FreeForm' content is opened WHEN required radio button has been selected THEN red icon should not be present on the wizard page"()
    {
        given: "wizard for FreeForm is opened"
        ContentWizardPanel wizard = findAndSelectContent( SET_IN_SET_CONTENT.getName() ) clickToolbarEdit();
        FreeFormViewPanel freeForm = new FreeFormViewPanel( getSession() );

        when: "the content should be displayed as invalid, because required inputs are not filled"
        freeForm.clickOnButtonRadioButton();

        and: "the content has been saved"
        wizard.save();
        saveScreenshot( "freeform_should_be_valid" );

        then: "red icon should not be displayed on  the wizard page"
        !wizard.isContentInvalid();
    }

    @Ignore
    def "GIVEN existing not valid 'FreeForm' content is opened WHEN required inputs are not filled THEN red icon should be present on the wizard page"()
    {
        given: "wizard for FreeForm is opened"
        ContentWizardPanel wizard = findAndSelectContent( SET_IN_SET_CONTENT.getName() ) clickToolbarEdit();
        FreeFormViewPanel freeForm = new FreeFormViewPanel( getSession() );

        when: "the content should be displayed as invalid, because required inputs are not filled"
        freeForm.clickOnButtonRadioButton();
        and: "the content has been saved"
        wizard.save();
        saveScreenshot( "freeform_should_be_valid" );

        then: "red icon should not be displayed on  the wizard page"
        !wizard.isContentInvalid();
    }

    //TODO remove it when bug  https://github.com/enonic/xp/issues/4830 will be fixed
    @Ignore
    def "GIVEN updated not valid 'FreeForm' content WHEN the content has been selected THEN the content should be displayed as invalid(in the grid)"()
    {
        when: "'FreeForm' content is selected"
        findAndSelectContent( SET_IN_SET_CONTENT.getName() );

        then: "the content should be displayed as invalid, because required inputs are not filled"
        contentBrowsePanel.isContentInvalid()
    }
    //OptionSet -Error appears when 'upload' button in the ImageSelector was pressed #4813
    //insert an image in the nested set.
    def "GIVEN existing not valid 'FreeForm' content is opened AND option with ImageSelector is selected WHEN image has been selected THEN red icon should should not be present on the wizard page"()
    {
        given: "wizard for FreeForm is opened"
        ContentWizardPanel wizard = findAndSelectContent( SET_IN_SET_CONTENT.getName() ) clickToolbarEdit();
        FreeFormViewPanel freeForm = new FreeFormViewPanel( getSession() );

        when: "the content should be displayed as invalid, because required inputs are not filled"
        freeForm.clickOnInputRadioButton().clickOnImageRadioButton().selectImage( "nord" );
        and: "the content has been saved"
        wizard.save();
        saveScreenshot( "freeform_should_be_valid" );

        then: "red icon should not be displayed on  the wizard page"
        !wizard.isContentInvalid();
    }

    protected Content buildFreeFormOptionSet_Content()
    {
        Content tinyMceContent = Content.builder().
            name( NameHelper.uniqueName( "freeform" ) ).
            displayName( "freeform content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_APP_NAME + ":freeform" ).
            build();
        return tinyMceContent;
    }
}
