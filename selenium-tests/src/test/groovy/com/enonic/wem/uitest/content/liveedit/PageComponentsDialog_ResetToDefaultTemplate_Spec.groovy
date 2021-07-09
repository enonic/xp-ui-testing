package com.enonic.wem.uitest.content.liveedit

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.PageComponentsViewDialog
import com.enonic.autotests.pages.form.liveedit.ImageComponentView
import com.enonic.autotests.pages.form.liveedit.LiveFormPanel
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import com.enonic.xp.content.ContentPath
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class PageComponentsDialog_ResetToDefaultTemplate_Spec
    extends BaseContentSpec
{
    @Shared
    String SITE_WITH_COMPONENTS_NAME = "site-component";

    @Shared
    String PAGE_CONTROLLER_NAME = "Country Region";

    @Shared
    Content SITE;

    @Shared
    Content PAGE_TEMPLATE;

    @Shared
    String TEMPLATE_DISPLAY_NAME = "country template";

    @Shared
    String TEXT_2 = "second text";

    @Shared
    String TEXT_1 = "text1";


    def "Preconditions: new site with a template should be added"()
    {
        given: "existing Site based on 'My First App'"
        SITE = buildMyFirstAppSite( SITE_WITH_COMPONENTS_NAME );
        addSite( SITE );
        filterPanel.typeSearchText( SITE.getName() )
        contentBrowsePanel.expandContent( ContentPath.from( SITE.getName() ) );
        PAGE_TEMPLATE = buildPageTemplate( COUNTRY_REGION_PAGE_CONTROLLER, TEMPLATE_SUPPORTS_SITE, TEMPLATE_DISPLAY_NAME, SITE.getName() );

        when: "'Templates' folder selected and new page-template added"
        addTemplateWithTextComponent( PAGE_TEMPLATE, TEXT_1 )
        sleep( 500 );

        then: "new page-template should be listed"
        filterPanel.typeSearchText( PAGE_TEMPLATE.getName() );
        contentBrowsePanel.exists( PAGE_TEMPLATE.getName() );
    }

    def "GIVEN text in the text component is updated WHEN root element in page component dialog has been selected and 'Reset' menu item clicked THEN site should be reset to default template"()
    {
        given: "site is opened"
        filterPanel.typeSearchText( SITE.getName() )
        ContentWizardPanel wizard = contentBrowsePanel.clickCheckboxAndSelectRow( SITE.getName() ).clickToolbarEdit();
        wizard.unlockPageEditorAndSwitchToContentStudio();
        saveScreenshot( "image-reset-to-template" );
        wizard.showComponentView();
        saveScreenshot( "image-from-template" );

        and: "remove the text component and insert a text component wit another text"
        PageComponentsViewDialog pageComponentsView = new PageComponentsViewDialog( getSession() );
        pageComponentsView.openMenu( TEXT_1 ).selectMenuItem( "Remove" );
        pageComponentsView.openMenu( "country" ).selectMenuItem( "Insert", "Text" );

        wizard.switchToLiveEditFrame();
        LiveFormPanel liveFormPanel = new LiveFormPanel( getSession() );
        liveFormPanel.typeTextInTextComponent( "test text" );
        wizard.switchToDefaultWindow();
        pageComponentsView.doCloseDialog();
        wizard.save(  );
        contentBrowsePanel.waitForNotificationMessage();
        saveScreenshot( "new-text-inserted" );

        when: "root element in 'page component' dialog has been selected and 'Reset' menu item clicked"
        wizard.showComponentView();
        sleep( 1000 );

        then: "site has been reset to default template, the text from the template appeared in the page editor"
        pageComponentsView.openMenu( PAGE_CONTROLLER_NAME ).selectMenuItem( "Reset" );
        sleep( 1000 );
        saveScreenshot( "text-reset-to-template" );

        and: "Saved button remains disabled after the reset"
        wizard.isSavedButtonDisplayed()
    }

    def "GIVEN site with 2 text-components is opened WHEN components have been swapped by DnD THEN components should be displayed in the new order"()
    {
        given: "site with 2 image-components is opened"
        filterPanel.typeSearchText( SITE_WITH_COMPONENTS_NAME )
        ContentWizardPanel wizard = contentBrowsePanel.clickCheckboxAndSelectRow( SITE.getName() ).clickToolbarEdit();
        wizard.unlockPageEditorAndSwitchToContentStudio().showComponentView();
        LiveFormPanel liveFormPanel = addTextComponent( "second text" );
        saveScreenshot( "two-text-component-in-view" );
        wizard.switchToLiveEditFrame(  );
        LinkedList<String> before = liveFormPanel.getTextInTextComponents();

        when: "components have been swapped"
        wizard.switchToDefaultWindow();
        wizard.showComponentView();
        PageComponentsViewDialog pageComponentsView = new PageComponentsViewDialog( getSession() );
        pageComponentsView.swapComponents( TEXT_2, TEXT_1);
        wizard.save();
        sleep( 2000 );
        wizard.switchToLiveEditFrame();
        LinkedList<String> after = liveFormPanel.getTextInTextComponents();
        saveScreenshot( "page_comp_view_images-swapped" );

        then: "components should be displayed in the new order"
        before.getFirst() == "second text" ;
        and: "components should be displayed in the new order"
        after.getFirst() == TEXT_1;
    }

    private LiveFormPanel addTextComponent( String text )
    {
        PageComponentsViewDialog pageComponentsView = new PageComponentsViewDialog( getSession() );
        pageComponentsView.openMenu( "country" ).selectMenuItem( "Insert", "Text" );
        pageComponentsView.doCloseDialog();
        ContentWizardPanel wizard = new ContentWizardPanel( getSession() );
        wizard.switchToLiveEditFrame(  )
        LiveFormPanel liveFormPanel = new LiveFormPanel( getSession() );
        liveFormPanel.typeTextInTextComponent( text );
        wizard.switchToDefaultWindow(  );
        wizard.save(  );
        sleep( 1000 );
        return new LiveFormPanel( getSession() );
    }

    private void addTemplateWithTextComponent( Content template, String text )
    {
        ContentWizardPanel wizard = contentBrowsePanel.selectContentInGrid( "_templates" ).clickToolbarNew().selectContentType(
            template.getContentTypeName() ).showPageEditor().typeData( template );
        wizard.switchToDefaultWindow();
        wizard.showComponentView();
        addTextComponent( text );
        wizard.closeBrowserTab().switchToBrowsePanelTab();
    }
}
