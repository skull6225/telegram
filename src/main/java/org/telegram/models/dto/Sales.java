package org.telegram.models.dto;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Table(name = "sales")
public class Sales {

    public Sales(String name, Long count, Long price, Long basePrice, Place place, Date publicationDate) {
        this.name = name;
        this.count = count.intValue();
        this.price = price.intValue();
        this.basePrice = basePrice.intValue();
        this.place = place;
        this.publicationDate = publicationDate;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private int count;
    private int price;
    private int basePrice;
    @Temporal(TemporalType.DATE)
    private Date publicationDate;
    @ManyToOne
    @JoinColumn(name = "place_id", referencedColumnName = "id")
    private Place place;
}
