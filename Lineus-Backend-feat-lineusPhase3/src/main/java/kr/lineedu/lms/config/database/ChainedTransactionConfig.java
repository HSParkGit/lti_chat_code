package kr.lineedu.lms.config.database;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class ChainedTransactionConfig {

    public static final String CHAINED_TRANSACTION_MANAGER = "chainedTransactionManager";

    private final List<PlatformTransactionManager> transactionManagerList;


    @Bean(name = CHAINED_TRANSACTION_MANAGER)
    public ChainedTransactionManager chainedTransactionManager() {
        return new ChainedTransactionManager(transactionManagerList.toArray(new PlatformTransactionManager[0]));
    }
}