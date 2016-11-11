package com.enonic.autotests.vo.contentmanager;

import java.util.List;

/**
 * Created  on 10.11.2016.
 */
public class ItemSetContent
    extends Content
{
    private List<TestItemSet> itemSets;

    public List<TestItemSet> getItemSets()
    {
        return this.itemSets;
    }

    protected ItemSetContent( final Builder builder )
    {
        super( builder );
        this.itemSets = builder.getItemSets();

    }

    public static Builder builder()
    {
        return new Builder();
    }
/////////////////////////////////////////////////////

    public static class Builder
        extends Content.Builder<ItemSetContent.Builder, ItemSetContent>
    {
        private List<TestItemSet> itemSets;

        private Builder()
        {
            super();
        }

        private Builder( final ItemSetContent itemSetContent )
        {
            super( itemSetContent );
            this.itemSets = itemSetContent.getItemSets();
        }

        public List<TestItemSet> getItemSets()
        {
            return this.itemSets;
        }


        public Builder itemSets( List<TestItemSet> itemSets )
        {
            this.itemSets = itemSets;
            return this;
        }

        public ItemSetContent build()
        {
            return new ItemSetContent( this );
        }
    }

}
