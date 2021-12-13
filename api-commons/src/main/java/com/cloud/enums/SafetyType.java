package com.cloud.enums;

/**
 *安全表ID类型
 *【0.高危安全作业证，1.吊装安全作业,2.动火安全作业,3.动土安全作业,4.断路安全作业,5.高处安全作业,6.临时用电安全作业,7.盲板抽堵安全作业,8.受限空间安全作业】
 */
public enum SafetyType{

    HIGHSAFETY(0,"workpermitmanagement","高危安全作业证"),
    LIFTINGSAFETY(1,"liftingsafe","吊装安全作业"),
    FIRESAFETY(2,"firesafety","动火安全作业"),
    GROUNDBREAKINGSAFETY(3,"groundbreakingSafe","动土安全作业"),
    OPENCIRCUITSAFETY(4,"breaksafe","断路安全作业"),
    SAFETYATHEIGHT(5,"heightsafe","高处安全作业"),
    TEMPORARYELECTRICITYSAFETY(6,"temporaryelectricity","临时用电安全作业"),
    BLINDPLATEPLUGGING(7,"blindplateplugging","盲板抽堵安全作业"),
    CONFINEDSPACE(8,"confinedspace","受限空间安全作业");



    private Integer type;
    private String processType;
    private String name;

    SafetyType(Integer type,String processType,String name) {
        this.type = type;
        this.processType = processType;
        this.name = name;
    }

    public String getProcessType() {
        return processType;
    }

    public void setProcessType(String processType) {
        this.processType = processType;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
