package com.upgrad.FoodOrderingApp.service.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "category")
@NamedQueries({
  @NamedQuery(
      name = "categoryByUuid",
      query = "select c from CategoryEntity c where c.uuid=:uuid order by categoryName"),
  @NamedQuery(
      name = "getAllCategoriesOrderedByName",
      query = "select c from CategoryEntity c order by categoryName asc"),
})
public class CategoryEntity implements Serializable {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Size(max = 200)
  @NotNull
  @Column(name = "uuid")
  private String uuid;

  @Size(max = 255)
  @NotNull
  @Column(name = "category_name")
  private String categoryName;

  @ManyToMany(
      fetch = FetchType.LAZY,
      cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinTable(
      name = "restaurant_category",
      joinColumns = @JoinColumn(name = "category_id"),
      inverseJoinColumns = @JoinColumn(name = "restaurant_id"))
  private List<RestaurantEntity> restaurants;

  @ManyToMany(
      fetch = FetchType.LAZY,
      cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinTable(
      name = "category_item",
      joinColumns = @JoinColumn(name = "category_id"),
      inverseJoinColumns = @JoinColumn(name = "item_id"))
  private List<ItemEntity> items;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public String getCategoryName() {
    return categoryName;
  }

  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }

  public List<RestaurantEntity> getRestaurants() {
    return restaurants;
  }

  public void setRestaurants(List<RestaurantEntity> restaurants) {
    this.restaurants = restaurants;
  }

  public List<ItemEntity> getItems() {
    return items;
  }

  public void setItems(List<ItemEntity> items) {
    this.items = items;
  }

  @Override
  public boolean equals(Object obj) {
    return new EqualsBuilder().append(this, obj).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(this).hashCode();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
  }
}
