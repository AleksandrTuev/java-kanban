package server;

import manager.Managers;

import java.io.IOException;

public class KVServerStarted {
    public static void main(String[] args) throws IOException {
        KVServer kvServer = Managers.getDefaultKVServer();
    }
}
