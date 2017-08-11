package com.enonic.wem.uitest.content.liveedit

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.PageComponentsViewDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.context_window.FragmentInspectionPanel
import com.enonic.autotests.pages.form.liveedit.PartComponentView
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Shared

/**
 * Created on 8/9/2017.
 *
 * Verifies: XP#5443 Inspection Panel - all fragments should be available in the FragmentDropdown selector
 * */
class Fragment_Change_In_InspectionPanel
    extends BaseContentSpec
{
    @Shared
    Content SITE1;

    @Shared
    Content SITE2;

    @Shared
    String PART_FRAGMENT_CITY_LIST = "Cities List";

    @Shared
    String PART_FRAGMENT_CITY_CREATION = "City Creation";


    def "GIVEN site wizard is opened and new part inserted WHEN new fragment has been created from the part THEN the fragment should be present in options on the Inspection Panel"()
    {
        given: "site wizard  is opened and the controller has been selected"
        SITE1 = buildSiteBasedOnFirstApp();
        ContentWizardPanel siteWizard = contentBrowsePanel.clickToolbarNew().selectContentType( SITE1.getContentTypeName() ).typeData(
            SITE1 ).selectPageDescriptor( "main region" ).save();
        and: "'Component View' has been opened"
        PageComponentsViewDialog pageComponentsView = siteWizard.showComponentView();

        and: "Country-part has been inserted and 'Inspection Panel' should be opened"
        insertPart( pageComponentsView, siteWizard, PART_FRAGMENT_CITY_LIST )

        and: "new fragment has been created from the part"
        siteWizard.showComponentView();
        pageComponentsView.openMenu( PART_FRAGMENT_CITY_LIST ).selectMenuItem( "Create Fragment" );
        saveScreenshot( "fragment_inspection_panel1" )

        when: "'Dropdown handle' has been clicked"
        FragmentInspectionPanel fragmentInspectionPanel = new FragmentInspectionPanel( getSession() );
        fragmentInspectionPanel.clickOnFragmentDropdownHandle();

        then: "drop down list should be expanded"
        fragmentInspectionPanel.isDropdownListExpanded();

        and: "one option should be present in the list"
        List<String> options = fragmentInspectionPanel.getDropdownOptions();
        options.size() == 1;
        and: "correct display name of fragment should be present"
        options.get( 0 ) == PART_FRAGMENT_CITY_LIST
    }

    def "GIVEN existing site with one fragment is opened WHEN one more fragment has been created THEN two fragments should be present in the dropdown list"()
    {
        given:
        ContentWizardPanel siteWizard = findAndSelectContent( SITE1.getName() ).clickToolbarEdit();
        PageComponentsViewDialog pageComponentsView = siteWizard.showComponentView();

        and: "Country-part has been inserted and 'Inspection Panel' should be opened"
        insertPart( pageComponentsView, siteWizard, PART_FRAGMENT_CITY_CREATION );

        and: "new fragment has been created from the part"
        siteWizard.showComponentView();
        pageComponentsView.openMenu( PART_FRAGMENT_CITY_CREATION ).selectMenuItem( "Create Fragment" );
        saveScreenshot( "fragment_inspection_panel2" )

        when: "'Dropdown handle' has been clicked"
        FragmentInspectionPanel fragmentInspectionPanel = new FragmentInspectionPanel( getSession() );
        fragmentInspectionPanel.clickOnFragmentDropdownHandle();


        then: "one option should be present in the list"
        List<String> options = fragmentInspectionPanel.getDropdownOptions();
        options.size() == 2;
        and: "correct display name of fragment should be present"
        options.get( 0 ) == PART_FRAGMENT_CITY_CREATION;

        and: "correct display name of fragment should be present"
        options.get( 1 ) == PART_FRAGMENT_CITY_LIST;
    }

    def "existing site with one fragment is opened WHEN Page Component View has been opened and a fragment clicked THEN correct selected option should be present on the Inspection panel"()
    {

    }

    private void insertPart( PageComponentsViewDialog pageComponentsView, ContentWizardPanel siteWizard, String partName )
    {
        pageComponentsView.openMenu( "main" ).selectMenuItem( "Insert", "Part" );
        pageComponentsView.doCloseDialog();
        siteWizard.switchToLiveEditFrame();
        PartComponentView partComponentView = new PartComponentView( getSession() );
        partComponentView.selectItem( partName );
        siteWizard.switchToDefaultWindow();
    }

}
