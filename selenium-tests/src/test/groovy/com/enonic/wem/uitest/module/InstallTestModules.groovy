package com.enonic.wem.uitest.module

import com.enonic.autotests.pages.modules.InstallModuleDialog
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.TestUtils

import static com.enonic.autotests.utils.SleepHelper.sleep

class InstallTestModules
    extends BaseModuleSpec
{
    def setup()
    {
        go "admin"
        moduleBrowsePanel = NavigatorHelper.openModules( getTestSession() );
    }


    def "WHEN the first module installed and started THEN new module appears with 'started' status"()
    {
        setup: "click on 'Install' button on the toolbar, dialog window appears"
        InstallModuleDialog dialog = moduleBrowsePanel.clickToolbarInstall();

        and: "url typed and 'Install' button on dialog pressed "
        moduleBrowsePanel = dialog.typeModuleURL( FIRST_MODULE_URL ).clickOnInstall();
        TestUtils.saveScreenshot( getSession(), "test_module_install" );

        when: "module started"
        moduleBrowsePanel.clickAndSelectRow( FIRST_MODULE_NAME );
        moduleBrowsePanel.clickOnToolbarStart();
        sleep( 1500 );


        then: "new module exists in the browse panel and status is stopped "
        moduleBrowsePanel.exists( FIRST_MODULE_NAME, true ) && moduleBrowsePanel.getModuleStatus( FIRST_MODULE_NAME ).equals( "started" );
    }

    def "WHEN the second module installed and started THEN new module appears with 'started' status"()
    {
        setup: "click on 'Install' button on the toolbar, dialog window appears"
        InstallModuleDialog dialog = moduleBrowsePanel.clickToolbarInstall();

        and: "url typed and 'Install' button on dialog pressed "
        moduleBrowsePanel = dialog.typeModuleURL( SECOND_MODULE_URL ).clickOnInstall();
        TestUtils.saveScreenshot( getSession(), "test_module_install" );

        when: "module started"
        moduleBrowsePanel.clickAndSelectRow( SECOND_MODULE_NAME );
        moduleBrowsePanel.clickOnToolbarStart();
        sleep( 1500 );


        then: "new module exists in the browse panel and status is stopped "
        moduleBrowsePanel.exists( SECOND_MODULE_NAME, true ) && moduleBrowsePanel.getModuleStatus( SECOND_MODULE_NAME ).equals( "started" );
    }

    def "WHEN the third module installed and started THEN new module appears with 'started' status"()
    {
        setup: "click on 'Install' button on the toolbar, dialog window appears"
        InstallModuleDialog dialog = moduleBrowsePanel.clickToolbarInstall();

        and: "url typed and 'Install' button on dialog pressed "
        moduleBrowsePanel = dialog.typeModuleURL( THIRD_MODULE_URL ).clickOnInstall();
        TestUtils.saveScreenshot( getSession(), "third_module_install" );

        when: "module started"
        moduleBrowsePanel.clickAndSelectRow( THIRD_MODULE_NAME );
        moduleBrowsePanel.clickOnToolbarStart();
        sleep( 1500 );


        then: "new module exists in the browse panel and status is stopped "
        moduleBrowsePanel.exists( THIRD_MODULE_NAME, true ) && moduleBrowsePanel.getModuleStatus( THIRD_MODULE_NAME ).equals( "started" );
    }

    def "WHEN the fourth module installed and started THEN new module appears with 'started' status"()
    {
        setup: "click on 'Install' button on the toolbar, dialog window appears"
        InstallModuleDialog dialog = moduleBrowsePanel.clickToolbarInstall();

        and: "url typed and 'Install' button on dialog pressed "
        moduleBrowsePanel = dialog.typeModuleURL( FOURTH_MODULE_URL ).clickOnInstall();
        TestUtils.saveScreenshot( getSession(), "fourth_module_install" );

        when: "module started"
        moduleBrowsePanel.clickAndSelectRow( FOURTH_MODULE_NAME );
        moduleBrowsePanel.clickOnToolbarStart();
        sleep( 1500 );


        then: "new module exists in the browse panel and status is stopped "
        moduleBrowsePanel.exists( FOURTH_MODULE_NAME, true ) && moduleBrowsePanel.getModuleStatus( FOURTH_MODULE_NAME ).equals( "started" );
    }


}