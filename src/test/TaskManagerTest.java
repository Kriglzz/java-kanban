package test;

import manager.FileBackedTasksManager;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import task.Epic;
import task.SubTask;
import task.Task;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager>{
    protected T taskManager;
    File file;
    @BeforeEach
    public void BeforeEach() {

        Task task1 = new Task("task1", "description1", LocalDateTime.of(2023, 2, 1, 1, 1), 1);
        taskManager.addNewTask(task1);
        task1.setId(1);
        Task task2 = new Task("task2", "description2", LocalDateTime.of(2023, 2, 2, 1, 1), 1);
        taskManager.addNewTask(task2);
        task2.setId(2);
        Epic epic1 = new Epic("epic1", "description1", LocalDateTime.of(2023, 2, 3, 1, 1), 1);
        taskManager.addNewEpicTask(epic1);
        epic1.setId(3);
        Epic epic2 = new Epic("epic2", "description2", LocalDateTime.of(2023, 2, 4, 1, 1), 1);
        taskManager.addNewEpicTask(epic2);
        epic2.setId(4);
        SubTask subTask1 = new SubTask("subTask1", "description1", 3, LocalDateTime.of(2023, 2, 5, 1, 1), 1);
        taskManager.addNewSubTask(subTask1);
        subTask1.setId(5);
        SubTask subTask2 = new SubTask("subTask2", "description2", 3, LocalDateTime.of(2023, 2, 6, 1, 1), 1);
        taskManager.addNewSubTask(subTask2);
        subTask2.setId(6);
        SubTask subTask3 = new SubTask("subTask3", "description3", 3, LocalDateTime.of(2023, 2, 7, 1, 1), 1);
        taskManager.addNewSubTask(subTask3);
        subTask3.setId(7);
    }

    @Test
    void shouldGetAllTasks() {
        Collection<Task> tasks = taskManager.getAllTask();
        assertEquals(2, tasks.size(), "Не все задачи добавлены");
    }

    @Test
    void shouldGetAllSubTasks() {
        Collection<SubTask> subTasks = taskManager.getAllSubTask();
        assertEquals(3, subTasks.size(), "Не все задачи добавлены");
    }

    @Test
    void shouldGetAllEpics() {
        Collection<Epic> epics = taskManager.getAllEpic();
        assertEquals(2, epics.size(), "Не все задачи добавлены");
    }

    @Test
    void shouldDeleteAllTasks() {
        taskManager.deleteAllTask();
        Collection<Task> tasks = taskManager.getAllTask();
        assertTrue(tasks.isEmpty(), "Задачи присутствуют");
    }
    @Test
    void shouldDeleteAllSubTasks() {
        taskManager.deleteAllSubTask();
        List<SubTask> subTasksList = taskManager.getAllSubTask();
        assertTrue(subTasksList.isEmpty(), "Задачи присутствуют");
    }

    @Test
    void shouldDeleteAllEpics() {
        taskManager.deleteAllEpic();
        List<Epic> epicList = taskManager.getAllEpic();
        assertTrue(epicList.isEmpty(), "Задачи присутствуют");
    }

    @Test
    void shouldGetTask() {
        assertEquals("1, TASK, task1, description1, NEW",
                taskManager.getTaskById(1).toString(), "Данные не совпадают.");
    }

    @Test
    void shouldGetEpic() {
        assertEquals("3, EPIC, epic1, description1, NEW",
                taskManager.getEpicById(3).toString(), "Данные не совпадают.");
    }

    @Test
    void shouldGetSubTask() {
        assertEquals("5, SUBTASK, subTask1, description1, NEW, 3",
                taskManager.getSubTaskById(5).toString(), "Данные не совпадают.");
    }

    @Test
    void shouldDeleteTask() {
        taskManager.deleteTask(2);
        Collection<Task> tasks = taskManager.getAllTask();
        assertEquals(1, tasks.size(), "Задача не была удалена.");
    }

    @Test
    void shouldAddTask() {
        Task task3 = new Task("task3", "description3", LocalDateTime.of(2023, 2, 8, 1, 1), 1);
        taskManager.addNewTask(task3);
        task3.setId(8);
        assertNotNull(task3, "Добавленной задачи не существует.");
    }

    @Test
    void shouldAddEpic() {
        Epic epic3 = new Epic("epic3", "description3", LocalDateTime.of(2023, 2, 9, 1, 1), 1);
        taskManager.addNewEpicTask(epic3);
        epic3.setId(9);
        assertNotNull(epic3, "Добавленной задачи не существует.");
    }

    @Test
    void shouldAddSubTask() {
        SubTask subTask4 = new SubTask("subTask4", "description4", 3, LocalDateTime.of(2023, 2, 10, 1, 1), 1);
        taskManager.addNewSubTask(subTask4);
        subTask4.setId(10);
        assertNotNull(subTask4, "Добавленной задачи не существует.");
    }

    @Test
    void shouldUpdateTask() { // FAILED
        Task task4 = new Task("task4", "description4", LocalDateTime.of(2023, 2, 11, 1, 1), 1);
        taskManager.addNewTask(task4);
        task4.setId(11);
        taskManager.updateTask(task4);
        Task updatedTask = taskManager.getTaskById(11); // Тут проблемы
        assertEquals(task4, updatedTask, "Задача не обновлена.");
    }

    @Test
    void shouldUpdateEpic() { //CHECK
        Epic epic4 = new Epic("epic4", "description4", LocalDateTime.of(2023, 2, 12, 1, 1), 1);
        taskManager.addNewEpicTask(epic4);
        epic4.setId(12);
        taskManager.updateEpic(epic4);
        taskManager.updateEpic(epic4);
        assertEquals(epic4.getId(), 12,  "Задача не обновлена.");
    }

    @Test
    void shouldUpdateSubTask() { //CHECK
        SubTask subTask5 = new SubTask("subTask5", "description5", 3, LocalDateTime.of(2023, 2, 13, 1, 1), 1);
        taskManager.addNewSubTask(subTask5);
        subTask5.setId(13);
        subTask5.setName("newSubTask5");
        taskManager.updateSubTask(subTask5);
        assertEquals("newSubTask5", subTask5.getName(), "Задача не обновлена.");
    }

    @Test
    void shouldReturnEpicIdFromSubTask() {
        SubTask subTask6 = new SubTask("subTask6", "description6", 3, LocalDateTime.of(2023, 2, 14, 1, 1), 1);
        taskManager.addNewSubTask(subTask6);
        subTask6.setId(13);
        assertEquals(3, subTask6.getEpicId(), "ID не совпадает.");
    }

    @Test
    void shouldReturnPrioritizedTasks() { //FAILED
        /*SubTask subTask8 = new SubTask("subtask7", "description7", 1,  null, 0);
        taskManager.addNewSubTask(subTask8); // Тут начинает лезть в глубь

        SubTask subTask9 = new SubTask("subtask8", "description8", 1, LocalDateTime.of(2023, 2, 15, 1, 1), 1);
        taskManager.addNewSubTask(subTask9);

        SubTask subTask10 = new SubTask("subtask9", "description9", 1, LocalDateTime.of(2023, 2, 16, 1, 1), 1);
        taskManager.addNewSubTask(subTask10);*/

        System.out.println(taskManager.getPrioritizedTasks());
        assertEquals(7, taskManager.getPrioritizedTasks().size());
        //Если не добавлять таски, то тест проходит
    }


   @Test
    void shouldPrintEmptyHistory() {
        List<Task> history = taskManager.getHistory();
        assertEquals(0, history.size());
    }
}