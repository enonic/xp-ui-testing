package com.enonic.autotests.pages.contentmanager;

/**
 * Created  on 3/16/2017.
 */
public enum ContentMenuItem
{

    NEW( "New..." ), EDIT( "Edit" ), ARCHIVE( "Archive..." ), DUPLICATE( "Duplicate..." ), MOVE( "Move..." ), SORT(
    "Sort..." ), PREVIEW( "Preview" ), PUBLISH( "Publish..." ), UNPUBLISH( "Unpublish..." );

    private String name;

    public String getName()
    {
        return this.name;
    }

    private ContentMenuItem( String name )
    {
        this.name = name;
    }
}
