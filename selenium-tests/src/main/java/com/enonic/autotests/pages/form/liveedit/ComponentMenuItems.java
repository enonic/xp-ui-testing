package com.enonic.autotests.pages.form.liveedit;

/**
 * Created on 09.01.2017.
 */
public enum ComponentMenuItems
{
    SELECT_PARENT( "Select parent" ), INSERT( "Insert" ), INSPECT( "Inspect" ), RESET( "Reset" ), REMOVE( "Remove" ), DUPLICATE(
    "Duplicate" ), EDIT_IN_NEW_TAB( "Edit" ), SAVE_AS_TEMPLATE( "Save as Template" ), SAVE_AS_FRAGMENT( "Save as Fragment" );

    private String value;

    public String getValue()
    {
        return value;
    }

    private ComponentMenuItems( String value )
    {
        this.value = value;
    }
}
