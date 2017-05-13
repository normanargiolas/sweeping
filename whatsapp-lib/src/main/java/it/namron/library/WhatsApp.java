package it.namron.library;


/**
 * Created by norman on 13/05/17.
 */

public class WhatsApp {

    private static String id = "whatsapp";

    public static enum folder {
        PACKAGE("com.whatsapp"),
        WHATSAPP_DIRECTORY("WhatsApp"),
        WHATSAPP_MEDIA("Media");

        private final String text;

        private folder(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }

    public WhatsApp(){

    }

    public static String getId() {
        return id;
    }

}
