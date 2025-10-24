package mods.cybercat.gigeresque.interfacing;

public interface AbstractAlien {

    int getBloodDiameter();

    boolean isFleeing();

    void setFleeingStatus(boolean fleeing);

    void setWakingUpStatus(boolean passout);

    boolean isWakingUp();

    boolean isExecuting();

    void setIsExecuting(boolean isExecuting);

    boolean isBiting();

    void setIsBiting(boolean isBiting);

    boolean isHissing();

    void setIsHissing(boolean isHissing);
}
