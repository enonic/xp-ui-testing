package com.enonic.autotests.vo.contentmanager;

/**
 * Created  on 10.11.2016.
 */
public class ItemSet
    extends Content
{
    private String textLineText;

    private String htmlAreaText;

    public String getTextLineText()
    {
        return this.textLineText;
    }

    public String getHtmlAreaText()
    {
        return this.htmlAreaText;
    }

    protected ItemSet( final Builder builder )
    {
        super( builder );
        this.htmlAreaText = builder.getHtmlAreaText();
        this.textLineText = builder.getTextLineText();
    }

    public static Builder builder()
    {
        return new Builder();
    }
/////////////////////////////////////////////////////

    public static class Builder
        extends Content.Builder<ItemSet.Builder, ItemSet>
    {
        private String textLineText;

        private String htmlAreaText;

        private Builder()
        {
            super();
        }

        private Builder( final ItemSet itemSet )
        {
            super( itemSet );
            this.htmlAreaText = itemSet.getHtmlAreaText();
            this.textLineText = itemSet.getTextLineText();
        }

        public String getHtmlAreaText()
        {
            return this.htmlAreaText;
        }

        public String getTextLineText()
        {
            return this.textLineText;
        }

        public Builder textLineText( String text )
        {
            this.textLineText = text;
            return this;
        }

        public Builder htmlAreaText( String nameInMenu )
        {
            this.htmlAreaText = nameInMenu;
            return this;
        }

        public ItemSet build()
        {
            return new ItemSet( this );
        }
    }

}
