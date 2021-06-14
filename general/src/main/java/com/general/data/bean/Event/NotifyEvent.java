package com.general.data.bean.Event;

public class NotifyEvent {
    private String event;
    private String value;
    private String second_value;
    private String third_value;
    private String forth_value;
    private Object custom_object;

    public Object getCustom_object() {
        return custom_object;
    }

    public void setCustom_object(Object custom_object) {
        this.custom_object = custom_object;
    }

    public String getThird_value() {
        return third_value;
    }

    public void setThird_value(String third_value) {
        this.third_value = third_value;
    }

    public String getForth_value() {
        return forth_value;
    }

    public void setForth_value(String forth_value) {
        this.forth_value = forth_value;
    }

    public NotifyEvent( ) {

    }
    public NotifyEvent(String event, Object custom_object, String value) {
        this.event = event;
        this.custom_object = custom_object;
        this.value = value;
    }

    public NotifyEvent(String event, String value, Object custom_object, String second_value) {
        this.event = event;
        this.value = value;
        this.custom_object = custom_object;
        this.second_value = second_value;
    }

    public NotifyEvent(String event, String value, Object custom_object) {
        this.event = event;
        this.value = value;
        this.custom_object = custom_object;
    }

    public NotifyEvent(String event) {
        this.event = event;
    }

    public NotifyEvent(String event, String value) {
        this.event = event;
        this.value = value;
    }

    public NotifyEvent(String event, Object custom_object) {
        this.event = event;
        this.custom_object = custom_object;
    }


    public NotifyEvent(String event, String value, String second_value) {
        this.event = event;
        this.value = value;
        this.second_value = second_value;
     }

    public NotifyEvent(String event, String value, String second_value, Object custom_object) {
        this.event = event;
        this.value = value;
        this.second_value = second_value;
        this.custom_object = custom_object;
    }

    public NotifyEvent(String event, String value, String second_value, String third_value) {
        this.event = event;
        this.value = value;
        this.second_value = second_value;
        this.third_value = third_value;
    }

    public String getValue() {
        return value;

    }

    public void setValue(String value) {
        this.value = value;
    }


    public String getSecond_value() {
        return second_value;
    }

    public void setSecond_value(String second_value) {
        this.second_value = second_value;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }



}
