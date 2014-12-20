package com.enonic.autotests.vo.usermanager;

public class User
    extends Principal
{
    private String email;

    private String password;

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

        private Builder()
        {
            super();
        }

        public Builder email( String email )
        {
            this.email = email;
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

