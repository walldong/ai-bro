package cn.tm.aibro.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    // 系统提示词
    private final static String SYSTEM_PROMPT = """
            你需要使用文档内容对用户提出的问题进行回复，同时你需要表现得天生就知道这些内容，
            不能在回复中体现出你是根据给出的文档内容进行回复的，这点非常重要。
            
            当用户提出的问题无法根据文档内容进行回复或者你也不清楚时，回复不知道即可。
                    
            文档内容如下:
            {documents}
            
            """;

    private final OllamaChatModel chatModel;
    private final VectorStore vectorStore;

    // 简单的对话，不对向量数据库进行检索
    public String simpleChat(String userMessage) {
        return chatModel.call(userMessage);
    }

    // 通过向量数据库进行检索
    public String chatByVectorStore(String message) {
        // 根据问题文本进行相似性搜索
        List<Document> listOfSimilarDocuments = vectorStore.similaritySearch(message);
        log.info("相似度搜索结果: {}", listOfSimilarDocuments);
        // 将Document列表中每个元素的content内容进行拼接获得documents
        String documents = listOfSimilarDocuments.stream().map(Document::getContent).collect(Collectors.joining());
        // 使用Spring AI 提供的模板方式构建SystemMessage对象
        Message systemMessage = new SystemPromptTemplate(SYSTEM_PROMPT).createMessage(Map.of("documents", documents));
        // 构建UserMessage对象
        UserMessage userMessage = new UserMessage(message);
        // 将Message列表一并发送给ChatGPT
        ChatResponse rsp = chatModel.call(new Prompt(List.of(systemMessage, userMessage)));
        return rsp.getResult().getOutput().getContent();
    }
}
