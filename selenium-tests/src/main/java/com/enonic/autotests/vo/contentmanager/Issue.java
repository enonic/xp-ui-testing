package com.enonic.autotests.vo.contentmanager;

import java.util.List;

/**
 * Created on 7/10/2017.
 */
public class Issue
{
    private final String title;

    private final String description;

    private final List<String> itemsToPublish;

    private final List<String> assignees;

    protected Issue( final Builder builder )
    {
        this.title = builder.title;
        this.description = builder.description;
        this.itemsToPublish = builder.items;
        this.assignees = builder.assignees;
    }

    public static Builder builder()
    {
        return new Builder();
    }

    public String getTitle()
    {
        return title;
    }

    public String getDescription()
    {
        return description;
    }

    public List<String> getAssignees()
    {
        return assignees;
    }

    public List<String> getItemsToPublish()
    {
        return itemsToPublish;
    }

    public static class Builder
    {
        private String title;

        private String description;

        private List<String> items;

        private List<String> assignees;

        public Builder title( final String title )
        {
            this.title = title;
            return this;
        }

        public Builder description( final String description )
        {
            this.description = description;
            return this;
        }

        public Builder assignees( List<String> assignees )
        {
            this.assignees = assignees;
            return this;
        }

        public Builder items( List<String> items )
        {
            this.items = items;
            return this;
        }

        Builder()
        {
        }

        public Builder( Issue issue )
        {
            this.title = issue.getTitle();
            this.description = issue.getDescription();
            this.assignees = issue.getAssignees();
            this.items = issue.getItemsToPublish();
        }

        public Issue build()
        {
            return new Issue( this );
        }
    }
}
