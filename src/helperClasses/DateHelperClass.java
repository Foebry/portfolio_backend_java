package helperClasses;

import java.util.ArrayList;
import java.util.Calendar;

public class DateHelperClass {

    public ArrayList<Integer> convertStringToIntegerArray(String dateString, String format) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        if (format == "date") {
            String[] splittedDate = dateString.split("-");
            String year = splittedDate[0];
            String month = splittedDate[1];
            String day = splittedDate[2].split(" ")[0];

            result.add(Integer.parseInt(year));
            result.add(Integer.parseInt(month));
            result.add(Integer.parseInt(day));
        }

        return result;
    }

    public Calendar convertStringToCalendar(String dateString, String format) {
        Calendar calendar = Calendar.getInstance();

        if (format == "date") {
            ArrayList<Integer> arraylist = convertStringToIntegerArray(dateString, format);
            // subtract 1 from month since Calendar uses 0-based index.
            // this means dateString 2024-01-20 will result in February 20th;
            calendar.set(arraylist.get(0), arraylist.get(1) - 1, arraylist.get(2));
        }

        return calendar;
    }
}
