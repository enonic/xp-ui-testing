package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.PdfFormViewPanel
import spock.lang.Shared

class Content_TextExtractionData_Spec
    extends BaseContentSpec
{
    @Shared
    String TEST_PDF_FILE = "pdf.pdf";

    @Shared
    String TEST_TXT_FILE = "test-text.txt";

    @Shared
    String PDF_EXTRACTION_TEXT = "test extraction text";

    @Shared
    String TXT_EXTRACTION_TEXT = "Minsk Belarus";

    def "WHEN existing PDF content has been opened THEN textarea for extraction of data should be present in the wizard"()
    {
        when: "existing PDF content is opened"
        findAndSelectContent( TEST_PDF_FILE ).clickToolbarEditAndSwitchToWizardTab();
        PdfFormViewPanel formViewPanel = new PdfFormViewPanel( getSession() );
        saveScreenshot( "test_text_extraction_pdf" )

        then: "textarea for extraction of data should be present"
        formViewPanel.isTextAreaPresent();
    }

    def "GIVEN existing PDF content is opened WHEN new extraction data has been typed AND the content saved THEN expected text should be displayed in the text-area"()
    {
        given: "existing PDF content is opened"
        ContentWizardPanel wizard = findAndSelectContent( TEST_PDF_FILE ).clickToolbarEditAndSwitchToWizardTab();
        PdfFormViewPanel formViewPanel = new PdfFormViewPanel( getSession() );

        when: "new extraction data has been typed and saved, wizard has been closed"
        formViewPanel.setExtractionData( PDF_EXTRACTION_TEXT );
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();

        and: "the content has been opened again"
        contentBrowsePanel.clickToolbarEditAndSwitchToWizardTab();
        saveScreenshot( "test_text_extraction_pdf_updated" );

        then: "expected text should be present in the text-area"
        formViewPanel.getExtractionData() == PDF_EXTRACTION_TEXT;
    }

    def "GIVEN existing pdf content with 'extraction text' WHEN the text typed in the search input THEN only one content should be displayed in the grid"()
    {
        when: "the text has been typed in the search input"
        filterPanel.typeSearchText( PDF_EXTRACTION_TEXT );
        saveScreenshot( "test_text_extraction_search_text_pdf" );

        then: "only one content should be displayed in the grid"
        contentBrowsePanel.getRowsCount() == 1;

        and: "content with expected name should be present"
        contentBrowsePanel.exists( TEST_PDF_FILE );
    }

    def "GIVEN existing txt-content WHEN text from this content typed in the search input THEN only one content should be present in the grid"()
    {
        when: "the text has been typed in the search input"
        filterPanel.typeSearchText( TXT_EXTRACTION_TEXT );
        saveScreenshot( "test_text_extraction_search_text_txt" );

        then: "only one content should be present in the grid"
        contentBrowsePanel.getRowsCount() == 1;

        and: "content with expected name should be present in the grid"
        contentBrowsePanel.exists( TEST_TXT_FILE );
    }
}
