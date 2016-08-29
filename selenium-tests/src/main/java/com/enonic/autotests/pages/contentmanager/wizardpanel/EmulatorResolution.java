package com.enonic.autotests.pages.contentmanager.wizardpanel;

/**
 * Created on 29.08.2016.
 */
public enum EmulatorResolution
{

    MEDIUM_PHONE( "Medium Phone" ), LARGE_TELEPHONE( "Large Phone" ), TABLET( "Tablet" ), NOTEBOOK_13( "13\" Notebook" );


    private String value;

    private EmulatorResolution( String value )
    {
        this.value = value;
    }

    public String getValue()
    {
        return this.value;
    }
}
