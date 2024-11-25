package cn.tm.aibro.config;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.PgVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class ApplicationConfig {

    /**
     * 向量数据库进行检索操作
     * @param embeddingModel
     * @param jdbcTemplate
     * @return
     */
    @Bean
    public VectorStore pgVectorStore(EmbeddingModel embeddingModel, JdbcTemplate jdbcTemplate){
        return new PgVectorStore(jdbcTemplate,embeddingModel);
    }

    /**
     * 文本分割器
     * @return
     */
    @Bean
    public TokenTextSplitter tokenTextSplitter() {
        return new TokenTextSplitter();
    }
}
