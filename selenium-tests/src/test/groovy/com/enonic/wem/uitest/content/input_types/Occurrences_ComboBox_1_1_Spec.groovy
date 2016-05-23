package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.ContentPublishDialog
import com.enonic.autotests.pages.contentmanager.ContentUtils
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.ComboBoxFormViewPanel
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.utils.TestUtils
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

    def "GIVEN wizard for adding a ComboBox-content(1:1) opened WHEN name typed and no options selected on the page THEN option filter input is present"()
    {
        given: "start to add a content with type 'ComboBox 1:1'"
        Content comboBoxContent = buildComboBox1_1_Content( 0 );
        ContentWizardPanel wizard = selectSiteOpenWizard( comboBoxContent.getContentTypeName() );

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
        selectSiteOpenWizard( comboBoxContent.getContentTypeName() ).typeData( comboBoxContent ).save().close(
            comboBoxContent.getDisplayName() );

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
        selectSiteOpenWizard( content_with_opt.getContentTypeName() ).typeData( content_with_opt ).save().close(
            content_with_opt.getDisplayName() );

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
        when: "content with selected option saved and published"
        comboBox1_1 = buildComboBox1_1_Content( 1 );
        ContentWizardPanel wizard = selectSiteOpenWizard( comboBox1_1.getContentTypeName() );
        wizard.typeData( comboBox1_1 ).save().clickOnWizardPublishButton().clickOnPublishNowButton();
        String publishMessage = contentBrowsePanel.waitPublishNotificationMessage( Application.EXPLICIT_NORMAL );
        wizard.close( comboBox1_1.getDisplayName() );
        filterPanel.typeSearchText( comboBox1_1.getName() );

        then: "content has a 'online' status"
        contentBrowsePanel.getContentStatus( comboBox1_1.getName() ).equalsIgnoreCase( ContentStatus.ONLINE.getValue() );
        and:
        publishMessage == String.format( Application.CONTENT_PUBLISHED_NOTIFICATION_MESSAGE, comboBox1_1.getDisplayName() );
    }

    def "GIVEN not valid content with 'modified' status WHEN content selected and 'Delete' pressed THEN content is 'Pending Delete' "()
    {
        given: "content with selected option saved and published"
        ContentWizardPanel wizard = contentBrowsePanel.selectAndOpenContentFromToolbarMenu( comboBox1_1 );
        ComboBoxFormViewPanel formViewPanel = new ComboBoxFormViewPanel( getSession() );
        formViewPanel.clickOnLastRemoveButton();
        wizard.save().close( comboBox1_1.getDisplayName() );

        when: "content has a 'online' status"
        filterPanel.typeSearchText( comboBox1_1.getName() );
        contentBrowsePanel.clickToolbarDelete().doDelete();
        then:
        contentBrowsePanel.getContentStatus( comboBox1_1.getName() ).equalsIgnoreCase( ContentStatus.PENDING_DELETE.getValue() );
        and:
        contentBrowsePanel.isContentInvalid( comboBox1_1.getName() )
    }

    def "GIVEN not valid content with status is 'Pending Delete'  WHEN content selected and 'Publish' button pressed THEN content not listed in the grid "()
    {
        given: "not valid content with status is 'Pending Delete'"
        filterPanel.typeSearchText( comboBox1_1.getName() );
        contentBrowsePanel.clickCheckboxAndSelectRow( comboBox1_1.getName() );
        ContentPublishDialog dialog = contentBrowsePanel.clickToolbarPublish();

        when: "content selected and 'Publish' button pressed"
        dialog.clickOnPublishNowButton();
        TestUtils.saveScreenshot( getTestSession(), "invalid_cb_1_1_published" );

        then: "content not listed in the grid"
        !contentBrowsePanel.exists( comboBox1_1.getName() )

    }

    def "GIVEN creating new ComboBox-content (1:1) on root WHEN required text input is empty THEN button 'Publish' is disabled"()
    {
        when: "start to add a content with type 'ComboBox-content (1:1)'"
        Content comboBoxContent = buildComboBox1_1_Content( 0 );
        ContentWizardPanel contentWizardPanel = selectSiteOpenWizard( comboBoxContent.getContentTypeName() );

        then: "button 'Publish' is disabled"
        !contentWizardPanel.isPublishButtonEnabled();
    }

    def "GIVEN opened content wizard WHEN required text input is empty saved and wizard closed THEN grid row with it content has a red icon"()
    {
        given: "new content with type date time added'"
        Content comboBoxContent = buildComboBox1_1_Content( 0 );
        ContentWizardPanel wizard = selectSiteOpenWizard( comboBoxContent.getContentTypeName() ).typeData( comboBoxContent );

        when: "content opened for edit"
        wizard.save().close( comboBoxContent.getDisplayName() );
        findAndSelectContent( comboBoxContent.getName() );
        TestUtils.saveScreenshot( getSession(), "combobox-not-valid" )

        then: "content should be invalid, because required field not filled"
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