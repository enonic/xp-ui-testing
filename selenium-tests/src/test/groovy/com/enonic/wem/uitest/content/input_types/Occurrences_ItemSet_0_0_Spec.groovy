package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.ItemSetViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Stepwise

/**
 * Created  on 10.11.2016.
 * Tasks: XP-4439 Add selenium tests for occurrences of ItemSet
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
}
