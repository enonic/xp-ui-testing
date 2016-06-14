package com.enonic.autotests.pages.contentmanager.wizardpanel.macro;


public enum MacroType
{
    EMBEDDED_CODE( "Embedded code macro" ), NO_FORMAT( "No Format macro" ), TWITTER( "Twitter macro" ), YOUTUBE(
    "YouTube macro" ), EMBED_IFRAME( "Embed IFrame" ), DISABLE_MACROS( "Disable macros" );

    private MacroType( String value )
    {
        this.value = value;

    }

    private String value;

    public String getValue()
    {
        return this.value;
    }

}
