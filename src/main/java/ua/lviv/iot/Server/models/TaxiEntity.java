package ua.lviv.iot.Server.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public abstract class TaxiEntity {
    protected Integer id;
    protected String name;
}
