package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.ContentUtils
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.SingleSelectorComboBoxFormView
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath
import com.enonic.xp.data.PropertyTree
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class Occurrences_SSelector_Combobox_0_1_Spec
    extends Base_InputFields_Occurrences

{

    @Shared
    Content content_wit_opt;


    def "WHEN wizard for adding a 'Single Selector Combo box-content' opened THEN option filter input is present "()
    {
        when: "start to add a content with type 'Single Selector ComboBox 0:1'"
        String option = null;
        Content comboBoxContent = buildSSelectorComboBox0_1_Content( option );
        filterPanel.typeSearchText( SITE_NAME );
        contentBrowsePanel.clickCheckboxAndSelectRow( SITE_NAME ).clickToolbarNew().selectContentType(
            comboBoxContent.getContentTypeName() )
        SingleSelectorComboBoxFormView formViewPanel = new SingleSelectorComboBoxFormView( getSession() );

        then: "option filter input is present and enabled"
        formViewPanel.isOptionFilterInputDisplayed();
    }

    def "GIVEN saving of not required 'Single Selector Combo box-content' without options WHEN content opened for edit THEN no one selected options present in form view"()
    {
        given: "new content with type 'Single Selector ComboBox'"
        String option = null;
        Content comboBoxContent = buildSSelectorComboBox0_1_Content( option );
        filterPanel.typeSearchText( SITE_NAME );
        contentBrowsePanel.clickCheckboxAndSelectRow( SITE_NAME ).clickToolbarNew().selectContentType(
            comboBoxContent.getContentTypeName() ).typeData( comboBoxContent ).save().close( comboBoxContent.getDisplayName() );

        when: "content opened for edit"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( comboBoxContent );
        SingleSelectorComboBoxFormView formViewPanel = new SingleSelectorComboBoxFormView( getSession() );

        then:
        formViewPanel.getSelectedOption().isEmpty();

    }

    def "GIVEN saving of not required 'Single Selector Combo box-content' without options WHEN 'Publish' button pressed THEN content with 'Online' status listed"()
    {
        given: "new content with type 'Single Selector ComboBox'"
        String option = null;
        Content comboBoxContent = buildSSelectorComboBox0_1_Content( option );
        filterPanel.typeSearchText( SITE_NAME );
        contentBrowsePanel.clickCheckboxAndSelectRow( SITE_NAME ).clickToolbarNew().selectContentType(
            comboBoxContent.getContentTypeName() ).typeData( comboBoxContent ).save().clickOnPublishButton().close(
            comboBoxContent.getDisplayName() );

        when: "content opened for edit"
        filterPanel.typeSearchText( comboBoxContent.getName() );

        then:
        contentBrowsePanel.getContentStatus( comboBoxContent.getPath() ).equals( ContentStatus.ONLINE.getValue() );
        and:
        !contentBrowsePanel.isInvalidContent( comboBoxContent.getPath().toString() );

    }

    def "GIVEN saving of not required 'Single Selector Combo box-content' with  selected option WHEN content opened for edit THEN correct selected option and button 'Remove' present in form view"()
    {
        given: "new content with type 'Single Selector ComboBox'"
        String option = "option A";
        content_wit_opt = buildSSelectorComboBox0_1_Content( option );
        filterPanel.typeSearchText( SITE_NAME );
        contentBrowsePanel.clickCheckboxAndSelectRow( SITE_NAME ).clickToolbarNew().selectContentType(
            content_wit_opt.getContentTypeName() ).typeData( content_wit_opt ).save().close( content_wit_opt.getDisplayName() );

        when: "content opened for edit"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( content_wit_opt );
        SingleSelectorComboBoxFormView formViewPanel = new SingleSelectorComboBoxFormView( getSession() );

        then:
        formViewPanel.getSelectedOption() == option;
        and: "options filter not displayed"
        !formViewPanel.isOptionFilterInputDisplayed();
    }

    def "GIVEN a not required 'Single Selector Combo box-content' with selected option WHEN content opened and option changed THEN new option displayed"()
    {
        given: "a content with type Single Selector ComboBox' opened for edit"
        String newOption = "option B";
        PropertyTree newData = ContentUtils.buildSingleSelectionData( newOption );
        ContentWizardPanel contentWizardPanel = contentBrowsePanel.selectAndOpenContentFromToolbarMenu( content_wit_opt );


        when: "new option selected"
        SingleSelectorComboBoxFormView formViewPanel = new SingleSelectorComboBoxFormView( getSession() );
        formViewPanel.clickOnRemoveOptionButton().type( newData );
        contentWizardPanel.save().close( content_wit_opt.getDisplayName() );
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( content_wit_opt );

        then: "new selected option displayed"
        formViewPanel.getSelectedOption() == newOption;

    }

    def "GIVEN creating new Single Selector ComboBox-content (0:1) on root WHEN required text input is empty and button 'Publish' pressed THEN validation message not appears"()
    {
        given: "start to add a content with type 'Single selector ComboBox-content (0:1)'"
        Content textLineContent = buildSSelectorComboBox0_1_Content( null );
        filterPanel.typeSearchText( SITE_NAME );
        ContentWizardPanel contentWizardPanel = contentBrowsePanel.clickCheckboxAndSelectRow(
            SITE_NAME ).clickToolbarNew().selectContentType( textLineContent.getContentTypeName() );

        when:
        contentWizardPanel.clickOnPublishButton();
        TestUtils.saveScreenshot( getSession(), "ss_cbox0_1_publish" )
        SingleSelectorComboBoxFormView formViewPanel = new SingleSelectorComboBoxFormView( getSession() );

        then: "new content listed in the grid and can be opened for edit"
        !formViewPanel.isValidationMessagePresent();

    }
    //TODO XP-259 impossible to remove a selected option
    @Ignore
    def "GIVEN a not required Single Selector' Combobox-content' with selected option WHEN option removed and 'close' and 'save' pressed THEN option not selected in form view"()
    {
        given: "new content with type 'Single Selector ComboBox'"
        ContentWizardPanel wizard = contentBrowsePanel.selectAndOpenContentFromToolbarMenu( content_wit_opt );
        SingleSelectorComboBoxFormView formViewPanel = new SingleSelectorComboBoxFormView( getSession() );
        boolean inputDisplayedBeforeRemoving = formViewPanel.isOptionFilterInputDisplayed();

        when: "content opened for edit"
        formViewPanel.clickOnRemoveOptionButton();
        wizard.save().close( content_wit_opt.getDisplayName() );
        //TODO XP-261 remove it's string when bug with searchText will be fixed
        contentBrowsePanel.refreshPanelInBrowser();
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( content_wit_opt );

        then:
        !inputDisplayedBeforeRemoving;
        and:
        formViewPanel.isOptionFilterInputDisplayed();
        and:
        formViewPanel.getSelectedOption().isEmpty();

    }

    private Content buildSSelectorComboBox0_1_Content( String option )
    {
        PropertyTree data = ContentUtils.buildSingleSelectionData( option );
        Content textLineContent = Content.builder().
            name( NameHelper.uniqueName( "ss_cbox0_1_" ) ).
            displayName( "ss_cbox0_1 content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_MODULE_NAME + ":ss_combobox0_1" ).data( data ).
            build();
        return textLineContent;
    }
}
