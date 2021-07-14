package com.upgrad.FoodOrderingApp.service.entity;

import org.apache.commons.lang3.builder.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "restaurant_category")
@NamedQueries(
        {
                @NamedQuery(name = "customerByUuid", query = "select c from CustomerEntity c where c.uuid = :uuid"),
                @NamedQuery(name = "customerById", query = "select c from CustomerEntity c where c.id = :id"),
                @NamedQuery(name = "customerByContactNumber", query = "select c from CustomerEntity c where c.contactNumber = :contactNumber"),
                @NamedQuery(name = "customerByEmail", query = "select c from CustomerEntity c where c.email =:email"),
                //@NamedQuery(name="deleteUser",query = "delete from UserEntity u where u.uuid=:uuid")
        }
)


public class RestaurantCategoryEntity implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "RESTAURANT_ID")
    private Integer restaurant_id;

    public Integer getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(Integer restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public Integer getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Integer category_id) {
        this.category_id = category_id;
    }

    @Column(name = "CATEGORY_ID")
    private Integer category_id;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
