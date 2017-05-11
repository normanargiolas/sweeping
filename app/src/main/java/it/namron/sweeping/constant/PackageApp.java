package it.namron.sweeping.constant;

import java.util.AbstractMap.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by norman on 08/05/17.
 */

public class PackageApp {

    public static final String WHATSAPP = "com.whatsapp";
    public static final String TELEGRAM = "com.telegram";


//Require API level 24
//    public static final Map<String, String> ListApp = Stream.of(
//            new SimpleEntry<>("WHATSAPP", "com.whatsapp"),
//            new SimpleEntry<>("tmp", "tmp"))
//            .collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue));

//    public static final Map<String, String> listApp;
//    static {
//        Map<String, String> aApp = new HashMap<String, String>();
//        aApp.put("WHATSAPP", "com.whatsapp");
//        aApp.put("tmp", "tmp");
//        listApp = Collections.unmodifiableMap(aApp);
//    }
}
