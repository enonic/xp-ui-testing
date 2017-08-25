package com.enonic.autotests.exceptions;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by sgauruseu on 8/23/2017.
 */
public class Tets1
{
    public static void main( String[] args )
    {
        Path currentRelativePath = Paths.get( "" );
        String s = currentRelativePath.toAbsolutePath().toString();
        currentRelativePath.toAbsolutePath().toString();
        System.out.println( "Current relative path is: " + s );
        File file = new File( currentRelativePath.toAbsolutePath().toString() );
        System.out.println( "cccccc" + file.isDirectory() );
        System.out.println( "cccccc" + file.getName() );
    }
}
