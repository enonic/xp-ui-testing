package com.enonic.autotests.vo.schemamanger;

import com.enonic.autotests.pages.schemamanager.KindOfContentTypes;

/**
 * Model of ContentType.
 */
public class ContentType
{

    private String name;

    private String configData;

    private KindOfContentTypes kind;

    public void setDisplayNameInConfig( String newDisplayName )
    {
        String tagStart = "<display-name>";
        String tagEnd = "</display-name>";
        int start = configData.indexOf( tagStart );
        int end = configData.indexOf( tagEnd );
        configData = configData.substring( 0, start + tagStart.length() ) + newDisplayName + configData.substring( end );
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

    public KindOfContentTypes getKind()
    {
        return kind;
    }

    public void setKind( KindOfContentTypes kind )
    {
        this.kind = kind;
    }


    public String getConfigData()
    {
        return configData;
    }

    public void setConfigData( String configData )
    {
        this.configData = configData;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public ContentType cloneContentType()
    {
        ContentType clon = new ContentType();
        clon.setName( this.getName() );
        clon.setConfigData( this.getConfigData() );
        clon.setKind( this.getKind() );
        return clon;
    }

    public static Builder with()
    {
        return new Builder();
    }

    public static class Builder
    {
        private String bName;

        private KindOfContentTypes bKind;

        private String bConfigData;

        public Builder()
        {

        }

        public Builder name( String name )
        {
            this.bName = name;
            return this;
        }


        public Builder configuration( String conf )
        {
            this.bConfigData = conf;
            return this;
        }

        public Builder kind( KindOfContentTypes kind )
        {
            this.bKind = kind;
            return this;
        }

        public ContentType build()
        {
            ContentType ctype = new ContentType();

            ctype.name = this.bName;
            ctype.configData = this.bConfigData;
            ctype.kind = this.bKind;
            return ctype;
        }
    }

}
