package com.enonic.autotests.pages.contentmanager.browsepanel;

import java.util.List;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.BaseDeleteDialog;

/**
 *  This Dialog appears, when customer try to delete a content.
 *
 */
public class DeleteContentDialog extends BaseDeleteDialog
{
	private final String TITLE_XPATH = "//div[contains(@class,'modal-dialog delete-dialog')]//h2[contains(.,'Delete Content')]";
	/**
	 * The constructor.
	 * 
	 * @param session
	 * @param displayNamesToDelete
	 */
	public DeleteContentDialog( TestSession session, List<String> displayNamesToDelete )
	{
		super(session, displayNamesToDelete);
		
	}

	@Override
	public String getTitleXpath()
	{		
		return TITLE_XPATH;
	}

}
