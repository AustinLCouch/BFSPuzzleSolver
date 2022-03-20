package puzzles.jam.model;

/**
 * This class connects the model and view and allows the model to send
 * messages to the view and display to the user
 *
 * @author Austin Couch
 */
public class JamClientData{
        /** message to be passed when an action is formed */
        private final String message;

        /** Creates a JamClientData object with a passed message */
        public JamClientData(String message){
                this.message = message;
        }

        /**
         * @return message to be displayed for the user
         */
        public String getMessage(){
                return this.message;
        }

}

