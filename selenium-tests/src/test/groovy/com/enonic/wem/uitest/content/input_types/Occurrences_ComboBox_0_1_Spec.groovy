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

    def "WHEN wizard for  ComboBox-content(0:1) is opened THEN option filter input should be present"()
    {
        when: "start to add a content with type 'ComboBox 0:1'"
        Content comboBoxContent = buildComboBox0_1_Content( 0 );
        selectSitePressNew( comboBoxContent.getContentTypeName() );
        ComboBoxFormViewPanel formViewPanel = new ComboBoxFormViewPanel( getSession() );

        then: "option filter input should be present and enabled"
        formViewPanel.isOptionFilterInputEnabled();
        and: "no selected should be on the form"
        formViewPanel.getSelectedOptionValues().size() == 0;
    }

    def "GIVEN ComboBox-content (0:1) without options was added WHEN content is opened THEN no one options should be displayed on the page"()
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

    def "GIVEN saving of  ComboBox-content (0:1) with one option WHEN content opened for edit THEN one selected option  present on page and options filter input is disabled"()
    {
        given: "new content with type ComboBox0_1 was added'"
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

    def "GIVEN wizard for ComboBox-content (0:1) with one selected option is opened and the option was removed and content saved WHEN content is opened THEN options should not be selected on the page"()
    {
        given: "content with one option is  opened "
        ContentWizardPanel wizard = contentBrowsePanel.selectAndOpenContentFromToolbarMenu( content_with_opt );
        ComboBoxFormViewPanel formViewPanel = new ComboBoxFormViewPanel( getSession() );
        and: "one option was removed"
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
    }

    def "WHEN content without option was saved and published THEN the content should be displayed with 'Online' status"()
    {
        given: "content without option saved and published"
        Content comboBoxContent = buildComboBox0_1_Content( 0 );
        ContentWizardPanel wizard = selectSitePressNew( comboBoxContent.getContentTypeName() ).typeData( comboBoxContent ).save();
        wizard.clickOnWizardPublishButton().waitUntilDialogShown(
            Application.EXPLICIT_NORMAL ).clickOnPublishNowButton().waitForDialogClosed();
        wizard.closeBrowserTab().switchToBrowsePanelTab();

        when:"the content is filtered"
        filterPanel.typeSearchText( comboBoxContent.getName() );

        then: "content has a 'Published' status"
        contentBrowsePanel.getContentStatus( comboBoxContent.getName() ).equalsIgnoreCase( ContentStatus.PUBLISHED.getValue() );
    }

    private Content buildComboBox0_1_Content( int numberOptions )
    {
        PropertyTree data = ContentUtils.buildComboBoxData( numberOptions );
        Content comboboxContent = Content.builder().
            name( NameHelper.uniqueName( "cbox0_1_" ) ).
            displayName( "combobox0_1 content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_APP_NAME + ":combobox0_1" ).data( data ).
            build();
        return comboboxContent;
    }
}