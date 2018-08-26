package com.nowcoder.async;

import java.util.List;

/**
 * Created by tofuc on 2018/6/24.
 */
public interface EventHandler {
    void doHandle(EventModel eventModel);
    List<EventType> getSupportEventType();
}
