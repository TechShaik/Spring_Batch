package com.Files.SpringBatch.config;

 
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step; 
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import com.Files.SpringBatch.Entity.Customer;
import com.Files.SpringBatch.repo.CustRepo;

 import lombok.RequiredArgsConstructor;
 
@Configuration
 @RequiredArgsConstructor
public class SpringBatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    
    @Autowired
    private CustRepo custRepo;

    @Bean
    public FlatFileItemReader<Customer> reader() {
        FlatFileItemReader<Customer> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource("src/main/resources/customers.csv"));
        itemReader.setName("csvReader");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(lineMapper());
        return itemReader;
    }

    private LineMapper<Customer> lineMapper() {
        DefaultLineMapper<Customer> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("id", "firstName", "lastName", "email", "gender", "contactNo", "country", "dob");

        BeanWrapperFieldSetMapper<Customer> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Customer.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        return lineMapper;
    }
    
    @Bean
    public CustomerProcessor processor() {
      return new CustomerProcessor();
    }
    
    
    public RepositoryItemWriter<Customer>writer(){
      RepositoryItemWriter<Customer>writer=new RepositoryItemWriter<>();
    writer.setRepository(custRepo);
    writer.setMethodName("save");
   return writer;
    }
    
    @Bean
    public Step step1() {
          return new StepBuilder("csvImport",jobRepository).
        		  <Customer,Customer>chunk(10,platformTransactionManager)
        		  .reader(reader()).processor(processor()).writer(writer()).build();
        		  
 }
    @Bean
    public Job runJob() {
    	return new JobBuilder("importCustomers",jobRepository)
    			.start(step1()).build();
		
	}
}
