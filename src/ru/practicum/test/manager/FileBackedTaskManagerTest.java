package manager;

import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager>{
    private static final File TEST_FILE = new File("test_file.csv");

    @BeforeEach
    public void setUp(){
        taskManager = new FileBackedTaskManager(TEST_FILE);
    }

//    @AfterAll
//    public static void tearDown() throws IOException {
//        TEST_FILE.delete();
//    }

    @Test
    public void shouldLoadFromFile(){
    FileBackedTaskManager restoredFromFile = FileBackedTaskManager.loadFromFile(TEST_FILE);
    Assertions.assertEquals("Description/Task1",restoredFromFile.getTaskToId(1).getDescription());
    Assertions.assertEquals("Description 1",restoredFromFile.getEpicToId(3).getDescription());
    Assertions.assertEquals("Description 11/Epic 1",restoredFromFile.getSubtaskToId(4).getDescription());
    }
}