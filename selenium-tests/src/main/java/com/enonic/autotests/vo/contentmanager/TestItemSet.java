package com.enonic.autotests.vo.contentmanager;

/**
 * Created on 11.11.2016.
 */
public class TestItemSet
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

    protected TestItemSet( final Builder builder )
    {
        this.htmlAreaText = builder.getHtmlAreaText();
        this.textLineText = builder.getTextLineText();
    }

    public static Builder builder()
    {
        return new Builder();
    }
/////////////////////////////////////////////////////

    public static class Builder

    {
        private String textLineText;

        private String htmlAreaText;

        private Builder()
        {
            super();
        }

        private Builder( final TestItemSet itemSet )
        {
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

        public TestItemSet build()
        {
            return new TestItemSet( this );
        }
    }

}