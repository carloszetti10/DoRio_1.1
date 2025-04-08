package com.projeto.domRio1.doRio.model.entity;


import com.projeto.domRio1.doRio.utils.FormatUtils;
import jakarta.persistence.*;
import lombok.Data;



@Data
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    private Category category;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private int price;
    private boolean valid = true;
    private String remark;

    public String getPriceStr() {
        return FormatUtils.formatNumber(price);
    }

    public String getValidStr() {
        return valid ? "Yes" : "No";
    }
}
