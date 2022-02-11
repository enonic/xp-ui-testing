package com.enonic.wem.uitest.content.details_panel

import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.PropertiesWidgetItemView
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.SettingsWizardStepForm
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.autotests.vo.contentmanager.ContentSettings
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Stepwise

import java.time.LocalDate

@Stepwise
@Ignore
class DetailsPanel_PropertiesWidgetItemView_Spec
    extends BaseContentSpec
{
    @Shared
    Content FOLDER_CONTENT;

    @Shared
    String MEDIA_APP_NAME = "media";

    @Shared
    String BASE_APP_NAME = "base";

    @Shared
    String EXECUTABLE_APP_NAME = "executable";

    def "WHEN existing '*.bat' has been selected THEN expected info should be displayed in the widget"()
    {
        when: "executable file has been selected"
        findAndSelectContent( EXECUTABLE_BAT );
        and: "Details Panel is opened"
        contentBrowsePanel.openContentDetailsPanel();
        PropertiesWidgetItemView view = contentBrowsePanel.getContentBrowseItemPanel().getContentDetailsPanel().getPropertiesWidgetItemView();
        saveScreenshot( "executable_bat_info_widget" );

        then: "application's name should be 'media'"
        view.getApplicationName() == MEDIA_APP_NAME;

        and: "'executable' type should be displayed"
        view.getType() == EXECUTABLE_APP_NAME;
    }

    def "WHEN existing '*.sh' file has been selected THEN expected info should be displayed in the widget"()
    {
        when: "executable file has been selected"
        findAndSelectContent( EXECUTABLE_SH );
        and: "Details Panel is opened"
        contentBrowsePanel.openContentDetailsPanel();
        PropertiesWidgetItemView view = contentBrowsePanel.getContentBrowseItemPanel().getContentDetailsPanel().getPropertiesWidgetItemView();
        saveScreenshot( "executable_sh_info_widget" );

        then: "application's name should be 'media'"
        view.getApplicationName() == MEDIA_APP_NAME;

        and: "'executable' type should be displayed"
        view.getType() == EXECUTABLE_APP_NAME;
    }

    def "WHEN existing '*.exe' file has been selected THEN expected info should be displayed in the widget"()
    {
        when: "executable file has been selected"
        findAndSelectContent( EXECUTABLE_EXE );
        and: "Details Panel is opened"
        contentBrowsePanel.openContentDetailsPanel();
        PropertiesWidgetItemView view = contentBrowsePanel.getContentBrowseItemPanel().getContentDetailsPanel().getPropertiesWidgetItemView();
        saveScreenshot( "executable_exe_info_widget" );

        then:"application's name should be 'media'"
        view.getApplicationName() == MEDIA_APP_NAME;

        and: "'executable' type should be displayed"
        view.getType() == EXECUTABLE_APP_NAME;
    }

    def "GIVEN new folder is selected WHEN details panel has been opened THEN expected properties should be displayed in the PropertiesWidgetItemView"()
    {
        given: "new folder is added"
        ContentSettings settings = ContentSettings.builder().language( NORSK_LANGUAGE ).build();
        FOLDER_CONTENT = buildFolderWithSettingsContent( "folder", "properties-test", settings );
        addContent( FOLDER_CONTENT );
        and:"the folder is selected"
        findAndSelectContent( FOLDER_CONTENT.getName() );

        when: "PropertiesWidgetItemView has bee opened"
        contentBrowsePanel.openContentDetailsPanel();
        PropertiesWidgetItemView view = contentBrowsePanel.getContentBrowseItemPanel().getContentDetailsPanel().getPropertiesWidgetItemView();
        saveScreenshot( "folder_info_properties" );

        then: "expected 'created' date should be displayed"
        view.getCreated().contains( LocalDate.now().toString() );

        and: "expected 'modified' date should be displayed"
        view.getModified().contains( LocalDate.now().toString() );

        and: "expected application-name should be displayed"
        view.getApplicationName() == BASE_APP_NAME;

        and: "expected owner should be displayed"
        view.getOwner() == "su";

        and: "expected type should be displayed"
        view.getType() == "folder";

        and: "correct language should be displayed"
        view.getLanguage() == "no";
    }

    def "WHEN image content has been selected and details panel is opened THEN expected type and app-name should be shown in PropertiesWidgetItemView"()
    {
        when: "image content was selected"
        findAndSelectContent( IMPORTED_IMAGE_BOOK_NAME );
        contentBrowsePanel.openContentDetailsPanel();
        PropertiesWidgetItemView view = contentBrowsePanel.getContentBrowseItemPanel().getContentDetailsPanel().getPropertiesWidgetItemView();
        saveScreenshot( "image_info_properties" );

        then: "Properties Widget should be displayed"
        view.isDisplayed();

        and: "type should be 'image'"
        view.getType() == "image";

        and: "correct application-name should be displayed"
        view.getApplicationName() == MEDIA_APP_NAME;
    }

    def "GIVEN existing content with owner is opened WHEN owner has been changed in the wizard THEN new owner should appears in the widget"()
    {
        given: "existing content with owner is opened "
        ContentWizardPanel wizard = findAndSelectContent( FOLDER_CONTENT.getName() ).clickToolbarEditAndSwitchToWizardTab();
        SettingsWizardStepForm form = wizard.clickOnSettingsTabLink();

        when: "super user was replaced with the Anonymous user"
        form.removeOwner( SUPER_USER ).selectOwner( ANONYMOUS_USER );
        and: "the folder has been saved"
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();

        and: "details panel has been opened"
        contentBrowsePanel.openContentDetailsPanel();
        PropertiesWidgetItemView view = contentBrowsePanel.getContentBrowseItemPanel().getContentDetailsPanel().getPropertiesWidgetItemView();
        saveScreenshot( "property-widget-owner-anonym" )

        then: "new owner should be shown on the widget"
        view.getOwner() == "anonymous";
    }

    def "GIVEN existing shortcut has been selected WHEN details panel is opened THEN expected content type should be displayed in the widget"()
    {
        given: "existing shortcut has been selected"
        Content shortcut = buildShortcut( "shortcut", null, "test-properties" );
        addContent( shortcut );
        findAndSelectContent( shortcut.getName() );

        when: "details panel has been opened(by default)"
        contentBrowsePanel.openContentDetailsPanel();
        PropertiesWidgetItemView view = contentBrowsePanel.getContentBrowseItemPanel().getContentDetailsPanel().getPropertiesWidgetItemView();
        saveScreenshot( "shortcut_info_properties" );

        then: "Properties Widget should be displayed"
        view.isDisplayed();

        and: "correct type of the content should be displayed"
        view.getType() == "shortcut";

        and: "correct application's name should be displayed"
        view.getApplicationName() == BASE_APP_NAME;
    }

    def "GIVEN existing site has been selected WHEN details panel has been opened THEN expected content type should be displayed in the widget"()
    {
        given: "existing site has been selected"
        Content site = buildSiteWithNameAndDispalyNameAndDescription( "site", "test-site", "properties test" );
        addContent( site );
        findAndSelectContent( site.getName() );
        when: "details panel has been opened"
        contentBrowsePanel.openContentDetailsPanel();
        PropertiesWidgetItemView view = contentBrowsePanel.getContentBrowseItemPanel().getContentDetailsPanel().getPropertiesWidgetItemView();
        saveScreenshot( "site_info_properties" );

        then: "Properties Widget should be displayed"
        view.isDisplayed();

        and: "Type of the content should be 'site'"
        view.getType() == "site";

        and: "application's name should be 'portal'"
        view.getApplicationName() == "portal";
    }
}
