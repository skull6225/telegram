package org.telegram.models.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;


@Entity
@Data
@RequiredArgsConstructor
@Table(name = "products")
public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private String name;
  private int count;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "place_id", referencedColumnName = "id")
  private Place place;
  @ManyToOne
  @JoinColumn(name = "categories_id", referencedColumnName = "id")
  private Category category;

}
