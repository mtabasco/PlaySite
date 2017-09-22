package com.playsiteapp.domain.models;

/**
 * Created by Coco on 07/07/2017.
 */

public class Playground extends PlaySitePOI{

    private Boolean schedule;
    private String scheduleStart;
    private String scheduleTimeEnd;
    private Boolean babySwing;
    private Boolean swing;
    private Boolean slide;
    private Boolean seesaw;
    private Boolean sandbox;
    private Boolean bathroom;
    private Boolean accesibility;

    public Playground(String name, LatLng locationLatLng, Boolean schedule, String scheduleStart, String scheduleTimeEnd, Boolean babySwing, Boolean swing, Boolean slide, Boolean seesaw, Boolean sandbox, Boolean bathroom, Boolean accesibility) {
        super(name, locationLatLng);

        this.schedule = schedule;
        this.scheduleStart = scheduleStart;
        this.scheduleTimeEnd = scheduleTimeEnd;
        this.babySwing = babySwing;
        this.swing = swing;
        this.slide = slide;
        this.seesaw = seesaw;
        this.sandbox = sandbox;
        this.bathroom = bathroom;
        this.accesibility = accesibility;
    }


    public Boolean getSchedule() {
        return schedule;
    }

    public void setSchedule(Boolean schedule) {
        this.schedule = schedule;
    }

    public String getScheduleStart() {
        return scheduleStart;
    }

    public void setScheduleStart(String scheduleStart) {
        this.scheduleStart = scheduleStart;
    }

    public String getScheduleTimeEnd() {
        return scheduleTimeEnd;
    }

    public void setScheduleTimeEnd(String scheduleTimeEnd) {
        this.scheduleTimeEnd = scheduleTimeEnd;
    }

    public Boolean getBabySwing() {
        return babySwing;
    }

    public void setBabySwing(Boolean babySwing) {
        this.babySwing = babySwing;
    }

    public Boolean getSwing() {
        return swing;
    }

    public void setSwing(Boolean swing) {
        this.swing = swing;
    }

    public Boolean getSlide() {
        return slide;
    }

    public void setSlide(Boolean slide) {
        this.slide = slide;
    }

    public Boolean getSeesaw() {
        return seesaw;
    }

    public void setSeesaw(Boolean seesaw) {
        this.seesaw = seesaw;
    }

    public Boolean getSandbox() {
        return sandbox;
    }

    public void setSandbox(Boolean sandbox) {
        this.sandbox = sandbox;
    }

    public Boolean getBathroom() {
        return bathroom;
    }

    public void setBathroom(Boolean bathroom) {
        this.bathroom = bathroom;
    }

    public Boolean getAccesibility() {
        return accesibility;
    }

    public void setAccesibility(Boolean accesibility) {
        this.accesibility = accesibility;
    }


    // Empty constructor for firebase
    public Playground() {
        super();
    }

    public Playground(String id, LatLng locationLatLng) {
        super(id, locationLatLng);
    }
}
