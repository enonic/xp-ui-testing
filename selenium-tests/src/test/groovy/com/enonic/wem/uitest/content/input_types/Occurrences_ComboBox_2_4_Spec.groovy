package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.ContentUtils
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.ComboBoxFormViewPanel
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath
import com.enonic.xp.data.PropertyTree
import spock.lang.Ignore
import spock.lang.Shared

class Occurrences_ComboBox_2_4_Spec
    extends Base_InputFields_Occurrences

{

    @Shared
    Content content_with_opt;

    def "GIVEN  wizard for adding a ComboBox-content(2:4) opened WHEN name typed and no options selected on the page THEN content is invalid and option filter input is present "()
    {
        given: "start to add a content with type 'ComboBox 2:4'"
        Content comboBoxContent = buildComboBox2_4_Content( 0 );
        filterPanel.typeSearchText( SITE_NAME );
        ContentWizardPanel wizard = contentBrowsePanel.clickCheckboxAndSelectRow( SITE_NAME ).clickToolbarNew().selectContentType(
            comboBoxContent.getContentTypeName() )
        ComboBoxFormViewPanel formViewPanel = new ComboBoxFormViewPanel( getSession() );

        when: "only the name typed and no option selected"
        wizard.typeDisplayName( comboBoxContent.getDisplayName() );
        then: "option filter input is present and enabled"
        formViewPanel.isOptionFilterInputEnabled();

        and: " and no options selected on the page"
        formViewPanel.getSelectedOptionValues().size() == 0;

        and: "content should be invalid, because required field- combobox2:4 not selected"
        wizard.isContentInvalid( comboBoxContent.getDisplayName() );


    }

    def "GIVEN saving of ComboBox-content (2:4) without options WHEN content opened for edit THEN no one selected options present on page "()
    {
        given: "new content with type ComboBox2_4 added'"
        Content comboBoxContent = buildComboBox2_4_Content( 0 );
        filterPanel.typeSearchText( SITE_NAME );
        contentBrowsePanel.clickCheckboxAndSelectRow( SITE_NAME ).clickToolbarNew().selectContentType(
            comboBoxContent.getContentTypeName() ).typeData( comboBoxContent ).save().close( comboBoxContent.getDisplayName() );

        when: "content opened for edit"
        ContentWizardPanel wizard = contentBrowsePanel.selectAndOpenContentFromToolbarMenu( comboBoxContent );
        ComboBoxFormViewPanel formViewPanel = new ComboBoxFormViewPanel( getSession() );
        List<String> optValues = formViewPanel.getSelectedOptionValues();

        then: "no options selected on the page"
        optValues.size() == 0;

        and: "options filter input is enabled"
        formViewPanel.isOptionFilterInputEnabled();

        and: "content should be invalid, because required field- combobox2:4 not selected"
        wizard.isContentInvalid( comboBoxContent.getDisplayName() );

    }

    def "GIVEN saving of ComboBox-content (2:4) with two option WHEN content opened for edit THEN two selected options present on page and options filter input is enabled"()
    {
        given: "new content with type ComboBox2_4 added'"
        content_with_opt = buildComboBox2_4_Content( 2 );
        filterPanel.typeSearchText( SITE_NAME );
        contentBrowsePanel.clickCheckboxAndSelectRow( SITE_NAME ).clickToolbarNew().selectContentType(
            content_with_opt.getContentTypeName() ).typeData( content_with_opt ).save().close( content_with_opt.getDisplayName() );

        when: "content opened for edit"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( content_with_opt );
        ComboBoxFormViewPanel formViewPanel = new ComboBoxFormViewPanel( getSession() );
        List<String> optValues = formViewPanel.getSelectedOptionValues();

        then: "one option value  present in form view"
        optValues.size() == 2;

        and: "option with correct text present"
        String[] options = ["option A", "option B"];
        optValues.containsAll( options.toList() );

        and: "options filter input is enabled, because 2 less than 4"
        formViewPanel.isOptionFilterInputEnabled();

    }
    //TODO remove it when sources will be updated on teamcity
    @Ignore
    def "GIVEN ComboBox-content (2:4) with two selected options and one option removed and content saved WHEN content opened for edit THEN one option selected on the page "()
    {
        given: "content with one required option opened for edit' and one option removed"
        ContentWizardPanel wizard = contentBrowsePanel.selectAndOpenContentFromToolbarMenu( content_with_opt );
        ComboBoxFormViewPanel formViewPanel = new ComboBoxFormViewPanel( getSession() );
        formViewPanel.clickOnLastRemoveButton();
        wizard.save().close( content_with_opt.getDisplayName() );

        when: "when content selected in the grid and opened for edit again"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( content_with_opt );

        then: "no options selected on the page "
        TestUtils.saveScreenshot( getSession(), "24remove_opt" )
        List<String> optValues = formViewPanel.getSelectedOptionValues();
        optValues.size() == 1;

        and: "content is invalid, because required fields- combobox2:4 not selected"
        wizard.isContentInvalid( content_with_opt.getDisplayName() );

    }

    def "GIVEN saving of  ComboBox-content (2:4) with one option WHEN content opened for edit THEN one selected option  present on page and options filter input is disabled"()
    {
        given: "new content with type ComboBox2_4 added'"
        Content comboBoxContent = buildComboBox2_4_Content( 4 );
        filterPanel.typeSearchText( SITE_NAME );
        contentBrowsePanel.clickCheckboxAndSelectRow( SITE_NAME ).clickToolbarNew().selectContentType(
            comboBoxContent.getContentTypeName() ).typeData( comboBoxContent ).save().close( comboBoxContent.getDisplayName() );

        when: "content opened for edit"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( comboBoxContent );
        ComboBoxFormViewPanel formViewPanel = new ComboBoxFormViewPanel( getSession() );
        List<String> optValues = formViewPanel.getSelectedOptionValues();

        then: "one option value  present in form view"
        optValues.size() == 4;

        and: "and options have a correct text"
        String[] options = ["option A", "option B", "option C", "option D"];
        optValues.containsAll( options.toList() );

        and: "options filter input is disabled, because this content have a maximum options"
        !formViewPanel.isOptionFilterInputEnabled();

    }

    def "WHEN content with 2 selected option saved and published THEN it content with 'Online'-status listed"()
    {
        when: "content without option saved and published"
        Content comboBoxContent = buildComboBox2_4_Content( 2 );
        filterPanel.typeSearchText( SITE_NAME );
        contentBrowsePanel.clickCheckboxAndSelectRow( SITE_NAME ).clickToolbarNew().selectContentType(
            comboBoxContent.getContentTypeName() ).typeData( comboBoxContent ).save().clickOnPublishButton().close(
            comboBoxContent.getDisplayName() );
        filterPanel.typeSearchText( comboBoxContent.getName() );

        then: "content has a 'online' status"
        contentBrowsePanel.getContentStatus( comboBoxContent.getPath() ).equals( ContentStatus.ONLINE.getValue() )
    }

    private Content buildComboBox2_4_Content( int numberOptions )
    {
        PropertyTree data = ContentUtils.buildComboBoxData( numberOptions );
        Content textLineContent = Content.builder().
            name( NameHelper.uniqueName( "cbox2_4_" ) ).
            displayName( "combobox2_4 content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_MODULE_NAME + ":combobox2_4" ).data( data ).
            build();
        return textLineContent;
    }
}
