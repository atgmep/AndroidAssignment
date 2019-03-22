package day12.mobilestudy.make5;

import java.nio.file.Path;
import java.nio.file.Paths;

public interface Fix {

    // http://192.168.100.146:8080
    // http://10.82.137.209:8080
   // 192.168.100.51
    // 10.82.137.209
    String URL = "http://10.82.137.209:8080";

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


    String PAYPAL_CLIENT_ID = "Ae-WAkmVpZzdf5yYCDsw712GLWeT1RMqWTwirxkRRFnEEvMWKKxvpcn6WW7k2tv6Ck7RmRDEPLLUdJ0F";
    String PAYPAL_CLIENT_SECRET = "EAbfxREiU06k1rVeIbql1kC0zNc0_-3Yw6JY0hpMCMjT3VLd4PQj_eJuvop4xINX6OJProI3f6F3B2Nn";
    String PAYPAL_MODE = "sandbox";
}
