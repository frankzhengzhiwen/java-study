package service;

import dao.DBInitDAO;

public class DBService {

    public static void init() {
        DBInitDAO.init();
    }
}
