package com.enonic.wem.uitest.content.liveedit

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContextWindowPageInsertablesPanel
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class ContextWindow_InsertPanel_Spec
    extends BaseContentSpec
{
    @Shared
    Content TEST_SITE;

    def "GIVEN creating of new site WHEN page controller is not selected THEN toggler buttons 'Components View' and 'Inspection Panel' should not be displayed"()
    {
        given: "creating of new site"
        TEST_SITE = buildMyFirstAppSite( "test-insert-panel" );

        when: "page controller is not selected"
        ContentWizardPanel wizardPanel = contentBrowsePanel.clickToolbarNew().selectContentType( TEST_SITE.getContentTypeName() ).typeData(
            TEST_SITE ).save();
        saveScreenshot( "test-site-controller-not-selected" );

        then: "'Components View' toggler should not be displayed"
        !wizardPanel.isComponentViewTogglerDisplayed();

        and: "'Inspection Panel' toggler should not be displayed"
        !wizardPanel.isInspectionPanelTogglerDisplayed()
    }

    def "GIVEN existing site without selected page controller WHEN page controller is  selected THEN toggler buttons for 'Components View' and 'Inspection Panel' should be displayed"()
    {
        given: "existing site without selected page controller is opened"
        ContentWizardPanel siteWizard = findAndSelectContent( TEST_SITE.getName() ).clickToolbarEdit();

        when: "page controller is not selected"
        siteWizard.selectPageDescriptor( COUNTRY_REGION_PAGE_CONTROLLER ).save();
        saveScreenshot( "test-site-controller-selected" );

        then: "'Components View' toggler should be displayed"
        siteWizard.isComponentViewTogglerDisplayed();

        and: "'Inspection Panel' toggler should be displayed"
        siteWizard.isInspectionPanelTogglerDisplayed()
    }

    def "GIVEN existing site is opened WHEN 'Insert' link clicked THEN 'Insertables' panel is displayed AND all available components should be present on the panel"()
    {
        given: "'Page Editor' for the existing site opened"
        ContentWizardPanel siteWizard = findAndSelectContent( TEST_SITE.getName() ).clickToolbarEdit();

        when: "'Inspect' link clicked"
        ContextWindowPageInsertablesPanel insertablesPanel = new ContextWindowPageInsertablesPanel( getSession() );
        siteWizard.showContextWindow().clickOnInsertLink();
        saveScreenshot( "insertables-panel-opened" );

        then: "'inspect panel' is displayed"
        insertablesPanel.isDisplayed();
        List<String> components = insertablesPanel.getAvailableComponents();

        and: "correct number of components are shown"
        components.size() == 5

        and: "'Image' component is present"
        components.contains( "Image" );

        and: "'Part' component is present"
        components.contains( "Part" );

        and: "'Layout' component is present"
        components.contains( "Layout" );

        and: "'Text' component is present"
        components.contains( "Text" );

        and: "'Fragment' component is present"
        components.contains( "Fragment" );

        and: "correct description is displayed"
        insertablesPanel.getTitle() == ContextWindowPageInsertablesPanel.TITLE
    }

}
