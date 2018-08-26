package com.nowcoder.async;

/**
 * Created by tofuc on 2018/6/24.
 */
public enum EventType {

    LIKE(0), COMMIT(1), LOGIN(2), MAIL(3);

    private int value;
    EventType(int value){
        this.value=value;
    }
    public int getValue(){
        return value;
    }

}
