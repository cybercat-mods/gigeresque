package com.bvanseg.gigeresque.common.util.nest;

public class NestBlockDataJava {
    private int coverage;
    private boolean isCorner;
    private boolean isFloor;
    private boolean isCeiling;
    private boolean isWall;
    private boolean upCoverage;
    private boolean downCoverage;
    private boolean northCoverage;
    private boolean southCoverage;
    private boolean eastCoverage;
    private boolean westCoverage;

    public NestBlockDataJava(int coverage, boolean isCorner, boolean isFloor, boolean isCeiling, boolean isWall, boolean upCoverage, boolean downCoverage, boolean northCoverage, boolean southCoverage, boolean eastCoverage, boolean westCoverage) {
        this.coverage = coverage;
        this.isCorner = isCorner;
        this.isFloor = isFloor;
        this.isCeiling = isCeiling;
        this.isWall = isWall;
        this.upCoverage = upCoverage;
        this.downCoverage = downCoverage;
        this.northCoverage = northCoverage;
        this.southCoverage = southCoverage;
        this.eastCoverage = eastCoverage;
        this.westCoverage = westCoverage;
    }

    public int getCoverage() {
        return coverage;
    }

    public boolean isCorner() {
        return isCorner;
    }

    public boolean isFloor() {
        return isFloor;
    }

    public boolean isCeiling() {
        return isCeiling;
    }

    public boolean isWall() {
        return isWall;
    }

    public boolean hasUpCoverage() {
        return upCoverage;
    }

    public boolean hasDownCoverage() {
        return downCoverage;
    }

    public boolean hasNorthCoverage() {
        return northCoverage;
    }

    public boolean hasSouthCoverage() {
        return southCoverage;
    }

    public boolean hasEastCoverage() {
        return eastCoverage;
    }

    public boolean hasWestCoverage() {
        return westCoverage;
    }
}
