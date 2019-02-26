package com.enonic.wem.uitest.content.liveedit

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.context_window.PageInsertablesPanel
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

        when: "page controller was not selected"
        ContentWizardPanel wizardPanel = contentBrowsePanel.clickToolbarNew().selectContentType( TEST_SITE.getContentTypeName() ).typeData(
            TEST_SITE ).save();
        saveScreenshot( "test-site-controller-not-selected" );

        then: "'Components View' toggler should not be displayed"
        !wizardPanel.isComponentViewTogglerDisplayed();

        and: "'Context Panel' toggler should be displayed"
        wizardPanel.isInspectionPanelTogglerDisplayed()
    }

    def "GIVEN existing site without a controller is opened WHEN page controller has been selected THEN toggler buttons for 'Components View' and 'Inspection Panel' should be displayed"()
    {
        given: "existing site without a controller is opened"
        ContentWizardPanel siteWizard = findAndSelectContent( TEST_SITE.getName() ).clickToolbarEdit();

        when: "page controller has been selected"
        siteWizard.selectPageDescriptor( COUNTRY_REGION_PAGE_CONTROLLER ).switchToDefaultWindow(); ;
        saveScreenshot( "test-site-controller-selected" );

        then: "'Components View' toggler should be displayed"
        siteWizard.isComponentViewTogglerDisplayed();

    }

    def "GIVEN existing site is opened WHEN 'Insert' link has been clicked THEN 'Insertables' panel should be displayed AND all available components should be present on the panel"()
    {
        given: "existing site is opened"
        ContentWizardPanel siteWizard = findAndSelectContent( TEST_SITE.getName() ).clickToolbarEdit();

        when: "'Insert' link has been clicked"
        PageInsertablesPanel insertablesPanel = new PageInsertablesPanel( getSession() );
        siteWizard.showContextWindow().clickOnInsertLink();
        saveScreenshot( "insertables-panel-opened" );

        then: "'Insertables panel' should be displayed"
        insertablesPanel.isDisplayed();
        List<String> components = insertablesPanel.getAvailableComponents();

        and: "correct number of available components should be shown"
        components.size() == 5

        and: "'Image' component should be available"
        components.contains( "Image" );

        and: "'Part' component should be available"
        components.contains( "Part" );

        and: "'Layout' component should be available"
        components.contains( "Layout" );

        and: "'Text' component should be available"
        components.contains( "Text" );

        and: "'Fragment' component should be available"
        components.contains( "Fragment" );

        and: "'Drag and drop components into the page' title should be displayed"
        insertablesPanel.getTitle() == PageInsertablesPanel.TITLE
    }
    // verifies the xp#5580 Site Wizard - endless spinner appears when Show-Hide button was pressed in the second time
    def "GIVEN existing site is opened  AND 'Hide Page Editor' button has been clicked WHEN 'Show Page Editor' has been clicked THEN "()
    {
        given: "'Page Editor' for the existing site opened"
        ContentWizardPanel siteWizard = findAndSelectContent( TEST_SITE.getName() ).clickToolbarEdit();
        and: "'Hide Page Editor' button has been clicked"
        siteWizard.hidePageEditor();

        when: "'Show Page Editor' has been clicked"
        siteWizard.showPageEditor();
        then: "spinner should not be displayed after the few seconds "
        siteWizard.waitInvisibilityOfSpinner( Application.EXPLICIT_LONG );
        and: "'Page Editor' frame should be displayed"
        siteWizard.isLiveEditFrameDisplayed();
    }
}
