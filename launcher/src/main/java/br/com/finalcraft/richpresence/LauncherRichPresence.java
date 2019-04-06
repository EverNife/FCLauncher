package br.com.finalcraft.richpresence;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;

public class LauncherRichPresence {

    public static DiscordRPC lib;
    public static DiscordRichPresence presence;

    public static void initialize() {
        lib = DiscordRPC.INSTANCE;
        String applicationId = "538435978486874112";
        String steamId = "";
        DiscordEventHandlers handlers = new DiscordEventHandlers();
        //handlers.ready = () -> System.out.println("Ready!");
        lib.Discord_Initialize(applicationId, handlers, true, steamId);
        presence = new DiscordRichPresence();
        presence.startTimestamp = System.currentTimeMillis() / 1000; // epoch second
        presence.details = "Selecionando Modpack";
        presence.state = "...";
        lib.Discord_UpdatePresence(presence);
        // in a worker thread
        Thread thread = new Thread() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    lib.Discord_RunCallbacks();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        };
        thread.setName("RPC-Callback-Handler");
        thread.start();
    }


    public static void updatePresence(){
        lib.Discord_UpdatePresence(presence);
    }

    public static void onInstanceDownloadStart(String instanceName){
        presence.details    = "Baixando o Modpack:";
        presence.state      = "  ❈ " + instanceName;
        updatePresence();
    }

    public static void onInstanceDownloadFinish(){
        presence.details = "Selecionando Modpack";
        presence.state = "...";
        updatePresence();
    }

    public static void onInstanceStart(String instanceName){
        //if (instanceName.startsWith("FinalCraft ")) instanceName = instanceName.split(" ")[1];
        presence.details    = "Jogando no Modpack:";
        presence.state      = "  ✎ " + instanceName;
        updatePresence();
    }

    public static void onMinecraftClose(){
        presence.details = "Selecionando Modpack";
        presence.state = "...";
        updatePresence();
    }

}
