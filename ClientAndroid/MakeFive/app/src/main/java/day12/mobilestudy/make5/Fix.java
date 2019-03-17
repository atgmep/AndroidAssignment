package day12.mobilestudy.make5;

import java.nio.file.Path;
import java.nio.file.Paths;

public interface Fix {

    // http://192.168.100.146:8080
    // http://10.82.137.209:8080
    String URL = "http://192.168.100.146:8080";

    /* API mapping */
    String MAP_ANY = "/any";
    String MAP_LOG = "/log";
    String MAP_ADM = "/adm";
    String MAP_MEM = "/mem";
    String MAP_API = "/api";

    /* Role */
    String ROL_MEM = "MEMBER";
    String ROL_ADM = "ADMIN";

    /*  */
    String YOU_WIN = "You won";
    String YOU_LOS = "You lost";
    String OPP_LOS = "Opponent lost";
    String OPP_WIN = "Opponent won";
    String OPP_MOV = "Opponent move";
    String OPP_AFK = "Opponent afk";

}
