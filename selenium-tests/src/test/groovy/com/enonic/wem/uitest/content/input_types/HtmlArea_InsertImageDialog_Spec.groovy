package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.wizardpanel.InsertImageModalDialog
import com.enonic.autotests.pages.form.HtmlArea0_1_FormViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Ignore

/**
 * Created  on 2/28/2017.
 *
 * Tasks: xp-ui-testing#18 Add Selenium tests for InsertImage modal dialog
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

        //TODO remove it when issue will be fixed
        //and: "'Cancel top' button should be displayed"
        //insertImageModalDialog.isCancelButtonTopDisplayed();
    }

    //TODO when bug will be fixed
    @Ignore
    def "GIVEN  InsertImageModalDialog is opened WHEN 'Cancel Top' button has been clicked THEN modal dialog should not be displayed"()
    {
        given: "wizard for html-area content is opened"
        Content htmlAreaContent = buildHtmlArea0_1_Content( null );
        selectSitePressNew( htmlAreaContent.getContentTypeName() );
        HtmlArea0_1_FormViewPanel formViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );
        InsertImageModalDialog insertImageModalDialog = formViewPanel.showToolbarAndClickOnInsertImageButton();

        when: "'Insert Image' button has been clicked"
        insertImageModalDialog.clickOnCancelTopButton();

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

        when: "'Insert Image' button has been clicked"
        insertImageModalDialog.clickOnCancelButton();

        then: "modal dialog should not be displayed"
        insertImageModalDialog.waitForClosed();
    }

    def "GIVEN  InsertImageModalDialog is opened WHEN an image was inserted THEN Image toolbar should be displayed"()
    {
        given: "InsertImageModalDialog is opened"
        Content htmlAreaContent = buildHtmlArea0_1_Content( null );
        selectSitePressNew( htmlAreaContent.getContentTypeName() );
        HtmlArea0_1_FormViewPanel formViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );
        InsertImageModalDialog insertImageModalDialog = formViewPanel.showToolbarAndClickOnInsertImageButton();

        when: "image was inserted"
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
}
