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
        given: "wizard for FreeForm is opened"
        SET_IN_SET_CONTENT = buildFreeFormOptionSet_Content();
        ContentWizardPanel wizard = selectSitePressNew( SET_IN_SET_CONTENT.getContentTypeName() );

        when: "the name has been typed and Save button pressed"
        wizard.typeData( SET_IN_SET_CONTENT ).save();
        saveScreenshot( "set_in_set_empty" );

        then: "red icon should be present on the wizard page, because required inputs are not filled"
        wizard.isContentInvalid()
    }

    def "GIVEN existing not valid 'FreeForm' content WHEN the content has been selected THEN the content should be invalid(in the grid)"()
    {
        when: "'FreeForm' content is selected"
        findAndSelectContent( SET_IN_SET_CONTENT.getName() );

        then: "the content should be displayed as invalid, because required inputs are not filled"
        contentBrowsePanel.isContentInvalid( SET_IN_SET_CONTENT.getName() )
    }

    def "GIVEN existing not valid 'FreeForm' content is opened WHEN required radio button has been selected THEN red icon should not be present on the wizard page"()
    {
        given: "wizard for FreeForm is opened"
        ContentWizardPanel wizard = findAndSelectContent( SET_IN_SET_CONTENT.getName() ) clickToolbarEdit();
        FreeFormViewPanel freeForm = new FreeFormViewPanel( getSession() );

        when: "the content should be displayed as invalid, because required inputs are not filled"
        freeForm.clickOnForm();
        freeForm.expandItemSetRadio();
        freeForm.clickOnButtonRadioButton();

        and: "the content has been saved"
        wizard.save();
        saveScreenshot( "freeform_should_be_valid1" );

        then: "red icon should not be displayed in the wizard page"
        !wizard.isContentInvalid();
    }


    def "GIVEN existing not valid 'FreeForm' content is opened WHEN required inputs has been cleared THEN red icon gets visible in the wizard page"()
    {
        given: "wizard for FreeForm is opened"
        ContentWizardPanel wizard = findAndSelectContent( SET_IN_SET_CONTENT.getName() ) clickToolbarEdit();
        FreeFormViewPanel freeForm = new FreeFormViewPanel( getSession() );

        when: "the content should be displayed as invalid, because required inputs are not filled"
        freeForm.clickOnForm();
        freeForm.expandItemSetRadio();
        freeForm.clickOnInputRadioButton();
        and: "the content has been saved"
        wizard.save();
        saveScreenshot( "freeform_should_be_invalid" );

        then: "red icon should be displayed on  the wizard page"
        wizard.isContentInvalid();
    }

    def "GIVEN updated not valid 'FreeForm' content WHEN the content has been selected THEN the content should be displayed as invalid(in the grid)"()
    {
        when: "'FreeForm' content is selected"
        findAndSelectContent( SET_IN_SET_CONTENT.getName() );

        then: "the content should be displayed as invalid, because required inputs are not filled"
        contentBrowsePanel.isContentInvalid( SET_IN_SET_CONTENT.getName() )
    }

    def "GIVEN existing not valid 'FreeForm' content is opened AND option with ImageSelector is selected WHEN image has been selected THEN red icon should should not be present on the wizard page"()
    {
        given: "wizard for FreeForm is opened"
        ContentWizardPanel wizard = findAndSelectContent( SET_IN_SET_CONTENT.getName() ) clickToolbarEdit();
        FreeFormViewPanel freeForm = new FreeFormViewPanel( getSession() );
        freeForm.clickOnForm();
        freeForm.expandItemSetRadio();
        freeForm.expandItemSetRadio2();

        when: "the content should be displayed as invalid, because required inputs are not filled"
        freeForm.clickOnInputRadioButton().clickOnImageRadioButton().selectImage( "nord" );
        and: "the content has been saved"
        wizard.save();
        saveScreenshot( "freeform_should_be_valid2" );

        then: "red icon should not be displayed on  the wizard page"
        !wizard.isContentInvalid();
    }

    protected Content buildFreeFormOptionSet_Content()
    {
        Content tinyMceContent = Content.builder().
            name( NameHelper.uniqueName( "freeform" ) ).
            displayName( "freeform content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_APP_NAME + "freeform" ).
            build();
        return tinyMceContent;
    }
}
