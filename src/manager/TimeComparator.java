package manager;

import task.Task;

import java.time.LocalDateTime;
import java.util.Comparator;

public class TimeComparator implements Comparator<Task> {

    @Override
    public int compare(Task o1, Task o2) {
        LocalDateTime startTime1 = o1.getStartTime();
        LocalDateTime startTime2 = o2.getStartTime();
        LocalDateTime endTime1 = o1.getEndTime();
        LocalDateTime endTime2 = o2.getEndTime();

        if (startTime1 == null && endTime1 == null && startTime2 == null && endTime2 == null) {
            return o1.getName().compareTo(o2.getName());
        } else {
            // Сравниваем по начальному времени
            if (startTime1 != null && startTime2 != null) {
                int startTimeComparison = startTime1.compareTo(startTime2);
                if (startTimeComparison != 0) {
                    return startTimeComparison;
                }
            } else if (startTime1 != null) {
                return 1;
            } else if (startTime2 != null) {
                return -1;
            }
            // Если время одинаковое
            if (endTime1 != null && endTime2 != null) {
                int endTimeComparison = endTime1.compareTo(endTime2);
                if (endTimeComparison != 0) {
                    return endTimeComparison;
                }
            } else if (endTime1 != null) {
                return 1;
            } else if (endTime2 != null) {
                return -1;
            }
            // Начальное и конечное одинаковое
            return o1.getName().compareTo(o2.getName());
        }
    }
}
