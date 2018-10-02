package com.enonic.wem.uitest.content.liveedit

import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.DependenciesWidgetItemView
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.PageComponentsViewDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.context_window.FragmentInspectionPanel
import com.enonic.autotests.pages.form.liveedit.LiveFormPanel
import com.enonic.autotests.pages.form.liveedit.PartComponentView
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Shared
import spock.lang.Stepwise

/**
 * Created on 8/9/2017.
 *
 * Tasks:
 * xp-ui-testing#80  Add Selenium tests for checking of fragments on the Inspection Panel
 *
 * Verifies:
 * XP#5443 Inspection Panel - fragments from another sites should be available in the FragmentDropdown selector
 * XP #5454 Inspection Panel - DropDown list of options not updated, when new fragment has been created
 * */
@Stepwise
class Fragment_Change_In_InspectionPanel_Spec
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

    @Shared
    String PART_CITIES_DISTANCE_FACET = "Cities Distance Facet";

    def "GIVEN site wizard is opened and new part inserted WHEN new fragment has been created from the part THEN the fragment should be present in options on the Inspection Panel"()
    {
        given: "site wizard  is opened and the controller has been selected"
        SITE1 = buildSiteBasedOnFirstApp();
        ContentWizardPanel siteWizard = contentBrowsePanel.clickToolbarNew().selectContentType( SITE1.getContentTypeName() ).typeData(
            SITE1 ).selectPageDescriptor( "main region" );
        and: "'Component View' has been opened"
        PageComponentsViewDialog pageComponentsView = siteWizard.showComponentView();

        and: "Country-part has been inserted and 'Inspection Panel' should be opened"
        insertPart( pageComponentsView, siteWizard, PART_FRAGMENT_CITY_LIST )

        and: "new fragment has been created from the part"
        siteWizard.showComponentView();
        pageComponentsView.openMenu( PART_FRAGMENT_CITY_LIST ).selectMenuItem( "Save as Fragment" );
        saveScreenshot( "fragment_inspection_panel1" );
        sleep( 1000 );

        when: "'Dropdown handle' has been clicked"
        FragmentInspectionPanel fragmentInspectionPanel = new FragmentInspectionPanel( getSession() );
        fragmentInspectionPanel.clickOnFragmentDropdownHandle();

        then: "one option should be present in the list"
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
        pageComponentsView.openMenu( PART_FRAGMENT_CITY_CREATION ).selectMenuItem( "Save as Fragment" );
        sleep( 1000 );
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

    def "GIVEN existing site with two fragments is opened WHEN Page Component View has been opened and a fragment clicked THEN correct selected option should be present on the Inspection panel"()
    {
        given: "existing site with two fragments is opened"
        ContentWizardPanel siteWizard = findAndSelectContent( SITE1.getName() ).clickToolbarEdit();
        PageComponentsViewDialog pageComponentsView = siteWizard.showComponentView();

        when: "Page Component View has been opened and a fragment clicked"
        pageComponentsView.clickOnFragment( PART_FRAGMENT_CITY_CREATION );
        FragmentInspectionPanel fragmentInspectionPanel = new FragmentInspectionPanel( getSession() );

        then: "'Inspection Panel' should be opened"
        fragmentInspectionPanel.waitForOpened();
        saveScreenshot( "fragment_clicked_inspection_panel" );

        and: "correct selected option should be displayed"
        fragmentInspectionPanel.getSelectedFragmentDisplayName() == PART_FRAGMENT_CITY_CREATION;
    }

    def "GIVEN existing site with two fragments is opened WHEN one of the fragments has been changed on the Inspection Panel THEN changes should be automatically applied on the LiveEdit"()
    {
        given: "existing site with two fragments is opened"
        ContentWizardPanel siteWizard = findAndSelectContent( SITE1.getName() ).clickToolbarEdit();
        PageComponentsViewDialog pageComponentsView = siteWizard.showComponentView();

        when: "the first fragments on the Page Component View has been clicked"
        pageComponentsView.clickOnFragment( PART_FRAGMENT_CITY_CREATION );
        FragmentInspectionPanel fragmentInspectionPanel = new FragmentInspectionPanel( getSession() );
        fragmentInspectionPanel.waitForOpened();
        and: "second fragment has been selected in the dropdown options"
        fragmentInspectionPanel.clickOnFragmentDropdownHandle().clickOnDropdownOption( PART_FRAGMENT_CITY_LIST );
        siteWizard.switchToLiveEditFrame();
        sleep( 1000 );
        LiveFormPanel liveFormPanel = new LiveFormPanel( getSession() );

        then: "changes should be automatically applied on the LiveEdit"
        List<String> displayNames = liveFormPanel.getFragmentDisplayNames();
        displayNames.get( 0 ).contains( PART_FRAGMENT_CITY_LIST );
        saveScreenshot( "fragment_changed_inspection_panel" );

        and: ""
        displayNames.get( 1 ).contains( PART_FRAGMENT_CITY_LIST );
    }

    def "GIVEN existing site with fragments WHEN the site has been clicked AND 'Show Outbound' button pressed THEN required fragment should be filtered"()
    {
        DependenciesWidgetItemView dependenciesWidget = findAndSelectContent(
            SITE1.getName() ).openContentDetailsPanel().openDependenciesWidget();

        when: "'Show Outbound' button has been pressed"
        dependenciesWidget.clickOnShowOutboundButton();
        sleep( 2000 );
        NavigatorHelper.switchToNextTab( getTestSession() );

        then: "required fragment should be filtered"
        contentBrowsePanel.getContentDisplayNamesFromGrid().size() == 1;
        and: ""
        contentBrowsePanel.getContentDisplayNamesFromGrid().get( 0 ) == PART_FRAGMENT_CITY_LIST;
    }

    def "GIVEN existing site with fragments AND wizard for new wizard is opened WHEN new fragment has been added THEN fragments from the first site should be available in the second site"()
    {
        given: "site wizard  is opened and the controller has been selected"
        SITE2 = buildSiteBasedOnFirstApp();
        ContentWizardPanel siteWizard = contentBrowsePanel.clickToolbarNew().selectContentType( SITE2.getContentTypeName() ).typeData(
            SITE2 ).selectPageDescriptor( "main region" );

        and: "'Component View' has been opened"
        PageComponentsViewDialog pageComponentsView = siteWizard.showComponentView();

        and: "Country-part has been inserted and 'Inspection Panel' should be opened"
        insertPart( pageComponentsView, siteWizard, PART_CITIES_DISTANCE_FACET )

        and: "new fragment has been created from the part"
        siteWizard.showComponentView();
        pageComponentsView.openMenu( PART_CITIES_DISTANCE_FACET ).selectMenuItem( "Save as Fragment" );
        sleep( 1000 );
        saveScreenshot( "fragment_cities_distance" )

        when: "'Dropdown handle' has been clicked"
        FragmentInspectionPanel fragmentInspectionPanel = new FragmentInspectionPanel( getSession() );
        fragmentInspectionPanel.clickOnFragmentDropdownHandle();

        then: "fragments from the first site should be available in the second site"
        List<String> options = fragmentInspectionPanel.getDropdownOptions();
        !options.contains( CITY_CREATION_PART );
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
