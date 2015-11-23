package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.xp.schema.content.ContentTypeName

class ContentWizardPanel_Toolbar_Spec
    extends BaseContentSpec
{
    def "WHEN content wizard opened  THEN all buttons on toolbar have correct state"()
    {
        when: "content wizard opened"
        ContentWizardPanel wizardPanel = contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder() );

        then: "'Delete' button enabled"
        wizardPanel.isDeleteButtonEnabled();

        and: "'Save draft' button enabled"
        wizardPanel.isSaveButtonEnabled();

        and: "'Publish' button disabled"
        !wizardPanel.isPublishButtonEnabled();

        and: "'Duplicate' button disabled"
        !wizardPanel.isDuplicateButtonEnabled();

        and: "content status is offline"
        wizardPanel.getStatus() == "Offline";
    }

}
