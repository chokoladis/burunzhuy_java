package burunzhuy.resource.idea;

import burunzhuy.entity.Idea;
import burunzhuy.resource.FileResource;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@JsonPropertyOrder({"id", "title", "shortDescription", "preview", "priceMin", "priceInstanceBuy"})
final public class ShortResource {
    private final Long id;
    private final String title;
    private final String shortDescription;
    private final FileResource preview;

    private final BigDecimal priceMin;
    private final BigDecimal priceInstanceBuy;

    public ShortResource(Idea idea) {
        this.id = idea.getId();
        this.title = idea.getTitle();
        this.shortDescription = idea.getShortDescription();
        this.preview = new FileResource(idea.getPreview());

        this.priceMin = idea.getPriceMin();
        this.priceInstanceBuy = idea.getPriceInstanceBuy();
    }
}
