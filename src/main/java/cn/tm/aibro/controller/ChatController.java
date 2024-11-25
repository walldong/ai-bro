package cn.tm.aibro.controller;

import cn.tm.aibro.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/simple")
    public String simpleChat(@RequestParam String message) {
        return chatService.simpleChat(message);
    }

    @GetMapping("/rag")
    public String chat(@RequestParam String message) {
        return chatService.chatByVectorStore(message);
    }
}
