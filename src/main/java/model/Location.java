package model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer locationid;

    private String nome;

    private Double latitudine;

    private Double longitudine;

    //relazione dati meteo per ogni giorno della settimana
    @OneToMany(mappedBy = "location")
    private Set<WeatherDay> weatherDays=new HashSet<WeatherDay>();



//    public Location(Double latitudine, Double longitudine, String nome) {
//        this.latitudine = latitudine;
//        this.longitudine = longitudine;
//        this.nome = nome;
//    }

    public Location() {

    }

//    public Integer getLocationId() {
//        return locationid;
//    }
//
//    public void setLocationId(Integer locationid) {
//        this.locationid = locationid;
//    }

    public Double getLatitudine() {
        return latitudine;
    }

    public void setLatitudine(Double latitudine) {
        this.latitudine = latitudine;
    }

    public Double getLongitudine() {
        return longitudine;
    }

    public void setLongitudine(Double longitudine) {
        this.longitudine = longitudine;
    }

    public String getNome() {
       return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

//   // public Set<WeatherDay> getWeatherDays() {
//        return weatherDays;
//    }

//    public void setWeatherDays(Set<WeatherDay> weatherDays) {
//        this.weatherDays = weatherDays;
//    }

    @Override
    public String toString() {
        return "Location{" +
                "latitudine=" + latitudine +
                ", locationid=" + locationid +
                ", nome='" + nome + '\'' +
                ", longitudine=" + longitudine +
//                ", weatherDays=" + weatherDays +
                '}';
    }
}
