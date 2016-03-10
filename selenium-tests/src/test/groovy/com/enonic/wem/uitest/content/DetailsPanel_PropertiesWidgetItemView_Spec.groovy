package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.browsepanel.NewContentDialog
import com.enonic.autotests.pages.contentmanager.browsepanel.PropertiesWidgetItemView
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.SettingsWizardStepForm
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.autotests.vo.contentmanager.ContentSettings
import spock.lang.Shared
import spock.lang.Stepwise

import java.time.LocalDate

@Stepwise
class DetailsPanel_PropertiesWidgetItemView_Spec
    extends BaseContentSpec
{
    @Shared
    Content FOLDER_CONTENT;

    @Shared
    String EXECUTABLE_FILE_EXE = "/test-data/upload/server.bat";

    @Shared
    String EXECUTABLE_FILE_SH = "/test-data/upload/server.sh";

    @Shared
    String MEDIA_APP_NAME = "media";

    @Shared
    String BASE_APP_NAME = "base";

    @Shared
    String EXECUTABLE_APP_NAME = "executable";


    def "GIVEN opened a new content dialog WHEN upload button clicked and '.bat' file selected THEN correct info displayed in the widget"()
    {
        given: "existing executable file"
        NewContentDialog dialog = contentBrowsePanel.clickToolbarNew();
        dialog.doUploadFile( EXECUTABLE_FILE_EXE );
        sleep( 1000 );

        when: "executable file selected"
        findAndSelectContent( "server.bat" );
        contentBrowsePanel.clickOnDetailsToggleButton();
        PropertiesWidgetItemView view = contentBrowsePanel.getContentBrowseItemPanel().getContentDetailsPanel().getPropertiesWidgetItemView();

        then: "'media' is application's name"
        view.getApplicationName() == MEDIA_APP_NAME;

        and: "'executable' type is displayed"
        view.getType() == EXECUTABLE_APP_NAME;
    }

    def "GIVEN opened a new content dialog WHEN upload button clicked and '.sh' selected THEN correct info displayed in the widget"()
    {
        given: "existing executable file"
        NewContentDialog dialog = contentBrowsePanel.clickToolbarNew();
        dialog.doUploadFile( EXECUTABLE_FILE_SH );
        sleep( 1000 );

        when: "executable file selected"
        findAndSelectContent( "server.sh" );
        contentBrowsePanel.clickOnDetailsToggleButton();
        PropertiesWidgetItemView view = contentBrowsePanel.getContentBrowseItemPanel().getContentDetailsPanel().getPropertiesWidgetItemView();

        then: "'media' is application's name"
        view.getApplicationName() == MEDIA_APP_NAME;

        and: "'executable' type is displayed"
        view.getType() == EXECUTABLE_APP_NAME;
    }

    def "GIVEN a new created folder is selected WHEN details panel opened THEN correct properties displayed in the PropertiesWidgetItemView "()
    {
        given: "new created folder is selected"
        ContentSettings settings = ContentSettings.builder().language( NORSK_LANGUAGE ).build();
        FOLDER_CONTENT = buildFolderWithSettingsContent( "folder", "properties-test", settings );
        addContent( FOLDER_CONTENT );
        findAndSelectContent( FOLDER_CONTENT.getName() );

        when: "details panel opened"
        contentBrowsePanel.clickOnDetailsToggleButton();
        PropertiesWidgetItemView view = contentBrowsePanel.getContentBrowseItemPanel().getContentDetailsPanel().getPropertiesWidgetItemView();

        then: "'Id' present on the widget"
        view.isIdPresent();

        and: "'Created' present on the widget"
        view.isCreatedPresent();

        and: "'Modified' present on the widget"
        view.isIdModifiedPresent();

        and: "'Application-name' present on the widget"
        view.isApplicationNamePresent();

        and: "'Owner' present on the widget"
        view.isOwnerPresent();

        and: "'Type' present on the widget"
        view.isTypePresent();

        and: "'Language' present on the widget"
        view.isLanguagePresent();
    }

    def "GIVEN existing folder is selected WHEN details panel opened THEN correct values for properties are shown in the PropertiesWidgetItemView "()
    {
        given: "existing folder is selected"
        findAndSelectContent( FOLDER_CONTENT.getName() );

        when: "PropertiesWidgetItemView is shown"
        contentBrowsePanel.clickOnDetailsToggleButton();
        PropertiesWidgetItemView view = contentBrowsePanel.getContentBrowseItemPanel().getContentDetailsPanel().getPropertiesWidgetItemView();

        then: "correct 'created' date displayed"
        view.getCreated() == LocalDate.now().toString();

        and: "correct 'modified' date displayed"
        view.getModified() == LocalDate.now().toString();

        and: "correct application-name displayed"
        view.getApplicationName() == BASE_APP_NAME;

        and: "correct owner displayed"
        view.getOwner() == "su";

        and: "correct type displayed"
        view.getType() == "folder";

        and: "correct language displayed"
        view.getLanguage() == "no";
    }

    def "WHEN image content selected and details panel opened THEN correct type an app-name are shown in PropertiesWidgetItemView"()
    {
        when: "image content selected"
        findAndSelectContent( IMPORTED_BOOK_IMAGE );
        contentBrowsePanel.clickOnDetailsToggleButton();
        PropertiesWidgetItemView view = contentBrowsePanel.getContentBrowseItemPanel().getContentDetailsPanel().getPropertiesWidgetItemView();

        then: "Properties Widget is displayed"
        view.isDisplayed();

        and: "Type is image"
        view.getType() == "image";

        and: "correct application-name displayed"
        view.getApplicationName() == MEDIA_APP_NAME;
    }

    def "GIVEN existing content with owner opened WHEN owner changed  THEN new owner shown in the widget"()
    {
        given: "when content opened for edit"
        ContentWizardPanel wizard = findAndSelectContent( FOLDER_CONTENT.getName() ).clickToolbarEdit();
        SettingsWizardStepForm form = wizard.clickOnSettingsTabLink();

        when: "owner changed AND content saved"
        form.removeOwner( SUPER_USER ).selectOwner( ANONYMOUS_USER );
        wizard.save().close( FOLDER_CONTENT.getDisplayName() );
        contentBrowsePanel.clickOnDetailsToggleButton();
        PropertiesWidgetItemView view = contentBrowsePanel.getContentBrowseItemPanel().getContentDetailsPanel().getPropertiesWidgetItemView();
        TestUtils.saveScreenshot( getSession(), "property-widget-owner-anonym" )

        then: "new owner shown in the widget"
        view.getOwner() == "anonymous";
    }

    def "WHEN shortcut content selected and details panel opened THEN correct type an app-name are shown in PropertiesWidgetItemView"()
    {
        when: "shortcut content selected"
        Content shortcut = buildShortcut( "shortcut", null, "test-properties" );
        addContent( shortcut );
        findAndSelectContent( shortcut.getName() );
        contentBrowsePanel.clickOnDetailsToggleButton();
        PropertiesWidgetItemView view = contentBrowsePanel.getContentBrowseItemPanel().getContentDetailsPanel().getPropertiesWidgetItemView();

        then: "Properties Widget is displayed"
        view.isDisplayed();

        and: "Type is shortcut"
        view.getType() == "shortcut";

        and: "correct application-name displayed"
        view.getApplicationName() == BASE_APP_NAME;
    }

    def "WHEN unstructured content selected and details panel opened THEN correct type an app-name are shown in PropertiesWidgetItemView"()
    {
        when: "unstructured content selected"
        Content unstructured = buildUnstructured( "unstructured", null, "test-unstructured" );
        addContent( unstructured );
        findAndSelectContent( unstructured.getName() );
        contentBrowsePanel.clickOnDetailsToggleButton();
        PropertiesWidgetItemView view = contentBrowsePanel.getContentBrowseItemPanel().getContentDetailsPanel().getPropertiesWidgetItemView();

        then: "Properties Widget is displayed"
        view.isDisplayed();

        and: "Type is unstructured"
        view.getType() == "unstructured";

        and: "correct application-name displayed"
        view.getApplicationName() == BASE_APP_NAME;
    }

    def "WHEN site content selected and details panel opened THEN correct type an app-name are shown in PropertiesWidgetItemView"()
    {
        when: "unstructured content selected"
        Content site = buildSite( "site", "test-site", "properties test" );
        addContent( site );
        findAndSelectContent( site.getName() );
        contentBrowsePanel.clickOnDetailsToggleButton();
        PropertiesWidgetItemView view = contentBrowsePanel.getContentBrowseItemPanel().getContentDetailsPanel().getPropertiesWidgetItemView();

        then: "Properties Widget is displayed"
        view.isDisplayed();

        and: "Type is site"
        view.getType() == "site";

        and: "correct application-name displayed"
        view.getApplicationName() == "portal";
    }

}
