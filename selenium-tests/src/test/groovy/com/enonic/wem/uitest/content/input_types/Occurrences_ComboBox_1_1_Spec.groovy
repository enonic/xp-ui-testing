package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.ContentUtils
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.ComboBoxFormViewPanel
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath
import com.enonic.xp.data.PropertyTree
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
@Ignore
class Occurrences_ComboBox_1_1_Spec
    extends Base_InputFields_Occurrences
{
    @Shared
    Content content_with_opt;

    @Shared
    Content comboBox1_1

    def "GIVEN wizard for new ComboBox-content(1:1) is opened WHEN name has been typed THEN red icon should be present in the wizard, because the input is required"()
    {
        given: "wizard for new ComboBox-content(1:1) is opened"
        Content comboBoxContent = buildComboBox1_1_Content( 0 );
        ContentWizardPanel wizard = selectSitePressNew( comboBoxContent.getContentTypeName() );

        when: "a name has been typed and no option selected"
        wizard.typeDisplayName( comboBoxContent.getDisplayName() );
        ComboBoxFormViewPanel formViewPanel = new ComboBoxFormViewPanel( getSession() );

        then: "option filter input should be present and enabled"
        formViewPanel.isOptionFilterInputEnabled();

        and: "and there are no selected options on the page"
        formViewPanel.getSelectedOptionValues().size() == 0;

        and: "red icon should be present in the wizard, because required option is not selected"
        wizard.isContentInvalid();
    }

    def "GIVEN ComboBox-content(1:1) without options is saved WHEN content has been opened THEN no one option should be selected and red circle should be displayed"()
    {
        given: "new content with type ComboBox1_1 added"
        Content comboBoxContent = buildComboBox1_1_Content( 0 );
        selectSitePressNew( comboBoxContent.getContentTypeName() ).typeData(
            comboBoxContent ).save().closeBrowserTab().switchToBrowsePanelTab();

        when: "content has been opened"
        ContentWizardPanel wizard = contentBrowsePanel.selectAndOpenContentFromToolbarMenu( comboBoxContent );
        ComboBoxFormViewPanel formViewPanel = new ComboBoxFormViewPanel( getSession() );
        List<String> optValues = formViewPanel.getSelectedOptionValues();

        then: "no one option is selected on the page"
        optValues.size() == 0;

        and: "options filter input should be enabled"
        formViewPanel.isOptionFilterInputEnabled();

        and: "content should be invalid, because required field- combobox1:1 is not selected"
        formViewPanel.getFormValidationRecording( 0 ) == Application.REQUIRED_MESSAGE;

        and: "red circle should be displayed on the wizard page"
        wizard.isContentInvalid();

        and: "'Publish' menu item should be disabled"
        !wizard.showPublishMenu().isPublishMenuItemEnabled();
    }

    def "GIVEN ComboBox-content (1:1) with one option is saved WHEN the content has been opened THEN one selected option should be present and options filter input is disabled"()
    {
        given: "ComboBox-content (1:1) with one selected option has been added"
        content_with_opt = buildComboBox1_1_Content( 1 );
        selectSitePressNew( content_with_opt.getContentTypeName() ).typeData(
            content_with_opt ).save().closeBrowserTab().switchToBrowsePanelTab();

        when: "content has been opened"
        ContentWizardPanel wizard = contentBrowsePanel.selectAndOpenContentFromToolbarMenu( content_with_opt );
        ComboBoxFormViewPanel formViewPanel = new ComboBoxFormViewPanel( getSession() );
        List<String> optValues = formViewPanel.getSelectedOptionValues();

        then: "one selected option should be present in form view"
        optValues.size() == 1;

        and: "expected option should be displayed"
        optValues.get( 0 ) == "option A";

        and: "options filter input should not be displayed, because the content has a maximum number of options"
        !formViewPanel.isOptionFilterInputDisplayed();

        and: "content is valid, because option is selected"
        !formViewPanel.isFormValidationMessageDisplayed();

        and: "red circle should not be displayed on the page, because the required option is selected"
        !wizard.isContentInvalid();
    }

    def "GIVEN existing ComboBox-content(1:1) is opened and one option removed WHEN content has been opened THEN no options selected on the page"()
    {
        given: "content is opened and one option removed"
        ContentWizardPanel wizard = contentBrowsePanel.selectAndOpenContentFromToolbarMenu( content_with_opt );
        ComboBoxFormViewPanel formViewPanel = new ComboBoxFormViewPanel( getSession() );
        formViewPanel.clickOnLastRemoveButton();
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();

        when: "when content selected in the grid and opened for edit again"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( content_with_opt );

        then: "no options selected on the page "
        List<String> optValues = formViewPanel.getSelectedOptionValues();
        optValues.size() == 0;

        and: "and 'options filter' input should be enabled"
        formViewPanel.isOptionFilterInputEnabled();

        and: "validation message should be displayed, because the option is required"
        formViewPanel.getFormValidationRecording( 0 ) == Application.REQUIRED_MESSAGE;

        and: "red circle should be displayed, because required option is not selected"
        wizard.isContentInvalid();
    }

    def "WHEN content with selected option has been published THEN this content gets 'Published'"()
    {
        when: "content with selected option has been published"
        comboBox1_1 = buildComboBox1_1_Content( 1 );
        ContentWizardPanel wizard = selectSitePressNew( comboBox1_1.getContentTypeName() );
        wizard.typeData( comboBox1_1 ).showPublishMenu().clickOnMarkAsReadyMenuItem();
        wizard.clickOnPublishButton().clickOnPublishNowButton();
        wizard.closeBrowserTab().switchToBrowsePanelTab();
        filterPanel.typeSearchText( comboBox1_1.getName() );

        then: "this content gets 'Published'"
        contentBrowsePanel.getContentStatus( comboBox1_1.getName() ).equalsIgnoreCase( ContentStatus.PUBLISHED.getValue() );
    }

    def "WHEN required option is not selected in wizard THEN the content should be displayed in browse panel with red icon"()
    {
        given: "new combobox content(1:1) is added'"
        Content comboBoxContent = buildComboBox1_1_Content( 0 );
        ContentWizardPanel wizard = selectSitePressNew( comboBoxContent.getContentTypeName() ).typeData( comboBoxContent );
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();

        when: "content has been selected"
        findAndSelectContent( comboBoxContent.getName() );
        saveScreenshot( "combobox-not-valid" );

        then: "content should be invalid, because required option was not selected"
        contentBrowsePanel.isContentInvalid( comboBoxContent.getName() );
    }

    private Content buildComboBox1_1_Content( int numberOptions )
    {
        PropertyTree data = ContentUtils.buildComboBoxData( numberOptions );
        Content textLineContent = Content.builder().
            name( NameHelper.uniqueName( "cbox1_1_" ) ).
            displayName( "combobox1_1 content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_APP_NAME + "combobox1_1" ).data( data ).
            build();
        return textLineContent;
    }
}
