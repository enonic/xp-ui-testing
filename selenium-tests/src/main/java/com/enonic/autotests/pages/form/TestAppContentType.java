package com.enonic.autotests.pages.form;


public enum TestAppContentType
{
    DATE( ":date0_1" ), DATE_TIME0_1( ":datetime0_1" ), DATE_TIME1_1( ":datetime1_1" ), DOUBLE( ":double" ), TIME0_0( ":time0_0" ), LONG(
    ":long" ), GEO_POINT( ":geopoint0_0" ), CHECKBOX( ":checkbox" ), TEXTLINE1_0( ":textline1_0" ), TEXTLINE1_1(
    ":textline1_1" ), TEXTLINE0_1( ":textline0_1" ), TEXTLINE2_5( ":textline2_5" ), TAG0_5( ":tag0_5" ), TAG2_5( ":tag2_5" ), TAG_UNLIM(
    ":tag_unlim" ), COMBOBOX0_0( ":combobox0_0" ), COMBOBOX0_1( ":combobox0_1" ), COMBOBOX1_1( ":combobox1_1" ), COMBOBOX2_4(
    ":combobox2_4" ), SINGLE_SELECTOR_COMBOBOX0_1( ":ss_combobox0_1" ), SINGLE_SELECTOR_COMBOBOX1_1( ":ss_combobox1_1" ), TEXT_AREA(
    ":textarea" ), RADIO_BUTTONS( ":radiobutton" ), HTMLAREA0_1( ":htmlarea0_1" ), HTMLAREA0_2( ":htmlarea0_2" ), HTMLAREA0_0(
    ":htmlarea0_0" ), IMAGE_SELCTOR0_0( ":imageselector0_0" ), IMAGE_SELCTOR0_1( ":imageselector0_1" ), IMAGE_SELCTOR1_1(
    ":imageselector1_1" ), IMAGE_SELCTOR2_4( ":imageselector2_4" ), DEFAULT_RELATION0_1( ":default_relation0_1" ), DEFAULT_RELATION2_4(
    ":default_relation2_4" ), CUSTOM_RELATION0_1( ":custom-relationship0_1" ), ARTICLE( ":article" ), COUNTRY( ":country" ), CITY(
    ":city" );

    private String name;

    public String getName()
    {
        return this.name;
    }

    private TestAppContentType( String name )
    {
        this.name = name;
    }
}