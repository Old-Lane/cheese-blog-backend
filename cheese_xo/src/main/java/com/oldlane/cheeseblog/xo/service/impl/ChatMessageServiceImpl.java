package com.oldlane.cheeseblog.xo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oldlane.cheeseblog.commons.entity.ChatMessage;
import com.oldlane.cheeseblog.xo.mapper.ChatMessageMapper;
import com.oldlane.cheeseblog.xo.service.ChatMessageService;
import org.springframework.stereotype.Service;

/**
* @author lenovo
* @description 针对表【chat_message】的数据库操作Service实现
* @createDate 2023-03-06 22:26:03
*/
@Service("chatMessage")
public class ChatMessageServiceImpl extends ServiceImpl<ChatMessageMapper, ChatMessage>
    implements ChatMessageService {

    @Override
    public void saveMessage(ChatMessage chatMsg) {
        this.save(chatMsg);
    }
}




