package puzzles.hoppers.model;

/**
 * Sends a message to be displayed to the user, from the model to the view
 *
 * @author Hritik "Ricky" Gupta | rg4825@rit.edu
 */
public class HoppersClientData {

    /** message to be passed when an action is formed */
    private final String message;

    /**
     * Creates a HoppersClientData object with a message
     */
    public HoppersClientData(String message) {
        this.message = message;
    }

    /**
     * @return message to be shown to user
     */
    public String getMessage() {
        return this.message;
    }
}
