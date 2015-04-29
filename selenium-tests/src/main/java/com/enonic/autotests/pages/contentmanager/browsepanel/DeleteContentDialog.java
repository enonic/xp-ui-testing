package com.enonic.autotests.pages.contentmanager.browsepanel;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.BaseDeleteDialog;

/**
 * This Dialog appears, when customer try to delete a content.
 */
public class DeleteContentDialog
    extends BaseDeleteDialog
{
    private final String TITLE_XPATH =
        "//div[contains(@id,'ContentDeleteDialog')]//div[contains(@id,'api.ui.dialog.ModalDialogHeader') and child::h2[text()='Delete item']]";

    /**
     * The constructor.
     *
     * @param session
     */
    public DeleteContentDialog( TestSession session )
    {
        super( session );

    }

    @Override
    public String getTitleXpath()
    {
        return TITLE_XPATH;
    }

}
