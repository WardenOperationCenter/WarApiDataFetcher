package com.woc;

import com.woc.entity.ShardConfEntity;
import io.quarkus.logging.Log;
import io.quarkus.picocli.runtime.annotations.TopCommand;
import io.quarkus.scheduler.Scheduler;
import jakarta.inject.Inject;
import lombok.SneakyThrows;
import picocli.CommandLine;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;


@TopCommand
@CommandLine.Command(mixinStandardHelpOptions = true, subcommands = {StaticFetchCommand.class, DynamicFetchCommand.class, DaemonFetchCommand.class})
class EntryCommand {
}

@CommandLine.Command(name = "static", description = "Fetch all static data from the api")
class StaticFetchCommand implements Runnable {

    @Inject
    WarApiService warApiService;

    @Override
    public void run() {
        for (ShardConfEntity shardConf : ShardConfEntity.listAllActive()) {
            Log.info(String.format("Processing server %s", shardConf.getName()));
            warApiService.fetchAndStoreStaticData(shardConf);
        }
    }
}

@CommandLine.Command(name = "dynamic", description = "fetch dynamic api data")
class DynamicFetchCommand implements Runnable {
    @Inject
    WarApiService warApiService;

    @Override
    public void run() {
        warApiService.fetchAllServersDynamicData(false);
    }
}

@CommandLine.Command(name = "daemon", description = "continuously fetch dynamic api data")
class DaemonFetchCommand implements Runnable {
    @Inject
    Scheduler scheduler;

    @Inject
    WarApiService warApiService;

    @CommandLine.Option(names = {"-c", "--cron"}, description = "Cron used to fetch data ", defaultValue = "0 0/10 * * * ?")
    String interval;
    private Thread runningThread;

    private static final String JOB_NAME = "fetchAllData";

    private void scheduleJob() {
        scheduler.newJob(JOB_NAME)
                .setCron(interval)
                .setTask(executionContext -> {
                    runningThread = Thread.currentThread();
                    Log.info("Running job to DL data");
                    Instant start = Instant.now();
                    warApiService.fetchAllServersDynamicData(true);
                    Instant end = Instant.now();
                    Duration timeElapsed = Duration.between(start, end);
                    Log.info(String.format("Job ended in %ss %sms",  timeElapsed.toSeconds(), timeElapsed.toMillisPart()));
                    runningThread = null;
                }).schedule();
        Log.info(String.format("Setup download of data with following cron '%s'", interval));
    }

    @SneakyThrows(InterruptedException.class)
    @Override
    public void run() {
        Log.info("Starting WarApiFetcher in daemon mode");
        this.scheduleJob();
        try {
            Log.info("Press enter key to exit");
            System.in.read();
            scheduler.unscheduleJob(JOB_NAME);
            if (runningThread != null) {
                Log.info("Waiting running job to exit");
                runningThread.join();
            }
            Log.info("Closing application");
        } catch (IOException e) {
            Log.info("Closing application (Interrupted)");
        }
    }
}