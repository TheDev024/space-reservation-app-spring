//package org.td024.console;

//import org.springframework.stereotype.Component;
//import org.td024.entity.Interval;
//import org.td024.entity.Interval.IntervalBuilder;
//import org.td024.exception.DatetimeParseException;
//import org.td024.exception.InvalidTimeIntervalException;

//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;

//import static org.td024.console.util.ConsoleReader.readLine;

/*@Component
public class IntervalConsole {

    public Interval getInterval() throws InvalidTimeIntervalException, DatetimeParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        String dateStr = readLine("Enter date (yyyy-MM-dd): ");
        String startTimeStr = readLine("Enter start time (HH:mm): ");
        String endTimeStr = readLine("Enter end time (HH:mm): ");

        Date startTime;
        Date endTime;

        try {
            startTime = dateFormat.parse(dateStr + " " + startTimeStr);
            endTime = dateFormat.parse(dateStr + " " + endTimeStr);
        } catch (ParseException e) {
            throw new DatetimeParseException("Invalid date or time format!");
        }

        return new IntervalBuilder().startTime(startTime).endTime(endTime).build();
    }
}*/
