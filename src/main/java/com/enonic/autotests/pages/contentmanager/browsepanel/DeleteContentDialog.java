package com.enonic.autotests.pages.contentmanager.browsepanel;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.BaseDeleteDialog;

/**
 *  This Dialog appears, when customer try to delete a content.
 *
 */
public class DeleteContentDialog extends BaseDeleteDialog
{
	private final String TITLE_XPATH = "//div[contains(@class,'modal-dialog delete-dialog')]/div[@class='dialog-header' and contains(.,'Delete Content')]";
	/**
	 * The constructor.
	 * 
	 * @param session
	 * @param displayNamesToDelete
	 */
	public DeleteContentDialog( TestSession session)
	{
		super(session);
		
	}

	@Override
	public String getTitleXpath()
	{		
		return TITLE_XPATH;
	}

}
