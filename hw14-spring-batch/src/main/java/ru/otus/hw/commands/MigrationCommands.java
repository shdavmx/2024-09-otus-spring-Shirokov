package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;


@RequiredArgsConstructor
@ShellComponent
public class MigrationCommands {
    private final Job genreJob;

    private final JobLauncher jobLauncher;

    @ShellMethod(value = "Run migration method", key = "sm-run")
    public void RunMigrations() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("startAt", System.currentTimeMillis())
                .toJobParameters();
        JobExecution jobExecution = jobLauncher.run(genreJob, jobParameters);
    }
}
