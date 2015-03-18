package com.enonic.autotests.vo.usermanager;

import java.util.ArrayList;
import java.util.List;

public class User
    extends Principal
{
    private String email;

    private String password;

    private List<String> roles;

    public void addRole( String role )
    {
        if ( roles == null )
        {
            roles = new ArrayList<>();
        }
        roles.add( role );
    }

    public List<String> getRoles()
    {
        return roles;
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
        }


        public User build()
        {
            return new User( this );
        }
    }
}

