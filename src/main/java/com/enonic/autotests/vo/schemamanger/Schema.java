package com.enonic.autotests.vo.schemamanger;

import com.enonic.autotests.pages.schemamanager.SchemaKindUI;

public class Schema
{

    protected final String name;

    protected final String configData;

    protected SchemaKindUI schemaKindUI;

    /**
     * Constructor
     *
     * @param builder
     */
    protected Schema( final Builder builder )
    {
        this.name = builder.name;
        this.configData = builder.configData;
    }

    public String getDisplayNameFromConfig()
    {
        String tagStart = "<display-name>";
        String tagEnd = "</display-name>";
        int start = configData.indexOf( tagStart );
        int end = configData.indexOf( tagEnd );
        return configData.substring( start + tagStart.length(), end );
    }

    public String getSuperTypeNameFromConfig()
    {
        String tagStart = "<super-type>";
        String tagEnd = "</super-type>";
        int start = configData.indexOf( tagStart );
        if ( start == -1 )
        {
            return null;
        }
        int end = configData.indexOf( tagEnd );
        return configData.substring( start + tagStart.length(), end );

    }

    public String getConfigData()
    {
        return configData;
    }

    public String getName()
    {
        return name;
    }

    public SchemaKindUI getSchemaKindUI()
    {
        return schemaKindUI;
    }

    public static class Builder<T extends Builder>
    {
        private String name;

        private String configData;

        public Builder()
        {

        }

        public Builder( final Schema schema )
        {
            this.name = schema.name;
            this.configData = schema.configData;
        }

        private T getThis()
        {
            return (T) this;
        }

        public T name( final String value )
        {
            this.name = value;
            return getThis();
        }

        public T configData( String configData )
        {
            this.configData = configData;
            return getThis();
        }

    }
}
