package be.itlive.common.enums;

/**
 * @author vbiertho
 *
 */
public enum ResponseStatusEnum {

    /** Succes.*/
    SUCCESSFULL,

    /** Failed.*/
    FAILED,

    /** Unauthorized.*/
    UNAUTHORIZED;

    /**
     * @return true is failed
     */
    public boolean isFailed() {
        return this.equals(FAILED);
    }

    /**
     * @return true is successfull
     */
    public boolean isSuccessFull() {
        return this.equals(SUCCESSFULL);
    }

}
