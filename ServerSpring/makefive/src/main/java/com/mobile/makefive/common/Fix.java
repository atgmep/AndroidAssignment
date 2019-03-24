package com.mobile.makefive.common;

import com.mobile.makefive.model.Stat;

import java.nio.file.Path;
import java.nio.file.Paths;

public interface Fix {


    String URL = "http://localhost:8080/";
    Path IMG_DIR_PATH = Paths.get("src/main/resources/static/img/").toAbsolutePath().normalize();


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


    /* Account Status */
    Stat ACC_NEW = new Stat(1, "new");
    Stat ACC_BAN = new Stat(3, "banned");
    Stat[] ACC_STAT_LIST = {ACC_NEW, ACC_BAN};


    int WAIT_SHORT = 100;
    int WAIT_LONG = 3 * 60 * 1000;

}
