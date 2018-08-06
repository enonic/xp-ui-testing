package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.ItemSetViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.autotests.vo.contentmanager.TestItemSet
import com.enonic.xp.data.PropertyTree
import spock.lang.Shared
import spock.lang.Stepwise

/**
 * Created  on 15.11.2016.
 *
 * Task:  XP-4475 Add selenium tests for ItemSet content: add two items and swap it
 *
 * Verifies:
 *    XP-4277 After an item-set has been drag'n'dropped, HTML Area inside stops working
 *    XP-4397 After an item-set has been drag'n'dropped, HTML Area inside stops working
 * */
@Stepwise
class ItemSet_Swap_Values_Spec
    extends Base_InputFields_Occurrences
{
    @Shared
    String HTML_AREA_TEXT1 = "text for htmlArea 1"

    @Shared
    String HTML_AREA_TEXT2 = "text for htmlArea 2"

    @Shared
    String TEXT_LINE_TEXT1 = "text line 1";

    @Shared
    String TEXT_LINE_TEXT2 = "text line 2";

    @Shared
    Content ITEM_SET_WITH_DATA;

    def "GIVEN creating of ItemSet content with 2 sets WHEN data typed AND Save button pressed THEN content is valid and publish button is getting enabled"()
    {
        given: "wizard for adding of new ItemSet content is opened"
        ITEM_SET_WITH_DATA = buildItemSetWithTwoTextLineAndHtmlArea();
        ContentWizardPanel wizard = selectSitePressNew( ITEM_SET_WITH_DATA.getContentTypeName() );
        ItemSetViewPanel itemSetViewPanel = new ItemSetViewPanel( getSession() );

        when: "2 item sets was added"
        itemSetViewPanel.clickOnAddButton();
        itemSetViewPanel.clickOnAddButton();

        and: "data typed"
        wizard.typeData( ITEM_SET_WITH_DATA );

        and: "Save button pressed"
        wizard.save();
        saveScreenshot( "itemset_two_items_saved" );

        then: "content is valid, because all required inputs are filled"
        !itemSetViewPanel.isValidationMessagePresent();

        and: "Publish button is enabled"
        wizard.isPublishButtonEnabled();

        and: ""
        itemSetViewPanel.getNumberOfSets() == 2;
    }

    def "GIVEN existing itemSet content WHEN content opened THEN correct data is displayed on both sets"()
    {
        when: "existing itemSet content is opened"
        findAndSelectContent( ITEM_SET_WITH_DATA.getName() ).clickToolbarEdit();
        ItemSetViewPanel itemSetViewPanel = new ItemSetViewPanel( getSession() );

        then: "correct data is displayed on both sets"
        itemSetViewPanel.getInnerTextFromHtmlAreas().contains( "<p>"+HTML_AREA_TEXT1 +"</p>")
        and:
        itemSetViewPanel.getInnerTextFromHtmlAreas().contains("<p>"+ HTML_AREA_TEXT2 +"</p>");
        and:
        itemSetViewPanel.getTextFromTextLines().contains( TEXT_LINE_TEXT1 );
        and:
        itemSetViewPanel.getTextFromTextLines().contains( TEXT_LINE_TEXT2 );

    }

    def "GIVEN existing ItemSet content with 2 sets WHEN sets have been swapped THEN texts displayed in correct order"()
    {
        given: "existing itemSet content is opened"
        findAndSelectContent( ITEM_SET_WITH_DATA.getName() ).clickToolbarEdit();
        ItemSetViewPanel itemSetViewPanel = new ItemSetViewPanel( getSession() );

        when: "sets have been swapped"
        itemSetViewPanel.doSwapItems();

        then: "texts displayed in correct order"
        itemSetViewPanel.getTextFromTextLines().get( 1 ) == TEXT_LINE_TEXT1;

        and: ""
        itemSetViewPanel.getTextFromTextLines().get( 0 ) == TEXT_LINE_TEXT2;
    }

    private Content buildItemSetWithTwoTextLineAndHtmlArea()
    {
        TestItemSet itemSet1 = buildItemSetValues( TEXT_LINE_TEXT1, HTML_AREA_TEXT1 );
        TestItemSet itemSet2 = buildItemSetValues( TEXT_LINE_TEXT2, HTML_AREA_TEXT2 );
        List<TestItemSet> items = new ArrayList<>();
        items.add( itemSet1 );
        items.add( itemSet2 );
        PropertyTree data = build_ItemSet_Data( items );
        return buildItemSetContentWitData( data );
    }
}
