package com.enonic.autotests.utils;

public class SleepHelper
{

	public static void sleep(long millis)
	{
		try
		{
			Thread.sleep(millis);
		} catch (InterruptedException e)
		{

		}
	}

}
