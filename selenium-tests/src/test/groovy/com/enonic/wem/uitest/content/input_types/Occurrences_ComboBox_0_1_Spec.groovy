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

    def "WHEN wizard for adding a ComboBox-content(0:1) opened THEN option filter input is present"()
    {
        when: "start to add a content with type 'ComboBox 0:1'"
        Content comboBoxContent = buildComboBox0_1_Content( 0 );
        selectSiteOpenWizard( comboBoxContent.getContentTypeName() );
        ComboBoxFormViewPanel formViewPanel = new ComboBoxFormViewPanel( getSession() );

        then: "option filter input is present and enabled"
        formViewPanel.isOptionFilterInputEnabled();
        and:
        formViewPanel.getSelectedOptionValues().size() == 0;
    }

    def "GIVEN saving of ComboBox-content (0:1) without options WHEN content opened for edit THEN no one selected options present on page"()
    {
        given: "new content with type ComboBox0_1 added'"
        Content comboBoxContent = buildComboBox0_1_Content( 0 );
        selectSiteOpenWizard( comboBoxContent.getContentTypeName() ).typeData( comboBoxContent ).save().close(
            comboBoxContent.getDisplayName() );

        when: "content opened for edit"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( comboBoxContent );
        ComboBoxFormViewPanel formViewPanel = new ComboBoxFormViewPanel( getSession() );
        List<String> optValues = formViewPanel.getSelectedOptionValues();

        then: "no one options present in form view"
        optValues.size() == 0;

        and: "options filter input is enabled"
        formViewPanel.isOptionFilterInputEnabled();
    }

    def "GIVEN saving of  ComboBox-content (0:1) with one option WHEN content opened for edit THEN one selected option  present on page and options filter input is disabled"()
    {
        given: "new content with type ComboBox0_1 added'"
        content_with_opt = buildComboBox0_1_Content( 1 );
        selectSiteOpenWizard( content_with_opt.getContentTypeName() ).typeData( content_with_opt ).save().close(
            content_with_opt.getDisplayName() );

        when: "content opened for edit"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( content_with_opt );
        ComboBoxFormViewPanel formViewPanel = new ComboBoxFormViewPanel( getSession() );
        List<String> optValues = formViewPanel.getSelectedOptionValues();

        then: "one option value  present in form view"
        optValues.size() == 1;

        and: "option with correct text present"
        optValues.get( 0 ) == "option A";

        and: "options filter input is disabled"
        !formViewPanel.isOptionFilterInputEnabled();

    }

    def "GIVEN a existing new ComboBox 0:1 with one option  WHEN content opened and 'Publish' on toolbar pressed THEN it content with status equals 'Online' listed"()
    {
        given: "existing new ComboBox 0:0 with options'"
        ContentWizardPanel wizard = contentBrowsePanel.selectAndOpenContentFromToolbarMenu( content_with_opt );

        when: "type a data and 'save' and 'publish'"
        wizard.clickOnWizardPublishButton().clickOnPublishNowButton().waitForDialogClosed();
        wizard.close( content_with_opt.getDisplayName() );
        filterPanel.typeSearchText( content_with_opt.getName() );

        then: "content has a 'online' status"
        contentBrowsePanel.getContentStatus( content_with_opt.getName() ).equalsIgnoreCase( ContentStatus.ONLINE.getValue() );
    }

    def "GIVEN ComboBox-content (0:1) with one selected option and one option removed and content saved WHEN content opened for edit THEN no options selected on the page "()
    {
        given: "content with tree options opened for edit' and one option removed"
        ContentWizardPanel wizard = contentBrowsePanel.selectAndOpenContentFromToolbarMenu( content_with_opt );
        ComboBoxFormViewPanel formViewPanel = new ComboBoxFormViewPanel( getSession() );
        formViewPanel.clickOnLastRemoveButton();
        wizard.save().close( content_with_opt.getDisplayName() );

        when: "when content opened for edit again"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( content_with_opt );

        then: "no options selected on the page "
        List<String> optValues = formViewPanel.getSelectedOptionValues();
        optValues.size() == 0;

        and: "and options filter input enabled again"
        formViewPanel.isOptionFilterInputEnabled();
    }

    def "WHEN content without option saved and published THEN just created content with status equals 'Online' listed"()
    {
        given: "content without option saved and published"
        Content comboBoxContent = buildComboBox0_1_Content( 0 );
        selectSiteOpenWizard( comboBoxContent.getContentTypeName() ).typeData(
            comboBoxContent ).save().clickOnWizardPublishButton().waitUntilDialogShowed(
            Application.EXPLICIT_NORMAL ).clickOnPublishNowButton().waitForDialogClosed();
        ContentWizardPanel.getWizard( getSession() ).close( comboBoxContent.getDisplayName() );

        when:
        filterPanel.typeSearchText( comboBoxContent.getName() );

        then: "content has a 'online' status"
        contentBrowsePanel.getContentStatus( comboBoxContent.getName() ).equalsIgnoreCase( ContentStatus.ONLINE.getValue() );
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