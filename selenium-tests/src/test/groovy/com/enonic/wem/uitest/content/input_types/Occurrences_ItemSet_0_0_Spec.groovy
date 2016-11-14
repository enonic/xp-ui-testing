package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.AllContentVersionsView
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.ContentDetailsPanel
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.ContentVersionInfoView
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.ItemSetViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.autotests.vo.contentmanager.TestItemSet
import com.enonic.xp.data.PropertyTree
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Stepwise

/**
 * Created  on 10.11.2016.
 * Tasks:
 *   XP-4439 Add selenium tests for occurrences of ItemSet
 *   XP-4450 Add selenium tests for saving of data in the ItemSet content
 *   XP-4467 Add selenium tests for restoring of version and publishing of ItemSet
 *
 * Verifies Bug: XP-4422 ItemSet content wizard - 'save before close' should appear, when there are unsaved changes
 *
 * */
@Stepwise
class Occurrences_ItemSet_0_0_Spec
    extends Base_InputFields_Occurrences
{

    @Shared
    String ITEM_SET_STEP = "Item set"

    @Shared
    Content ITEM_SET_CONTENT;

    @Shared
    Content ITEM_SET_WITH_DATA;

    @Shared
    String TEST_TEXT_HTML_AREA = "text for htmlArea 1"

    @Shared
    String NEW_TEXT_HTML_AREA = "new text for htmlArea 1"

    @Shared
    String TEST_TEXT_TEXT_LINE = "text line 1";


    def "WHEN wizard for adding of new ItemSet content is opened THEN 'Add ItemSet' button is displayed"()
    {
        when: "wizard for adding of new ItemSet content is opened"
        Content itemSetContent = buildItemSetContent();
        ContentWizardPanel wizard = selectSiteOpenWizard( itemSetContent.getContentTypeName() );
        ItemSetViewPanel itemSetViewPanel = new ItemSetViewPanel( getSession() );

        then: "required wizard's step is present "
        wizard.isWizardStepPresent( ITEM_SET_STEP );

        and: "'Add ItemSet' button is displayed"
        itemSetViewPanel.isAddButtonPresent();
    }

    def "GIVEN creating of ItemSet WHEN 'Add ItemSet' button pressed THEN Item Set with text line and htmlArea are displayed AND 'Add ItemSet' button is displayed"()
    {
        given: "wizard for adding of new ItemSet content is opened"
        ITEM_SET_CONTENT = buildItemSetContent();
        ContentWizardPanel wizard = selectSiteOpenWizard( ITEM_SET_CONTENT.getContentTypeName() );
        ItemSetViewPanel itemSetViewPanel = new ItemSetViewPanel( getSession() );

        when: "required wizard's step is present "
        itemSetViewPanel.clickOnAddButton();
        saveScreenshot( "1_item_set_added" );

        and: "name and display name were typed"
        wizard.typeData( ITEM_SET_CONTENT ).save();

        then: "'Add ItemSet' button is displayed"
        itemSetViewPanel.isAddButtonPresent();

        and: "Item Set with text line and htmlArea are displayed"
        itemSetViewPanel.isFormItemSetDisplayed();

        and: "content is not valid, because both required inputs are not filled"
        wizard.isContentInvalid( ITEM_SET_CONTENT.getDisplayName() );

        and: "Publish button is disabled"
        !wizard.isPublishButtonEnabled();

        and: "one Item Set is displayed"
        itemSetViewPanel.getNumberOfSets() == 1;
    }

    def "GIVEN existing content content with one added ItemSet WHEN one more ItemSet was added THEN two Item Sets are displayed"()
    {
        given: "existing ItemSet content with one added set"
        findAndSelectContent( ITEM_SET_CONTENT.getName() ).clickToolbarEdit();
        ItemSetViewPanel itemSetViewPanel = new ItemSetViewPanel( getSession() );

        when: "'Add ItemSet' button was clicked"
        itemSetViewPanel.clickOnAddButton();
        saveScreenshot( "2_item_set_added" );

        then: " two Item Sets are displayed"
        itemSetViewPanel.getNumberOfSets() == 2;

        and: "'Add ItemSet' button is displayed"
        itemSetViewPanel.isAddButtonPresent();
    }

    def "GIVEN existing content with one added ItemSet WHEN the ItemSet has been removed THEN no one ItemSet are displayed "()
    {
        given: "existing ItemSet content with one added set"
        findAndSelectContent( ITEM_SET_CONTENT.getName() ).clickToolbarEdit();
        ItemSetViewPanel itemSetViewPanel = new ItemSetViewPanel( getSession() );

        when: "the ItemSet has been removed"
        itemSetViewPanel.removeOneItem();
        saveScreenshot( "item_set_removed" );

        then: "no one ItemSet are displayed"
        itemSetViewPanel.getNumberOfSets() == 0;

        and: "'Add ItemSet' button is displayed"
        itemSetViewPanel.isAddButtonPresent();
    }
    //Verifies Bug: XP-4422 ItemSet content wizard - 'save before close' should appear, when there are unsaved changes
    @Ignore
    def "GIVEN existing content with one added ItemSet WHEN the ItemSet has been removed AND close button pressed THEN 'Save Before Close' dialog should appear"()
    {
        given: "existing ItemSet content with one added set"
        ContentWizardPanel wizard = findAndSelectContent( ITEM_SET_CONTENT.getName() ).clickToolbarEdit();
        ItemSetViewPanel itemSetViewPanel = new ItemSetViewPanel( getSession() );

        when: "the ItemSet has been removed"
        itemSetViewPanel.removeOneItem();

        and: "Close button pressed"
        def dialog = wizard.close( ITEM_SET_CONTENT.getDisplayName() );

        then: "'Save Before Close' dialog should appear"
        dialog != null;
    }

    def "GIVEN creating of ItemSet with data WHEN data typed and saved THEN content is getting valid"()
    {
        given: "wizard for adding of new ItemSet content is opened"
        ITEM_SET_WITH_DATA = buildItemSetWithOneTextLineAndHtmlArea();
        ContentWizardPanel wizard = selectSiteOpenWizard( ITEM_SET_WITH_DATA.getContentTypeName() );
        ItemSetViewPanel itemSetViewPanel = new ItemSetViewPanel( getSession() );

        when: "all required inputs are filled"
        itemSetViewPanel.clickOnAddButton();
        wizard.typeData( ITEM_SET_WITH_DATA );
        saveScreenshot( "1_item_set_with_data_added" );

        and: "Save button pressed"
        wizard.save();

        then: "content is valid, because all required inputs are filled"
        !wizard.isContentInvalid( ITEM_SET_WITH_DATA.getDisplayName() );

        and: "Publish button is enabled"
        wizard.isPublishButtonEnabled();
    }

    def "GIVEN existing ItemSet-content with saved data WHEN content opened THEN correct text is present in the text-line and html-area inputs"()
    {
        when: "wizard for adding of new ItemSet content is opened"
        ContentWizardPanel wizard = findAndSelectContent( ITEM_SET_WITH_DATA.getName() ).clickToolbarEdit();
        ItemSetViewPanel itemSetViewPanel = new ItemSetViewPanel( getSession() );

        then: "correct text is present in html-area inputs"
        itemSetViewPanel.getInnerTextFromHtmlAreas().get( 0 ) == TEST_TEXT_HTML_AREA;

        and: "correct text is displayed in the text-line"
        itemSetViewPanel.getTextFromTextLines().get( 0 ) == TEST_TEXT_TEXT_LINE;

        and: "content is valid, because all required inputs are filled"
        !wizard.isContentInvalid( ITEM_SET_WITH_DATA.getDisplayName() );

        and: "Publish button is enabled"
        wizard.isPublishButtonEnabled();
    }

    def "GIVEN existing ItemSet-content with saved valid data WHEN the content has been published THEN 'online' status is displayed"()
    {
        given: "existing ItemSet-content with saved valid data"
        ContentWizardPanel wizard = findAndSelectContent( ITEM_SET_WITH_DATA.getName() ).clickToolbarEdit();

        when: "the content has been published"
        wizard.clickOnWizardPublishButton().waitUntilDialogShown(
            Application.EXPLICIT_NORMAL ).clickOnPublishNowButton().waitForDialogClosed();

        then: "'online' status is displayed"
        wizard.getStatus() == ContentStatus.ONLINE.getValue();
    }

    def "GIVEN existing ItemSet-content with saved valid data WHEN text in the htmlArea changed THEN 'modified' status is displayed"()
    {
        given: "existing ItemSet-content with saved valid data"
        ContentWizardPanel wizard = findAndSelectContent( ITEM_SET_WITH_DATA.getName() ).clickToolbarEdit();
        ItemSetViewPanel itemSetViewPanel = new ItemSetViewPanel( getSession() );

        when: "the content has been published"
        itemSetViewPanel.typeTextInHtmlArea( NEW_TEXT_HTML_AREA );
        wizard.save();

        then: "'online' status is displayed"
        wizard.getStatus() == ContentStatus.MODIFIED.getValue();
    }

    def "GIVEN existing ItemSet-content with a several versions WHEN when the version with empty required fields is restored THEN ItemSet shown correctly"()
    {
        given: "content added selected and version history opened"
        ContentWizardPanel wizard = findAndSelectContent( ITEM_SET_WITH_DATA.getName() ).clickToolbarEdit();
        ContentDetailsPanel contentDetailsPanel = contentBrowsePanel.getContentDetailsPanel();

        and: "AppHome button was pressed"
        contentBrowsePanel.pressAppHomeButton();
        and: "details panel is opened"
        contentBrowsePanel.clickOnDetailsToggleButton();

        when: "when the version with empty required fields is restored"
        AllContentVersionsView allContentVersionsView = contentDetailsPanel.openVersionHistory();
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 1 );
        versionItem.doRestoreVersion( versionItem.getId() );
        saveScreenshot( "item_set_text_reverted" );

        and: "wizard-tab activated again"
        contentBrowsePanel.clickOnTab( ITEM_SET_WITH_DATA.getDisplayName() );
        ItemSetViewPanel itemSetViewPanel = new ItemSetViewPanel( getSession() );

        then: "correct text in the htmlArea is displayed"
        itemSetViewPanel.getInnerTextFromHtmlAreas().get( 0 ) == TEST_TEXT_HTML_AREA;

        and: "correct text in the text-line is displayed"
        itemSetViewPanel.getTextFromTextLines().get( 0 ) == TEST_TEXT_TEXT_LINE;

        then: "'online' status is displayed"
        wizard.getStatus() == ContentStatus.MODIFIED.getValue();
    }

    private Content buildItemSetWithOneTextLineAndHtmlArea()
    {
        TestItemSet itemSet1 = buildItemSetValues( TEST_TEXT_TEXT_LINE, TEST_TEXT_HTML_AREA );
        List<TestItemSet> items = new ArrayList<>();
        items.add( itemSet1 );
        PropertyTree data = build_ItemSet_Data( items );
        return buildItemSetContentWitData( data );
    }
}
