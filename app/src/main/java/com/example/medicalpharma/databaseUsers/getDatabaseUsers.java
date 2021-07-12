package com.example.medicalpharma.databaseUsers;

public class getDatabaseUsers {

    //Ne mundemi me marr numrin e telefonit si KEY value nga JSON
    //por jo emrin ddhe passwordin prandaj na duhet kjo klase

    private String Name;
    private String Password;

    public getDatabaseUsers() {
    }

    public getDatabaseUsers(String name, String password){
        Name = name;
        Password = password;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
