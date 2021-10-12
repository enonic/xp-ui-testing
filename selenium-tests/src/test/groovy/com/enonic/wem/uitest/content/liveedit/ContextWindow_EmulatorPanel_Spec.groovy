package com.enonic.wem.uitest.content.liveedit

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.EmulatorResolution
import com.enonic.autotests.pages.contentmanager.wizardpanel.context_window.PageEmulatorPanel
import com.enonic.autotests.pages.form.liveedit.WizardContextPanel
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

    def "GIVEN wizard for new site is opened WHEN Emulator menu item has been clicked THEN Emulator panel gets visible AND 8 available resolutions should be present"()
    {
        given: "new site is opened"
        TEST_SITE = buildMyFirstAppSite( "emulator_panel" );
        ContentWizardPanel siteWizard = contentBrowsePanel.clickToolbarNew().selectContentType( TEST_SITE.getContentTypeName() ).typeData(
            TEST_SITE );
        siteWizard.selectPageDescriptor( COUNTRY_REGION_PAGE_CONTROLLER );

        when: "When context window has been opened and 'Emulator' option has been clicked"
        siteWizard.showContextWindow();
        WizardContextPanel contextPanel = new WizardContextPanel(getSession(  ));
        saveScreenshot( "emulator-panel-not-activated" );
        PageEmulatorPanel emulatorPanel = contextPanel.openEmulatorWidget(  );
        saveScreenshot( "emulator-panel-activated" );

        then: "emulator panel gets visible"
        emulatorPanel.isDisplayed();

        and: "8 available resolutions are present"
        emulatorPanel.getAvailableResolutions().size() == 8;
    }

    def "GIVEN Emulator Panel opened WHEN resolution changed THEN correct size of frame is applied"()
    {
        given: "Emulator Panel is opened "
        ContentWizardPanel wizard = findAndSelectContent( TEST_SITE.getName() ).clickToolbarEdit();
        wizard.showContextWindow();
        WizardContextPanel contextPanel = new WizardContextPanel(getSession(  ));
        PageEmulatorPanel emulatorPanel = contextPanel.openEmulatorWidget(  );

        when: "Medium resolution selected"
        emulatorPanel.selectResolution( EmulatorResolution.MEDIUM_PHONE );
        saveScreenshot( "emulator_375_667" );

        then: "editor get correct width"
        wizard.getWidthOfPageEditor() == MEDIUM_WIDTH;

        and: "editor get correct height"
        wizard.getHeightOfPageEditor() == MEDIUM_HEIGHT;
    }

    def "GIVEN Emulator Panel is opened WHEN resolution changed to LARGE_TELEPHONE THEN correct size of frame is applied"()
    {
        given: "Emulator Panel is opened "
        ContentWizardPanel wizard = findAndSelectContent( TEST_SITE.getName() ).clickToolbarEdit();
        wizard.showContextWindow();
        WizardContextPanel contextPanel = new WizardContextPanel(getSession(  ));
        PageEmulatorPanel emulatorPanel = contextPanel.openEmulatorWidget(  );

        when: "Medium resolution selected"
        emulatorPanel.selectResolution( EmulatorResolution.LARGE_TELEPHONE );
        saveScreenshot( "emulator_414_736" );

        then: "editor get correct width"
        wizard.getWidthOfPageEditor() == LARGE_WIDTH;

        and: "editor get correct height"
        wizard.getHeightOfPageEditor() == LARGE_HEIGHT;
    }
}
