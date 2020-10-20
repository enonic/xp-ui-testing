package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.VersionHistoryWidget
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.ContentDetailsPanel
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.ContentVersionInfoView
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.ItemSetViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.autotests.vo.contentmanager.TestItemSet
import com.enonic.xp.data.PropertyTree
import spock.lang.Shared
import spock.lang.Stepwise

/**
 * Created  on 10.11.2016.
 * Verifies Bug: XP-4422 ItemSet content wizard - 'save before close' should appear, when there are unsaved changes
 * */
@Stepwise
class Occurrences_ItemSet_0_0_Spec
    extends Base_InputFields_Occurrences
{

    @Shared
    String ITEM_SET_STEP = "item-set"

    @Shared
    Content ITEM_SET_CONTENT;

    @Shared
    Content ITEM_SET_WITH_DATA;

    @Shared
    String TEST_TEXT_HTML_AREA = "<p>text for htmlArea 1</p>"

    @Shared
    String NEW_TEXT_HTML_AREA = "new text for htmlArea 1"

    @Shared
    String TEST_TEXT_TEXT_LINE = "text line 1";

    def "WHEN wizard for ItemSet content is opened THEN 'Add ItemSet' button should be displayed"()
    {
        when: "wizard for ItemSet content is opened"
        Content itemSetContent = buildItemSetContent();
        ContentWizardPanel wizard = selectSitePressNew( itemSetContent.getContentTypeName() );
        ItemSetViewPanel itemSetViewPanel = new ItemSetViewPanel( getSession() );

        then: "required wizard's step is present "
        wizard.isWizardStepPresent( ITEM_SET_STEP );

        and: "'Add ItemSet' button should be displayed"
        itemSetViewPanel.isAddButtonPresent();
    }

    def "GIVEN creating of ItemSet WHEN 'Add ItemSet' button pressed THEN Item Set with text line and htmlArea are displayed AND 'Add ItemSet' button is displayed"()
    {
        given: "wizard for adding of new ItemSet content is opened"
        ITEM_SET_CONTENT = buildItemSetContent();
        ContentWizardPanel wizard = selectSitePressNew( ITEM_SET_CONTENT.getContentTypeName() );
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

        and: "content should be not valid, because both required inputs are not filled"
        wizard.isContentInvalid();

        and: "Publish button should be disabled"
        !wizard.showPublishMenu().isPublishMenuItemEnabled();

        and: "one Item Set should be displayed"
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
    def "GIVEN existing content with one added ItemSet WHEN the ItemSet has been removed AND close button pressed THEN 'Save Before Close' dialog should appear"()
    {
        given: "existing ItemSet content with one added set"
        ContentWizardPanel wizard = findAndSelectContent( ITEM_SET_CONTENT.getName() ).clickToolbarEdit();
        ItemSetViewPanel itemSetViewPanel = new ItemSetViewPanel( getSession() );

        when: "the ItemSet has been removed"
        itemSetViewPanel.removeOneItem();

        and: "Close button pressed"
        wizard.executeCloseWizardScript();

        then: "'Alert' dialog should appear"
        wizard.waitIsAlertDisplayed();
    }

    def "GIVEN new wizard for ItemSet is opened WHEN all requred data has been typed and saved THEN content is getting valid"()
    {
        given: "wizard for adding of new ItemSet content is opened"
        ITEM_SET_WITH_DATA = buildItemSetWithOneTextLineAndHtmlArea();
        ContentWizardPanel wizard = selectSitePressNew( ITEM_SET_WITH_DATA.getContentTypeName() );
        ItemSetViewPanel itemSetViewPanel = new ItemSetViewPanel( getSession() );

        when: "all required inputs are filled"
        itemSetViewPanel.clickOnAddButton();
        wizard.typeData( ITEM_SET_WITH_DATA );
        saveScreenshot( "1_item_set_with_data_added" );

        and: "Save button has been pressed"
        wizard.save();

        then: "Publish menu item should be enabled"
        wizard.showPublishMenu().isPublishMenuItemEnabled();

        and: "content should be valid, because all required inputs are filled"
        !wizard.isContentInvalid();
    }

    def "WHEN existing ItemSet-content is opened THEN expected text should be present in the text-line and in html-area inputs"()
    {
        when: "existing ItemSet-content is opened"
        ContentWizardPanel wizard = findAndSelectContent( ITEM_SET_WITH_DATA.getName() ).clickToolbarEdit();
        ItemSetViewPanel itemSetViewPanel = new ItemSetViewPanel( getSession() );

        then: "expected text is present in html-area inputs"
        itemSetViewPanel.getInnerTextFromHtmlAreas().get( 0 ) == TEST_TEXT_HTML_AREA;

        and: "expected text is displayed in the text-line"
        itemSetViewPanel.getTextFromTextLines().get( 0 ) == TEST_TEXT_TEXT_LINE;

        and: "content is valid, because all required inputs are filled"
        !wizard.isContentInvalid();

        and: "Publish menu item should be enabled"
        wizard.showPublishMenu().isPublishMenuItemEnabled();
    }

    def "GIVEN existing ItemSet-content is opened WHEN the content has been published THEN status gets 'Published'"()
    {
        given: "existing ItemSet-content with saved valid data"
        ContentWizardPanel wizard = findAndSelectContent( ITEM_SET_WITH_DATA.getName() ).clickToolbarEdit();

        when: "the content has been published"
        wizard.clickOnMarkAsReadyAndDoPublish();

        then: "'Published' status should be displayed"
        wizard.getStatus() == ContentStatus.PUBLISHED.getValue();
    }

    def "GIVEN existing ItemSet-content with saved valid data WHEN text in the htmlArea has been changed THEN status gets 'modified'"()
    {
        given: "existing ItemSet-content with saved valid data"
        ContentWizardPanel wizard = findAndSelectContent( ITEM_SET_WITH_DATA.getName() ).clickToolbarEdit();
        ItemSetViewPanel itemSetViewPanel = new ItemSetViewPanel( getSession() );

        when: "text in the htmlArea has been changed"
        itemSetViewPanel.typeTextInHtmlArea( NEW_TEXT_HTML_AREA );
        sleep( 700 );
        wizard.save().waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );

        then: "status gets 'modified'"
        wizard.getStatus() == ContentStatus.MODIFIED.getValue();
    }

    def "GIVEN existing ItemSet-content with a several versions WHEN when the previous version is restored THEN expected ItemSet-data should be displayed AND status gets modified"()
    {
        given: "content added selected and version history opened"
        ContentWizardPanel wizard = findAndSelectContent( ITEM_SET_WITH_DATA.getName() ).clickToolbarEdit();
        ContentDetailsPanel contentDetailsPanel = contentBrowsePanel.getContentDetailsPanel();

        and: "navigated to the browse panel"
        wizard.switchToBrowsePanelTab();
        and: "details panel is opened"
        contentBrowsePanel.openContentDetailsPanel();

        when: "when the previous version has been restored"
        VersionHistoryWidget allContentVersionsView = contentDetailsPanel.openVersionHistory();
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionItem( 1 );
        versionItem.doRevertVersion();

        and: "navigated to the wizard-tab again"
        contentBrowsePanel.switchToBrowserTabByTitle( ITEM_SET_WITH_DATA.getDisplayName() );
        sleep( 1500 );
        saveScreenshot( "item_set_text_reverted" );
        ItemSetViewPanel itemSetViewPanel = new ItemSetViewPanel( getSession() );

        then: "expected text in the htmlArea should be reverted"
        itemSetViewPanel.getInnerTextFromHtmlAreas().get( 0 ) == TEST_TEXT_HTML_AREA;

        and: "required text should be reverted in the text-line"
        itemSetViewPanel.getTextFromTextLines().get( 0 ) == TEST_TEXT_TEXT_LINE;

        then: "'Modified' status should be displayed in the wizard"
        wizard.getStatus() == ContentStatus.MODIFIED.getValue();
    }

    private Content buildItemSetWithOneTextLineAndHtmlArea()
    {
        TestItemSet itemSet1 = buildItemSetValues( TEST_TEXT_TEXT_LINE, "text for htmlArea 1" );
        List<TestItemSet> items = new ArrayList<>();
        items.add( itemSet1 );
        PropertyTree data = build_ItemSet_Data( items );
        return buildItemSetContentWitData( data );
    }
}
