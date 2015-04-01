package com.enonic.autotests.vo.contentmanager;

public class ComboBoxOption
{
    private String value;

    private String label;

    public ComboBoxOption( Builder builder )
    {
        this.value = builder.value;
        this.label = builder.label;

    }

    public String getLabel()
    {
        return label;
    }


    public String getValue()
    {
        return value;
    }


    public static class Builder
    {
        private String label;

        private String value;


        Builder()
        {
        }

        public Builder( final ComboBoxOption option )
        {
            this.label = option.getLabel();
            this.value = option.getValue();
        }

        public Builder label( final String label )
        {
            this.label = label;
            return this;
        }


        public Builder value( final String value )
        {
            this.value = value;
            return this;
        }


        public ComboBoxOption build()
        {
            return new ComboBoxOption( this );
        }
    }

    public static Builder builder()
    {
        return new Builder();
    }


}
