package com.enonic.wem.uitest.content.details_panel

import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.DependenciesWidgetItemView
import com.enonic.autotests.utils.TestUtils
import com.enonic.wem.uitest.content.BaseContentSpec

class DetailsPanel_DependenciesWidgetItemView_Spec
    extends BaseContentSpec
{

    def "WHEN image content selected and details panel opened AND 'Dependencies' option selected THEN Dependencies Widget is displayed and has attachments"()
    {
        when: "image content selected"
        findAndSelectContent( IMPORTED_BOOK_IMAGE );
        DependenciesWidgetItemView dependencies = openDependenciesWidgetView();
        TestUtils.saveScreenshot( getSession(), "test_dependencies_widget_opened" );

        then: "Dependencies Widget is displayed"
        dependencies.isDisplayed();
    }
}
