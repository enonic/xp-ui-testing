package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.ContentUtils
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.ComboBoxFormViewPanel
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.utils.SleepHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath
import com.enonic.xp.data.PropertyTree
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class Occurrences_ComboBox_0_0_Spec
    extends Base_InputFields_Occurrences

{

    @Shared
    Content content_wit_opt;

    def "WHEN wizard for adding a Combo box-content(0:0) opened THEN option filter input is present "()
    {
        when: "start to add a content with type 'ComboBox 0:0'"
        Content comboBoxContent = buildComboBox0_0_Content( 1 );
        contentBrowsePanel.clickCheckboxAndSelectRow( SITE_NAME ).clickToolbarNew().selectContentType(
            comboBoxContent.getContentTypeName() )
        ComboBoxFormViewPanel formViewPanel = new ComboBoxFormViewPanel( getSession() );

        then: "option filter input is present and enabled"
        formViewPanel.isOptionFilterInputEnabled();
        and:
        formViewPanel.getSelectedOptionValues().size() == 0;
    }

    def "GIVEN saving of Combobox-content (0:0) without options WHEN content opened for edit THEN no one selected options present in form view"()
    {
        given: "new content with type Combobbox0_0 added'"
        Content comboBoxContent = buildComboBox0_0_Content( 0 );
        contentBrowsePanel.clickCheckboxAndSelectRow( SITE_NAME ).clickToolbarNew().selectContentType(
            comboBoxContent.getContentTypeName() ).typeData( comboBoxContent ).save().close( comboBoxContent.getDisplayName() );

        when: "content opened for edit"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( comboBoxContent );
        ComboBoxFormViewPanel formViewPanel = new ComboBoxFormViewPanel( getSession() );
        List<String> optValues = formViewPanel.getSelectedOptionValues();


        then: "no one options present in form view"
        optValues.size() == 0;

        and: "options filter input is enabled"
        formViewPanel.isOptionFilterInputEnabled();

    }

    def "GIVEN saving of  Combobox-content (0:0) with one option WHEN content opened for edit THEN one selected option and button 'Remove' present in form view"()
    {
        given: "new content with type ComboBox 0_0 added"
        Content comboBoxContent = buildComboBox0_0_Content( 1 );
        contentBrowsePanel.clickCheckboxAndSelectRow( SITE_NAME ).clickToolbarNew().selectContentType(
            comboBoxContent.getContentTypeName() ).typeData( comboBoxContent ).save().close( comboBoxContent.getDisplayName() );

        when: "content opened for edit"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( comboBoxContent );
        ComboBoxFormViewPanel formViewPanel = new ComboBoxFormViewPanel( getSession() );
        List<String> optValues = formViewPanel.getSelectedOptionValues();

        then: "one option present in form view"
        optValues.size() == 1;

        and: "option has a correct text"
        optValues.get( 0 ) == "option A";

        and: "options filter input is enabled"
        formViewPanel.isOptionFilterInputEnabled();

    }

    def "GIVEN saving of  ComboBox-content (0:0) with three options WHEN content opened for edit THEN three selected options and buttons 'Remove' present on page "()
    {
        given: "new content with type ComboBox0_0 added'"
        content_wit_opt = buildComboBox0_0_Content( 3 );
        contentBrowsePanel.clickCheckboxAndSelectRow( SITE_NAME ).clickToolbarNew().selectContentType(
            content_wit_opt.getContentTypeName() ).typeData( content_wit_opt ).save().close( content_wit_opt.getDisplayName() );

        when: "content opened for edit"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( content_wit_opt );
        ComboBoxFormViewPanel formViewPanel = new ComboBoxFormViewPanel( getSession() );
        List<String> optValues = formViewPanel.getSelectedOptionValues();

        then: "three options present in the form view"
        optValues.size() == 3;

        and: "and options have a correct text"
        String[] options = ["option A", "option B", "option C"];
        optValues.containsAll( options.toList() );

        and: "options filter input is enabled"
        formViewPanel.isOptionFilterInputEnabled();

    }
    //TODO remove it when sources will be updated on teamcity
    @Ignore
    def "GIVEN ComboBox-content (0:0) with three options and one option removed and content saved WHEN content opened for edit THEN two selected options and buttons 'Remove' present on the page "()
    {
        given: "content with tree options opened for edit' and one option removed"
        ContentWizardPanel wizard = contentBrowsePanel.selectAndOpenContentFromToolbarMenu( content_wit_opt );
        ComboBoxFormViewPanel formViewPanel = new ComboBoxFormViewPanel( getSession() );
        formViewPanel.clickOnLastRemoveButton();
        wizard.save().close( content_wit_opt.getDisplayName() );

        when: "when content opened for edit again"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( content_wit_opt );

        then: "only two options are present in the form view"
        List<String> optValues = formViewPanel.getSelectedOptionValues();
        TestUtils.saveScreenshot( getSession(), "00remove_opt" )
        optValues.size() == 2;

        and: "and options have a correct text"
        String[] options = ["option A", "option B"];
        optValues.containsAll( options.toList() );

    }

    def "GIVEN a existing new ComboBox 0:0 with options  WHEN content opened and 'Publish' on toolbar pressed THEN it content with status equals 'Online' listed"()
    {
        given: "existing new ComboBox 0:0 with options'"
        ContentWizardPanel wizard = contentBrowsePanel.selectAndOpenContentFromToolbarMenu( content_wit_opt );

        when: "type a data and 'save' and 'publish'"
        wizard.clickOnPublishButton().close( content_wit_opt.getDisplayName() );
        //TODO remove it
        SleepHelper.sleep( 2000 );
        TestUtils.saveScreenshot( getSession(), "occ" )
        filterPanel.clickOnCleanFilter().typeSearchText( content_wit_opt.getName() );

        then: "content has a 'online' status"
        contentBrowsePanel.getContentStatus( content_wit_opt.getPath() ).equals( ContentStatus.ONLINE.getValue() )
    }

    def "GIVEN a creating of a new ComboBox 0:0 without options WHEN content saved and 'Publish' on toolbar pressed THEN new content with status equals 'Online' listed"()
    {
        given: "a creating of a new ComboBox 0:0 without options"
        Content comboBoxContent = buildComboBox0_0_Content( 0 );
        ContentWizardPanel wizard = contentBrowsePanel.clickCheckboxAndSelectRow( SITE_NAME ).clickToolbarNew().selectContentType(
            comboBoxContent.getContentTypeName() ).typeData( comboBoxContent ).save();

        when: "type a data and 'save' and 'publish'"
        wizard.clickOnPublishButton().close( comboBoxContent.getDisplayName() );
        filterPanel.typeSearchText( comboBoxContent.getName() );

        then: "content has a 'online' status"
        contentBrowsePanel.getContentStatus( comboBoxContent.getPath() ).equals( ContentStatus.ONLINE.getValue() )
    }


    private Content buildComboBox0_0_Content( int numberOptions )
    {
        PropertyTree data = ContentUtils.buildComboBoxData( numberOptions );
        Content textLineContent = Content.builder().
            name( NameHelper.uniqueName( "cbox0_0_" ) ).
            displayName( "combobox0_0 content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_MODULE_NAME + ":combobox0_0" ).data( data ).
            build();
        return textLineContent;
    }

}