package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.SetInSetFormView
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath
import com.enonic.xp.data.PropertyTree
import org.apache.commons.lang3.StringUtils
import spock.lang.Shared
import spock.lang.Stepwise

/**
 * Created on 5/15/2017.
 * Tasks:
 * xp-ui-testing#40 Add Selenium tests for content that contains Set in Set
 * */
@Stepwise
class Set_In_Set_Validation_Spec
    extends Base_InputFields_Occurrences
{
    @Shared
    Content SET_IN_SET;

    def "GIVEN wizard for 'Set in Set' content is opened WHEN name has been typed AND  'Save' button pressed THEN red circle should be present on the wizard page"()
    {
        given: "'HtmlArea content has been added and 'div' is wrapper element"
        SET_IN_SET = build_Set_In_Set_Content( null, null );
        ContentWizardPanel wizard = selectSitePressNew( SET_IN_SET.getContentTypeName() );
        wizard.typeDisplayName( SET_IN_SET.getName() );

        when: "required inputs are empty and 'Save' button has been clicked"
        wizard.save();
        saveScreenshot( "set_in_set_is_invalid" );

        then: "red circle should be present on the wizard page, because required inputs are empty"
        wizard.isContentInvalid();
    }

    def "GIVEN existing 'Set in Set' content WHEN required fields are empty THEN the content should be displayed as invalid"()
    {
        when: "existing 'Set in Set' content is selected"
        findAndSelectContent( SET_IN_SET.getName() );

        then: "the content should be displayed as invalid"
        contentBrowsePanel.isContentInvalid( SET_IN_SET.getName() );
    }

    def "GIVEN existing 'Set in Set' content is opened WHEN required first name and last name are filled THEN the content should be valid"()
    {
        given: "existing 'Set in Set' content is opened"
        ContentWizardPanel wizard = findAndSelectContent( SET_IN_SET.getName() ).clickToolbarEdit();
        SetInSetFormView setInSetFormView = new SetInSetFormView( getSession() );

        when: "the first name and last name have been typed"
        setInSetFormView.typeNameAndLastName( "John", "Doe" );
        and: "'Save' button has been pressed"
        wizard.save();

        then: "red icon should not be present on the wizard page, because required inputs are filled"
        !wizard.isContentInvalid();
    }

    def "GIVEN existing 'Set in Set' content WHEN required fields are filled THEN the content should be displayed as valid"()
    {
        when: "existing 'Set in Set' content is selected (required fields are filled)"
        findAndSelectContent( SET_IN_SET.getName() );

        then: "the content should be displayed as valid"
        !contentBrowsePanel.isContentInvalid( SET_IN_SET.getName() );
    }

    def "GIVEN existing valid 'Set in Set' content is opened WHEN required first name and last name has been cleared THEN red icon should appear on the wizard"()
    {
        given: "existing valid 'Set in Set' content is opened"
        ContentWizardPanel wizard = findAndSelectContent( SET_IN_SET.getName() ).clickToolbarEdit();
        SetInSetFormView setInSetFormView = new SetInSetFormView( getSession() );

        when: "the first name and last name have been cleared"
        setInSetFormView.typeNameAndLastName( "", "" );
        and: "'Save' button has been pressed"
        wizard.save();

        then: "red icon should be present on the wizard page, because required inputs are empty"
        wizard.isContentInvalid();

        and: "correct validation message should be present(First Name)"
        setInSetFormView.getValidationMessageForFirstNameInput() == Application.REQUIRED_MESSAGE;
        and: "correct validation message should be present(Last Name)"
        setInSetFormView.getValidationMessageForLastNameInput() == Application.REQUIRED_MESSAGE

    }

    def "GIVEN existing valid 'Set in Set' content is opened WHEN required first name and last name is filled AND 'Add Phone Number' button pressed AND Save button pressed THEN red icon should appear on the wizard"()
    {
        given: "existing valid 'Set in Set' content is opened"
        ContentWizardPanel wizard = findAndSelectContent( SET_IN_SET.getName() ).clickToolbarEdit();
        SetInSetFormView setInSetFormView = new SetInSetFormView( getSession() );

        and: "the first name and last name have been filled"
        setInSetFormView.typeNameAndLastName( "John", "Doe" );

        when: "'Add Phone Number' button has been pressed"
        setInSetFormView.clickOnAddContactInfoButton().clickOnAddPhoneNumberButton();
        and: "'Save' button has been pressed"
        wizard.save();
        saveScreenshot( "required_phone_number_is_empty" )

        then: "red icon should be present on the wizard page, because required 'phone number'-Item Set is not filled"
        wizard.isContentInvalid();
    }

    def "GIVEN existing 'Set in Set' content WHEN required 'phone number' Item Set is not filled THEN the content should be displayed as invalid"()
    {
        when: "existing 'Set in Set' content is selected (required 'phone number'-Item Set is not filled)"
        findAndSelectContent( SET_IN_SET.getName() );

        then: "the content should be displayed as invalid"
        contentBrowsePanel.isContentInvalid( SET_IN_SET.getName() );
    }

    private Content build_Set_In_Set_Content( String firstName, String lastName )
    {
        PropertyTree data = new PropertyTree();
        if ( StringUtils.isNotEmpty( firstName ) )
        {
            data.addStrings( SetInSetFormView.FIRST_NAME_KEY, firstName );
        }
        if ( StringUtils.isNotEmpty( lastName ) )
        {
            data.addStrings( SetInSetFormView.FIRST_NAME_KEY, lastName );
        }

        Content tinyMceContent = Content.builder().
            name( NameHelper.uniqueName( "se-in-set" ) ).
            displayName( "set in set content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_APP_NAME + ":set-in-set" ).data( data ).
            build();
        return tinyMceContent;
    }
}
