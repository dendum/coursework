package ua.lviv.iot.Server.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Driver extends TaxiEntity {
    private String phone;
    private String experience;
}
