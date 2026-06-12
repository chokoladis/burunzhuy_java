package burunzhuy.resource.idea;

import burunzhuy.entity.File;
import burunzhuy.entity.Idea;
import burunzhuy.resource.FileResource;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
final public class ShortResource {
    private Long id;
    private String title;
    private String shortDescription;
    private FileResource preview;

    private BigDecimal priceMin;
    private BigDecimal priceInstanceBuy;

    public ShortResource(Idea idea)
    {
        this.id = idea.getId();
        this.title = idea.getTitle();
        this.shortDescription = idea.getShortDescription();
        this.preview = new FileResource(idea.getPreview());

        this.priceMin = idea.getPriceMin();
        this.priceInstanceBuy = idea.getPriceInstanceBuy();
    }
}
