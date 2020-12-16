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
import spock.lang.Shared

class Occurrences_ComboBox_0_1_Spec
    extends Base_InputFields_Occurrences
{
    @Shared
    Content content_with_opt;

    def "GIVEN ComboBox-content (0:1) without options is added WHEN content has been opened THEN no one options should be displayed on the page"()
    {
        given: "new ComboBox0_1 is added"
        Content comboBoxContent = buildComboBox0_1_Content( 0 );
        selectSitePressNew( comboBoxContent.getContentTypeName() ).typeData(
            comboBoxContent ).save().closeBrowserTab().switchToBrowsePanelTab();

        when: "content is opened"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( comboBoxContent );
        ComboBoxFormViewPanel formViewPanel = new ComboBoxFormViewPanel( getSession() );
        List<String> optValues = formViewPanel.getSelectedOptionValues();

        then: "no one options should be displayed on the page"
        optValues.size() == 0;

        and: "options filter input should be enabled"
        formViewPanel.isOptionFilterInputEnabled();
    }

    def "GIVEN new wizard for ComboBox0_1 is opened AND one option has been selected WHEN content has been re-opened THEN one selected option should be present and options-filter input should be disabled"()
    {
        given: "new ComboBox0_1 is saved"
        content_with_opt = buildComboBox0_1_Content( 1 );
        selectSitePressNew( content_with_opt.getContentTypeName() ).typeData(
            content_with_opt ).save().closeBrowserTab().switchToBrowsePanelTab();

        when: "content is opened"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( content_with_opt );
        ComboBoxFormViewPanel formViewPanel = new ComboBoxFormViewPanel( getSession() );
        List<String> optValues = formViewPanel.getSelectedOptionValues();

        then: "one option value  present in form view"
        optValues.size() == 1;

        and: "option with expected text should be displayed"
        optValues.get( 0 ) == "option A";

        and: "options filter input should not be displayed"
        !formViewPanel.isOptionFilterInputDisplayed();
    }

    def "GIVEN existing ComboBox 0:1 with (one selected option) is opened WHEN the content has been published THEN content's status is getting 'Published'"()
    {
        given: "existing ComboBox 0:0 with options'"
        ContentWizardPanel wizard = contentBrowsePanel.selectAndOpenContentFromToolbarMenu( content_with_opt );

        when: "the content has been published"
        wizard.showPublishMenu(  ).clickOnMarkAsReadyMenuItem(  );
        wizard.clickOnWizardPublishButton().clickOnPublishButton().waitForDialogClosed();
        wizard.closeBrowserTab().switchToBrowsePanelTab(); ;
        filterPanel.typeSearchText( content_with_opt.getName() );

        then: "content has a 'Published' status"
        contentBrowsePanel.getContentStatus( content_with_opt.getName() ).equalsIgnoreCase( ContentStatus.PUBLISHED.getValue() );
    }

    def "GIVEN existing ComboBox-content(0:1) is opened AND selected option has been removed WHEN content has been reopened THEN the option should not be selected in the combobox"()
    {
        given: "content with one option is opened "
        ContentWizardPanel wizard = contentBrowsePanel.selectAndOpenContentFromToolbarMenu( content_with_opt );
        ComboBoxFormViewPanel formViewPanel = new ComboBoxFormViewPanel( getSession() );
        and: "one option has been removed"
        formViewPanel.clickOnLastRemoveButton();
        and: "the content has been saved and closed"
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();

        when: "content has been reopened"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( content_with_opt );

        then: "no options should be selected on the wizard"
        List<String> optValues = formViewPanel.getSelectedOptionValues();
        optValues.size() == 0;

        and: "and options filter should be enabled"
        formViewPanel.isOptionFilterInputEnabled();

        and: "content should be 'Modified'"
        wizard.getStatus().equalsIgnoreCase( ContentStatus.MODIFIED.getValue() );
    }

    private Content buildComboBox0_1_Content( int numberOptions )
    {
        PropertyTree data = ContentUtils.buildComboBoxData( numberOptions );
        Content comboboxContent = Content.builder().
            name( NameHelper.uniqueName( "cbox0_1_" ) ).
            displayName( "combobox0_1 content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_APP_NAME + "combobox0_1" ).data( data ).
            build();
        return comboboxContent;
    }
}
