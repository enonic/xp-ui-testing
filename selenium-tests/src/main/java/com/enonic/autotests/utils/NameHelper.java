package com.enonic.autotests.utils;

import java.util.Random;

public class NameHelper
{

    /**
     * Builds unique name for content
     *
     * @param name
     */
    public static String uniqueName( String name )
    {
        return name + Math.abs( new Random().nextInt() );

    }

    public static String resolveScreenshotName( String item )
    {
        if ( item.contains( "/" ) )
        {
            return item.substring( item.lastIndexOf( "/" ) + 1 );
        }
        else
        {
            return item;
        }
    }

}
