package mods.cybercat.gigeresque.common.util.nest;

public record NestBlockData(int coverage, boolean isCorner, boolean isFloor, boolean isCeiling, boolean isWall,
                            boolean upCoverage, boolean downCoverage, boolean northCoverage, boolean southCoverage,
                            boolean eastCoverage, boolean westCoverage) {

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
