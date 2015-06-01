package com.enonic.wem.uitest.content.move_publish_sort

import com.enonic.autotests.pages.contentmanager.browsepanel.SortContentDialog
import com.enonic.wem.uitest.content.BaseContentSpec

class SortContentDialogSpec
    extends BaseContentSpec
{

    def "GIVEN Content BrowsePanel WHEN one content selected and 'Sort' button clicked THEN 'Sort Content' appears"()
    {
        given: "one selected content"
        contentBrowsePanel.clickCheckboxAndSelectRow( 0 );

        when:
        SortContentDialog sortContentDialog = contentBrowsePanel.clickToolbarSort();

        then:
        sortContentDialog.isPresent();
    }
}
