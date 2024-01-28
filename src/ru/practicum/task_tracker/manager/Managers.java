package manager;

import adapter.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import server.HttpTaskManager;
import server.KVServer;

import java.io.IOException;
import java.time.LocalDateTime;

public class Managers {

    public static TaskManager getDefault(){
//        return new FileBackedTaskManager();
        return new HttpTaskManager();
    }

    public static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }

    public static KVServer getDefaultKVServer() throws IOException{
        final KVServer kvServer = new KVServer();
        kvServer.start();
        return kvServer;
    }

    public static Gson getGson(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.serializeNulls(); //для NULL
        return gsonBuilder.create();
    }
}
