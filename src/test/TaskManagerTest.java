package test;

import manager.TaskManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import task.Epic;
import task.Status;
import task.SubTask;
import task.Task;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {

    protected final T taskManager;
    protected int taskId;
    protected int epicId;
    protected int subTaskId;
    protected TaskManagerTest(T taskManager){
        this.taskManager = taskManager;
    }
    @BeforeEach
    public void BeforeEach(){
        taskManager.deleteAllTask();
        taskManager.deleteAllEpic();
        taskManager.deleteAllSubTask();
    }
    @Test
    void shouldAddTask() {
        Task task = new Task("task", "description", LocalDateTime.now(), 1);
        taskManager.addNewTask(task);
        taskId = task.getId();
        Task savedTask = taskManager.getTaskById(taskId);
        savedTask.setId(taskId);
        assertNotNull(savedTask, "Добавленной задачи не существует.");
        assertEquals(task, savedTask, "Задачи не совпадают.");
    }

    @Test
    void shouldAddEpic() {
        Epic epic = new Epic("epic", "description", LocalDateTime.now(), 1);
        taskManager.addNewEpicTask(epic);
        epicId = epic.getId();
        Epic savedEpic = taskManager.getEpicById(epicId);
        savedEpic.setId(epicId);

        assertNotNull(savedEpic, "Добавленной задачи не существует.");
        assertEquals(epic, savedEpic, "Задачи не совпадают");
        assertEquals(Status.NEW,savedEpic.getStatus());
    }

    @Test
    void shouldAddSubTask() {
        Epic epicTemp = new Epic("epicTemp", "descriptionTemp", LocalDateTime.now(), 1);
        SubTask subTask = new SubTask("subTask","description", epicTemp.getId(), LocalDateTime.now().plusMinutes(1), 1);
        taskManager.addNewEpicTask(epicTemp);
        taskManager.addNewSubTask(subTask);
        epicId = epicTemp.getId();
        subTaskId = subTask.getId();
        SubTask savedSubtask = taskManager.getSubTaskById(subTaskId);
        savedSubtask.setId(subTaskId);

        assertNotNull(savedSubtask, "Добавленной задачи не существует.");
        assertEquals(subTask, savedSubtask, "Задачи не совпадают");
        assertEquals(epicId, subTask.getEpicId(), "Epic ID не совпадает");
    }
    @Test
    void shouldGetTask() {
        Task task = new Task("task","description", LocalDateTime.now(), 1);
        taskManager.addNewTask(task);
        taskId = task.getId();
        assertNotNull(taskManager.getTaskById(taskId), "Задача не возвращается");
        assertEquals(task, taskManager.getTaskById(taskId), "Задачи не совпадают");
    }

    @Test
    void shouldGetEpic() {
        Epic epic = new Epic("epic","description", LocalDateTime.now(), 1);
        taskManager.addNewEpicTask(epic);
        epicId = epic.getId();
        assertNotNull(taskManager.getEpicById(epicId), "Задача не возвращается");
        assertEquals(epic, taskManager.getEpicById(epicId), "Задачи не совпадают");
    }

    @Test
    void shouldGetSubTask() {
        Epic epicTemp = new Epic("epic","description", LocalDateTime.now(), 1);
        SubTask subTask = new SubTask("subTask","description",epicTemp.getId(), LocalDateTime.now().plusMinutes(1), 1);
        taskManager.addNewEpicTask(epicTemp);
        taskManager.addNewSubTask(subTask);
        epicId = epicTemp.getId();
        subTaskId = subTask.getId();
        assertNotNull(taskManager.getSubTaskById(subTaskId), "Задача не возвращается");
        assertEquals(subTask, taskManager.getSubTaskById(subTaskId), "Задачи не совпадают");
        assertEquals(epicId, subTask.getEpicId(), "Epic ID не совпадает");
    }

    @Test
    void shouldGetAllTasks() {
        Task task1 = new Task("task1","description", LocalDateTime.now(), 1);
        Task task2 = new Task("task2","description", LocalDateTime.now().plusMinutes(1), 1);
        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);
        Collection<Task> tasks = taskManager.getAllTask();
        assertEquals(2, tasks.size(), "Не все задачи добавлены");
    }

    @Test
    void shouldGetAllSubTasks() {
        Epic epicTemp = new Epic("epic","description", LocalDateTime.now(), 1);
        SubTask subTask1 = new SubTask("subTask1","description",epicTemp.getId(), LocalDateTime.now().plusMinutes(1), 1);
        SubTask subTask2 = new SubTask("subTask2","description",epicTemp.getId(), LocalDateTime.now().plusMinutes(3), 1);
        taskManager.addNewEpicTask(epicTemp);
        taskManager.addNewSubTask(subTask1);
        taskManager.addNewSubTask(subTask2);
        Collection<SubTask> subTasks = taskManager.getAllSubTask();
        assertEquals(2, subTasks.size(), "Не все задачи добавлены");
    }

    @Test
    void shouldGetAllEpics() {
        Epic epic1 = new Epic("epic1","description", LocalDateTime.now(), 1);
        Epic epic2 = new Epic("epic2","description", LocalDateTime.now().plusMinutes(1), 1);
        taskManager.addNewEpicTask(epic1);
        taskManager.addNewEpicTask(epic2);
        Collection<Epic> epics = taskManager.getAllEpic();
        assertEquals(2, epics.size(), "Не все задачи добавлены");
    }

    @Test
    void shouldDeleteAllTasks() {
        Task task1 = new Task("task1","description", LocalDateTime.now(), 1);
        Task task2 = new Task("task2","description", LocalDateTime.now().plusMinutes(1), 1);
        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);
        taskManager.deleteAllTask();
        Collection<Task> tasks = taskManager.getAllTask();
        assertTrue(tasks.isEmpty(), "Задачи присутствуют");
    }
    @Test
    void shouldDeleteAllEpics() {
        Epic epic1 = new Epic("epic1","description", LocalDateTime.now(), 1);
        Epic epic2 = new Epic("epic2","description", LocalDateTime.now().plusMinutes(1), 1);
        taskManager.addNewEpicTask(epic1);
        taskManager.addNewEpicTask(epic2);
        taskManager.deleteAllEpic();
        List<Epic> epicList = taskManager.getAllEpic();
        assertTrue(epicList.isEmpty(), "Задачи присутствуют");
    }
    @Test
    void shouldDeleteAllSubTasks() {
        Epic epicTemp = new Epic("epic","description", LocalDateTime.now(), 1);
        SubTask subTask1 = new SubTask("subTask1","description",epicTemp.getId(), LocalDateTime.now().plusMinutes(1), 1);
        SubTask subTask2 = new SubTask("subTask2","description",epicTemp.getId(), LocalDateTime.now().plusMinutes(3), 1);
        taskManager.addNewEpicTask(epicTemp);
        taskManager.addNewSubTask(subTask1);
        taskManager.addNewSubTask(subTask2);
        taskManager.deleteAllSubTask();
        List<SubTask> subTasksList = taskManager.getAllSubTask();
        assertTrue(subTasksList.isEmpty(), "Задачи присутствуют");
    }


    @Test
    void shouldDeleteTask() {
        Task task1 = new Task("task1","description", LocalDateTime.now(), 1);
        Task task2 = new Task("task2","description", LocalDateTime.now().plusMinutes(1), 1);
        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);
        taskId = task1.getId();
        taskManager.deleteTask(taskId);
        Collection<Task> tasks = taskManager.getAllTask();
        assertEquals(1, tasks.size(), "Задача не была удалена.");
    }



    @Test
    void shouldUpdateTask() { // FAILED
        Task task = new Task("task", "description", LocalDateTime.now(), 1);
        taskManager.addNewTask(task);
        task = new Task("taskNewName", "newDescription", LocalDateTime.now().plusMinutes(1), 1);
        taskManager.updateTask(task);
        assertEquals("newDescription", task.getDescription(), "Задача не была обновлена");
    }

    @Test
    void shouldUpdateEpic() {
        Epic epic = new Epic("epic", "description", LocalDateTime.now(), 1);
        taskManager.addNewTask(epic);
        epic = new Epic("epicNewName", "newDescription", LocalDateTime.now().plusMinutes(1), 1);
        taskManager.updateTask(epic);
        assertEquals("epicNewName", epic.getName(), "Задача не была обновлена");
    }

    @Test
    void shouldUpdateSubTask() { //CHECK
        Epic epicTemp = new Epic("epic","description", LocalDateTime.now(), 1);
        SubTask subTask = new SubTask("subTask", "description5", epicTemp.getId(), LocalDateTime.now().plusMinutes(1), 1);
        taskManager.addNewEpicTask(epicTemp);
        taskManager.addNewSubTask(subTask);
        subTask = new SubTask("subTaskNewName", "newDescription", epicTemp.getId(), LocalDateTime.now().plusMinutes(2), 1);
        taskManager.updateSubTask(subTask);
        assertEquals("subTaskNewName", subTask.getName(), "Задача не обновлена.");
    }

    @Test
    void shouldReturnEpicIdFromSubTask() {
        Epic epicTemp = new Epic("epic","description", LocalDateTime.now(), 1);
        SubTask subTask = new SubTask("subTask", "description5", epicTemp.getId(), LocalDateTime.now().plusMinutes(1), 1);
        epicId = epicTemp.getId();
        taskManager.addNewEpicTask(epicTemp);
        taskManager.addNewSubTask(subTask);
        assertEquals(epicId, subTask.getEpicId(), "ID не совпадает.");
    }
}