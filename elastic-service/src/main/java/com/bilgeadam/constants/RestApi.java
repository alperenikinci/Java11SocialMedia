package com.bilgeadam.constants;

public class RestApi {

    public static final String VERSION = "/api/v1";
    public static final String ELASTIC = VERSION+"/elastic";
    public static final String USER = ELASTIC + "/user";

    //UserController
    public static final String CREATE = "/create";
    public static final String UPDATE = "/update";
    public static final String DELETEBYID = "/delete_by_id";
    public static final String FINDBYID = "/find_by_id";
    public static final String FINDBYROLE = "/find_by_role";
    public static final String FINDALL = "/find_all";
    public static final String ACTIVATESTATUS = "/activate_status";
    public static final String ACTIVATESTATUS2 = "/activate_status2";

}
