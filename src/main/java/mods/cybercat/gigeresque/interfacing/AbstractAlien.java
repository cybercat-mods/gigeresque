package mods.cybercat.gigeresque.interfacing;

import net.minecraft.core.BlockPos;

public interface AbstractAlien {

    int getAcidDiameter();

    boolean isFleeing();

    void setFleeingStatus(boolean fleeing);

    boolean isUpsideDown();

    boolean isCrawling();

    void setIsCrawling(boolean shouldCrawl);

    boolean isTunnelCrawling();

    void setIsTunnelCrawling(boolean shouldTunnelCrawl);

    void setWakingUpStatus(boolean passout);

    boolean isWakingUp();

    boolean isExecuting();

    void setIsExecuting(boolean isExecuting);

    boolean isBiting();

    void setIsBiting(boolean isBiting);

    boolean isSearching();

    void setIsSearching(boolean isHissing);

    boolean isHissing();

    void setIsHissing(boolean isHissing);

    float getGrowth();

    void setGrowth(float growth);

    boolean isPassedOut();

    void setPassedOutStatus(boolean passout);
}
