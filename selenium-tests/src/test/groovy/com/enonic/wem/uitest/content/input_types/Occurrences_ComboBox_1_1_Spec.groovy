package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.ContentPublishDialog
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

    @Shared
    Content comboBox1_1

    def "GIVEN wizard for adding a ComboBox-content(1:1) opened WHEN name typed and no options selected on the page THEN option filter input should be present"()
    {
        given: "start to add a content with type 'ComboBox 1:1'"
        Content comboBoxContent = buildComboBox1_1_Content( 0 );
        ContentWizardPanel wizard = selectSitePressNew( comboBoxContent.getContentTypeName() );

        when: "only the name typed and no option selected"
        wizard.typeDisplayName( comboBoxContent.getDisplayName() );
        ComboBoxFormViewPanel formViewPanel = new ComboBoxFormViewPanel( getSession() );

        then: "option filter input is present and enabled"
        formViewPanel.isOptionFilterInputEnabled();

        and: "and there are no selected options on the page"
        formViewPanel.getSelectedOptionValues().size() == 0;

        and: "the content should be valid, required option is not selected"
        wizard.isContentInvalid();
    }

    def "GIVEN saving of ComboBox-content (1:1) without options WHEN content is opened THEN no one option should be selected and red circle should be displayed"()
    {
        given: "new content with type ComboBox1_1 added'"
        Content comboBoxContent = buildComboBox1_1_Content( 0 );
        selectSitePressNew( comboBoxContent.getContentTypeName() ).typeData(
            comboBoxContent ).save().closeBrowserTab().switchToBrowsePanelTab();

        when: "content is opened"
        ContentWizardPanel wizard = contentBrowsePanel.selectAndOpenContentFromToolbarMenu( comboBoxContent );
        ComboBoxFormViewPanel formViewPanel = new ComboBoxFormViewPanel( getSession() );
        List<String> optValues = formViewPanel.getSelectedOptionValues();

        then: "no one option is selected on the page"
        optValues.size() == 0;

        and: "options filter input should be enabled"
        formViewPanel.isOptionFilterInputEnabled();

        and: "content should be invalid, because required field- combobox1:1 not selected"
        formViewPanel.isValidationMessagePresent();

        and: "red circle should be displayed on the wizard page"
        wizard.isContentInvalid();

        and: "button 'Publish' should be disabled"
        !wizard.isPublishButtonEnabled();
    }

    def "GIVEN ComboBox-content (1:1) with one option has been added WHEN the content is opened THEN one selected option  should be present and options filter input is disabled"()
    {
        given: "ComboBox-content (1:1) with one option has been added"
        content_with_opt = buildComboBox1_1_Content( 1 );
        selectSitePressNew( content_with_opt.getContentTypeName() ).typeData(
            content_with_opt ).save().closeBrowserTab().switchToBrowsePanelTab();

        when: "content is opened"
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
        !formViewPanel.isValidationMessagePresent();

        and: "red circle should not be displayed on the page"
        !wizard.isContentInvalid();
    }

    def "GIVEN ComboBox-content (1:1) with one selected option and one option removed and content saved WHEN content opened for edit THEN no options selected on the page"()
    {
        given: "content with one required option opened for edit' and one option removed"
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
        formViewPanel.isValidationMessagePresent();

        and: "red circle should be displayed"
        wizard.isContentInvalid();
    }

    def "WHEN content with selected option has been published THEN the content with status equals 'Online' should be displayed"()
    {
        when: "content with selected option has been published"
        comboBox1_1 = buildComboBox1_1_Content( 1 );
        ContentWizardPanel wizard = selectSitePressNew( comboBox1_1.getContentTypeName() );
        wizard.typeData( comboBox1_1 ).save().clickOnWizardPublishButton().clickOnPublishNowButton();
        String publishMessage = contentBrowsePanel.waitPublishNotificationMessage( Application.EXPLICIT_NORMAL );
        wizard.closeBrowserTab().switchToBrowsePanelTab();
        filterPanel.typeSearchText( comboBox1_1.getName() );

        then: "content should be with 'Published' status"
        contentBrowsePanel.getContentStatus( comboBox1_1.getName() ).equalsIgnoreCase( ContentStatus.PUBLISHED.getValue() );
        and:
        //publishMessage == String.format( Application.ONE_CONTENT_PUBLISHED_NOTIFICATION_MESSAGE, comboBox1_1.getDisplayName() );
        publishMessage ==
            String.format( Application.ONE_CONTENT_PUBLISHED_NOTIFICATION_MESSAGE_TMP, "/" + SITE_NAME + "/" + comboBox1_1.getName() );
    }

    def "GIVEN not valid content with 'modified' status WHEN content selected and 'Delete' pressed THEN content with 'Deleted' status should be displayed"()
    {
        given: "not valid content with 'modified' status"
        ContentWizardPanel wizard = contentBrowsePanel.selectAndOpenContentFromToolbarMenu( comboBox1_1 );
        ComboBoxFormViewPanel formViewPanel = new ComboBoxFormViewPanel( getSession() );
        formViewPanel.clickOnLastRemoveButton();
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();

        when: "content selected and 'Delete' has been pressed"
        filterPanel.typeSearchText( comboBox1_1.getName() );
        contentBrowsePanel.clickToolbarDelete().doDelete();
        then:
        contentBrowsePanel.getContentStatus( comboBox1_1.getName() ).equalsIgnoreCase( ContentStatus.DELETED.getValue() );
        and: "the content should be displayed as not valid, because required option was removed"
        contentBrowsePanel.isContentInvalid( comboBox1_1.getName() )
    }

    def "GIVEN not valid content with 'Deleted' status WHEN content selected and 'Publish' button pressed THEN content should not be deleted"()
    {
        given: "not valid content with status is 'Deleted'"
        filterPanel.typeSearchText( comboBox1_1.getName() );
        contentBrowsePanel.clickCheckboxAndSelectRow( comboBox1_1.getName() );
        ContentPublishDialog dialog = contentBrowsePanel.clickToolbarPublish();

        when: "content was selected and 'Publish' button pressed"
        dialog.clickOnPublishNowButton();
        saveScreenshot( "invalid_cb_1_1_published" );

        then: "content should not be deleted"
        !contentBrowsePanel.exists( comboBox1_1.getName() )
    }

    def "GIVEN opened content wizard WHEN required text input is empty saved and wizard closed THEN grid row with it content has a red icon"()
    {
        given: "new content with type date time added'"
        Content comboBoxContent = buildComboBox1_1_Content( 0 );
        ContentWizardPanel wizard = selectSitePressNew( comboBoxContent.getContentTypeName() ).typeData( comboBoxContent );

        when: "content opened for edit"
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();
        findAndSelectContent( comboBoxContent.getName() );
        saveScreenshot( "combobox-not-valid" )

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
            contentType( ALL_CONTENT_TYPES_APP_NAME + ":combobox1_1" ).data( data ).
            build();
        return textLineContent;
    }
}