package burunzhuy.dto.idea;

import burunzhuy.entity.File;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
final public class CreateRequest {

    @NotBlank
    @Size(min = 5,max = 150)
    private String title;

    @NotBlank
    @Size(min = 15,max = 500)
    private String shortDescription;

    @NotBlank
    @Size(min = 50, max = 1000)
    private String fullDescription;

    @DecimalMin(value = "10")
    private BigDecimal priceMin;

    //default = priceMin * 5
    @DecimalMin(value = "50")
    private BigDecimal priceInstanceBuy;
}
