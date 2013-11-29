package com.enonic.autotests.pages.spaceadmin;

import java.util.List;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.BaseDeleteDialog;

/**
 * When user try to delete a space, Dialog should appears.
 * 
 */
public class DeleteSpaceDialogPage extends BaseDeleteDialog
{
	private final String TITLE_XPATH = "//div[@class='modal-dialog delete-dialog']/h2[contains(.,'Delete Space')]";


	/**
	 * The constructor
	 * 
	 * @param session
	 * @param spacesToDelete
	 */
	public DeleteSpaceDialogPage( TestSession session, List<String> spacesToDelete )
	{
		super(session, spacesToDelete);
	}



	@Override
	public String getTitleXpath()
	{	
		return  TITLE_XPATH;
	}
}
