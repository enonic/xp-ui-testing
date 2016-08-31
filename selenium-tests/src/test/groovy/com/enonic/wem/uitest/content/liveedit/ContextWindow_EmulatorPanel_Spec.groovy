package com.enonic.wem.uitest.content.liveedit

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContextWindowPageEmulatorPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.EmulatorResolution
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Shared
import spock.lang.Stepwise

/**
 * Created on 29.08.2016.*/
@Stepwise
class ContextWindow_EmulatorPanel_Spec
    extends BaseContentSpec
{
    @Shared
    Content TEST_SITE;

    @Shared
    int MEDIUM_WIDTH = 375;

    @Shared
    int MEDIUM_HEIGHT = 667;

    @Shared
    int LARGE_WIDTH = 414;

    @Shared
    int LARGE_HEIGHT = 736;

    def "GIVEN creating of new site WHEN When context window opened and Emulator link clicked THEN Emulator panel is activated AND correct title displayed"()
    {
        given: "creating of new site"
        TEST_SITE = buildMyFirstAppSite( NameHelper.uniqueName( "emulator_panel" ) );
        ContentWizardPanel siteWizard = contentBrowsePanel.clickToolbarNew().selectContentType( TEST_SITE.getContentTypeName() ).typeData(
            TEST_SITE ).save();
        siteWizard.selectPageDescriptor( COUNTRY_REGION_PAGE_CONTROLLER ).save();

        when: "When context window opened and Emulator link clicked"
        siteWizard.showContextWindow().clickOnEmulatorLink();
        ContextWindowPageEmulatorPanel emulatorPanel = new ContextWindowPageEmulatorPanel( getSession() );
        TestUtils.saveScreenshot( getSession(), "emulator-panel-activated" );

        then: "emulator panel is displayed"
        emulatorPanel.isDisplayed();

        and: "correct title displayed on the panel"
        emulatorPanel.getTitle() == ContextWindowPageEmulatorPanel.TITLE;

        and: "correct title displayed on the panel"
        emulatorPanel.getAvailableResolutions().size() == 8;
    }

    def "GIVEN Emulator Panel opened WHEN resolution changed THEN correct size of frame applied"()
    {
        given: " Emulator Panel opened "
        ContentWizardPanel wizard = findAndSelectContent( TEST_SITE.getName() ).clickToolbarEdit();
        wizard.showContextWindow().clickOnEmulatorLink();
        ContextWindowPageEmulatorPanel emulatorPanel = new ContextWindowPageEmulatorPanel( getSession() );

        when: "Medium resolution selected"
        emulatorPanel.selectResolution( EmulatorResolution.MEDIUM_PHONE );
        TestUtils.saveScreenshot( getSession(), "emulator_375_667" );

        then: "editor get correct width"
        wizard.getWidthOfPageEditor() == MEDIUM_WIDTH;

        and: "editor get correct height"
        wizard.getHeightOfPageEditor() == MEDIUM_HEIGHT;
    }

    def "GIVEN Emulator Panel opened WHEN resolution changed to LARGE_TELEPHONE THEN correct size of frame applied"()
    {
        given: " Emulator Panel opened "
        ContentWizardPanel wizard = findAndSelectContent( TEST_SITE.getName() ).clickToolbarEdit();
        wizard.showContextWindow().clickOnEmulatorLink();
        ContextWindowPageEmulatorPanel emulatorPanel = new ContextWindowPageEmulatorPanel( getSession() );

        when: "Medium resolution selected"
        emulatorPanel.selectResolution( EmulatorResolution.LARGE_TELEPHONE );
        TestUtils.saveScreenshot( getSession(), "emulator_414_736" );

        then: "editor get correct width"
        wizard.getWidthOfPageEditor() == LARGE_WIDTH;

        and: "editor get correct height"
        wizard.getHeightOfPageEditor() == LARGE_HEIGHT;
    }
}
