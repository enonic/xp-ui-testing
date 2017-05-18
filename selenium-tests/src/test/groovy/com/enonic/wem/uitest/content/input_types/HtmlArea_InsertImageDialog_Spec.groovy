package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.wizardpanel.InsertImageModalDialog
import com.enonic.autotests.pages.form.HtmlArea0_1_FormViewPanel
import com.enonic.autotests.vo.contentmanager.Content

/**
 * Created  on 2/28/2017.
 *
 * Tasks:
 * xp-ui-testing#18 Add Selenium tests for InsertImage modal dialog
 * xp-ui-testing#4 Check fixed application's bugs and add Selenium tests for each fixed bugs
 *
 * verifies:
 * Error appears in the console when an Image has been inserted into HtmlArea #4957
 * */
class HtmlArea_InsertImageDialog_Spec
    extends Base_InputFields_Occurrences
{
    def "GIVEN wizard for html-area content is opened WHEN 'Insert Image' button has been clicked THEN modal dialog should be displayed"()
    {
        given: "wizard for html-area content is opened"
        Content htmlAreaContent = buildHtmlArea0_1_Content( null );
        selectSitePressNew( htmlAreaContent.getContentTypeName() );
        HtmlArea0_1_FormViewPanel formViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );

        when: "'Insert Image' button has been clicked"
        InsertImageModalDialog insertImageModalDialog = formViewPanel.showToolbarAndClickOnInsertImageButton();

        then: " modal dialog should be displayed"
        insertImageModalDialog.getHeader() == InsertImageModalDialog.HEADER_TEXT;

        and: "option filter input should be displayed"
        insertImageModalDialog.isOptionFilterInputDisplayed();

        and: "'Cancel' button should be displayed"
        insertImageModalDialog.isCancelButtonDisplayed();

        and: "'Insert' button should be displayed"
        insertImageModalDialog.isInsertButtonDisplayed();

        and: "Image toolbar should not be displayed"
        !insertImageModalDialog.isToolbarDisplayed();

    }
    // verifies XP-4949 HTML Area - Modal dialogs must handle close on Esc
    def "GIVEN  InsertImageModalDialog is opened WHEN 'Escape' key has been pressed THEN modal dialog should not be displayed"()
    {
        given: "wizard for html-area content is opened"
        Content htmlAreaContent = buildHtmlArea0_1_Content( null );
        selectSitePressNew( htmlAreaContent.getContentTypeName() );
        HtmlArea0_1_FormViewPanel formViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );
        InsertImageModalDialog insertImageModalDialog = formViewPanel.showToolbarAndClickOnInsertImageButton();

        when: "'Escape' key has been clicked"
        insertImageModalDialog.pressEscapeKey();

        then: "modal dialog should not be displayed"
        insertImageModalDialog.waitForClosed();
    }

    def "GIVEN  InsertImageModalDialog is opened WHEN 'Cancel' button has been clicked THEN modal dialog should not be displayed"()
    {
        given: "InsertImageModalDialog is opened"
        Content htmlAreaContent = buildHtmlArea0_1_Content( null );
        selectSitePressNew( htmlAreaContent.getContentTypeName() );
        HtmlArea0_1_FormViewPanel formViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );
        InsertImageModalDialog insertImageModalDialog = formViewPanel.showToolbarAndClickOnInsertImageButton();

        when: "'Cancel' button has been clicked"
        insertImageModalDialog.clickOnCancelButton();

        then: "modal dialog should not be displayed"
        insertImageModalDialog.waitForClosed();
    }

    def "GIVEN InsertImageModalDialog is opened WHEN an image was inserted THEN Image toolbar should be displayed"()
    {
        given: "InsertImageModalDialog is opened"
        Content htmlAreaContent = buildHtmlArea0_1_Content( null );
        selectSitePressNew( htmlAreaContent.getContentTypeName() );
        HtmlArea0_1_FormViewPanel formViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );
        InsertImageModalDialog insertImageModalDialog = formViewPanel.showToolbarAndClickOnInsertImageButton();

        when: "image has been inserted"
        insertImageModalDialog.selectImage( "book" );

        then: "'Image toolbar' should be displayed"
        insertImageModalDialog.isToolbarDisplayed();

        and: "Justify button should be displayed"
        insertImageModalDialog.isJustifyButtonDisplayed();

        and: "Justify button should be displayed"
        insertImageModalDialog.isJustifyButtonDisplayed();

        and: "Align Center button should be displayed"
        insertImageModalDialog.isAlignCenterButtonDisplayed();

        and: "Align Left button should be displayed"
        insertImageModalDialog.isAlignLeftButtonDisplayed();

        and: "Align Right button should be displayed"
        insertImageModalDialog.isAlignRightButtonDisplayed();

        and: "'Image Cropping' selector should be displayed"
        insertImageModalDialog.isCroppingSelectorDisplayed();
    }
    //verifies Error appears in the console when an Image has been inserted into HtmlArea #4957
    def "GIVEN InsertImageModalDialog is opened WHEN an image has been inserted AND 'Insert' button pressed THEN InsertImageModalDialog should be closed"()
    {
        given: "InsertImageModalDialog is opened"
        Content htmlAreaContent = buildHtmlArea0_1_Content( null );
        selectSitePressNew( htmlAreaContent.getContentTypeName() );
        HtmlArea0_1_FormViewPanel formViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );
        InsertImageModalDialog insertImageModalDialog = formViewPanel.showToolbarAndClickOnInsertImageButton();

        when: "image has been inserted"
        insertImageModalDialog.selectImage( "book" ).clickOnInsertButton();

        then: "the dialog should be closed"
        insertImageModalDialog.waitForClosed();
    }
    //verifies Error appears in the console when an Image has been inserted into HtmlArea #4957
    def "GIVEN InsertImageModalDialog is opened ADN text is inserted WHEN an image has been inserted AND 'Insert' button pressed THEN InsertImageModalDialog should be closed"()
    {
        given: "creating of a HtmlArea content"
        Content htmlAreaContent = buildHtmlArea0_1_Content( "test text" );
        selectSitePressNew( htmlAreaContent.getContentTypeName() );
        HtmlArea0_1_FormViewPanel formViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );
        and: "text has been typed"
        formViewPanel.type( htmlAreaContent.getData() );
        and: "InsertImageModalDialog is opened"
        InsertImageModalDialog insertImageModalDialog = formViewPanel.showToolbarAndClickOnInsertImageButton();

        when: "image has been inserted"
        insertImageModalDialog.selectImage( "book" ).clickOnInsertButton();

        then: "the dialog should be closed"
        insertImageModalDialog.waitForClosed();
    }
}
