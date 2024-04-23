package cn.itsource.listener;

import org.springframework.context.ApplicationEvent;

public class MySelfMessageEvent extends ApplicationEvent {
    public MySelfMessageEvent(Object source) {
        super(source);
    }
}
