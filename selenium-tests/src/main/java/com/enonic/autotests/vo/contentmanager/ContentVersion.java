package com.enonic.autotests.vo.contentmanager;


public class ContentVersion
{
    private String info;

    private String status;

    public ContentVersion( Builder builder )
    {
        this.info = builder.info;
        this.status = builder.status;
    }

    public String getInfo()
    {
        return info;
    }

    public String getStatus()
    {
        return status;
    }

    public static class Builder
    {
        private String info;


        private String status;

        Builder()
        {
        }

        public Builder( final ContentVersion versionInfo )
        {
            this.info = versionInfo.getInfo();
            this.status = versionInfo.getStatus();

        }

        public Builder info( final String info )
        {
            this.info = info;
            return this;
        }


        public Builder status( final String status )
        {
            this.status = status;
            return this;
        }

        public ContentVersion build()
        {
            return new ContentVersion( this );
        }
    }

    public static Builder builder()
    {
        return new Builder();
    }


}
