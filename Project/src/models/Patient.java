package models;

public class Patient {

    private int id;

    private String name;

    private int age;

    private String gender;

    private String disease;

    private String contact;

    // CONSTRUCTOR
    public Patient(int id, String name, int age,
                   String gender, String disease,
                   String contact) {

        this.id = id;

        this.name = name;

        this.age = age;

        this.gender = gender;

        this.disease = disease;

        this.contact = contact;
    }

    // GETTERS

    public int getId() {

        return id;
    }

    public String getName() {

        return name;
    }

    public int getAge() {

        return age;
    }

    public String getGender() {

        return gender;
    }

    public String getDisease() {

        return disease;
    }

    public String getContact() {

        return contact;
    }

    // SETTERS

    public void setId(int id) {

        this.id = id;
    }

    public void setName(String name) {

        this.name = name;
    }

    public void setAge(int age) {

        this.age = age;
    }

    public void setGender(String gender) {

        this.gender = gender;
    }

    public void setDisease(String disease) {

        this.disease = disease;
    }

    public void setContact(String contact) {

        this.contact = contact;
    }
}