package com.ass2.i190417_i192048;

public class ChatModal {
    String person_name,person_number,person_image;

    public String getPerson_name() {
        return person_name;
    }

    public void setPerson_name(String person_name) {
        this.person_name = person_name;
    }

    public String getPerson_number() {
        return person_number;
    }

    public void setPerson_number(String person_number) {
        this.person_number = person_number;
    }

    public String getPerson_image() {
        return person_image;
    }

    public void setPerson_image(String person_image) {
        this.person_image = person_image;
    }

    public ChatModal(String person_name, String person_number, String person_image) {
        this.person_name = person_name;
        this.person_number = person_number;
        this.person_image = person_image;
    }
}
