package manager;

import task.Task;

import java.time.LocalDateTime;
import java.util.Comparator;

public class TimeComparator implements Comparator<Task> {

    @Override
    public int compare(Task o1, Task o2) {
        LocalDateTime time1 = o1.getStartTime();
        LocalDateTime time2 = o2.getStartTime();
        if (time1 == null && time2 == null) {
            return o1.getName().compareTo(o2.getName());
        } else if (time1 == null) {
            return 1;
        } else if (time2 == null) {
            return -1;
        } else {
            int dateTimeComparison = time1.compareTo(time2);
            if (dateTimeComparison == 0) {
                return o1.getName().compareTo(o2.getName());
            }
            return dateTimeComparison;
        }
    }
}
