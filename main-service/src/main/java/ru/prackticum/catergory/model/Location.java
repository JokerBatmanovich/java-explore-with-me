package ru.prackticum.catergory.model;

import lombok.Data;

import javax.persistence.*;

@Embeddable
@Data
public class Location {
    @Column
    private Double lat;
    @Column
    private Double lon;
}
