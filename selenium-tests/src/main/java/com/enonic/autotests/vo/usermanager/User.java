package com.enonic.autotests.vo.usermanager;

import java.util.ArrayList;
import java.util.List;

public class User
    extends Principal
{
    private String email;

    private String password;

    private List<String> roles;

    private List<String> groups;

    public void addRole( String role )
    {
        if ( roles == null )
        {
            roles = new ArrayList<>();
        }
        roles.add( role );
    }

    public void addGroup( String group )
    {
        if ( groups == null )
        {
            groups = new ArrayList<>();
        }
        groups.add( group );
    }


    public List<String> getRoles()
    {
        return roles;
    }

    public List<String> getGroups()
    {
        return groups;
    }

    public static Builder create( final User user )
    {
        return new Builder( user );
    }

    public static Builder builder()
    {
        return new Builder();
    }

    private User( final Builder builder )
    {
        super( builder );
        this.email = builder.email;
        this.password = builder.password;
        this.roles = builder.roles;
        this.groups = builder.groups;
    }

    public String getEmail()
    {
        return this.email;
    }

    public String getPassword()
    {
        return this.password;
    }


    public static class Builder
        extends Principal.Builder<Builder>
    {
        private String email;

        private String password;

        private List<String> roles;

        private List<String> groups;

        private Builder()
        {
            super();
        }

        public Builder email( String email )
        {
            this.email = email;
            return this;
        }

        public Builder roles( List<String> roles )
        {
            this.roles = roles;
            return this;
        }

        public Builder groups( List<String> groups )
        {
            this.groups = groups;
            return this;
        }

        public Builder password( String password )
        {
            this.password = password;
            return this;
        }

        private Builder( final User user )
        {
            super( user );
            this.email = user.getEmail();
            this.password = user.getPassword();
            this.groups = user.getGroups();
            this.roles = user.getRoles();
        }


        public User build()
        {
            return new User( this );
        }
    }
}

