package com.enonic.autotests;

import java.util.HashMap;
import java.util.Map;

public enum BrowserName
{
    FIREFOX( "firefox" ), HTMLUNIT( "htmlunit" ), IE( "ie" ), OPERA( "opera" ), CHROME( "chrome" );

    private String name;

    public String getName()
    {
        return name;
    }

    private final static Map<String, BrowserName> map = new HashMap<String, BrowserName>();

    static
    {
        for ( BrowserName browser : values() )
        {
            map.put( browser.name, browser );
        }
    }

    private BrowserName( String name )
    {
        this.name = name;
    }

    public static BrowserName findByValue( String value )
    {
        return value != null ? map.get( value.toLowerCase() ) : null;
    }
}
