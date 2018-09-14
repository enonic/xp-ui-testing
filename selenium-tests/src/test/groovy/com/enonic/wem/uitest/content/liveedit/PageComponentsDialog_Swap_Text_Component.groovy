package com.enonic.wem.uitest.content.liveedit

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.PageComponentsViewDialog
import com.enonic.autotests.pages.form.liveedit.LiveFormPanel
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Shared

class PageComponentsDialog_Swap_Text_Component
    extends BaseContentSpec
{
    @Shared
    Content SITE;

    @Shared
    String TEXT1 = "text 1";

    @Shared
    String TEXT2 = "text 2";


    def "Preconditions: new site wizard is opened WHEN name and 2 text components have been inserted THEN 2 required strings should be present on the Live Form Panel"()
    {
        given: "data typed and saved and wizard closed"
        SITE = buildMyFirstAppSite( "site" );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( SITE.getContentTypeName() );
        wizard.typeData( SITE ).selectPageDescriptor( COUNTRY_REGION_PAGE_CONTROLLER );
        PageComponentsViewDialog pageComponentsView = wizard.showComponentView();

        when: "2 text components have been inserted"
        pageComponentsView.openMenu( "country" ).selectMenuItem( "Insert", "Text" );
        wizard.switchToLiveEditFrame();
        LiveFormPanel liveFormPanel = new LiveFormPanel( getSession() );
        liveFormPanel.typeTextInTextComponent( TEXT1 );
        and:
        wizard.switchToDefaultWindow();
        pageComponentsView.openMenu( "country" ).selectMenuItem( "Insert", "Text" );
        wizard.switchToLiveEditFrame();
        liveFormPanel.typeTextInTextComponent( TEXT2 );
        wizard.switchToDefaultWindow();
        pageComponentsView.doCloseDialog();
        wizard.save();

        then: "2 required strings should be present on the Live Form Panel"
        wizard.switchToLiveEditFrame();
        liveFormPanel.getTextFromTextComponents().size() == 2;
    }

    def "GIVEN site with 2 text-components is opened WHEN swapping components by DnD THEN components should be displayed in the new order"()
    {
        given: "site with 2 text-components is opened"
        filterPanel.typeSearchText( SITE.getName(  ) );
        ContentWizardPanel wizard = contentBrowsePanel.clickCheckboxAndSelectRow( SITE.getName() ).clickToolbarEdit();
        wizard.switchToLiveEditFrame();
        LiveFormPanel liveFormPanel =  new LiveFormPanel(getSession(  ));
        LinkedList<String> before = liveFormPanel.getTextFromTextComponents();
        saveScreenshot( "page_comp_view_text_before_swapping" );

        when: "swapping of components by DnD"
        wizard.switchToDefaultWindow();
        wizard.showComponentView();
        PageComponentsViewDialog pageComponentsView = new PageComponentsViewDialog( getSession() );
        pageComponentsView.swapComponents( TEXT1, TEXT2 ).doCloseDialog(  );
        wizard.save();
        sleep( 2000 );
        wizard.switchToLiveEditFrame();
        LinkedList<String> after = liveFormPanel.getTextFromTextComponents();
        saveScreenshot( "page_comp_view_text_swapped" );

        then: "components should be displayed in the new order"
        before.get(0) == TEXT2;
        and: "components should be displayed in the new order"
        after.get(0) == TEXT1;
    }
}
