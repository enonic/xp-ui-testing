package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.wizardpanel.InsertImageModalDialog
import com.enonic.autotests.pages.form.HtmlArea0_1_FormViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Ignore

/**
 * Created  on 2/28/2017.
 *
 * verifies:
 * Error appears in the console when an Image has been inserted into HtmlArea #4957
 * */
class HtmlArea_InsertImageDialog_Spec
    extends Base_InputFields_Occurrences
{
    def "GIVEN wizard for html-area content is opened WHEN 'Insert Image' button has been clicked THEN modal dialog should appear"()
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

    def "GIVEN InsertImageModalDialog is opened WHEN an image was inserted THEN Image toolbar should be displayed"()
    {
        given: "InsertImageModalDialog is opened"
        Content htmlAreaContent = buildHtmlArea0_1_Content( null );
        selectSitePressNew( htmlAreaContent.getContentTypeName() );
        HtmlArea0_1_FormViewPanel formViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );
        InsertImageModalDialog insertImageModalDialog = formViewPanel.showToolbarAndClickOnInsertImageButton();

        when: "image has been inserted"
        insertImageModalDialog.selectImage( "book" );
        saveScreenshot( "insert_image_button_dialog" );

        then: "'Image Dialog toolbar' should be displayed"
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

        and: "'Apply style' selector should be displayed"
        insertImageModalDialog.isApplyStyleSelectorDisplayed();
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
        saveScreenshot( "insert_image_button_pressed1" );

        then: "the dialog should be closed"
        insertImageModalDialog.waitForClosed();
    }
    //verifies Error appears in the console when an Image has been inserted into HtmlArea #4957
    def "GIVEN InsertImageModalDialog is opened AND text is inserted WHEN an image has been inserted AND 'Insert' button pressed THEN InsertImageModalDialog should be closed"()
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
        saveScreenshot( "insert_image_button_pressed2" );

        then: "the dialog should be closed"
        insertImageModalDialog.waitForClosed();
    }
}
