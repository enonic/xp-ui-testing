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

    def "GIVEN ComboBox-content (0:1) without options is added WHEN content is opened THEN no one options should be displayed on the page"()
    {
        given: "new content with type ComboBox0_1 was added'"
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

    def "GIVEN new ComboBox0_1(0:1) is saved AND one option is selected WHEN content has been opened THEN one selected option should be present on the page and options filter input is disabled"()
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

        and: "option with correct text should be displayed"
        optValues.get( 0 ) == "option A";

        and: "options filter input should be disabled"
        !formViewPanel.isOptionFilterInputEnabled();
    }

    def "GIVEN a existing new ComboBox 0:1 with one option  WHEN content was opened and 'Publish' on toolbar pressed THEN content's status is getting 'Online'"()
    {
        given: "existing new ComboBox 0:0 with options'"
        ContentWizardPanel wizard = contentBrowsePanel.selectAndOpenContentFromToolbarMenu( content_with_opt );

        when: "the content has been published"
        wizard.clickOnWizardPublishButton().clickOnPublishNowButton().waitForDialogClosed();
        wizard.closeBrowserTab().switchToBrowsePanelTab(); ;
        filterPanel.typeSearchText( content_with_opt.getName() );

        then: "content has a 'Published' status"
        contentBrowsePanel.getContentStatus( content_with_opt.getName() ).equalsIgnoreCase( ContentStatus.PUBLISHED.getValue() );
    }

    def "GIVEN existing ComboBox-content(0:1) with selected option is opened and the option is removed AND content saved WHEN content is opened THEN options should not be selected on the page"()
    {
        given: "content with one option is  opened "
        ContentWizardPanel wizard = contentBrowsePanel.selectAndOpenContentFromToolbarMenu( content_with_opt );
        ComboBoxFormViewPanel formViewPanel = new ComboBoxFormViewPanel( getSession() );
        and: "one option is removed"
        formViewPanel.clickOnLastRemoveButton();
        and: "the content is saved and closed"
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();

        when: "content is opened again"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( content_with_opt );

        then: "no options is selected on the page "
        List<String> optValues = formViewPanel.getSelectedOptionValues();
        optValues.size() == 0;

        and: "and options filter should be enabled"
        formViewPanel.isOptionFilterInputEnabled();

        and: "content should be 'Modified'"
        wizard.getStatus(  ).equalsIgnoreCase( ContentStatus.MODIFIED.getValue() );
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