package com.enonic.wem.uitest.content.liveedit

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContextWindowPageInsertablesPanel
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class ContextWindow_InsertablesPanel_Spec
    extends BaseContentSpec
{
    @Shared
    Content TEST_SITE;

    def "GIVEN creating of new site WHEN page controller is not selected THEN toggler buttons 'Components View' and 'Inspection Panel' are not displayed"()
    {
        given: "creating of new site"
        TEST_SITE = buildMyFirstAppSite( "test-insertables-panel" );

        when: "page controller is not selected"
        ContentWizardPanel wizardPanel = contentBrowsePanel.clickToolbarNew().selectContentType( TEST_SITE.getContentTypeName() ).typeData(
            TEST_SITE ).save();
        TestUtils.saveScreenshot( getSession(), "test-site-controller-not-selected" );

        then: "'Components View' toggler is not displayed"
        !wizardPanel.isComponentViewTogglerDisplayed();

        and: "'Inspection Panel' toggler is not displayed"
        !wizardPanel.isInspectionPanelTogglerDisplayed()
    }

    def "GIVEN existing site without selected page controller WHEN page controller is  selected THEN toggler buttons 'Components View' and 'Inspection Panel' are displayed"()
    {
        given: "creating of new site"
        ContentWizardPanel siteWizard = findAndSelectContent( TEST_SITE.getName() ).clickToolbarEdit();

        when: "page controller is not selected"
        siteWizard.selectPageDescriptor( COUNTRY_REGION_PAGE_CONTROLLER ).save();
        TestUtils.saveScreenshot( getSession(), "test-site-controller-selected" );

        then: "'Components View' toggler is not displayed"
        siteWizard.isComponentViewTogglerDisplayed();

        and: "'Inspection Panel' toggler is not displayed"
        siteWizard.isInspectionPanelTogglerDisplayed()

    }

    def "GIVEN 'Page Editor' opened WHEN 'Insert' link clicked THEN 'Insertables' panel is displayed AND all available components are present on the panel"()
    {
        given: "'Page Editor' for the existing site opened"
        ContentWizardPanel siteWizard = findAndSelectContent( TEST_SITE.getName() ).clickToolbarEdit();

        when: "'Inspect' link clicked"
        ContextWindowPageInsertablesPanel insertablesPanel = new ContextWindowPageInsertablesPanel( getSession() );
        siteWizard.showContextWindow().clickOnInsertLink();
        TestUtils.saveScreenshot( getSession(), "insertables-panel-opened" );

        then: "'inspect panel' is displayed"
        insertablesPanel.isDisplayed();
        List<String> components = insertablesPanel.getAvailableComponents();

        and: "correct number of components are shown"
        components.size() == 5

        and: "'Image' component are present"
        components.contains( "Image" );

        and: "'Part' component are present"
        components.contains( "Part" );

        and: "'Layout' component are present"
        components.contains( "Layout" );

        and: "'Text' component are present"
        components.contains( "Text" );

        and: "'Fragment' component are present"
        components.contains( "Fragment" );

        and: "correct description displayed"
        insertablesPanel.getTitle() == ContextWindowPageInsertablesPanel.TITLE
    }

}
