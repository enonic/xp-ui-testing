package com.enonic.wem.uitest.content.move_publish_sort

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
import com.enonic.autotests.pages.contentmanager.browsepanel.SortContentDialog
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.wem.uitest.BaseGebSpec
import spock.lang.Shared

class SortContentDialogSpec
    extends BaseGebSpec
{

    @Shared
    ContentBrowsePanel contentBrowsePanel;

    def setup()
    {
        go "admin"
        contentBrowsePanel = NavigatorHelper.openContentApp( getTestSession() );
    }

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
