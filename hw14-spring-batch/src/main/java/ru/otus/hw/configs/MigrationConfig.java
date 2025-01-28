package ru.otus.hw.configs;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.MongoPagingItemReader;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.models.Genre;

import java.util.HashMap;
import java.util.Objects;

@Configuration
public class MigrationConfig {
    @Autowired
    private MigrationProperties migrationProperties;

    @StepScope
    @Bean
    public MongoPagingItemReader<Genre> genreReader(MongoTemplate mongoTemplate) {
        MongoPagingItemReader<Genre> reader = new MongoPagingItemReader<>();
        reader.setTemplate(mongoTemplate);
        reader.setTargetType(Genre.class);
        reader.setSort(new HashMap<>() {
            { put("_id", Sort.Direction.ASC); }
        });
        reader.setQuery("{}");
        return reader;
    }

    @StepScope
    @Bean
    public ItemProcessor<Genre, Genre> genreProcessor() {
        return genre -> { return genre; };
    }

    @StepScope
    @Bean
    public JdbcBatchItemWriter<Genre> genreWriter(JdbcTemplate jdbcTemplate) {
        return new JdbcBatchItemWriterBuilder<Genre>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO genres (name) " +
                     "VALUES (?)")
                .itemPreparedStatementSetter((genre, ps) -> {
                    ps.setString(1, genre.getName());
                })
                .dataSource(Objects.requireNonNull(jdbcTemplate.getDataSource()))
                .build();
    }

    @Bean
    public Step genreStep(MongoPagingItemReader<Genre> reader,
                          ItemProcessor<Genre, Genre> processor,
                          JdbcBatchItemWriter<Genre> writer,
                          PlatformTransactionManager platformTransactionManager,
                          JobRepository jobRepository) throws Exception {
        return new StepBuilder("genreStep", jobRepository)
                .<Genre, Genre>chunk(migrationProperties.getMigrationChunkSize(), platformTransactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .startLimit(1)
                .build();
    }

    @Bean
    public Job genreJob(Step genreStep, JobRepository jobRepository) {
        return new JobBuilder("genreJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(genreStep)
                .end()
                .build();
    }
}
