package com.enonic.autotests.vo.contentmanager;


public class ArticleContent
    extends BaseAbstractContent
{

    protected ArticleContent( Builder<?> builder )
    {
        super( builder );
        //setContentTypeName(ContentTypeName.ARCHIVE.getValue());

    }

    private String title;

    private String text;

    public static abstract class Builder<T extends ArticleContent>
        extends BaseAbstractContent.Builder<T>
    {

        public abstract T build();
    }

    public static Builder<?> builder()
    {
        return new Builder<ArticleContent>()
        {
            @Override
            public ArticleContent build()
            {
                return new ArticleContent( this );
            }
        };
    }
}
