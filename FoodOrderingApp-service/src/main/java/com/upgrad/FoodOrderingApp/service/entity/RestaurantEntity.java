// package com.upgrad.FoodOrderingApp.service.entity;

// public class RestaurantEntity {
// }

package com.upgrad.FoodOrderingApp.service.entity;

import org.apache.commons.lang3.builder.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "restaurant")
@NamedQueries(
        {
                @NamedQuery(name = "customerByUuid", query = "select c from CustomerEntity c where c.uuid = :uuid"),
                @NamedQuery(name = "customerById", query = "select c from CustomerEntity c where c.id = :id"),
                @NamedQuery(name = "customerByContactNumber", query = "select c from CustomerEntity c where c.contactNumber = :contactNumber"),
                @NamedQuery(name = "customerByEmail", query = "select c from CustomerEntity c where c.email =:email"),
                //@NamedQuery(name="deleteUser",query = "delete from UserEntity u where u.uuid=:uuid")
        }
)


public class RestaurantEntity implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "UUID")
    @NotNull
    @Size(max = 200)
    private String uuid;

    @Column(name = "RESTAURANT_NAME")
    @NotNull
    @Size(max = 50)
    private String restaurant_name;

    @Column(name = "PHOTO_URL")
    @Size(max = 255)
    private String photo_url;

    @Column(name = "CUSTOMER_RATING")
    private float customer_rating;

    @Column(name = "AVERAGE_PRICE_FOR_TWO")
    private Integer average_price_for_two;

    @Column(name = "NUMBER_OF_CUSTOMERS_RATED")
    private Integer numberOfCustomersRated;

    @Column(name = "ADDRESS_ID")
    private Integer addressId;

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

    public String getRestaurant_name() {
        return restaurant_name;
    }

    public void setRestaurant_name(String restaurant_name) {
        this.restaurant_name = restaurant_name;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public float getCustomer_rating() {
        return customer_rating;
    }

    public void setCustomer_rating(float customer_rating) {
        this.customer_rating = customer_rating;
    }

    public Integer getAverage_price_for_two() {
        return average_price_for_two;
    }

    public void setAverage_price_for_two(Integer average_price_for_two) {
        this.average_price_for_two = average_price_for_two;
    }

    public Integer getNumberOfCustomersRated() {
        return numberOfCustomersRated;
    }

    public void setNumberOfCustomersRated(Integer numberOfCustomersRated) {
        this.numberOfCustomersRated = numberOfCustomersRated;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
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
