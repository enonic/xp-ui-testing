package com.enonic.autotests.vo.contentmanager;

import java.util.List;


public class PageTemplate
    extends Content
{
    private List<String> supports;

    private String nameInMenu;

    public String getNameInMenu()
    {
        return nameInMenu;
    }

    public void setNameInMenu( String nameInMenu )
    {
        this.nameInMenu = nameInMenu;
    }

    public List<String> getSupports()
    {
        return this.supports;
    }

    public void setSupports( List<String> supports )
    {
        this.supports = supports;
    }

    protected PageTemplate( final Builder builder )
    {
        super( builder );
    }

    public static class Builder
        extends Content.Builder<Builder, PageTemplate>
    {
        private List<String> supports;

        private String nameInMenu;

        private Builder()
        {
            super();
        }

        private Builder( final PageTemplate template )
        {
            super( template );
            this.supports = template.getSupports();
            this.nameInMenu = template.getNameInMenu();
        }

        public Builder supports( List<String> supports )
        {
            this.supports = supports;
            return this;
        }

        public Builder nameInMenu( String nameInMenu )
        {
            this.nameInMenu = nameInMenu;
            return this;
        }

        public PageTemplate build()
        {
            return new PageTemplate( this );
        }
    }
}
