package com.upgrad.FoodOrderingApp.service.entity;

        import org.apache.commons.lang3.builder.*;

        import javax.persistence.*;
        import javax.validation.constraints.NotNull;
        import javax.validation.constraints.Size;
        import java.io.Serializable;

@Entity
@Table(name = "item")
@NamedQueries(
        {
                @NamedQuery(name = "customerByUuid", query = "select c from CustomerEntity c where c.uuid = :uuid"),
                @NamedQuery(name = "customerById", query = "select c from CustomerEntity c where c.id = :id"),
                @NamedQuery(name = "customerByContactNumber", query = "select c from CustomerEntity c where c.contactNumber = :contactNumber"),
                @NamedQuery(name = "customerByEmail", query = "select c from CustomerEntity c where c.email =:email"),
                //@NamedQuery(name="deleteUser",query = "delete from UserEntity u where u.uuid=:uuid")
        }
)


public class ItemEntity implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "UUID")
    @NotNull
    @Size(max = 200)
    private String uuid;

    @Column(name = "ITEM_NAME")
    @NotNull
    @Size(max = 50)
    private String item_name;


    @Column(name = "PRICE")
    private Integer price;

    @Column(name = "TYPE")
    @NotNull
    @Size(max = 10)
    private String type;


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

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
