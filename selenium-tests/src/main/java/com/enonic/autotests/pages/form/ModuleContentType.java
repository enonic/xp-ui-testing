package com.enonic.autotests.pages.form;


public enum ModuleContentType
{
    DATE( ":date" ), DATE_TIME( ":datetime" ), DOUBLE( ":double" ), TIME( ":time" ), LONG( ":long" ), GEO_POINT( ":geopoint" ), CHECKBOX(
    ":checkbox" ), TEXTLINE1_0( ":textline1_0" ), TEXTLINE1_1( ":textline1_1" ), TEXTLINE0_1( ":textline0_1" ), TEXTLINE2_5(
    ":textline2_5" ), TAG0_5( ":tag0_5" ), TAG2_5( ":tag2_5" ), TAG_UNLIM( ":tag_unlim" ), COMBOBOX0_0( ":combobox0_0" ), COMBOBOX0_1(
    ":combobox0_1" ), COMBOBOX1_1( ":combobox1_1" ), COMBOBOX2_4( ":combobox2_4" ), SINGLE_SELECTOR_COMBOBOX0_0(
    ":ss_combobox0_0" ), TEXT_AREA( ":textarea" );

    private String name;

    public String getName()
    {
        return this.name;
    }

    private ModuleContentType( String name )
    {
        this.name = name;
    }
}
