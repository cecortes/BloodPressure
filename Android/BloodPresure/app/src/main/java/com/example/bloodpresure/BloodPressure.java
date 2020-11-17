package com.example.bloodpresure;

public class BloodPressure {
    //Atributos
    public int blood_id;
    public int sisto;
    public int diasto;
    public String estado;
    public String blood_create;

    //Constructores
    public BloodPressure() {
    }
    public BloodPressure(int blood_id, int sisto, int diasto, String estado, String blood_create) {
        this.blood_id = blood_id;
        this.sisto = sisto;
        this.diasto = diasto;
        this.estado = estado;
        this.blood_create = blood_create;
    }

    //Get & Set

    public int getBlood_id() {
        return blood_id;
    }

    public void setBlood_id(int blood_id) {
        this.blood_id = blood_id;
    }

    public int getSisto() {
        return sisto;
    }

    public void setSisto(int sisto) {
        this.sisto = sisto;
    }

    public int getDiasto() {
        return diasto;
    }

    public void setDiasto(int diasto) {
        this.diasto = diasto;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getBlood_create() {
        return blood_create;
    }

    public void setBlood_create(String blood_create) {
        this.blood_create = blood_create;
    }

    @Override
    public String toString() {
        blood_create += " | Sistólica: " + sisto + " / Diastólica: " + diasto + " | ID: " + blood_id;
        return blood_create;
    }
}
