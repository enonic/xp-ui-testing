package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.PdfFormViewPanel
import spock.lang.Shared

class ContentWizard_TextExtractionData
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

    def "GIVEN existing PDF content WHEN content opened THEN textarea for extraction of data is present"()
    {
        when: ""
        findAndSelectContent( TEST_PDF_FILE ).clickToolbarEdit().waitUntilWizardOpened();
        PdfFormViewPanel formViewPanel = new PdfFormViewPanel( getSession() );
        saveScreenshot( "test_text_extraction_pdf" )

        then: "textarea for extraction of data is present"
        formViewPanel.isTextAreaPresent();
    }

    def "GIVEN existing PDF content WHEN content opened AND new extraction data typed AND content saved THEN correct text displayed in the text-area"()
    {
        given: "existing PDF content"
        ContentWizardPanel wizard = findAndSelectContent( TEST_PDF_FILE ).clickToolbarEdit().waitUntilWizardOpened();
        PdfFormViewPanel formViewPanel = new PdfFormViewPanel( getSession() );

        when: "content opened, new extraction data typed"
        formViewPanel.setExtractionData( PDF_EXTRACTION_TEXT );
        wizard.save().close( TEST_PDF_FILE );

        and: "the content opend again "
        contentBrowsePanel.clickToolbarEdit();
        saveScreenshot( "test_text_extraction_pdf_updated" );

        then: "correct text displayed in the text-area"
        formViewPanel.getExtractionData() == PDF_EXTRACTION_TEXT;
    }

    def "GIVEN existing pdf content with saved 'extraction text' WHEN the text typed in the search input THEN only one content shown in the grid"()
    {
        when: "the text typed in the search input"
        filterPanel.typeSearchText( PDF_EXTRACTION_TEXT );
        saveScreenshot( "test_text_extraction_search_text_pdf" );

        then: "only one content shown in the grid"
        contentBrowsePanel.getRowsCount() == 1;

        and: "content with correct name is shown"
        contentBrowsePanel.exists( TEST_PDF_FILE );
    }

    def "GIVEN existing txt-content WHEN text from this content typed in the search input THEN only one content shown in the grid"()
    {
        when: "the text typed in the search input"
        filterPanel.typeSearchText( TXT_EXTRACTION_TEXT );
        saveScreenshot( "test_text_extraction_search_text_txt" );

        then: "only one content shown in the grid"
        contentBrowsePanel.getRowsCount() == 1;

        and: "content with correct name is shown"
        contentBrowsePanel.exists( TEST_TXT_FILE );
    }
}
