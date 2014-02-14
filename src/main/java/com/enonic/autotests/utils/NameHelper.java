package com.enonic.autotests.utils;

import java.util.Random;

public class NameHelper
{

    /**
     * Builds unique name for content
     *
     * @param name
     */
    public static String unqiueContentName( String name )
    {
        return name + Math.abs( new Random().nextInt() );

    }

}
