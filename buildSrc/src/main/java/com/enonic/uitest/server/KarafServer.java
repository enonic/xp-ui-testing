package com.enonic.uitest.server;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.ops4j.pax.exam.CoreOptions;
import org.ops4j.pax.exam.ExamSystem;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.TestContainer;
import org.ops4j.pax.exam.karaf.options.KarafDistributionBaseConfigurationOption;
import org.ops4j.pax.exam.karaf.options.KarafDistributionOption;
import org.ops4j.pax.exam.spi.PaxExamRuntime;

public final class KarafServer
{
    private TestContainer container;

    private Set<File> files;

    private File unpackDir;

    private long startupDelay = 1000;

    public void files( final Set<File> files )
    {
        this.files = files;
    }
/*
        for ( final File file : files )
        {
            if ( file.getName().startsWith( "wem-distro-" ) )
            {
                this.options.add( karafFramework( file.toURI().toString() ) );
            }
            else
            {
                this.options.add( bundleFile( file.toURI().toString() ) );
            }
        }
    }*/

    public void unpackDir( final File dir )
    {
        this.unpackDir = dir;
    }

/*
        this.options.add( KarafDistributionOption.karafDistributionConfiguration().unpackDirectory( dir ) );
        System.out.println( dir );
    }*/

    public void startupDelay( final long delay )
    {
        this.startupDelay = delay;
    }

    private Option[] getOptions()
    {
        final List<Option> options = new ArrayList<>();
        options.add( KarafDistributionOption.configureConsole().ignoreLocalConsole() );
        options.add( KarafDistributionOption.keepRuntimeFolder() );

        final KarafDistributionBaseConfigurationOption distConfig = KarafDistributionOption.karafDistributionConfiguration();
        options.add( distConfig );

        distConfig.useDeployFolder( true );
        if ( this.unpackDir != null )
        {
            distConfig.unpackDirectory( this.unpackDir );
        }

        for ( final File file : files )
        {
            if ( file.getName().startsWith( "distro-" ) )
            {
                distConfig.frameworkUrl( file.toURI().toString() );
            }
            else
            {
                options.add( bundleFile( file.toURI().toString() ) );
            }
        }

        return options.toArray( new Option[options.size()] );
    }

    public void start()
        throws Exception
    {
        final ExamSystem system = PaxExamRuntime.createServerSystem( getOptions() );
        this.container = PaxExamRuntime.createContainer( system );
        this.container.start();

        System.out.println( "Waiting for " + this.startupDelay + " ms for server to start..." );
        Thread.sleep( this.startupDelay );
    }

    public void stop()
    {
        this.container.stop();
    }

    private static Option karafFramework( final String url )
    {
        return KarafDistributionOption.karafDistributionConfiguration().frameworkUrl( url );
    }

    private static Option bundleFile( final String url )
    {
        return CoreOptions.bundle( url ).start();
    }
}
