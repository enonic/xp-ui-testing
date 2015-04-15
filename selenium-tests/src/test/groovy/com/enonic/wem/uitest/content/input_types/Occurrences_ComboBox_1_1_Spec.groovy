package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.ContentUtils
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.ComboBoxFormViewPanel
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath
import com.enonic.xp.data.PropertyTree
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class Occurrences_ComboBox_1_1_Spec
    extends Base_InputFields_Occurrences

{

    @Shared
    Content content_with_opt;

    def "GIVEN wizard for adding a ComboBox-content(1:1) opened WHEN name typed and no options selected on the page THEN option filter input is present"()
    {
        given: "start to add a content with type 'ComboBox 1:1'"
        Content comboBoxContent = buildComboBox1_1_Content( 0 );
        ContentWizardPanel wizard = contentBrowsePanel.clickCheckboxAndSelectRow( SITE_NAME ).clickToolbarNew().selectContentType(
            comboBoxContent.getContentTypeName() );

        when: "only the name typed and no option selected"
        wizard.typeDisplayName( comboBoxContent.getDisplayName() );
        ComboBoxFormViewPanel formViewPanel = new ComboBoxFormViewPanel( getSession() );

        then: "option filter input is present and enabled"
        formViewPanel.isOptionFilterInputEnabled();

        and: "content should be invalid, because required field- combobox1:1 not selected"
        wizard.isContentInvalid( comboBoxContent.getDisplayName() );

        and: " and no options selected on the page"
        formViewPanel.getSelectedOptionValues().size() == 0;
    }

    def "GIVEN saving of ComboBox-content (1:1) without options WHEN content opened for edit THEN no one selected options present on page "()
    {
        given: "new content with type ComboBox1_1 added'"
        Content comboBoxContent = buildComboBox1_1_Content( 0 );
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

        and: "content should be invalid, because required field- combobox1:1 not selected"
        wizard.isContentInvalid( comboBoxContent.getDisplayName() );

    }

    def "GIVEN saving of  ComboBox-content (1:1) with one option WHEN content opened for edit THEN one selected option  present on page and options filter input is disabled"()
    {
        given: "new content with type ComboBox1_1 added'"
        content_with_opt = buildComboBox1_1_Content( 1 );
        contentBrowsePanel.clickCheckboxAndSelectRow( SITE_NAME ).clickToolbarNew().selectContentType(
            content_with_opt.getContentTypeName() ).typeData( content_with_opt ).save().close( content_with_opt.getDisplayName() );

        when: "content opened for edit"
        ContentWizardPanel wizard = contentBrowsePanel.selectAndOpenContentFromToolbarMenu( content_with_opt );
        ComboBoxFormViewPanel formViewPanel = new ComboBoxFormViewPanel( getSession() );
        List<String> optValues = formViewPanel.getSelectedOptionValues();

        then: "one option value  present in form view"
        optValues.size() == 1;

        and: "option with correct text present"
        optValues.get( 0 ) == "option A";

        and: "options filter input is disabled, because the content has a maximum number of options"
        !formViewPanel.isOptionFilterInputEnabled();

        and: "content is valid, because option is selected"
        !wizard.isContentInvalid( content_with_opt.getDisplayName() );

    }

    def "GIVEN ComboBox-content (1:1) with one selected option and one option removed and content saved WHEN content opened for edit THEN no options selected on the page "()
    {
        given: "content with one required option opened for edit' and one option removed"
        ContentWizardPanel wizard = contentBrowsePanel.selectAndOpenContentFromToolbarMenu( content_with_opt );
        ComboBoxFormViewPanel formViewPanel = new ComboBoxFormViewPanel( getSession() );
        formViewPanel.clickOnLastRemoveButton();
        wizard.save().close( content_with_opt.getDisplayName() );

        when: "when content selected in the grid and opened for edit again"
        //TODO remove it's string when bug with searchText will be fixed
        contentBrowsePanel.refreshPanelInBrowser();
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( content_with_opt );

        then: "no options selected on the page "
        List<String> optValues = formViewPanel.getSelectedOptionValues();
        optValues.size() == 0;

        and: "and options filter input enabled again"
        formViewPanel.isOptionFilterInputEnabled();

        and:
        wizard.isContentInvalid( content_with_opt.getDisplayName() );

    }

    def "WHEN content with one option saved and published THEN it content with status equals 'Online' listed"()
    {
        when: "content without option saved and published"
        Content comboBoxContent = buildComboBox1_1_Content( 1 );
        contentBrowsePanel.clickCheckboxAndSelectRow( SITE_NAME ).clickToolbarNew().selectContentType(
            comboBoxContent.getContentTypeName() ).typeData( comboBoxContent ).save().clickOnPublishButton().close(
            comboBoxContent.getDisplayName() );
        filterPanel.typeSearchText( comboBoxContent.getName() );

        then: "content has a 'online' status"
        contentBrowsePanel.getContentStatus( comboBoxContent.getPath() ).equals( ContentStatus.ONLINE.getValue() )
    }

    private Content buildComboBox1_1_Content( int numberOptions )
    {
        PropertyTree data = ContentUtils.buildComboBoxData( numberOptions );
        Content textLineContent = Content.builder().
            name( NameHelper.uniqueName( "cbox1_1_" ) ).
            displayName( "combobox1_1 content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_MODULE_NAME + ":combobox1_1" ).data( data ).
            build();
        return textLineContent;
    }
}