package com.enonic.wem.uitest.contentimport com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanelimport com.enonic.autotests.pages.contentmanager.browsepanel.NewContentDialogimport com.enonic.autotests.pages.contentmanager.browsepanel.NewContentDialog.FilterNameimport com.enonic.autotests.services.NavigatorHelperimport com.enonic.wem.uitest.BaseGebSpecimport spock.lang.Sharedclass NewContentDialogSpec    extends BaseGebSpec{    @Shared    ContentBrowsePanel contentBrowsePanel;    def setup()    {        go "admin"        contentBrowsePanel = NavigatorHelper.openContentApp( getTestSession() );    }    def "GIVEN content App BrowsePanel WHEN New button clicked THEN 'new-content' dialog with title 'What do you whant to create' showed"()    {        when:        NewContentDialog newContentDialog = contentBrowsePanel.clickToolbarNew();        then:        newContentDialog.isOpened();    }    def "GIVEN content App BrowsePanel  WHEN 'new-content dialog' was opened THEN 'All' 'Content' 'Sites' filter items are present"()    {        when:        NewContentDialog newContentDialog = contentBrowsePanel.clickToolbarNew();        then:        newContentDialog.isPresentElement( NewContentDialog.SHOW_ALL_LINK ) &&            newContentDialog.isPresentElement( NewContentDialog.FILTER_BY_SITES_LINK ) &&            newContentDialog.isPresentElement( NewContentDialog.FILTER_BY_CONTENT_LINK );    }    def "GIVEN opened a 'new-content' dialog WHEN 'Sites' filter item was clicked THEN number of items in the list-view and number in link Sites(...) are equals"()    {        given:        NewContentDialog newContentDialog = contentBrowsePanel.clickToolbarNew();        int beforeFiltering = newContentDialog.getNumberContentTypesFromList();        // get number of types from the string: 'Sites(3)', or Content(43):        int expectedNumberOfContentTypes = newContentDialog.getNumberItemsFromFilterLink( FilterName.SITES );        when:        newContentDialog.doFilterBySites();        then:        int filterdContentTypes = newContentDialog.getNumberContentTypesFromList();        filterdContentTypes < beforeFiltering && filterdContentTypes == expectedNumberOfContentTypes;    }    def "GIVEN opened a 'new-content' dialog WHEN 'Content(...)' link was clicked THEN number of items in the list-view and number in link Content(...) are equals"()    {        given:        NewContentDialog newContentDialog = contentBrowsePanel.clickToolbarNew();        int beforeFiltering = newContentDialog.getNumberContentTypesFromList();        // get number of types from the string: 'Sites(3)', or Content(43):        int expectedNumberOfListItems = newContentDialog.getNumberItemsFromFilterLink( FilterName.CONTENT );        when:        newContentDialog.doFilterByContent();        then:        int filteredContentTypes = newContentDialog.getNumberContentTypesFromList();        filterdContentTypes < beforeFiltering && filteredContentTypes == expectedNumberOfListItems;    }    def "GIVEN opened a 'new-content' dialog WHEN 'All(...)' link was clicked THEN number of items in the list-view and number in link All(...) are equals"()    {        given:        NewContentDialog newContentDialog = contentBrowsePanel.clickToolbarNew();        // get number of types from the string: 'Sites(3)', or Content(43):        int expectedNumberOfListItems = newContentDialog.getNumberItemsFromFilterLink( FilterName.SHOW_ALL );        when:        newContentDialog.doClickShowAll();        then:        int filterdContentTypes = newContentDialog.getNumberContentTypesFromList();        filterdContentTypes == expectedNumberOfListItems;    }    def "GIVEN opened a 'new-content' dialog WHEN 'Sites(...)' link was clicked THEN number of items in the list and number in link All(...) are equals"()    {        given:        NewContentDialog newContentDialog = contentBrowsePanel.clickToolbarNew();        when:        newContentDialog.doFilterBySites();        then:        int filterdContentTypes = newContentDialog.getNumberContentTypesFromList();    }    def "GIVEN opened a 'new-content' dialog and 'All' link is active WHEN 'Content(...)' link was clicked THEN 'Content' link is active "()    {        given:        NewContentDialog newContentDialog = contentBrowsePanel.clickToolbarNew();        when:        newContentDialog.doFilterByContent();        then:        newContentDialog.isLinkActive( FilterName.CONTENT ) && !newContentDialog.isLinkActive( FilterName.SHOW_ALL );    }    def "GIVEN opened a 'new-content' dialog and 'All' link is active WHEN 'Sites(...)' link was clicked THEN 'Sites' link is active "()    {        given:        NewContentDialog newContentDialog = contentBrowsePanel.clickToolbarNew();        when:        newContentDialog.doFilterBySites();        then:        newContentDialog.isLinkActive( FilterName.SITES ) && !newContentDialog.isLinkActive( FilterName.SHOW_ALL );    }    def "GIVEN opened a 'new-content' dialog WHEN adding text-search 'archive' , the name of a content type THEN only one content type showed in the list "()    {        given:        NewContentDialog newContentDialog = contentBrowsePanel.clickToolbarNew();        when:        newContentDialog.typeSearchText( "archive" );        then:        newContentDialog.getNumberContentTypesFromList() == 1;    }    def "GIVEN opened a 'new-content' dialog, 'All()' link is active WHEN adding text-search 'HTML Area' , the display-name of a content type THEN only one content type showed in the list "()    {        given:        NewContentDialog newContentDialog = contentBrowsePanel.clickToolbarNew();        when:        newContentDialog.typeSearchText( "HTML Area" );        then:        newContentDialog.getNumberContentTypesFromList() == 1;    }    def "GIVEN opened a 'new-content' dialog, 'Sites()' link is active WHEN adding text-search 'HTML Area' -content's name THEN empty list should be showed "()    {        given:        NewContentDialog newContentDialog = contentBrowsePanel.clickToolbarNew();        newContentDialog.doFilterBySites();        when:        newContentDialog.typeSearchText( "HTML Area" );        then:        newContentDialog.getNumberContentTypesFromList() == 0;    }    def "GIVEN opened a 'new-content' dialog, 'Content()' link is active WHEN adding text-search 'bluman' - site's name THEN empty list should be showed "()    {        given:        NewContentDialog newContentDialog = contentBrowsePanel.clickToolbarNew();        newContentDialog.doFilterByContent();        when:        newContentDialog.typeSearchText( "bluman" );        then:        newContentDialog.getNumberContentTypesFromList() == 0;    }    def "GIVEN opened a 'new-content' dialog and added text-search 'archive'  WHEN search input cleared THEN initial list of content types showed"()    {        given:        NewContentDialog newContentDialog = contentBrowsePanel.clickToolbarNew();        int number = newContentDialog.getNumberContentTypesFromList();        newContentDialog.typeSearchText( "archive" );        when:        newContentDialog.clearSearchInput();        then:        newContentDialog.getNumberContentTypesFromList() == number;    }}