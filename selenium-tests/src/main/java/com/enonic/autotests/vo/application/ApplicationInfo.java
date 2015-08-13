package com.enonic.autotests.vo.application;


public class ApplicationInfo
{
    private String buildDate;

    private String version;

    private String key;

    private String systemRequired;

    public ApplicationInfo( Builder builder )
    {
        this.buildDate = builder.buildDate;
        this.version = builder.version;
        this.key = builder.key;
        this.systemRequired = builder.systemRequired;
    }

    public String getSystemRequired()
    {
        return this.systemRequired;
    }

    public String getBuildDate()
    {
        return buildDate;
    }

    public String getVersion()
    {
        return version;
    }

    public String getKey()
    {
        return this.key;
    }

    public static class Builder
    {
        private String buildDate;

        private String version;

        private String key;

        private String systemRequired;

        Builder()
        {
        }

        public Builder( final ApplicationInfo applicationInfo )
        {
            this.buildDate = applicationInfo.getBuildDate();
            this.version = applicationInfo.getVersion();
            this.key = applicationInfo.getKey();
            this.systemRequired = applicationInfo.getSystemRequired();
        }

        public Builder buildDate( final String buildDate )
        {
            this.buildDate = buildDate;
            return this;
        }

        public Builder version( final String version )
        {
            this.version = version;
            return this;
        }

        public Builder key( final String key )
        {
            this.key = key;
            return this;
        }

        public ApplicationInfo build()
        {
            return new ApplicationInfo( this );
        }
    }

    public static Builder builder()
    {
        return new Builder();
    }


}

