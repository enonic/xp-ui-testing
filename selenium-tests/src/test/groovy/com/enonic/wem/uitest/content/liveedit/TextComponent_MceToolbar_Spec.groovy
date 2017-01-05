package com.enonic.wem.uitest.content.liveedit

import com.enonic.autotests.pages.contentmanager.SourceCodeMceWindow
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.LiveEditComponentContextMenu
import com.enonic.autotests.pages.contentmanager.wizardpanel.PageComponentsViewDialog
import com.enonic.autotests.pages.form.liveedit.LiveFormPanel
import com.enonic.autotests.pages.form.liveedit.MceToolbar
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Shared

/**
 * Created on 13.09.2016.
 *
 * TASKS: XP-4298 Add selenium tests to verify XP-4280
 *        XP-4306 Add selenium tests for SourceCode modal window
 *       XP-4068 Add selenium tests for mce-toolbar in a Text-component
 *
 * Verifies: XP-4280
 * */
class TextComponent_MceToolbar_Spec
    extends BaseContentSpec
{

    @Shared
    Content SITE

    @Shared
    String TEST_TEXT = "test text";

    def "GIVEN existing site with selected controller WHEN text component inserted THEN mce-toolbar appears AND all buttons are present"()
    {
        given:
        SITE = buildSimpleSiteApp();
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( SITE.getContentTypeName() ).typeData(
            SITE ).selectPageDescriptor( MAIN_REGION_PAGE_DESCRIPTOR_NAME ).save();
        PageComponentsViewDialog pageComponentsView = wizard.showComponentView();

        when: "new site should be present"
        pageComponentsView.openMenu( "main" ).selectMenuItem( "Insert", "Text" );
        wizard.switchToLiveEditFrame();
        LiveFormPanel liveFormPanel = new LiveFormPanel( getSession() );
        liveFormPanel.typeTextInTextComponent( TEST_TEXT );
        MceToolbar mceToolbar = liveFormPanel.getMceToolbar();
        saveScreenshot( "mce_toolbar_should_be_displayed" );


        then: "mce toolbar is displayed"
        mceToolbar.isDisplayed();

        and: "'Formats' menu is displayed on the toolbar"
        mceToolbar.isFormatsMenuDisplayed();

        and: "'Align Left' menu is displayed on the toolbar"
        mceToolbar.isAlignLeftButtonDisplayed();

        and: "'align Right' menu is displayed on the toolbar"
        mceToolbar.isAlignRightButtonDisplayed();

        and: "'Align Center' menu is displayed on the toolbar"
        mceToolbar.isAlignCenterButtonDisplayed();

        and: "'Justify' menu is displayed on the toolbar"
        mceToolbar.isAlignJustifyButtonDisplayed();

        and: "'Bullet List' menu is displayed on the toolbar"
        mceToolbar.isBulListButtonDisplayed();

        and: "'Numbered List' menu is displayed on the toolbar"
        mceToolbar.isNumListButtonDisplayed();


        and: "'Decrease Indent' menu is displayed on the toolbar"
        mceToolbar.isOutdentButtonDisplayed();

        and: "'Increase Indent' menu is displayed on the toolbar"
        mceToolbar.isIndentButtonDisplayed();

        and: "'Special Characters' menu is displayed on the toolbar"
        mceToolbar.isSpecialCharButtonDisplayed();

        and: "'Anchor' menu is displayed on the toolbar"
        mceToolbar.isAnchorButtonDisplayed();

        and: "'Insert Table' menu is displayed on the toolbar"
        mceToolbar.isInsertTableButtonDisplayed();

        and: "'Insert Image' menu is displayed on the toolbar"
        mceToolbar.isInsertImageButtonDisplayed();

        and: "'Insert Macro' menu is displayed on the toolbar"
        mceToolbar.isInsertMacroButtonDisplayed();

        and: "'Remove Link' menu is displayed on the toolbar"
        mceToolbar.isRemoveLinkButtonDisplayed();

        and: "'Insert Link' menu is displayed on the toolbar"
        mceToolbar.isInsertLinkButtonDisplayed();

        and: "'Source Code' menu is displayed on the toolbar"
        mceToolbar.isSourceCodeButtonDisplayed();
    }

    def "GIVEN existing text component  WHEN the component right-clicked AND 'Edit' menu item was selected AND 'Source Code' button clicked THEN Source Code window is opened"()
    {
        given: "existing text component"
        ContentWizardPanel wizard = findAndSelectContent( SITE.getName() ).clickToolbarEdit();
        PageComponentsViewDialog pageComponentsView = wizard.showComponentView();
        pageComponentsView.openMenu( "main" ).selectMenuItem( "Insert", "Text" );
        wizard.switchToLiveEditFrame();
        LiveFormPanel liveFormPanel = new LiveFormPanel( getSession() );
        liveFormPanel.typeTextInTextComponent( TEST_TEXT );
        wizard.switchToDefaultWindow();
        pageComponentsView.doCloseDialog();

        and: "right-click on the text-component"
        wizard.switchToLiveEditFrame();
        LiveEditComponentContextMenu contextMenu = liveFormPanel.clickOnTextComponentAndShowContextMenu();

        and: "'Edit' menu item was selected"
        contextMenu.clickOnEdit();
        saveScreenshot( "test_component_edit" )

        when: "'Source Code' button clicked"
        MceToolbar mceToolbar = liveFormPanel.getMceToolbar();
        SourceCodeMceWindow sourceCodeMceWindow = mceToolbar.clickOnSourceCodeButton();

        then: "modal dialog is opened"
        sourceCodeMceWindow.isOpened();

        and: "correct text is present in the window"
        sourceCodeMceWindow.getText().contains( TEST_TEXT );

        and: "source code window has the correct title"
        sourceCodeMceWindow.isTitlePresent();

        and: "source code window has the correct title"
        sourceCodeMceWindow.getTitle() == SourceCodeMceWindow.WINDOW_TITLE;

        and: "button Cancel is displayed"
        sourceCodeMceWindow.isButtonCancelPresent();

        and: "button Ok is displayed"
        sourceCodeMceWindow.isButtonOkPresent();

        and: "button 'Close' is displayed"
        sourceCodeMceWindow.isButtonClosePresent();
    }
}