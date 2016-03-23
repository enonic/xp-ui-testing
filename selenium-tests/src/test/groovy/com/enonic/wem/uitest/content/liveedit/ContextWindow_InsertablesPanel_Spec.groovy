package com.enonic.wem.uitest.content.liveedit

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContextWindowPageInsertablesPanel
import com.enonic.autotests.utils.TestUtils
import com.enonic.wem.uitest.content.BaseContentSpec
import com.enonic.xp.schema.content.ContentTypeName

class ContextWindow_InsertablesPanel_Spec
    extends BaseContentSpec
{
    def "GIVEN 'Page Editor' opened WHEN 'Insert' link clicked THEN 'Insertables' panel is displayed AND "()
    {
        given: "'Page Editor' for the existing site opened"
        ContentWizardPanel wizardPanel = contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.site() );

        when: "'Inspect' link clicked"
        ContextWindowPageInsertablesPanel insertablesPanel = new ContextWindowPageInsertablesPanel( getSession() );
        wizardPanel.showContextWindow().clickOnInsertLink();
        TestUtils.saveScreenshot( getSession(), "insert-opened" );

        then: "'inspect panel' is displayed"
        insertablesPanel.isDisplayed();
        List<String> components = insertablesPanel.getAvailableComponents();

        and: "correct number of components are shown"
        components.size() == 5

        and: "'Image' component are present"
        components.contains( "Image" );

        and: "'Part' component are present"
        components.contains( "Part" );

        and: "'Layout' component are present"
        components.contains( "Layout" );

        and: "'Text' component are present"
        components.contains( "Text" );

        and: "'Fragment' component are present"
        components.contains( "Fragment" );

        and: "correct description displayed"
        insertablesPanel.getTitle() == ContextWindowPageInsertablesPanel.TITLE

    }

}
