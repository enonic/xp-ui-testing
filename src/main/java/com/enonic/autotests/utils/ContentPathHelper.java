package com.enonic.autotests.utils;

import com.enonic.wem.api.content.ContentPath;
import com.google.common.base.Joiner;

public class ContentPathHelper
{

	/**
	 * @param args
	 */
	public static ContentPath buildContentPath(String[] names, String contentName)
	{
		
		 String pathAsString = "";
	        if ( names != null && names.length != 0 )
	        {
	            pathAsString = Joiner.on( "/" ).skipNulls().join( names );
	        }
	        pathAsString += "/" + contentName;
	        return ContentPath.from( pathAsString );
	}
	
	public static ContentPath buildRootContentPath()
	{
	        return ContentPath.from( "/" );
	}
	


}
