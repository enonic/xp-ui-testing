package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.PageComponentsViewDialog
import com.enonic.autotests.pages.form.HtmlArea0_1_FormViewPanel
import com.enonic.autotests.pages.form.SiteFormViewPanel
import com.enonic.autotests.pages.form.liveedit.CkeToolbar
import com.enonic.autotests.pages.form.liveedit.LiveFormPanel
import com.enonic.autotests.pages.usermanager.browsepanel.UserBrowsePanel
import com.enonic.autotests.pages.usermanager.browsepanel.UserItemName
import com.enonic.autotests.pages.usermanager.wizardpanel.UserWizardPanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.autotests.vo.contentmanager.security.ContentAclEntry
import com.enonic.autotests.vo.contentmanager.security.PermissionSuite
import com.enonic.autotests.vo.usermanager.RoleName
import com.enonic.autotests.vo.usermanager.User
import com.enonic.wem.uitest.BaseGebSpec
import com.enonic.xp.content.ContentPath
import com.enonic.xp.data.PropertyTree
import spock.lang.Shared
import spock.lang.Stepwise

/**
 * Created  on 08.12.2016.
 * */
@Stepwise
class Display_SourceButton_Spec
    extends BaseGebSpec
{
    @Shared
    UserBrowsePanel userBrowsePanel;

    @Shared
    ContentBrowsePanel contentBrowsePanel;

    @Shared
    private String USER_NAME = NameHelper.uniqueName( "user" );

    @Shared
    User USER

    @Shared
    String USER_PASSWORD = Application.MEDIUM_PASSWORD;

    @Shared
    Content SITE;

    def "Preconditions: new user should be added with roles: 'Content Manager App' AND 'Administration Console Login' "()
    {
        setup: "navigate to USERS app"
        go "admin"
        userBrowsePanel = NavigatorHelper.openUsersApp( getTestSession() );

        and: "adding a user by the SU"
        String[] roles = [RoleName.ADMIN_CONSOLE.getValue(), RoleName.CM_APP.getValue()];
        USER = User.builder().displayName( USER_NAME ).email( USER_NAME + "@gmail.com" ).password( USER_PASSWORD ).roles(
            roles.toList() ).build();

        userBrowsePanel.clickOnExpander( UserItemName.SYSTEM.getValue() );
        UserWizardPanel userWizardPanel = userBrowsePanel.clickOnRowByName( "users" ).clickOnToolbarNew( UserItemName.USERS_FOLDER );

        when: "data has been typed and user saved"
        userWizardPanel.typeData( USER ).save().close( USER.getDisplayName() );
        userBrowsePanel.getFilterPanel().typeSearchText( USER.getDisplayName() );

        then: "new user should be present beneath a system store"
        userBrowsePanel.exists( USER.getDisplayName(), true );
    }

    def "GIVEN SU is logged in WHEN new site with 'Full Access' for just created user has been added THEN Content is listed in BrowsePanel"()
    {
        given: "navigate to 'Content Studio'"
        ContentAclEntry entry = ContentAclEntry.builder().principalName( USER_NAME ).suite( PermissionSuite.FULL_ACCESS ).build();
        List<ContentAclEntry> aclEntries = new ArrayList<>()
        aclEntries.add( entry );
        SITE = buildSimpleSiteApp( aclEntries );

        go "admin"
        ContentBrowsePanel contentBrowsePanel = NavigatorHelper.openContentStudioApp( getTestSession() );

        when: "new site with permissions 'Full Access' for the user has been saved"
        contentBrowsePanel.clickToolbarNew().selectContentType( SITE.getContentTypeName() ).
            typeData( SITE ).selectPageDescriptor( "main region" ).closeBrowserTab().switchToBrowsePanelTab();

        then: "content should be listed in the grid"
        contentBrowsePanel.getFilterPanel().typeSearchText( SITE.getName() );
        contentBrowsePanel.exists( SITE.getName() );
    }

    def "GIVEN SU is logged in WHEN existing site is opened and text component has been added THEN component should be added"()
    {
        given: "navigate to 'Content Studio'"
        go "admin"
        contentBrowsePanel = NavigatorHelper.openContentStudioApp( getTestSession() );
        and: "open the site"
        ContentWizardPanel siteWizard = findAndSelectContent( SITE.getName() ).clickToolbarEdit();
        PageComponentsViewDialog pageComponentsView = siteWizard.showComponentView();

        when: "new text component has been inserted"
        pageComponentsView.openMenu( "main" ).selectMenuItem( "Insert", "Text" );
        siteWizard.switchToLiveEditFrame();
        LiveFormPanel liveFormPanel = new LiveFormPanel( getSession() );
        liveFormPanel.typeTextInTextComponent( "test text" );
        siteWizard.switchToDefaultWindow();
        pageComponentsView.doCloseDialog();

        and: "site was saved in the wizard"
        siteWizard.save();
        siteWizard.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );

        then: "text component should be displayed"
        siteWizard.switchToLiveEditFrame();
        liveFormPanel.isTextComponentPresent();
    }

    def "GIVEN login with the User AND open the site WHEN user without required roles tries to edit the text component THEN Source Button should not be present"()
    {
        given: "'users' is opened"
        go "admin"
        getTestSession().setUser( USER );
        ContentBrowsePanel contentBrowsePanel = NavigatorHelper.openContentStudioApp( getTestSession() );
        saveScreenshot( "logged_" + USER_NAME );
        contentBrowsePanel.getFilterPanel().typeSearchText( SITE.getName() );
        ContentWizardPanel wizard = contentBrowsePanel.clickCheckboxAndSelectRow( SITE.getName() ).clickToolbarEdit();
        sleep( 1000 );
        wizard.switchToLiveEditFrame();
        LiveFormPanel liveFormPanel = new LiveFormPanel( getSession() );
        CkeToolbar ckeToolbar = new CkeToolbar( getSession() );

        when: "the text component has been double clicked"
        liveFormPanel.doubleClickOnTextComponent( "test text" );
        saveScreenshot( "text_component_edit_inline_mode" );

        then: "'Source Button button should not be displayed in the editor-toolbar"
        !ckeToolbar.isSourceCodeButtonDisplayed();
    }

    def "GIVEN login with the User WHEN user without required roles tries to add a htmlArea content THEN Source Button should not be displayed on the htmlarea-toolbar"()
    {
        given: "login with the User"
        go "admin"
        getTestSession().setUser( USER );
        ContentBrowsePanel contentBrowsePanel = NavigatorHelper.openContentStudioApp( getTestSession() );
        saveScreenshot( "logged_" + USER_NAME );

        when: "double click on the text component is performed"
        contentBrowsePanel.clickCheckboxAndSelectRow( SITE.getName() ).clickToolbarNew().selectContentType( "htmlarea0_1" );
        saveScreenshot( "htmlarea_source_button_not_displayed" );
        HtmlArea0_1_FormViewPanel htmlArea = new HtmlArea0_1_FormViewPanel( getSession() );

        then: "'Source Button button should not be displayed"
        !htmlArea.isSourceCodeButtonDisplayed();
    }

    def "GIVEN existing user opened in the wizard WHEN required role added THEN new role is displayed on the wizard"()
    {
        setup: "navigate to USERS app"
        go "admin"
        getTestSession().setUser( null );
        userBrowsePanel = NavigatorHelper.openUsersApp( getTestSession() );

        and: "existing user is found"
        userBrowsePanel.getFilterPanel().typeSearchText( USER.getDisplayName() );

        and: "the user is opened"
        userBrowsePanel.clickCheckboxAndSelectUser( USER.getDisplayName() );
        UserWizardPanel userWizardPanel = userBrowsePanel.clickToolbarEdit();

        when: "required role was added"
        userWizardPanel.addRole( RoleName.CM_EXPERT.getValue() );
        userWizardPanel.save();

        then: "new role should be displayed on the wizard"
        TestUtils.isContains( userWizardPanel.getRoleNames(), RoleName.CM_EXPERT.getValue() );
    }

    def "GIVEN login with the User AND open the site WHEN user with required roles tries to edit the text component THEN Source Button should be displayed on the mce-toolbar"()
    {
        given: "login with the User AND open the site"
        go "admin"
        getTestSession().setUser( USER );
        ContentBrowsePanel contentBrowsePanel = NavigatorHelper.openContentStudioApp( getTestSession() );
        saveScreenshot( "logged_" + USER_NAME );
        ContentWizardPanel wizard = contentBrowsePanel.clickCheckboxAndSelectRow( SITE.getName() ).clickToolbarEdit();
        wizard.switchToLiveEditFrame();
        LiveFormPanel liveFormPanel = new LiveFormPanel( getSession() );
        CkeToolbar ckeToolbar = new CkeToolbar( getSession() );

        when: "double click on the text component is performed"
        liveFormPanel.doubleClickOnTextComponent( "test text" );
        saveScreenshot( "text_component_edit_inline_mode" );

        then: "'Source Button button should be displayed"
        ckeToolbar.isSourceCodeButtonDisplayed();
    }

    def "GIVEN login with the User WHEN user with required roles tries to add a htmlArea content THEN 'Source Button' should be displayed on the htmlarea-toolbar"()
    {
        given: "user manager is opened"
        go "admin"
        getTestSession().setUser( USER );
        ContentBrowsePanel contentBrowsePanel = NavigatorHelper.openContentStudioApp( getTestSession() );
        saveScreenshot( "logged_" + USER_NAME );

        when: "double click on the text component is performed"
        contentBrowsePanel.clickCheckboxAndSelectRow( SITE.getName() ).clickToolbarNew().selectContentType( "htmlarea0_1" );
        saveScreenshot( "htmlarea_source_button_should_be_displayed" );
        HtmlArea0_1_FormViewPanel htmlArea = new HtmlArea0_1_FormViewPanel( getSession() );
        htmlArea.showToolbar();

        then: "'Source Button button should be displayed"
        htmlArea.isSourceCodeButtonDisplayed();
    }

    protected Content buildSimpleSiteApp( List<ContentAclEntry> aclEntries )
    {
        String name = NameHelper.uniqueName( "site" );
        PropertyTree data = new PropertyTree();
        data.addStrings( SiteFormViewPanel.APP_KEY, "Simple Site App" );
        data.addStrings( "description", "simple site " )
        Content site = Content.builder().
            parent( ContentPath.ROOT ).
            name( name ).
            displayName( "simple site" ).
            parent( ContentPath.ROOT ).
            contentType( "Site" ).data( data ).aclEntries( aclEntries ).
            build();
        return site;
    }

    private ContentBrowsePanel findAndSelectContent( String name )
    {
        contentBrowsePanel.getFilterPanel().typeSearchText( name );
        if ( !contentBrowsePanel.isRowSelected( name ) )
        {
            contentBrowsePanel.clickCheckboxAndSelectRow( name );
        }
        return contentBrowsePanel;
    }
}
