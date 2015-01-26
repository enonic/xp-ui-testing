package com.enonic.wem.uitest

import com.enonic.wem.uitest.content.*
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite.class)
@Suite.SuiteClasses([VersiontestResetApp.class, ContentBrowsePanel_ContextMenu_Spec.class, ContentBrowsePanel_GridPanel_DeleteSpec.class, ContentBrowsePanel_GridPanel_SaveSpec.class, ContentBrowsePanel_GridPanel_Spec.class, ContentBrowsePanel_ItemsSelectionPanel_DeleteSpec.class,
    ContentBrowsePanel_ItemsSelectionPanel_Spec.class, ContentBrowsePanel_Performance.class, ContentBrowsePanelToolbarSpec.class, ContentItemViewPanelSpec.class, ContentWizardPanel_SaveBeforeCloseDialogSpec.class,
    ContentWizardPanel_TabMenuSpec.class, DeleteContentDialogSpec.class, NewContentDialogSpec.class, LoginSpec.class])


public class IntegrationSuite
{

}