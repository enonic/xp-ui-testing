package com.enonic.autotests.vo.contentmanager;


public class Site
    extends Content
{
    private String moduleName;

    protected Site( final Builder builder )
    {
        super( builder );

    }

    public String getModuleName()
    {
        return this.moduleName;
    }

    public void setModuleName( String moduleName )
    {
        this.moduleName = moduleName;
    }

    public String getDescription()
    {
        return this.getData().getString( "description" );
    }

    public static class Builder
        extends Content.Builder<Builder>
    {
        private String moduleName;

        private Builder()
        {
            super();
        }

        private Builder( final Site site )
        {
            super( site );
            this.moduleName = site.getModuleName();
        }

        public Builder moduleName( String moduleName )
        {
            this.moduleName = moduleName;
            return this;
        }

        public Site build()
        {
            return new Site( this );
        }
    }
}
