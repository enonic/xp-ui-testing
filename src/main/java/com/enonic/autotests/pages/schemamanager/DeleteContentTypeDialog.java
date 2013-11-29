package com.enonic.autotests.pages.schemamanager;

import java.util.List;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.BaseDeleteDialog;

/**
 * When user try to delete a space, Dialog should appears.
 * 
 */
public class DeleteContentTypeDialog extends BaseDeleteDialog
{
	private final String TITLE_XPATH = "//div[contains(@class,'admin-window')]//h2[contains(.,'Delete schema(s)')]";


	/**
	 * The constructor
	 * 
	 * @param session
	 * @param spacesToDelete
	 */
	public DeleteContentTypeDialog( TestSession session, List<String> spacesToDelete )
	{
		super(session, spacesToDelete);
	}



	@Override
	public String getTitleXpath()
	{	
		return  TITLE_XPATH;
	}
}
