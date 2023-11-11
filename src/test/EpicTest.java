package test;

import static org.junit.jupiter.api.Assertions.*;

import manager.TaskManager;
import manager.Managers;
import org.junit.jupiter.api.Assertions;
import task.Epic;
import task.Status;
import task.SubTask;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class EpicTest {

    private static TaskManager taskManager;
    Epic epic;


    @BeforeAll
    public static void BeforeAll() {
        taskManager = Managers.getDefault();

    }
    @Test
    public void shouldReturnNewWhenEpicIsEmpty(){
        Epic epic = new Epic("epic", "description", LocalDateTime.of(2023, 11, 1, 1, 1), 1);
        taskManager.addNewEpicTask(epic);
        Assertions.assertEquals(Status.NEW, epic.getStatus());
        taskManager.deleteAllEpic();
    }
    @Test
    public void shouldReturnNewWhenAllSubtasksNew(){

        Epic epic = new Epic("epic", "description", LocalDateTime.of(2023, 11, 1, 1, 1), 1);
        taskManager.addNewEpicTask(epic);
        assertEquals(epic.getStatus(), Status.NEW);
        SubTask subTask1 = new SubTask("subtask1", "description1", 1, LocalDateTime.of(2023, 11, 1, 1, 1), 1);
        taskManager.addNewSubTask(subTask1);
        SubTask subTask2 = new SubTask("subtask2", "description2", 1, LocalDateTime.of(2023, 11, 1, 1, 1), 1);
        taskManager.addNewSubTask(subTask2);
        assertEquals(Status.NEW, epic.getStatus(), "Статус не совпадает");
        taskManager.deleteAllSubTask();
        taskManager.deleteAllEpic();

    }

    @Test
    public void shouldReturnDoneWhenAllSubtasksDone(){

        Epic epic = new Epic("epic", "description", LocalDateTime.of(2023, 11, 11, 1, 1), 1);
        epic.setId(1);
        taskManager.addNewEpicTask(epic);
        SubTask subTask1 = new SubTask("subtask1", "description1", 1, LocalDateTime.of(2023, 11, 1, 1, 1), 1);
        subTask1.setId(2);
        subTask1.setStatus(Status.DONE);
        taskManager.addNewSubTask(subTask1);
        SubTask subTask2 = new SubTask("subtask2", "description2", 1, LocalDateTime.of(2023, 11, 1, 1, 1), 1);
        subTask2.setId(3);
        subTask2.setStatus(Status.DONE);
        taskManager.addNewSubTask(subTask2);
        assertEquals(Status.DONE, epic.getStatus(), "Статус не совпадает");
        taskManager.deleteAllSubTask();
        taskManager.deleteAllEpic();
    }

    @Test
    public void shouldReturnInProgressWhenSubtasksDoneAndNew(){ // FAIL

        Epic epic = new Epic("epic", "description", LocalDateTime.of(2023, 11, 1, 1, 1), 1);
        taskManager.addNewEpicTask(epic);
        SubTask subTask1 = new SubTask("subtask1", "description1", 1, LocalDateTime.of(2023, 11, 1, 1, 1), 1);
        subTask1.setId(2);
        taskManager.addNewSubTask(subTask1);
        subTask1.setStatus(Status.DONE);
        taskManager.updateSubTask(subTask1);
        SubTask subTask2 = new SubTask("subtask2", "description2", 1, LocalDateTime.of(2023, 11, 1, 1, 1), 1);
        subTask2.setId(3);
        taskManager.addNewSubTask(subTask2);
        taskManager.updateEpic(epic);
        assertEquals(Status.IN_PROGRESS, epic.getStatus(), "Статус не совпадает");
        taskManager.deleteAllSubTask();
        taskManager.deleteAllEpic();
    }

    @Test
    public void shouldReturnInProgressWhenAllSubtasksInProgress(){

        Epic epic = new Epic("epic", "description", LocalDateTime.of(2023, 11, 1, 1, 1), 1);
        taskManager.addNewEpicTask(epic);
        assertEquals(epic.getStatus(), Status.NEW);
        SubTask subTask1 = new SubTask("subtask1", "description1", 1, LocalDateTime.of(2023, 11, 1, 1, 1), 1);
        subTask1.setId(2);
        subTask1.setStatus(Status.IN_PROGRESS);
        taskManager.addNewSubTask(subTask1);
        SubTask subTask2 = new SubTask("subtask2", "description2", 1, LocalDateTime.of(2023, 11, 1, 1, 1), 1);
        subTask2.setId(3);
        subTask2.setStatus(Status.IN_PROGRESS);
        taskManager.addNewSubTask(subTask2);
        assertEquals(Status.IN_PROGRESS, epic.getStatus(), "Статус не совпадает");
        taskManager.deleteAllSubTask();
        taskManager.deleteAllEpic();
    }
}