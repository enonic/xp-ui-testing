package com.enonic.autotests.pages;

public enum BaseContentType
{
    FOLDER( "Folder" ), SHORTCUT( "Shortcut" ), SITE( "Site" ),PAGE_TEMPLATE("Page Template");

    private String displayName;

    public String getDisplayName()
    {
        return this.displayName;
    }

    private BaseContentType( String displayName )
    {
        this.displayName = displayName;
    }
}
