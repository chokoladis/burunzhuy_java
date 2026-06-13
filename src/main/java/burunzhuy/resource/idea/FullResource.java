package burunzhuy.resource.idea;

import burunzhuy.entity.File;
import burunzhuy.entity.Idea;
import burunzhuy.resource.FileResource;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
final public class FullResource {
    private Long id;
    private String title;
    private String shortDescription;
    private FileResource preview;

    private String fullDescription;
    private Set<FileResource> attaches;

    private BigDecimal priceMin;
    private BigDecimal priceInstanceBuy;

    @JsonPropertyOrder({"id", "title", "shortDescription", "preview", "fullDescription", "attaches", "priceMin", "priceInstanceBuy"})
    public FullResource(Idea idea)
    {
        this.id = idea.getId();
        this.title = idea.getTitle();
        this.shortDescription = idea.getShortDescription();
        this.preview = new FileResource(idea.getPreview());

        this.fullDescription = idea.getFullDescription();
        this.attaches = idea.getAttaches()
                .stream()
                .map(file -> new FileResource(file))
                .collect(Collectors.toSet());

        this.priceMin = idea.getPriceMin();
        this.priceInstanceBuy = idea.getPriceInstanceBuy();
    }
}
