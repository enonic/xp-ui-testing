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

    def "GIVEN wizard for ItemSet content with 2 sets is opened WHEN data typed AND Save button pressed THEN content is valid and publish button is getting enabled"()
    {
        given: "wizard for new ItemSet content is opened"
        ITEM_SET_WITH_DATA = buildItemSetWithTwoTextLineAndHtmlArea();
        ContentWizardPanel wizard = selectSitePressNew( ITEM_SET_WITH_DATA.getContentTypeName() );
        ItemSetViewPanel itemSetViewPanel = new ItemSetViewPanel( getSession() );

        when: "2 item sets has been added"
        itemSetViewPanel.clickOnAddButton();
        itemSetViewPanel.clickOnAddButton();

        and: "data has been typed"
        wizard.typeData( ITEM_SET_WITH_DATA );

        and: "Save button has been pressed"
        wizard.save();
        saveScreenshot( "itemset_two_items_saved" );

        then: "content is valid, because all required inputs are filled"
        !itemSetViewPanel.isFormValidationMessageDisplayed();

        and: "Publish menu item gets enabled"
        wizard.showPublishMenu().isPublishMenuItemEnabled();

        and: ""
        itemSetViewPanel.getNumberOfSets() == 2;
    }

    def "GIVEN existing itemSet content is opened WHEN content opened THEN correct data is displayed on both sets"()
    {
        when: "existing itemSet content is opened"
        findAndSelectContent( ITEM_SET_WITH_DATA.getName() ).clickToolbarEdit();
        ItemSetViewPanel itemSetViewPanel = new ItemSetViewPanel( getSession() );

        then: "correct data should be displayed in both sets"
        itemSetViewPanel.getInnerTextFromHtmlAreas().contains( "<p>" + HTML_AREA_TEXT1 + "</p>" )
        and:
        itemSetViewPanel.getInnerTextFromHtmlAreas().contains( "<p>" + HTML_AREA_TEXT2 + "</p>" );
        and:
        itemSetViewPanel.getTextFromTextLines().contains( TEXT_LINE_TEXT1 );
        and:
        itemSetViewPanel.getTextFromTextLines().contains( TEXT_LINE_TEXT2 );

    }

    def "GIVEN existing ItemSet content with 2 sets is opened WHEN sets have been swapped THEN texts displayed in correct order"()
    {
        given: "existing itemSet content is opened"
        findAndSelectContent( ITEM_SET_WITH_DATA.getName() ).clickToolbarEdit();
        ItemSetViewPanel itemSetViewPanel = new ItemSetViewPanel( getSession() );
        sleep( 3000 );

        when: "2 sets have been swapped"
        itemSetViewPanel.doSwapItems();
        saveScreenshot( "item_set_area_swapped" );

        then: "texts should be displayed correct order"
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
