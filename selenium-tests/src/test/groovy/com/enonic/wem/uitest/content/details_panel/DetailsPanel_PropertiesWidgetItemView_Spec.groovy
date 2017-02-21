package com.enonic.wem.uitest.content.details_panel

import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.PropertiesWidgetItemView
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.SettingsWizardStepForm
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.autotests.vo.contentmanager.ContentSettings
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Shared
import spock.lang.Stepwise

import java.time.LocalDate

/**
 * Tasks:
 *  XP-4424 Add selenium tests for 'Published from' property, that displayed at the Info Widget
 **/
@Stepwise
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

    def "GIVEN existing '*.bat' WHEN the file was selected THEN correct info should be displayed on the widget"()
    {
        when: "executable file is selected"
        findAndSelectContent( EXECUTABLE_BAT );
        and: "Details Panel is opened"
        contentBrowsePanel.clickOnDetailsToggleButton();
        PropertiesWidgetItemView view = contentBrowsePanel.getContentBrowseItemPanel().getContentDetailsPanel().getPropertiesWidgetItemView();
        saveScreenshot( "executable_bat_info_widget" );

        then: "application's name should be 'media'"
        view.getApplicationName() == MEDIA_APP_NAME;

        and: "'executable' type should be displayed"
        view.getType() == EXECUTABLE_APP_NAME;
    }

    def "GIVEN existing '*.sh' WHEN file was selected THEN correct info should be displayed on the widget"()
    {
        when: "executable file was selected"
        findAndSelectContent( EXECUTABLE_SH );
        and: "Details Panel is opened"
        contentBrowsePanel.clickOnDetailsToggleButton();
        PropertiesWidgetItemView view = contentBrowsePanel.getContentBrowseItemPanel().getContentDetailsPanel().getPropertiesWidgetItemView();
        saveScreenshot( "executable_sh_info_widget" );

        then: "application's name should be 'media'"
        view.getApplicationName() == MEDIA_APP_NAME;

        and: "'executable' type should be displayed"
        view.getType() == EXECUTABLE_APP_NAME;
    }


    def "GIVEN existing '*.exe' WHEN the file was selected THEN correct info should be displayed on the widget"()
    {
        when: "executable file was selected"
        findAndSelectContent( EXECUTABLE_EXE );
        and: "Details Panel is opened"
        contentBrowsePanel.clickOnDetailsToggleButton();
        PropertiesWidgetItemView view = contentBrowsePanel.getContentBrowseItemPanel().getContentDetailsPanel().getPropertiesWidgetItemView();
        saveScreenshot( "executable_exe_info_widget" );

        then:"application's name should be 'media'"
        view.getApplicationName() == MEDIA_APP_NAME;

        and: "'executable' type is displayed"
        view.getType() == EXECUTABLE_APP_NAME;
    }

    def "GIVEN folder was added an it selected WHEN details panel was opened THEN correct properties should be displayed in the PropertiesWidgetItemView "()
    {
        given: "folder was added"
        ContentSettings settings = ContentSettings.builder().language( NORSK_LANGUAGE ).build();
        FOLDER_CONTENT = buildFolderWithSettingsContent( "folder", "properties-test", settings );
        addContent( FOLDER_CONTENT );
        and:"the folder is selected"
        findAndSelectContent( FOLDER_CONTENT.getName() );

        when: "details panel was opened"
        contentBrowsePanel.clickOnDetailsToggleButton();
        PropertiesWidgetItemView view = contentBrowsePanel.getContentBrowseItemPanel().getContentDetailsPanel().getPropertiesWidgetItemView();

        then: "'Id' should be present on the widget"
        view.isIdPresent();

        and: "'Created' should be present on the widget"
        view.isCreatedPresent();

        and: "'Modified' should be present on the widget"
        view.isIdModifiedPresent();

        and: "'Application-name' present on the widget"
        view.isApplicationNamePresent();

        and: "'Owner' should be present on the widget"
        view.isOwnerPresent();

        and: "'Type' should be present on the widget"
        view.isTypePresent();

        and: "'Language' should be present on the widget"
        view.isLanguagePresent();
    }

    def "GIVEN existing folder is selected WHEN details panel is opened THEN correct values for properties should be shown on the PropertiesWidgetItemView "()
    {
        given: "existing folder is selected"
        findAndSelectContent( FOLDER_CONTENT.getName() );

        when: "PropertiesWidgetItemView is shown"
        contentBrowsePanel.clickOnDetailsToggleButton();
        PropertiesWidgetItemView view = contentBrowsePanel.getContentBrowseItemPanel().getContentDetailsPanel().getPropertiesWidgetItemView();
        saveScreenshot( "folder_info_properties" );

        then: "correct 'created' date should be displayed"
        view.getCreated().contains( LocalDate.now().toString() );

        and: "correct 'modified' date should be displayed"
        view.getModified().contains( LocalDate.now().toString() );

        and: "correct application-name should be displayed"
        view.getApplicationName() == BASE_APP_NAME;

        and: "correct owner displayed"
        view.getOwner() == "su";

        and: "correct type should be displayed"
        view.getType() == "folder";

        and: "correct language should be displayed"
        view.getLanguage() == "no";
    }

    def "WHEN image content was selected and details panel opened THEN correct type an app-name are shown in PropertiesWidgetItemView"()
    {
        when: "image content was selected"
        findAndSelectContent( IMPORTED_IMAGE_BOOK_NAME );
        contentBrowsePanel.clickOnDetailsToggleButton();
        PropertiesWidgetItemView view = contentBrowsePanel.getContentBrowseItemPanel().getContentDetailsPanel().getPropertiesWidgetItemView();
        saveScreenshot( "image_info_properties" );

        then: "Properties Widget should be displayed"
        view.isDisplayed();

        and: "type should be 'image'"
        view.getType() == "image";

        and: "correct application-name should be displayed"
        view.getApplicationName() == MEDIA_APP_NAME;
    }

    def "GIVEN existing content with owner opened WHEN owner changed  THEN new owner shown in the widget"()
    {
        given: "when content is opened"
        ContentWizardPanel wizard = findAndSelectContent( FOLDER_CONTENT.getName() ).clickToolbarEditAndSwitchToWizardTab();
        SettingsWizardStepForm form = wizard.clickOnSettingsTabLink();

        when: "super user was replaced with the Anonymous user"
        form.removeOwner( SUPER_USER ).selectOwner( ANONYMOUS_USER );
        and: "the folder has been saved"
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();

        and: "details panel has been opened"
        contentBrowsePanel.clickOnDetailsToggleButton();
        PropertiesWidgetItemView view = contentBrowsePanel.getContentBrowseItemPanel().getContentDetailsPanel().getPropertiesWidgetItemView();
        saveScreenshot( "property-widget-owner-anonym" )

        then: "new owner should be shown on the widget"
        view.getOwner() == "anonymous";
    }

    def "GIVEN shortcut content was added and it selected in the grid WHEN details panel is opened THEN correct type of the content should be displayed"()
    {
        given: "shortcut content was added and it selected in the grid"
        Content shortcut = buildShortcut( "shortcut", null, "test-properties" );
        addContent( shortcut );
        findAndSelectContent( shortcut.getName() );

        when: "details panel is opened"
        contentBrowsePanel.clickOnDetailsToggleButton();
        PropertiesWidgetItemView view = contentBrowsePanel.getContentBrowseItemPanel().getContentDetailsPanel().getPropertiesWidgetItemView();
        saveScreenshot( "shortcut_info_properties" );

        then: "Properties Widget should be displayed"
        view.isDisplayed();

        and: "correct type of the content should be displayed"
        view.getType() == "shortcut";

        and: "correct application's name should be displayed"
        view.getApplicationName() == BASE_APP_NAME;
    }

    def "GIVEN unstructured content was added and it selected in the grid WHEN details panel was opened THEN correct type of the content should be displayed"()
    {
        given: "unstructured content was added and it selected in the grid"
        Content unstructured = buildUnstructured( "unstructured", null, "test-unstructured" );
        addContent( unstructured );
        findAndSelectContent( unstructured.getName() );

        when: "details panel is opened"
        contentBrowsePanel.clickOnDetailsToggleButton();
        PropertiesWidgetItemView view = contentBrowsePanel.getContentBrowseItemPanel().getContentDetailsPanel().getPropertiesWidgetItemView();
        saveScreenshot( "unstructured_info_properties" );

        then: "Properties Widget should be displayed"
        view.isDisplayed();

        and: "correct type of the content should be displayed"
        view.getType() == "unstructured";

        and: "correct application-name should be displayed"
        view.getApplicationName() == BASE_APP_NAME;
    }

    def "GIVEN site content was added and it selected in the grid WHEN details panel is opened THEN correct type of the content should be displayed"()
    {
        given: "site content was added and it selected in the grid WHEN details panel is opened"
        Content site = buildSiteWithNameAndDispalyNameAndDescription( "site", "test-site", "properties test" );
        addContent( site );
        findAndSelectContent( site.getName() );
        when: "details panel is opened"
        contentBrowsePanel.clickOnDetailsToggleButton();
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
