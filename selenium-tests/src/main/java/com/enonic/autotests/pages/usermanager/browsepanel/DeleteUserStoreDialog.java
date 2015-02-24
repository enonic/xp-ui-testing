package com.enonic.autotests.pages.usermanager.browsepanel;


import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.BaseDeleteDialog;

public class DeleteUserStoreDialog
    extends BaseDeleteDialog
{
    private final String TITLE_XPATH =
        "//div[contains(@id,'UserStoreDeleteDialog')]//div[contains(@id,'api.ui.dialog.ModalDialogHeader') and child::h2[text()='Delete User Store']]";

    /**
     * The constructor.
     *
     * @param session
     */
    public DeleteUserStoreDialog( TestSession session )
    {
        super( session );

    }

    @Override
    public String getTitleXpath()
    {
        return TITLE_XPATH;
    }

}
