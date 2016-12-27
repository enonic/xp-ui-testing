package com.enonic.autotests.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Created  on 23.12.2016.
 */
public class TimeUtils
{
    public static String addMinutesToCurrentDateTime( long minutes )
    {
        LocalDateTime now = LocalDateTime.now();
        now.plusMinutes( minutes );
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm" );
        return now.format( formatter );
    }

    public static String minusMinutesFromCurrentDateTime( long minutes )
    {
        LocalDateTime now = LocalDateTime.now();
        now.minusMinutes( minutes );
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm" );
        return now.format( formatter );
    }

    public static String getTomorrowDateTime()
    {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime tomorrow = today.plus( 1, ChronoUnit.DAYS );
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm" );
        String formatDateTime = tomorrow.format( formatter );
        return formatDateTime;
    }

    public static String getYesterdayDateTime()
    {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime tomorrow = today.minus( 1, ChronoUnit.DAYS );
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm" );
        String formatDateTime = tomorrow.format( formatter );
        return formatDateTime;
    }

    public static String getNowDateTime()
    {
        LocalDateTime today = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm" );
        String formatDateTime = today.format( formatter );
        return formatDateTime;
    }

    public static String getNowDate()
    {
        LocalDate nowDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "yyyy-MM-dd" );
        String formatLocalDate = nowDate.format( formatter );
        return formatLocalDate;
    }
}
