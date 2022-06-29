package ua.lviv.iot.Server.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Order extends TaxiEntity {
    private Integer carID;
    private Integer clientID;
    private Double distance;
    private Double price;
}
