package com.enonic.autotests;


public enum XP_Windows
{
    CONTENT_STUDIO( "content-studio" ), USER_MANAGER( "user-manager" ), LIVE_EDIT( "live-edit" );

    private String windowName;


    private XP_Windows( String windowName )
    {
        this.windowName = windowName;
    }

    public String getWindowName()
    {
        return windowName;
    }
}
