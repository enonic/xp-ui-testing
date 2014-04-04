package com.enonic.autotests.utils;

public class SchemaCfgHelper
{

    /**
     * @param args
     */
    public static String changeDisplayName( String displayName, String configData )
    {
        String tagStart = "<display-name>";
        String tagEnd = "</display-name>";
        int start = configData.indexOf( tagStart );
        int end = configData.indexOf( tagEnd );
        configData = configData.substring( 0, start + tagStart.length() ) + displayName + configData.substring( end );
        return configData;

    }

}
