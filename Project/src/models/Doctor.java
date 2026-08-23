
package models;

public class Doctor {

    private int id;

    private String name;

    private int experience;
    private String gender;

    private String specialization;

    private String contact;

    public Doctor(int id,
                  String name,
                  int experience,
                  String gender,
                  String specialization,
                  String contact) {

        this.id = id;

        this.name = name;
        this.experience = experience;
        this.gender = gender;               
        this.specialization = specialization;

        this.contact = contact;
    }

    public int getId() {

        return id;
    }

    public String getName() {

        return name;
    }

    public String getSpecialization() {

        return specialization;
    }

    public int getExperience() {

        return experience;
    }

    public String getContact() {

        return contact;
    }

    public void setName(String name) {

        this.name = name;
    }

    public void setSpecialization(String specialization) {

        this.specialization = specialization;
    }

    public void setExperience(int experience) {

        this.experience = experience;
    }

    public void setContact(String contact) {

        this.contact = contact;
    }

    public String getGender() {

        return gender;
    }
    public void setGender(String gender) {

        this.gender = gender;
    }
}

